package com.gamatechno.chato.sdk.utils;

import android.content.Context;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import uk.co.chrisjenx.calligraphy.CalligraphyUtils;
import uk.co.chrisjenx.calligraphy.TypefaceUtils;

public class SmartOfficeToolbar extends Toolbar {
    public SmartOfficeToolbar(Context context) {
        super(context);
        setToolBarFont(context);
    }

    public SmartOfficeToolbar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setToolBarFont(context);
    }

    public SmartOfficeToolbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setToolBarFont(context);
    }

    private void setToolBarFont(Context context) {
        for (int i = 0; i < this.getChildCount(); i++) {
            final View child = this.getChildAt(i);
            if (child instanceof TextView) {
                final TextView textView = (TextView) child;
                CalligraphyUtils.applyFontToTextView(textView, TypefaceUtils.load(context.getAssets(), "fonts/SanFransisco-Regular.otf"));
            }
        }
    }
}
