package com.memolease.realmtoy.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.memolease.realmtoy.activity.PagerActivity;
import com.memolease.realmtoy.event.DeleteBookEvent;
import com.memolease.realmtoy.R;
import com.memolease.realmtoy.activity.BookDetailActivity;
import com.memolease.realmtoy.model.Book;
import com.memolease.realmtoy.util.BusProvider;

import java.io.File;
import java.util.List;

/**
 * Created by bro on 2017-08-17.
 */

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {
    public Context mContext;
    //List<NaverBook> naverBookList;
    List<Book> bookList;
    public int bookSize = -1;

    private final int BOOK_BLANK = 0, BOOK = 1;


    public BookAdapter(List<Book> BookList) {
        this.bookList = BookList;
    }

    public boolean isLast(int position) {
        return this.bookSize == (position + 1);
    }

    @Override
    public int getItemViewType(int position) {
        if (bookList.get(position).getType() == 0) {
            return BOOK_BLANK;
        } else {
            return BOOK;
        }
    }

    @Override
    public long getItemId(int position) {
        return bookList.get(position).getId();
    }

    @Override
    public BookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = View.inflate(parent.getContext(), R.layout.book_item, null);
        BookViewHolder bookViewHolder = new BookViewHolder(rootView);

        switch (viewType) {
            case BOOK_BLANK:
                bookViewHolder.book_container.setVisibility(View.GONE);
                break;
            case BOOK:
                bookViewHolder.book_container.setVisibility(View.VISIBLE);
                break;
        }
        return bookViewHolder;
    }

    @Override
    public void onBindViewHolder(final BookViewHolder holder, int position) {
        //NaverBook naverBook = naverBookList.get(position);
        Book book = bookList.get(position);

        holder.context = mContext;
        holder.mAdapter = this;
        holder.setAdapter(this);

        if (book.getType() == 1) {
            File file = new File(book.getImagePath());
            Log.d("파일경로", file.getPath());
            //File file = new File(mBook.getImage_path());
            Glide.with(holder.book_image.getContext())
                    .load(file)
                    .into(holder.book_image);
        }
/*        Glide.with(holder.book_image.getContext())
                .load(book.getImage())
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(new GlideDrawableImageViewTarget(holder.book_image) {
                    @Override
                    public void onResourceReady(GlideDrawable drawable, GlideAnimation anim) {
                        super.onResourceReady(drawable, null);

                        holder.book_image.setScaleType(ImageView.ScaleType.MATRIX);
                        holder.book_image.setVisibility(View.VISIBLE);
                    }
                });*/
    }

    @Override
    public int getItemCount() {
        //return naverBookList.size();
        return bookList.size();
    }

    class BookViewHolder extends RecyclerView.ViewHolder {
        ImageView book_image;
        BookAdapter mAdapter;
        FrameLayout book_container;
        Context context;

        public BookViewHolder(View itemView) {
            super(itemView);
            book_image = (ImageView) itemView.findViewById(R.id.book_image);
            book_container = (FrameLayout) itemView.findViewById(R.id.book_container);

            book_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Book book = bookList.get(getAdapterPosition());
                    if (book.getType() == 1) {
                        //BusProvider.getInstance().post(bookList.get(getAdapterPosition()));
                        Intent detail = new Intent(context, BookDetailActivity.class);
                        detail.putExtra("id", book.getId());
                        detail.putExtra("readState", book.getReadState());
                        detail.putExtra("title", book.getTitle());
                        detail.putExtra("author", book.getAuthor());
                        detail.putExtra("dedatil", makeBookDescription(book));
                        detail.putExtra("image", book.getImagePath());
                        context.startActivity(detail);
                    } else {
                        Intent pagerIntent = new Intent(context, PagerActivity.class);
                        context.startActivity(pagerIntent);
                    }
                }
            });

            book_image.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
/*                    Book naverBook = bookList.get(getAdapterPosition());
                    removeSelectedItem(naverBook.getId());
                    DeleteBookEvent bookEvent = new DeleteBookEvent();
                    bookEvent.setId(naverBook.getId());
                    BusProvider.getInstance().post(bookEvent);*/
                    Intent pagerIntent = new Intent(context, PagerActivity.class);
                    context.startActivity(pagerIntent);
                    return false;
                }
            });


        }

        public void setAdapter(BookAdapter adapter) {
            this.mAdapter = adapter;
        }

    }

    public void removeSelectedItem(int id) {
        for (Book naverBook : bookList) {
            if (naverBook.getId() == id) {
                int position = bookList.indexOf(naverBook);
                bookList.remove(position);
                notifyItemRemoved(position);
                break;
            }
        }
    }

    private String makeBookDescription(Book book) {
        return book.getPublisher() + " / " + book.getPubdateWithDot().replace("-", ". ");
        //return book.getPublisher() + " / " + book.getPubdate().replace("-",". ");
    }

}
