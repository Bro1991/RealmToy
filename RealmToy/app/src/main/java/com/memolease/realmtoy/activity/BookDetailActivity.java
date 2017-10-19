package com.memolease.realmtoy.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.memolease.realmtoy.event.EditMemoEvent;
import com.memolease.realmtoy.adapter.MemoAdapter;
import com.memolease.realmtoy.event.PutChangeStateBookEvent;
import com.memolease.realmtoy.R;
import com.memolease.realmtoy.event.deleteMemo;
import com.memolease.realmtoy.model.Book;
import com.memolease.realmtoy.model.Memo;
import com.memolease.realmtoy.util.BusProvider;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.io.File;

import io.realm.Realm;
import io.realm.RealmList;

public class BookDetailActivity extends AppCompatActivity {
    ImageView mBookImageView;
    TextView mTitleView;
    TextView mTitleParse;
    TextView mAuthorView;
    TextView mDescriptionView;
    TextView textView4;
    Button mAddMemoButton;
    RadioGroup mBookStateGroup;
    RadioButton bookStateSeg_1, bookStateSeg_2, bookStateSeg_3, bookStateSeg_4;
    Book mBook;
    Bus mBus = BusProvider.getInstance();
    Realm realm;

    RealmList<Memo> memoList = new RealmList<>();
    RecyclerView recycler_memo;
    LinearLayoutManager linearLayoutManager;

    MemoAdapter memoAdapter;
    //String ids = getIntent().getStringExtra("id");
    int id;
    int readState;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);
        realm = Realm.getDefaultInstance();
        mBus.register(this);
        id = getIntent().getExtras().getInt("id");
        readState = getIntent().getExtras().getInt("readState");
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
/*        String[] chunks = mBook.getTitle().split("\\(");
        if (chunks.length > 1) {
            String[] afterChunks = chunks[1].split("\\)");

            if (afterChunks.length == 1) {
                mTitleParse.setVisibility(View.VISIBLE);
                mTitleParse.setText("- " + afterChunks[0]);
                mTitleView.setText(chunks[0]);
            }
        } else {
            mTitleParse.setVisibility(View.GONE);
            mTitleView.setText(mBook.getTitle());
        }*/

        if (mBook.getSub_title() == null) {
            mTitleParse.setVisibility(View.GONE);
            mTitleView.setText(mBook.getTitle());
        } else {
            mTitleParse.setVisibility(View.VISIBLE);
            mTitleParse.setText(mBook.getSub_title());
            mTitleView.setText(mBook.getTitle());
        }

        switch (mBook.getReadState()) {
            case 1 :
                bookStateSeg_1.setChecked(true);
                break;
            case 2 :
                bookStateSeg_2.setChecked(true);
                break;
            case 3 :
                bookStateSeg_3.setChecked(true);
                break;
            case 4 :
                bookStateSeg_4.setChecked(true);
                break;
        }

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

        //File file = new File(getFilesDir().getPath() + mBook.getImage_path());
        File file = new File(mBook.getImagePath());
        Log.d("파일경로", file.getPath());
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
        bookStateSeg_1 = (RadioButton) findViewById(R.id.bookStateSeg_1);
        bookStateSeg_2 = (RadioButton) findViewById(R.id.bookStateSeg_2);
        bookStateSeg_3 = (RadioButton) findViewById(R.id.bookStateSeg_3);
        bookStateSeg_4 = (RadioButton) findViewById(R.id.bookStateSeg_4);

        mBookStateGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                PutChangeStateBookEvent event = new PutChangeStateBookEvent();
                event.setBookid(id);
                int selectedIdx = 0;
                switch (checkedId) {
                    case R.id.bookStateSeg_1:
                        selectedIdx = 1;
                        break;

                    case R.id.bookStateSeg_2:
                        selectedIdx = 2;
                        break;

                    case R.id.bookStateSeg_3:
                        selectedIdx = 3;
                        break;

                    case R.id.bookStateSeg_4:
                        selectedIdx = 4;
                        break;
                }

                event.setReadState(selectedIdx);
                mBus.post(event);
            }
        });
    }

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

    @Subscribe
    public void editReadState(final PutChangeStateBookEvent event) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Book book = realm.where(Book.class).equalTo("id", event.getBookid()).findFirst();
                book.setReadState(event.getReadState());
                realm.copyToRealmOrUpdate(book);
            }
        });

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