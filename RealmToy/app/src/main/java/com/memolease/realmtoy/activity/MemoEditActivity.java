package com.memolease.realmtoy.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo_edit);
        realm = Realm.getDefaultInstance();
        mBus.register(this);

        title = (EditText) findViewById(R.id.title);
        mContent = (EditText) findViewById(R.id.memoEditText);
        save_btn = (Button) findViewById(R.id.save_btn);
        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long now = System.currentTimeMillis();
                Date date = new Date(now);
                SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy.MM.dd");
                String strNow = sdfNow.format(date);

                Memo memo = new Memo(title.getText().toString(), mContent.getText().toString(), strNow);
                mBus.post(memo);
                finish();
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
