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
package com.gamatechno.ggfw.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Pair;
import android.view.Display;
import android.view.WindowManager;

import java.lang.reflect.Method;

/**
 * Class to get the real screen resolution includes the system status bar.
 * We can get the value by calling the getRealMetrics method if API >= 17
 * Reflection needed on old devices..
 * */
public class ScreenResolution {
  /**
   * Gets the resolution,
   * @return a pair to return the width and height
   * */
  public static Pair<Integer,Integer> getResolution(Context ctx){
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
      return getRealResolution(ctx);
    }
    else {
      return getRealResolutionOnOldDevice(ctx);
    }
  }

  /**
   * Gets resolution on old devices.
   * Tries the reflection to get the real resolution first.
   * Fall back to getDisplayMetrics if the above method failed.
   * */
  private static Pair<Integer, Integer> getRealResolutionOnOldDevice(Context ctx) {
    try{
      WindowManager wm = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
      Display display = wm.getDefaultDisplay();
      Method mGetRawWidth = Display.class.getMethod("getRawWidth");
      Method mGetRawHeight = Display.class.getMethod("getRawHeight");
      Integer realWidth = (Integer) mGetRawWidth.invoke(display);
      Integer realHeight = (Integer) mGetRawHeight.invoke(display);
      return new Pair<Integer, Integer>(realWidth, realHeight);
    }
    catch (Exception e) {
      DisplayMetrics disp = ctx.getResources().getDisplayMetrics();
      return new Pair<Integer, Integer>(disp.widthPixels, disp.heightPixels);
    }
  }

  /**
   * Gets real resolution via the new getRealMetrics API.
   * */
  @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
  private static Pair<Integer,Integer> getRealResolution(Context ctx) {
    WindowManager wm = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
    Display display = wm.getDefaultDisplay();
    DisplayMetrics metrics = new DisplayMetrics();
    display.getRealMetrics(metrics);
    return new Pair<Integer, Integer>(metrics.widthPixels, metrics.heightPixels);
  }
}
