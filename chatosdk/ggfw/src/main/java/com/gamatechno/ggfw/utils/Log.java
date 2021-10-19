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

import com.gamatechno.ggfw.BuildConfig;

import java.util.MissingFormatArgumentException;

public class Log {
	public static final String TAG = "ggfw[GamaGovFramework]";

	public static void INFO(String msg, Object... args) {
		try {
			if (BuildConfig.DEBUG)
				android.util.Log.i(TAG, String.format(msg, args));
		} catch (MissingFormatArgumentException e) {
			android.util.Log.wtf(TAG, "INFO.Log", e);
			android.util.Log.wtf(TAG, msg);
		}
	}

	public static void DEBUG(String msg, Object... args) {
		try {
			if (BuildConfig.DEBUG) 
				android.util.Log.wtf(TAG, String.format(msg, args));
		} catch (MissingFormatArgumentException e) {
			android.util.Log.wtf(TAG, "DEBUG.Log", e);
			android.util.Log.wtf(TAG, msg);
		}
	}

	public static void ERROR(String msg, Object... args) {
		try {
				android.util.Log.wtf(TAG, String.format(msg, args));
		} catch (MissingFormatArgumentException e) {
			android.util.Log.wtf(TAG, "ERROR.Log", e);
			android.util.Log.wtf(TAG, msg);
		}
	}

	public static void ERROR(String msg, Throwable t) {
		android.util.Log.e(TAG, msg, t);
	}
}
