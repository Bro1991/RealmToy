package com.memolease.realmtoy.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.memolease.realmtoy.R;
import com.memolease.realmtoy.event.MenuOpenEvent;
import com.memolease.realmtoy.event.ViewPagerButtonEvent;
import com.memolease.realmtoy.model.Book;
import com.memolease.realmtoy.util.BusProvider;

import java.io.File;
import java.util.List;

/**
 * Created by bro on 2017-12-14.
 */

public class CustomPageAdapter extends PagerAdapter {
    Context mContext;
    List<Book> bookList;
    private LayoutInflater layoutInflater;
    public boolean menu = false;

    public CustomPageAdapter(Context mContext, List<Book> bookList) {
        this.mContext = mContext;
        this.bookList = bookList;
        this.layoutInflater = (LayoutInflater) this.mContext.getSystemService(this.mContext.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return bookList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((View) object);
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = this.layoutInflater.inflate(R.layout.book_blank_item, container, false);
        final LinearLayout menu_container = (LinearLayout) itemView.findViewById(R.id.menu_container);
        Button button1 = (Button) itemView.findViewById(R.id.button1);
        Button button2 = (Button) itemView.findViewById(R.id.button2);
        Button button3 = (Button) itemView.findViewById(R.id.button3);
        ImageView book_image = (ImageView) itemView.findViewById(R.id.book_image);
        TextView title_txt = (TextView) itemView.findViewById(R.id.title_txt);

        Book book = bookList.get(position);
        File file = new File(book.getImagePath());
        Log.d("파일경로", file.getPath());
        //File file = new File(mBook.getImage_path());
        title_txt.setText(book.getTitle());
        Glide.with(book_image.getContext())
                .load(file)
                .into(book_image);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (menu == false) {
                    Log.d("보냈니", menu + "메뉴버튼 보냈다");
                    MenuOpenEvent menuOpenEvent = new MenuOpenEvent();
                    menuOpenEvent.setOpen(menu);
                    BusProvider.getInstance().post(menuOpenEvent);
                    menu = true;
                    menu_container.setVisibility(View.VISIBLE);
                } else {
                    Log.d("보냈니", menu + "메뉴버튼 보냈다");
                    MenuOpenEvent menuOpenEvent = new MenuOpenEvent();
                    menuOpenEvent.setOpen(menu);
                    BusProvider.getInstance().post(menuOpenEvent);
                    menu = false;
                    menu_container.setVisibility(View.INVISIBLE);
                }
            }
        });

/*        itemView.setOnClickListener(new View.OnClickListener() {
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
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewPagerButtonEvent viewPagerButtonEvent = new ViewPagerButtonEvent();
                viewPagerButtonEvent.setOpen(menu);
                BusProvider.getInstance().post(viewPagerButtonEvent);
                menu = false;
                menu_container.setVisibility(View.INVISIBLE);
                Toast.makeText(mContext, "첫번째 버튼이 눌렸습니다", Toast.LENGTH_SHORT).show();
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewPagerButtonEvent viewPagerButtonEvent = new ViewPagerButtonEvent();
                viewPagerButtonEvent.setOpen(menu);
                BusProvider.getInstance().post(viewPagerButtonEvent);
                menu = false;
                menu_container.setVisibility(View.INVISIBLE);
                Toast.makeText(mContext, "두번째 버튼이 눌렸습니다", Toast.LENGTH_SHORT).show();
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewPagerButtonEvent viewPagerButtonEvent = new ViewPagerButtonEvent();
                viewPagerButtonEvent.setOpen(menu);
                BusProvider.getInstance().post(viewPagerButtonEvent);
                menu = false;
                menu_container.setVisibility(View.INVISIBLE);
                Toast.makeText(mContext, "세번째 버튼이 눌렸습니다", Toast.LENGTH_SHORT).show();
            }
        });

        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
