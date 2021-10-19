package com.gamatechno.chato.sdk.utils.animation;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

import androidx.interpolator.view.animation.FastOutSlowInInterpolator;

import com.gamatechno.chato.sdk.R;
import com.gamatechno.chato.sdk.utils.ChatoUtils;

import java.util.Timer;
import java.util.TimerTask;

public class AnimationToggle extends RelativeLayout {
    private View current;
    private Animation inAnimation;
    private Animation outAnimation;

    public AnimationToggle(Context context) {
        super(context);
        this.outAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.animation_toggle_out);
        this.inAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.animation_toggle_in);
        this.outAnimation.setInterpolator(new FastOutSlowInInterpolator());
        this.inAnimation.setInterpolator(new FastOutSlowInInterpolator());
    }

    public AnimationToggle(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.outAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.animation_toggle_out);
        this.inAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.animation_toggle_in);
        this.outAnimation.setInterpolator(new FastOutSlowInInterpolator());
        this.inAnimation.setInterpolator(new FastOutSlowInInterpolator());
    }

    public AnimationToggle(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.outAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.animation_toggle_out);
        this.inAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.animation_toggle_in);
        this.outAnimation.setInterpolator(new FastOutSlowInInterpolator());
        this.inAnimation.setInterpolator(new FastOutSlowInInterpolator());
    }

    public void setInOutAnimation(int inAnimation, int outAnimation){
        this.outAnimation = AnimationUtils.loadAnimation(getContext(), outAnimation);
        this.inAnimation = AnimationUtils.loadAnimation(getContext(), inAnimation);
        this.outAnimation.setInterpolator(new FastOutSlowInInterpolator());
        this.inAnimation.setInterpolator(new FastOutSlowInInterpolator());
    }

    public void addView(View view, int i, LayoutParams layoutParams) {
        super.addView(view, i, layoutParams);
        if (getChildCount() == 1) {
            this.current = view;
            view.setVisibility(View.GONE);
        } else {
            view.setVisibility(View.GONE);
        }
        view.setClickable(false);
    }

    public void display(View view) {
        if (view != this.current) {
            if (this.current != null) {
                ChatoUtils.animateOut(this.current, this.outAnimation);
            }
            if (view != null) {
                ChatoUtils.animateIn(view, this.inAnimation);
            }
            this.current = view;
        }
    }

    public void displaying(View view) {
        ChatoUtils.animateIn(view, this.inAnimation);
    }

    public boolean isDisplaying(View view){
        if(view.getVisibility() == VISIBLE){
            return true;
        } else {
            return false;
        }
    }

    public void hide(View view) {
        ChatoUtils.animateOut(view, this.outAnimation);
    }

    public void hide() {
        ChatoUtils.animateOut(this, this.outAnimation);
    }

    public void show() {
        ChatoUtils.animateIn(this, this.inAnimation);
    }

    public void showForAWhile(Activity activity) {
        show();
        new Timer().schedule(
                new TimerTask() {
                    @Override
                    public void run() {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                hide();
                            }
                        });
                    }
                },
                3000
        );
    }

    public void displayQuick(View view) {
        if (view != this.current) {
            if (this.current != null) {
                this.current.setVisibility(View.GONE);
            }
            if (view != null) {
                view.setVisibility(View.GONE);
            }
            this.current = view;
        }
    }
}
