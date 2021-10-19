package com.gamatechno.chato.sdk.utils;

import android.graphics.Bitmap;

import com.squareup.picasso.Transformation;

public class BitmapTransform implements Transformation {

    private final int maxWidth;
    private final int maxHeight;
    private static final int MAX_WIDTH = 400;
    private static final int MAX_HEIGHT = 280;

    public BitmapTransform() {
        int size = (int) Math.ceil(Math.sqrt(MAX_WIDTH * MAX_HEIGHT));
        this.maxWidth = size;
        this.maxHeight = size;
    }

    public BitmapTransform(int maxWidth, int maxHeight) {
        this.maxWidth = maxWidth;
        this.maxHeight = maxHeight;
    }

    @Override
    public Bitmap transform(Bitmap source) {
        int targetWidth, targetHeight;
        double aspectRatio;

        if (source.getWidth() > source.getHeight()) {
            targetWidth = maxWidth;
            aspectRatio = (double) source.getHeight() / (double) source.getWidth();
            targetHeight = (int) (targetWidth * aspectRatio);
        } else {
            targetHeight = maxHeight;
            aspectRatio = (double) source.getWidth() / (double) source.getHeight();
            targetWidth = (int) (targetHeight * aspectRatio);
        }

        Bitmap result = Bitmap.createScaledBitmap(source, targetWidth, targetHeight, false);
        if (result != source) {
            source.recycle();
        }
        return result;
    }

    @Override
    public String key() {
        return maxWidth + "x" + maxHeight;
    }

}
