package com.memolease.realmtoy.activity;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.eftimoff.viewpagertransformers.StackTransformer;
import com.eftimoff.viewpagertransformers.ZoomOutSlideTransformer;
import com.memolease.realmtoy.CustomViewPager;
import com.memolease.realmtoy.DepthPageTransformer;
import com.memolease.realmtoy.FlipPageViewTransformer;
import com.memolease.realmtoy.HunderedDepthPageTransformer;
import com.memolease.realmtoy.PageCurlPageTransformer;
import com.memolease.realmtoy.R;
import com.memolease.realmtoy.ZoomOutPageTransformer;
import com.memolease.realmtoy.adapter.CustomPageAdapter;
import com.memolease.realmtoy.event.MenuOpenEvent;
import com.memolease.realmtoy.event.ViewPagerButtonEvent;
import com.memolease.realmtoy.model.Book;
import com.memolease.realmtoy.util.BusProvider;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class PagerActivity extends AppCompatActivity {
    List<Book> bookList = new ArrayList<>();
    //ViewPager viewpager;
    CustomViewPager viewPager;
    CustomPageAdapter customPageAdapter;
    private Realm realm;
    FrameLayout menu_container;
    Bus mBus = BusProvider.getInstance();
    boolean menu = false;
    Button button1, button2, button3;
    TextView seekbar_title;
    SeekBar seek_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pager);
        realm = Realm.getDefaultInstance();
        mBus.register(this);
        viewPager = (CustomViewPager) findViewById(R.id.viewpager);
        viewPager.setPagingEnabled(true);
        initBookRealm();
        initUi();
        customPageAdapter = new CustomPageAdapter(this, bookList);
        viewPager.setAdapter(customPageAdapter);
        //viewPager.setPageTransformer(false, new DepthPageTransformer(this));
        viewPager.setPageTransformer(false, new HunderedDepthPageTransformer(this));
        //viewPager.setPageTransformer(true, new StackTransformer());
        //viewPager.setPageTransformer(false, new PageCurlPageTransformer());

    }

    private void initUi() {
        seek_bar = (SeekBar) findViewById(R.id.seek_bar);
        seek_bar.setMax(bookList.size() -1);
        menu_container = (FrameLayout) findViewById(R.id.menu_container);
        seekbar_title = (TextView) findViewById(R.id.seekbar_title);
        button1 = (Button) findViewById(R.id.button1);
        button2 = (Button) findViewById(R.id.button2);
        button3 = (Button) findViewById(R.id.button3);
/*        viewPager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (menu == false) {
                    menu = true;
                    menu_container.setVisibility(View.VISIBLE);
                } else {
                    menu = false;
                    menu_container.setVisibility(View.INVISIBLE);
                }
            }
        });*/

        menu_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (menu == false) {
                    menu_container.setVisibility(View.VISIBLE);
                    menu = true;
                } else {
                    menu_container.setVisibility(View.INVISIBLE);
                    menu = false;
                }
            }
        });
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menu = false;
                menu_container.setVisibility(View.INVISIBLE);
                int position = viewPager.getCurrentItem();
                Log.d("눌린버튼", position + " 번째 버튼이 눌렸습니다");

                Toast.makeText(PagerActivity.this, position + " 번째 버튼이 눌렸습니다", Toast.LENGTH_SHORT).show();
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menu = false;
                menu_container.setVisibility(View.INVISIBLE);
                Log.d("눌린버튼", " 두번째 버튼이 눌렸습니다");
                Toast.makeText(PagerActivity.this, "두번째 버튼이 눌렸습니다", Toast.LENGTH_SHORT).show();
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menu = false;
                menu_container.setVisibility(View.INVISIBLE);
                Log.d("눌린버튼", " 세번째 버튼이 눌렸습니다");
                Toast.makeText(PagerActivity.this, "세번째 버튼이 눌렸습니다", Toast.LENGTH_SHORT).show();
            }
        });

        seek_bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                viewPager.setCurrentItem(seekBar.getProgress());
                seekbar_title.setText(bookList.get(progress).getTitle());

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
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

    @Subscribe
    public void menuOpen(MenuOpenEvent menuOpenEvent) {
        Log.d("받았니", "메뉴버튼 받았다");
        /*viewPager.setPagingEnabled(menuOpenEvent.isOpen());
        //menu = menuOpenEvent.isOpen();
        if (menuOpenEvent.isOpen() == false) {
            menu_container.setVisibility(View.VISIBLE);
            menu = true;
        } else {
            menu_container.setVisibility(View.INVISIBLE);
            menu = false;
        }*/
        if (menu == false) {
            viewPager.setPagingEnabled(true);
            menu_container.setVisibility(View.VISIBLE);
            menu = true;
        }else {
            viewPager.setPagingEnabled(false);

            menu_container.setVisibility(View.INVISIBLE);
            menu = false;
        }

    }

    @Subscribe
    public void viewPagerButtonClick(ViewPagerButtonEvent viewPagerButtonEvent) {
        Log.d("받았니", "버튼이벤트 받았다");
        viewPager.setPagingEnabled(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
        mBus.unregister(this);
    }

}
