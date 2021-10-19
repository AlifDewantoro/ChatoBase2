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

import java.util.Arrays;
import java.util.Iterator;

public class StringUtils {

	public static int convertToInt(String str) throws NumberFormatException {
		int s, e;
		for (s = 0; s < str.length(); s++)
			if (Character.isDigit(str.charAt(s)))
				break;
		for (e = str.length(); e > 0; e--)
			if (Character.isDigit(str.charAt(e - 1)))
				break;
		if (e > s) {
			try {
				return Integer.parseInt(str.substring(s, e));
			} catch (NumberFormatException ex) {
				Log.ERROR("convertToInt", ex);
				throw new NumberFormatException();
			}
		} else {
			throw new NumberFormatException();
		}
	}

	public static String getShortMonthName(int month) {
		String m = "";
		switch (month) {
			case 0:
				m = "Jan";
				break;
			case 1:
				m = "Feb";
				break;
			case 2:
				m = "Mar";
				break;
			case 3:
				m = "Apr";
				break;
			case 4:
				m = "Mei";
				break;
			case 5:
				m = "Jun";
				break;
			case 6:
				m = "Jul";
				break;
			case 7:
				m = "Agu";
				break;
			case 8:
				m = "Sep";
				break;
			case 9:
				m = "Okt";
				break;
			case 10:
				m = "Nov";
				break;
			case 11:
				m = "Des";
				break;
		}
		return m;
	}

	public static String getCompleteMonthName(int month) {
		String m = "";
		switch (month) {
			case 0:
				m = "Januari";
				break;
			case 1:
				m = "Februari";
				break;
			case 2:
				m = "Maret";
				break;
			case 3:
				m = "April";
				break;
			case 4:
				m = "Mei";
				break;
			case 5:
				m = "Juni";
				break;
			case 6:
				m = "Juli";
				break;
			case 7:
				m = "Agustus";
				break;
			case 8:
				m = "September";
				break;
			case 9:
				m = "Oktober";
				break;
			case 10:
				m = "November";
				break;
			case 11:
				m = "Desember";
				break;
		}
		return m;
	}

}
