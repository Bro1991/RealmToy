package com.memolease.realmtoy.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.memolease.realmtoy.util.BookRegistDialogView;
import com.memolease.realmtoy.network.networkModel.NaverBook;
import com.memolease.realmtoy.R;
import com.memolease.realmtoy.util.BusProvider;

import org.jsoup.Jsoup;

import java.util.ArrayList;
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

    public boolean isLast(int position) {
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

    public void addItems(ArrayList<NaverBook> newItems) {
        for (NaverBook item : newItems)
            naverBookList.add(item);
        notifyDataSetChanged();
    }

    public void refresh() {
        if (naverBookList == null)
            naverBookList = new ArrayList<>();
        else
            naverBookList.clear();
    }


    class SearchViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        Context context;
        SearchAdapter searchAdapter;
        NaverBook naverBook;
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

        public void setAdapter(SearchAdapter adapter) {
            this.searchAdapter = adapter;
        }

        @Override
        public void onClick(View v) {
            //((SearchBookActivity)SearchBookActivity.mContext).finish();
            naverBook = naverBookList.get(getAdapterPosition());

            final BookRegistDialogView dialogView = new BookRegistDialogView(context, naverBook);

            MaterialDialog materialDialog = new MaterialDialog.Builder(context)
                    .customView(dialogView, false)
                    .negativeText("취소")
                    .negativeColorRes(R.color.aqua_color)
                    .positiveText("등록")
                    .positiveColorRes(R.color.aqua_color)
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            NaverBook event = naverBook;
                            //NaverBook event = naverBookList.get(getAdapterPosition());
                            String paresTitle = Jsoup.parse(naverBook.getTitle()).text();
                            String[] chunks = paresTitle.split("\\(");
                            if (chunks.length > 1) {
                                String[] afterChunks = chunks[1].split("\\)");

                                if (afterChunks.length >= 1) {
                                    event.setTitle(chunks[0]);
                                    event.setSub_title(("- " + afterChunks[0]));
                                }
                            } else {
                                event.setTitle(naverBook.getTitle());
                            }
                            String[] isbnArray = event.getIsbn().split(" ");
                            event.setType(1);
                            event.setIsbn(isbnArray[0]);
                            event.setIsbn13(isbnArray[1]);
                            event.setImage(event.getImage().replace("m1", "m5").replace("m80", "m260"));
                            event.setTitle((Jsoup.parse(naverBook.getTitle()).text()));
                            event.setAuthor(Jsoup.parse(naverBook.getAuthor()).text().replace("|", ", "));
                            event.setPublisher(Jsoup.parse(naverBook.getPublisher()).text());
                            event.setReadState(dialogView.readState);
                            BusProvider.getInstance().post(event);
                            Log.d("imageLink", event.getImage());
                        }
                    })
                    .show();
        }
    }


}
