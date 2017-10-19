package com.memolease.realmtoy.activity;

import android.content.Context;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.memolease.realmtoy.event.EditMemoEvent;
import com.memolease.realmtoy.R;
import com.memolease.realmtoy.model.Memo;
import com.memolease.realmtoy.util.BusProvider;
import com.squareup.otto.Bus;

import java.text.SimpleDateFormat;
import java.util.Date;

import io.realm.Realm;

public class MemoEditActivity extends AppCompatActivity {
    EditText title;
    EditText mContent;

    Button save_btn;
    Boolean isNew;
    Bus mBus = BusProvider.getInstance();
    Realm realm;
    int id;
    int position;
    InputMethodManager imm;

    Memo memo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo_edit);
        realm = Realm.getDefaultInstance();
        mBus.register(this);
        isNew = getIntent().getExtras().getBoolean("isNew");
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);


        title = (EditText) findViewById(R.id.title);
        mContent = (EditText) findViewById(R.id.memoEditText);
        save_btn = (Button) findViewById(R.id.save_btn);

        if(!isNew) {
            id = getIntent().getExtras().getInt("id");
            position = getIntent().getExtras().getInt("position");
            memo = realm.where(Memo.class).equalTo("id", id).findFirst();
            title.setText(memo.getTitle());
            title.setSelection(title.getText().length());
            mContent.setText(memo.getContent());
            mContent.setSelection(mContent.getText().length());
            mContent.requestFocus();

            Handler mHandler = new Handler();
            mHandler.postDelayed(new Runnable() {
                public void run() {
                    imm.showSoftInput(mContent, InputMethodManager.SHOW_FORCED);
                }
            }, 100);
            //imm.showSoftInput(mContent, InputMethodManager.SHOW_FORCED);
        }

        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long now = System.currentTimeMillis();
                Date date = new Date(now);
                SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy.MM.dd");
                final String strNow = sdfNow.format(date);

                if (isNew) {
                    Memo newMemo = new Memo(title.getText().toString(), mContent.getText().toString(), strNow);
                    mBus.post(newMemo);
                    finish();
                } else {
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            memo.setTitle(title.getText().toString());
                            memo.setContent(mContent.getText().toString());
                            memo.setUpdatedAt(strNow);
                            realm.copyToRealmOrUpdate(memo);
                        }
                    });
                    EditMemoEvent editMemoEvent = new EditMemoEvent();
                    editMemoEvent.setMemo(memo);
                    editMemoEvent.setPosition(position);
                    mBus.post(editMemoEvent);
                    finish();
                }

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBus.unregister(this);
        realm.close();
    }
}
