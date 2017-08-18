package com.memolease.realmtoy;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

import org.jsoup.Jsoup;

import java.util.List;

/**
 * Created by bro on 2017-08-17.
 */

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {
    Context mContext;
    List<NaverBook> naverBookList;
    public int bookSize = -1;

    public SearchAdapter(Context context, List<NaverBook> naverBookList) {
        this.mContext = context;
        this.naverBookList = naverBookList;
    }

    public boolean isLast (int position) {
        return this.bookSize == (position + 1);
    }

    @Override
    public SearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = View.inflate(parent.getContext(), R.layout.search_item, null);
        SearchViewHolder searchViewHolder = new SearchViewHolder(rootView);
        return searchViewHolder;
    }

    @Override
    public void onBindViewHolder(final SearchViewHolder holder, int position) {
        NaverBook naverBook = naverBookList.get(position);

        holder.context = mContext;
        holder.searchAdapter = this;
        holder.setAdapter(this);

        holder.mTitle.setText(Jsoup.parse(naverBook.getTitle()).text());
        holder.mAuthor.setText(Jsoup.parse(naverBook.getAuthor()).text().replace("|", ", "));
        holder.mPublisher.setText(Jsoup.parse(naverBook.getPublisher()).text());
        holder.mPublishDate.setText(naverBook.getPubdateWithDot());
        Glide.with(holder.mBookImageView.getContext())
                .load(naverBook.getImage().replace("m1", "m5").replace("m80", "m260"))
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(new GlideDrawableImageViewTarget(holder.mBookImageView) {
                    @Override
                    public void onResourceReady(GlideDrawable drawable, GlideAnimation anim) {
                        super.onResourceReady(drawable, null);
                        holder.mBookImageView.setVisibility(View.VISIBLE);
                    }
                });
    }

    @Override
    public int getItemCount() {
        return naverBookList.size();
    }

    class SearchViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        Context context;
        SearchAdapter searchAdapter;
        TextView mTitle;
        ImageView mBookImageView;
        TextView mAuthor;
        TextView mPublisher;
        TextView mPublishDate;

        public SearchViewHolder(View v) {
            super(v);
            mTitle = (TextView) v.findViewById(R.id.bookTitle);
            mTitle.setTextColor(Color.parseColor("#333333"));
            mBookImageView = (ImageView) v.findViewById(R.id.bookImageView);
            mAuthor = (TextView) v.findViewById(R.id.bookAuthor);
            mPublisher = (TextView) v.findViewById(R.id.bookPublisher);
            mPublishDate = (TextView) v.findViewById(R.id.bookPublishDate);
            v.setOnClickListener(this);
        }

        public void setAdapter(SearchAdapter adapter) { this.searchAdapter = adapter; }

        @Override
        public void onClick(View v) {
            Toast.makeText(context, "아이템이 눌렸습니다", Toast.LENGTH_SHORT).show();
        }
    }


}
