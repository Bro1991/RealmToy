package com.memolease.realmtoy.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.memolease.realmtoy.R;
import com.memolease.realmtoy.model.Book;

import java.io.File;
import java.util.List;

/**
 * Created by bro on 2017-12-14.
 */

public class CustomPageAdapter extends PagerAdapter {
    Context mContext;
    List<Book> bookList;
    private LayoutInflater layoutInflater;

    public CustomPageAdapter(Context mContext, List<Book> bookList) {
        this.mContext = mContext;
        this.bookList = bookList;
        this.layoutInflater = (LayoutInflater)this.mContext.getSystemService(this.mContext.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return bookList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((View)object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = this.layoutInflater.inflate(R.layout.book_blank_item, container, false);
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
        /*ImageView displayImage = (ImageView)view.findViewById(R.id.large_image);
        TextView imageText = (TextView)view.findViewById(R.id.image_name);
        displayImage.setImageResource(this.dataObjectList.get(position).getImageId());
        imageText.setText(this.dataObjectList.get(position).getImageName());*/
        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
