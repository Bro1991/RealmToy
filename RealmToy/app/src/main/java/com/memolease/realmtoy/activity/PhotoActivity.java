package com.memolease.realmtoy.activity;

import android.content.Context;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.memolease.realmtoy.R;
import com.memolease.realmtoy.adapter.PhotoMemoAdapter;
import com.memolease.realmtoy.adapter.PhotoViewAdapter;
import com.memolease.realmtoy.event.DeletePhotoMemo;
import com.memolease.realmtoy.event.DeletePhotoSuccessEvent;
import com.memolease.realmtoy.event.deleteMemo;
import com.memolease.realmtoy.model.Book;
import com.memolease.realmtoy.model.PhotoMemo;
import com.memolease.realmtoy.util.BusProvider;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.io.File;
import java.io.FileOutputStream;

import io.realm.Realm;
import io.realm.RealmList;

public class PhotoActivity extends AppCompatActivity {
    //ImageView imageView;
    int id;
    Realm realm;
    Book mBook;

    RecyclerView recycler_photomemo;
    LinearLayoutManager linearLayoutManager;
    RealmList<PhotoMemo> photoMemoList = new RealmList<>();
    PhotoViewAdapter photoViewAdapter;
    private static int displayedposition = 0;

    Bus mBus = BusProvider.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        mBus.register(this);

        id = getIntent().getExtras().getInt("book_id");
        displayedposition = getIntent().getExtras().getInt("index");
        Log.d("가져온 ID 값", String.valueOf(id));

        //imageView = (ImageView) findViewById(R.id.imageView);
        realm = Realm.getDefaultInstance();
        /*PhotoMemo potomemo = realm.where(PhotoMemo.class).equalTo("id", id).findFirst();
        File file = new File(potomemo.getImagePath());
        Glide.with(this)
                .load(file)
                .into(imageView);*/
        initPhotoMemoRecycler();
    }

    @Subscribe
    public void deletePhotoMemo(final DeletePhotoMemo deletePhotoMemo) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                mBook.photoMemoList.remove(deletePhotoMemo.getPosition());
            }
        });
        photoMemoList.remove(deletePhotoMemo.getPosition());
        if (photoMemoList.size() == 0) {
            DeletePhotoSuccessEvent event = new DeletePhotoSuccessEvent();
            event.setPosition(deletePhotoMemo.getPosition());
            BusProvider.getInstance().post(event);
            finish();
        } else {
            photoViewAdapter.notifyItemRemoved(deletePhotoMemo.getPosition());
            DeletePhotoSuccessEvent event = new DeletePhotoSuccessEvent();
            event.setPosition(deletePhotoMemo.getPosition());
            BusProvider.getInstance().post(event);
        }

    }

    private void initPhotoMemoRecycler() {
        recycler_photomemo = (RecyclerView) findViewById(R.id.recycler_photomemo);
/*        recycler_photomemo.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

            }
        });*/

        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        linearLayoutManager.scrollToPosition(displayedposition);

        recycler_photomemo.setLayoutManager(linearLayoutManager);
        LinearSnapHelper snapHelper = new LinearSnapHelper() {
            @Override
            public int findTargetSnapPosition(RecyclerView.LayoutManager lm, int velocityX, int velocityY) {
                View centerView = findSnapView(lm);
                if (centerView == null)
                    return RecyclerView.NO_POSITION;

                int position = lm.getPosition(centerView);
                int targetPosition = -1;
                if (lm.canScrollHorizontally()) {
                    if (velocityX < 0) {
                        targetPosition = position - 1;
                    } else {
                        targetPosition = position + 1;
                    }
                }

                if (lm.canScrollVertically()) {
                    if (velocityY < 0) {
                        targetPosition = position - 1;
                    } else {
                        targetPosition = position + 1;
                    }
                }

                final int firstItem = 0;
                final int lastItem = lm.getItemCount() - 1;
                targetPosition = Math.min(lastItem, Math.max(targetPosition, firstItem));
                return targetPosition;
            }
        };
        snapHelper.attachToRecyclerView(recycler_photomemo);

        mBook = realm.where(Book.class).equalTo("id", id).findFirst();
        RealmList<PhotoMemo> photoMemos = mBook.getPhotoMemoList();
        if (photoMemos.size() != 0) {
            for (PhotoMemo photoMemo : photoMemos) {
                photoMemoList.add(photoMemo);
                Log.d("저장된 파일 경로", photoMemo.getImagePath());
            }
        } else {
            photoMemoList.clear();
        }
        photoViewAdapter = new PhotoViewAdapter(this, photoMemoList);
        photoViewAdapter.mContext = this;
        Log.d("저장된 사진의 갯수", String.valueOf(photoViewAdapter.getItemCount()));
        recycler_photomemo.setAdapter(photoViewAdapter);
        //linearLayoutManager.scrollToPositionWithOffset(displayedposition, photoMemoList.size());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBus.unregister(this);
        realm.close();
    }
}
