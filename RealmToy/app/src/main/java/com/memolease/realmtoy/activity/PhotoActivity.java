package com.memolease.realmtoy.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.memolease.realmtoy.R;
import com.memolease.realmtoy.model.Book;
import com.memolease.realmtoy.model.PhotoMemo;

import java.io.File;
import java.io.FileOutputStream;

import io.realm.Realm;

public class PhotoActivity extends AppCompatActivity {
    ImageView imageView;
    int id;
    Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        id = getIntent().getExtras().getInt("id");
        Log.d("가져온 ID 값", String.valueOf(id));

        imageView = (ImageView) findViewById(R.id.imageView);
        realm = Realm.getDefaultInstance();
        PhotoMemo potomemo = realm.where(PhotoMemo.class).equalTo("id", id).findFirst();
        File file = new File(potomemo.getImagePath());
        Glide.with(this)
                .load(file)
                .into(imageView);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }
}
