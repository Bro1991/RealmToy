package com.memolease.realmtoy;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bro on 2017-08-17.
 */

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {
    Context mContext;
    List<NaverBook> naverBookList;
    public int bookSize = -1;

    public BookAdapter(Context mContext, List<NaverBook> naverBookList) {
        this.mContext = mContext;
        this.naverBookList = naverBookList;
    }

    public boolean isLast (int position) {
        return this.bookSize == (position + 1);
    }

    @Override
    public BookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = View.inflate(mContext, R.layout.book_item, null);
        BookViewHolder bookViewHolder = new BookViewHolder(rootView);
        return bookViewHolder;
    }

    @Override
    public void onBindViewHolder(BookViewHolder holder, int position) {
        NaverBook naverBook = naverBookList.get(position);

        Glide.with(holder.book_image.getContext())
                .load(naverBook.getImage())
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(new GlideDrawableImageViewTarget(holder.book_image));
    }

    @Override
    public int getItemCount() {
        return naverBookList.size();
    }

    class BookViewHolder extends RecyclerView.ViewHolder {
        ImageView book_image;
        public BookViewHolder(View itemView) {
            super(itemView);
            book_image = (ImageView) itemView.findViewById(R.id.book_image);
        }
    }


}
