package com.memolease.realmtoy;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * Created by bro on 2017-12-15.
 */

public class HunderedDepthPageTransformer implements ViewPager.PageTransformer {
    //private static final float MIN_SCALE = 0.75f;
    private static final int DEFAULT_SCRIM_COLOR = R.color.viewpager;
    private static final int DRAWER_ELEVATION = 20;
    private float elevation;
    public  Context context;

    public HunderedDepthPageTransformer(Context paramContext) {
        this.elevation = (paramContext.getResources().getDisplayMetrics().density * 20.0F);
        this.context = paramContext;
    }

    public void transformPage(View paramView, float paramFloat) {
        int i = 8;
        if (paramFloat < -1.0F) {
            paramView.setAlpha(1.0F);
            paramView.setVisibility(View.VISIBLE);
            return;
        }
        if (paramFloat <= 0.0F) {
            paramView.setTranslationX(0.0F);
            paramView.setAlpha(1.0F);
            paramView.setVisibility(View.VISIBLE);
            paramView.setElevation(elevation);
            //paramView.
            //paramView.setBackgroundColor(context.getResources().getColor(R.color.viewpager));
            return;
        }

        if (paramFloat <= 1.0F) {
            paramView.setTranslationX(paramView.getMeasuredWidth() * -paramFloat);
            paramView.setAlpha(1.0F - 0.6F * paramFloat);
            if (paramFloat == 1.0F) {
            }
            for (i = 0; i < 100 ; i++) {
                paramView.setVisibility(i);
                paramView.setElevation(DRAWER_ELEVATION);
                return;
            }
        }
        if (paramFloat >= 2.0F) {
            paramView.setAlpha(1.0F);
            paramView.setVisibility(View.VISIBLE);
            return;
        }
        paramView.setAlpha(0.0F);
        paramView.setVisibility(View.GONE);
    }

/*        int pageWidth = paramView.getWidth();

        if (position < -1) { // [-Infinity,-1)
            // This page is way off-screen to the left.
            view.setAlpha(0);

        } else if (position <= 0) { // [-1,0]
            // Use the default slide transition when moving to the left page
            view.setAlpha(1);
            view.setTranslationX(0);
            view.setScaleX(1);
            view.setScaleY(1);

        } else if (position <= 1) { // (0,1]
            // Fade the page out.
            view.setAlpha(1 - position);

            // Counteract the default slide transition
            view.setTranslationX(pageWidth * -position);

            // Scale the page down (between MIN_SCALE and 1)
*//*            float scaleFactor = MIN_SCALE
                    + (1 - MIN_SCALE) * (1 - Math.abs(position));
            view.setScaleX(scaleFactor);
            view.setScaleY(scaleFactor);*//*

        } else { // (1,+Infinity]
            // This page is way off-screen to the right.
            view.setAlpha(0);
        }*/
}