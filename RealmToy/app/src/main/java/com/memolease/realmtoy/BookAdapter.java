package com.memolease.realmtoy;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.memolease.realmtoy.util.BusProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bro on 2017-08-17.
 */

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {
    Context mContext;
    List<NaverBook> naverBookList;
    public int bookSize = -1;

    public BookAdapter(List<NaverBook> naverBookList) {
        this.naverBookList = naverBookList;
    }

    public boolean isLast(int position) {
        return this.bookSize == (position + 1);
    }

    @Override
    public BookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = View.inflate(parent.getContext(), R.layout.book_item, null);
        BookViewHolder bookViewHolder = new BookViewHolder(rootView);
        return bookViewHolder;
    }

    @Override
    public void onBindViewHolder(final BookViewHolder holder, int position) {
        NaverBook naverBook = naverBookList.get(position);

        holder.context = mContext;
        holder.mAdapter = this;

        holder.setAdapter(this);

        Glide.with(holder.book_image.getContext())
                .load(naverBook.getImage())
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(new GlideDrawableImageViewTarget(holder.book_image) {
                    @Override
                    public void onResourceReady(GlideDrawable drawable, GlideAnimation anim) {
                        super.onResourceReady(drawable, null);

                        holder.book_image.setScaleType(ImageView.ScaleType.MATRIX);
                        holder.book_image.setVisibility(View.VISIBLE);
                    }
                });
    }

    @Override
    public int getItemCount() {
        return naverBookList.size();
    }

    class BookViewHolder extends RecyclerView.ViewHolder {
        ImageView book_image;
        BookAdapter mAdapter;
        Context context;

        public BookViewHolder(View itemView) {
            super(itemView);
            book_image = (ImageView) itemView.findViewById(R.id.book_image);
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    NaverBook naverBook = naverBookList.get(getAdapterPosition());
                    removeSelectedItem(naverBook.getId());
                    DeleteBookEvent bookEvent = new DeleteBookEvent();
                    bookEvent.setId(naverBook.getId());
                    BusProvider.getInstance().post(bookEvent);
                    return false;
                }
            });


        }

        public void setAdapter(BookAdapter adapter) {
            this.mAdapter = adapter;
        }

    }

    public void removeSelectedItem(int id) {
        for (NaverBook naverBook : naverBookList) {
            if (naverBook.getId() == id) {
                int position = naverBookList.indexOf(naverBook);
                naverBookList.remove(position);
                notifyItemRemoved(position);
                break;
            }
        }
    }

}
