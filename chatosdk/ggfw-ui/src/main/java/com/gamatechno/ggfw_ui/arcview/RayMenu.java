/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2018. Shendy Aditya Syamsudin
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

package com.gamatechno.ggfw_ui.arcview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.gamatechno.ggfw_ui.R;

public class RayMenu extends RelativeLayout {
	private RayLayout mRayLayout;

	private ImageView mHintView;

	public RayMenu(Context context) {
		super(context);
		if (!isInEditMode()) {
			init(context);
		}
	}

	public RayMenu(Context context, AttributeSet attrs) {
		super(context, attrs);
		if (!isInEditMode()) {
			init(context);
		}

	}

	private void init(Context context) {
		setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		setClipChildren(false);

		LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		li.inflate(R.layout.ray_menu, this);

		mRayLayout = findViewById(R.id.item_layout);

		final ViewGroup controlLayout = findViewById(R.id.control_layout);
		controlLayout.setClickable(true);
		controlLayout.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					mHintView.startAnimation(createHintSwitchAnimation(mRayLayout.isExpanded()));
					mRayLayout.switchState(true);
				}

				return false;
			}
		});

		mHintView = findViewById(R.id.control_hint);
	}

	public void addItem(View item, OnClickListener listener) {
		mRayLayout.addView(item);
		item.setOnClickListener(getItemClickListener(listener));
	}

	private OnClickListener getItemClickListener(final OnClickListener listener) {
		return new OnClickListener() {

			@Override
			public void onClick(final View viewClicked) {
				Animation animation = bindItemAnimation(viewClicked, true, 400);
				animation.setAnimationListener(new AnimationListener() {

					@Override
					public void onAnimationStart(Animation animation) {

					}

					@Override
					public void onAnimationRepeat(Animation animation) {

					}

					@Override
					public void onAnimationEnd(Animation animation) {
						postDelayed(new Runnable() {

							@Override
							public void run() {
								itemDidDisappear();
							}
						}, 0);
					}
				});

				final int itemCount = mRayLayout.getChildCount();
				for (int i = 0; i < itemCount; i++) {
					View item = mRayLayout.getChildAt(i);
					if (viewClicked != item) {
						bindItemAnimation(item, false, 300);
					}
				}

				mRayLayout.invalidate();
				mHintView.startAnimation(createHintSwitchAnimation(true));

				if (listener != null) {
					listener.onClick(viewClicked);
				}
			}
		};
	}

	private Animation bindItemAnimation(final View child, final boolean isClicked, final long duration) {
		Animation animation = createItemDisapperAnimation(duration, isClicked);
		child.setAnimation(animation);

		return animation;
	}

	private void itemDidDisappear() {
		final int itemCount = mRayLayout.getChildCount();
		for (int i = 0; i < itemCount; i++) {
			View item = mRayLayout.getChildAt(i);
			item.clearAnimation();
		}

		mRayLayout.switchState(false);
	}

	private static Animation createItemDisapperAnimation(final long duration, final boolean isClicked) {
		AnimationSet animationSet = new AnimationSet(true);
		animationSet.addAnimation(new ScaleAnimation(1.0f, isClicked ? 1.5f : 0.0f, 1.0f, isClicked ? 1.5f : 0.0f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f));
		animationSet.addAnimation(new AlphaAnimation(1.0f, 0.0f));

		animationSet.setDuration(duration);
		animationSet.setInterpolator(new DecelerateInterpolator());
		animationSet.setFillAfter(true);

		return animationSet;
	}

	private static Animation createHintSwitchAnimation(final boolean expanded) {
		Animation animation = new RotateAnimation(expanded ? 45 : 0, expanded ? 0 : 45, Animation.RELATIVE_TO_SELF,
				0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		animation.setStartOffset(0);
		animation.setDuration(100);
		animation.setInterpolator(new DecelerateInterpolator());
		animation.setFillAfter(true);

		return animation;
	}

}
