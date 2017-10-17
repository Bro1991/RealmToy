package com.memolease.realmtoy.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.memolease.realmtoy.BookAdapter;
import com.memolease.realmtoy.EditMemoEvent;
import com.memolease.realmtoy.MemoAdapter;
import com.memolease.realmtoy.R;
import com.memolease.realmtoy.deleteMemo;
import com.memolease.realmtoy.model.Book;
import com.memolease.realmtoy.model.Memo;
import com.memolease.realmtoy.util.BusProvider;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.io.File;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

public class BookDetailActivity extends AppCompatActivity {
    ImageView mBookImageView;
    TextView mTitleView;
    TextView mTitleParse;
    TextView mAuthorView;
    TextView mDescriptionView;
    TextView textView4;
    Button mAddMemoButton;
    RadioGroup mBookStateGroup;
    Book mBook;
    Bus mBus = BusProvider.getInstance();
    Realm realm;

    RealmList<Memo> memoList = new RealmList<>();
    RecyclerView recycler_memo;
    LinearLayoutManager linearLayoutManager;

    MemoAdapter memoAdapter;
    //String ids = getIntent().getStringExtra("id");
    int id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);
        realm = Realm.getDefaultInstance();
        mBus.register(this);
        id = getIntent().getExtras().getInt("id");
        //id = getIntent().getIntExtra("id", 1);

        Log.d("가져온 ID 값", String.valueOf(id));

        initUI();
        initRecycler();
        getbook();
    }

    private void initRecycler() {
        recycler_memo = (RecyclerView) findViewById(R.id.recycler_memo);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        recycler_memo.setLayoutManager(linearLayoutManager);
        mBook = realm.where(Book.class).equalTo("id", id).findFirst();

        RealmList<Memo> memos = mBook.getMemoList();
        if (memos.size() != 0) {
            for (Memo memo : memos) {
                memoList.add(memo);
            }
        } else {
            memoList.clear();
        }
        memoAdapter = new MemoAdapter(this, memoList);
        memoAdapter.context = this;
        recycler_memo.setAdapter(memoAdapter);
    }

    private void getbook() {
        //mBook = realm.where(Book.class).equalTo("id", id).findFirst();

        String[] chunks = mBook.getTitle().split("\\(");
        if (chunks.length > 1) {
            String[] afterChunks = chunks[1].split("\\)");

            if (afterChunks.length == 1) {
                mTitleParse.setVisibility(View.VISIBLE);
                mTitleParse.setText("- " + afterChunks[0]);
                mTitleView.setText(chunks[0]);
            }
        }
        textView4.setText(mBook.getImage_path());
        mAuthorView.setText(mBook.getAuthor());
        mDescriptionView.setText(makeBookDescription(mBook));
/*        Glide.with(mBookImageView.getContext())
                .load(mBook.getImage())
                .into(new GlideDrawableImageViewTarget(mBookImageView) {
                    @Override
                    public void onResourceReady(GlideDrawable drawable, GlideAnimation anim) {
                        super.onResourceReady(drawable, null);
                        mBookImageView.setScaleType(ImageView.ScaleType.MATRIX);
                    }
                });*/

        File file = new File(getFilesDir().getPath() + mBook.getImage_path());
        Log.d("파일경로", file.getPath());
        //File file = new File(mBook.getImage_path());
        Glide.with(mBookImageView.getContext())
                .load(file)
                .into(mBookImageView);
    }


    private void initUI() {
        mTitleView = (TextView) findViewById(R.id.title);
        mTitleView.setTextColor(Color.parseColor("#333333"));

        mTitleParse = (TextView) findViewById(R.id.titleDesc);
        mTitleParse.setTextColor(Color.parseColor("#427ED9"));

        mAuthorView = (TextView) findViewById(R.id.author);
        mAuthorView.setTextColor(Color.parseColor("#555555"));

        mDescriptionView = (TextView) findViewById(R.id.bookDescription);
        mDescriptionView.setTextColor(Color.parseColor("#555555"));

        textView4 = (TextView) findViewById(R.id.textView4);

        mBookImageView = (ImageView) findViewById(R.id.imageView);
        mAddMemoButton = (Button) findViewById(R.id.addMemoButton);
        mAddMemoButton.setTextColor(Color.parseColor("#5667ff"));
        mAddMemoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addMemo = new Intent(BookDetailActivity.this, MemoEditActivity.class);
                addMemo.putExtra("isNew", true);
                startActivity(addMemo);
            }
        });

        mBookStateGroup = (RadioGroup) findViewById(R.id.bookStateGroup);

    }


/*    @Subscribe
    public void getDetailbook(Book book) {
        this.mBook = book;
        Log.d("받아온 book", book.getTitle());
        Log.d("받아온 book", "받았니?");
        String[] chunks = book.getTitle().split("\\(");
        if (chunks.length > 1) {
            String[] afterChunks = chunks[1].split("\\)");

            if (afterChunks.length == 1) {
                mTitleParse.setVisibility(View.VISIBLE);
                mTitleParse.setText("- " + afterChunks[0]);
                mTitleView.setText(chunks[0]);
            }
        }

        Glide.with(mBookImageView.getContext())
                .load(book.getImage())
                .into(new GlideDrawableImageViewTarget(mBookImageView) {
                    @Override
                    public void onResourceReady(GlideDrawable drawable, GlideAnimation anim) {
                        super.onResourceReady(drawable, null);
                        mBookImageView.setScaleType(ImageView.ScaleType.MATRIX);
                    }
                });

        mAuthorView.setText(book.getAuthor());
        mDescriptionView.setText(makeBookDescription(book));
    }*/

    @Subscribe
    public void getMemo(final Memo memo) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Number currentIdNum = realm.where(Memo.class).max("id");
                int nextId;
                if(currentIdNum == null) {
                    nextId = 1;
                } else {
                    nextId = currentIdNum.intValue() + 1;
                }
                memo.setBookid(id);
                memo.setId(nextId);
                //Memo memo = realm.where(Memo.class).eq
                //Number currentIdNum = realm.where(Memo.class).equalTo()
                mBook.memoList.add(memo);
            }
        });
        memoList.add(memo);
        memoAdapter.notifyDataSetChanged();
    }

    @Subscribe
    public void deleteMemos(final deleteMemo deleteMemo) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                mBook.memoList.remove(deleteMemo.getPosition());
            }
        });
        memoList.remove(deleteMemo.getPosition());
        memoAdapter.notifyItemRemoved(deleteMemo.getPosition());

    }

    @Subscribe
    public void editMemo(EditMemoEvent editMemoEvent) {
        //Memo memo = memoList.get(editMemoEvent.getPosition());
        //memo.setTitle(editMemoEvent.getTitle());
        //memo.setContent(editMemoEvent.getContent());
        //memo.setUpdatedAt(editMemoEvent.getUpdatedAt());
        memoAdapter.notifyItemChanged(editMemoEvent.getPosition());
    }

    private String makeBookDescription(Book book) {
        return book.getPublisher() + " / " + book.getPubdateWithDot().replace("-", ". ");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBus.unregister(this);
        realm.close();
    }

}