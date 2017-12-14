package com.memolease.realmtoy;

import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

/**
 * Created by bro on 2017-12-15.
 */

public class PageCurlPageTransformer implements ViewPager.PageTransformer {

    @Override
    public void transformPage(View page, float position) {

        //Log.d(TAG, "transformPage, position = " + position + ", page = " + page.getTag(R.id.viewpager));
        if (page instanceof PageCurl) {
            if (position > -1.0F && position < 1.0F) {
                // hold the page steady and let the views do the work
                page.setTranslationX(-position * page.getWidth());
            } else {
                page.setTranslationX(0.0F);
            }
            if (position <= 1.0F && position >= -1.0F) {
                ((PageCurl) page).setCurlFactor(position);
            }
        }
    }
}