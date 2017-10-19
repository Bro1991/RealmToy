package com.memolease.realmtoy.util;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.memolease.realmtoy.R;
import com.memolease.realmtoy.network.networkModel.NaverBook;

import org.jsoup.Jsoup;

public class BookRegistDialogView extends LinearLayout {
    NaverBook mBookSearch;
    TextView mTitle;
    ImageView mBookImageView;
    TextView mAuthor;
    TextView mPublisher;
    TextView mPublishDate;
    RadioGroup mRadioGroup;
    public int readState = 1;

    public BookRegistDialogView(Context context, NaverBook bookSearch) {
        super(context);
        mBookSearch = bookSearch;
        init();
    }

    public BookRegistDialogView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public BookRegistDialogView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BookRegistDialogView(Context context) {
        super(context);
        init();
    }

    private void init() {
        View rootView = inflate(getContext(), R.layout.dialog_book_regist, null);

        mTitle = (TextView) rootView.findViewById(R.id.bookTitle);
        mBookImageView = (ImageView) rootView.findViewById(R.id.bookImageView);
        mAuthor = (TextView) rootView.findViewById(R.id.bookAuthor);
        mPublisher = (TextView) rootView.findViewById(R.id.bookPublisher);
        mPublishDate = (TextView) rootView.findViewById(R.id.bookPublishDate);
        mRadioGroup = (RadioGroup) rootView.findViewById(R.id.radioGroup);

        mTitle.setTextColor(Color.parseColor("#222222"));
        mTitle.setText(Jsoup.parse(mBookSearch.getTitle()).text());
        mAuthor.setText(Jsoup.parse(mBookSearch.getAuthor()).text().replace("|", ", "));
        mPublisher.setText(Jsoup.parse(mBookSearch.getPublisher()).text());
        mPublishDate.setText(mBookSearch.getPubdate());

        Glide.with(mBookImageView.getContext())
                .load(mBookSearch.getImage().replace("m1", "m5").replace("m80", "m260"))
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(new GlideDrawableImageViewTarget(mBookImageView) {
                    @Override
                    public void onResourceReady(GlideDrawable drawable, GlideAnimation anim) {
                        super.onResourceReady(drawable, null);
                        mBookImageView.setVisibility(View.VISIBLE);
                    }
                });

        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = (RadioButton) findViewById(checkedId);
                switch (radioButton.getId()) {
                    case R.id.radioButton:
                        readState = 1;
                        break;
                    case R.id.radioButton2:
                        readState = 2;
                        break;
                    case R.id.radioButton3:
                        readState = 3;
                        break;
                    case R.id.radioButton4:
                        readState = 4;
                        break;
                }
            }
        });

        addView(rootView);
    }
}
