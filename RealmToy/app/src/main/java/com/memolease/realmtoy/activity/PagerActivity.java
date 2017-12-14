package com.memolease.realmtoy.activity;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.eftimoff.viewpagertransformers.StackTransformer;
import com.eftimoff.viewpagertransformers.ZoomOutSlideTransformer;
import com.memolease.realmtoy.DepthPageTransformer;
import com.memolease.realmtoy.FlipPageViewTransformer;
import com.memolease.realmtoy.PageCurlPageTransformer;
import com.memolease.realmtoy.R;
import com.memolease.realmtoy.ZoomOutPageTransformer;
import com.memolease.realmtoy.adapter.CustomPageAdapter;
import com.memolease.realmtoy.model.Book;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class PagerActivity extends AppCompatActivity {
    List<Book> bookList = new ArrayList<>();
    ViewPager viewpager;
    CustomPageAdapter customPageAdapter;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pager);
        realm = Realm.getDefaultInstance();
        viewpager = (ViewPager)findViewById(R.id.viewpager);
        initBookRealm();
        customPageAdapter = new CustomPageAdapter(this, bookList);
        viewpager.setAdapter(customPageAdapter);
        //viewpager.setPageTransformer(true, new StackTransformer());
        viewpager.setPageTransformer(false, new PageCurlPageTransformer());

    }

    private void initBookRealm() {
        int id = 1;
        RealmResults<Book> books = realm.where(Book.class).equalTo("libraryid", id).findAll();
        //RealmResults<Book> books = realm.where(Book.class).findAll();
        if (books.size() != 0) {
            for (Book book : books) {
                bookList.add(book);
                //customPageAdapter.notifyDataSetChanged();
            }
/*            RealmResults<Memo> memos = realm.where(Memo.class).equalTo("bookid", 5).findAll();
            for (int i = 0; i < memos.size(); i++) {
                Log.d("메모 숫자", memos.get(i).getContent());
            }*/

        } else {
            bookList.clear();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }

}
