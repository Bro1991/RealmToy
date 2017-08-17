package com.memolease.realmtoy;

import android.content.Context;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by bro on 2017-08-17.
 */

public class TopCropImageView extends ImageView {

    public TopCropImageView(Context context) {
        super(context);
        setScaleType(ScaleType.MATRIX);
    }

    public TopCropImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setScaleType(ScaleType.MATRIX);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected boolean setFrame(int l, int t, int r, int b)

    {

        Matrix matrix = getImageMatrix();

        try {

            float scaleFactor = getWidth() / (float) getDrawable().getIntrinsicWidth();

            matrix.setScale(scaleFactor, scaleFactor, 0, 0);

            setImageMatrix(matrix);

        } catch (Exception e) {
        }

        return super.setFrame(l, t, r, b);

    }
}