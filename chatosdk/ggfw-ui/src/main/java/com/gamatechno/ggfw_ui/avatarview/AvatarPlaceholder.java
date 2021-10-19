/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2017. Shendy Aditya Syamsudin
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.gamatechno.ggfw_ui.avatarview;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;

import com.gamatechno.ggfw_ui.avatarview.utils.StringUtils;


public class AvatarPlaceholder extends Drawable {
    public static final String DEFAULT_PLACEHOLDER_STRING = "-";
    private static final String DEFAULT_PLACEHOLDER_COLOR = "#3F51B5";
    private static final String COLOR_FORMAT = "#FF%06X";
    public static final int DEFAULT_TEXT_SIZE_PERCENTAGE = 33;

    private Paint textPaint;
    private Paint backgroundPaint;
    private RectF placeholderBounds;

    private String avatarText;
    private int textSizePercentage;
    private String defaultString;

    private float textStartXPoint;
    private float textStartYPoint;

    public AvatarPlaceholder(String name) {
        this(name, DEFAULT_TEXT_SIZE_PERCENTAGE, DEFAULT_PLACEHOLDER_STRING);
    }

    public AvatarPlaceholder(String name, @IntRange int textSizePercentage) {
        this(name, textSizePercentage, DEFAULT_PLACEHOLDER_STRING);
    }

    public AvatarPlaceholder(String name, @NonNull String defaultString) {
        this(name, DEFAULT_TEXT_SIZE_PERCENTAGE, defaultString);
    }

    public AvatarPlaceholder(String name, @IntRange int textSizePercentage, @NonNull String defaultString) {
        this.defaultString = resolveStringWhenNoName(defaultString);
        this.avatarText = convertNameToAvatarText(name);
        this.textSizePercentage = textSizePercentage;

        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setColor(Color.parseColor("white"));
        textPaint.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));

        backgroundPaint = new Paint();
        backgroundPaint.setAntiAlias(true);
        backgroundPaint.setStyle(Paint.Style.FILL);
        backgroundPaint.setColor(Color.parseColor(convertStringToColor(name)));
    }

    public AvatarPlaceholder(String name, @NonNull int color, int text_color) {
        this.defaultString = resolveStringWhenNoName(DEFAULT_PLACEHOLDER_STRING);
        this.avatarText = convertNameToAvatarText(name);
        this.textSizePercentage = DEFAULT_TEXT_SIZE_PERCENTAGE;
        this.avatarText = convertNameToAvatarText(name);

        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setColor(Color.parseColor("white"));
        textPaint.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));

        backgroundPaint = new Paint();
        backgroundPaint.setAntiAlias(true);
        backgroundPaint.setStyle(Paint.Style.FILL);
        backgroundPaint.setColor(color);
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        if (placeholderBounds == null) {
            placeholderBounds = new RectF(0, 0, canvas.getWidth(), canvas.getHeight());
            setAvatarTextValues();
        }

        canvas.drawRect(placeholderBounds, backgroundPaint);
        canvas.drawText(avatarText, textStartXPoint, textStartYPoint, textPaint);
    }

    @Override
    public void setAlpha(int alpha) {
        textPaint.setAlpha(alpha);
        backgroundPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {
        textPaint.setColorFilter(colorFilter);
        backgroundPaint.setColorFilter(colorFilter);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    private void setAvatarTextValues() {
        textPaint.setTextSize(calculateTextSize());
        textStartXPoint = calculateTextStartXPoint();
        textStartYPoint = calculateTextStartYPoint();
    }

    private float calculateTextStartXPoint() {
        float stringWidth = textPaint.measureText(avatarText);
        return (getBounds().width() / 2f) - (stringWidth / 2f);
    }

    private float calculateTextStartYPoint() {
        return (getBounds().height() / 2f) - ((textPaint.ascent() + textPaint.descent()) / 2f);
    }

    private String resolveStringWhenNoName(String stringWhenNoName) {
        return StringUtils.isNotNullOrEmpty(stringWhenNoName) ? stringWhenNoName : DEFAULT_PLACEHOLDER_STRING;
    }

    private String convertNameToAvatarText(String name) {
        return StringUtils.isNotNullOrEmpty(name) ? name.substring(0, 1).toUpperCase() : defaultString;
    }

    private String convertStringToColor(String text) {
        return StringUtils.isNullOrEmpty(text) ? DEFAULT_PLACEHOLDER_COLOR : String.format(COLOR_FORMAT, (0xFFFFFF & text.hashCode()));
    }

    private float calculateTextSize() {
        if (textSizePercentage < 0 || textSizePercentage > 100) {
            textSizePercentage = DEFAULT_TEXT_SIZE_PERCENTAGE;
        }
        return getBounds().height() * (float) textSizePercentage / 100;
    }
}