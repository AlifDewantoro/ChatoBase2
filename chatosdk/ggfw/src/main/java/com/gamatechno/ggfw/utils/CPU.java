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

import android.os.Build;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CPU {
	private static final Map<String, String> cpuinfo = new HashMap<String, String>();
	private static int cachedFeature = -1;
	private static String cachedFeatureString = null;
	public static final int FEATURE_ARM_V5TE =  1 << 0;
	public static final int FEATURE_ARM_V6   =  1 << 1;
	public static final int FEATURE_ARM_VFP  =  1 << 2;
	public static final int FEATURE_ARM_V7A  =  1 << 3;
	public static final int FEATURE_ARM_VFPV3 = 1 << 4;
	public static final int FEATURE_ARM_NEON =  1 << 5;
	public static final int FEATURE_X86      =  1 << 6;
	public static final int FEATURE_MIPS     =  1 << 7;
	
	public static String getFeatureString() {
		getFeature();
		return cachedFeatureString;
	}

	public static int getFeature() {
		if (cachedFeature > 0)
			return getCachedFeature();

		cachedFeature = FEATURE_ARM_V5TE;

		if (cpuinfo.isEmpty()) {
			BufferedReader bis = null;
			try {
				bis = new BufferedReader(new FileReader(new File("/proc/cpuinfo")));
				String line;
				String[] pairs;
				while ((line = bis.readLine()) != null) {
					if (!line.trim().equals("")) {
						pairs = line.split(":");
						if (pairs.length > 1)
							cpuinfo.put(pairs[0].trim(), pairs[1].trim());
					}
				}
			} catch (Exception e) {
				Log.ERROR("getCPUFeature", e);
			} finally {
				try {
					if (bis != null)
						bis.close();
				} catch (IOException e) {
					Log.ERROR("getCPUFeature", e);
				}
			}
		}

		if (!cpuinfo.isEmpty()) {
			for (String key : cpuinfo.keySet())
				Log.DEBUG("%s:%s", key, cpuinfo.get(key));

			boolean hasARMv6 = false;
			boolean hasARMv7 = false;

			String val = cpuinfo.get("CPU architecture");
			if (!TextUtils.isEmpty(val)) {
				try {
					int i = StringUtils.convertToInt(val);
					Log.DEBUG("CPU architecture: %s", i);
					if (i >= 7) {
						hasARMv6 = true;
						hasARMv7 = true;
					} else if (i >= 6) {
						hasARMv6 = true;
						hasARMv7 = false;
					}
				} catch (NumberFormatException ex) {
					Log.ERROR("getCPUFeature", ex);
				}
				
				val = cpuinfo.get("Processor");
				if (TextUtils.isEmpty(val)) {
				    val = cpuinfo.get("model name");
				}
				if (val != null && (val.contains("(v7l)") || val.contains("ARMv7"))) {
					hasARMv6 = true;
					hasARMv7 = true;
				}
				if (val != null && (val.contains("(v6l)") || val.contains("ARMv6"))) {
					hasARMv6 = true;
					hasARMv7 = false;
				}

				if (hasARMv6)
					cachedFeature |= FEATURE_ARM_V6;
				if (hasARMv7)
					cachedFeature |= FEATURE_ARM_V7A;

				val = cpuinfo.get("Features");
				if (val != null) {
					if (val.contains("neon"))
						cachedFeature |= FEATURE_ARM_VFP | FEATURE_ARM_VFPV3 | FEATURE_ARM_NEON;
					else if (val.contains("vfpv3"))
						cachedFeature |= FEATURE_ARM_VFP | FEATURE_ARM_VFPV3;
					else if (val.contains("vfp"))
						cachedFeature |= FEATURE_ARM_VFP;
				}
			} else {
					String vendor_id = cpuinfo.get("vendor_id");
					String mips = cpuinfo.get("cpu model");
				 if (!TextUtils.isEmpty(vendor_id) && vendor_id.contains("GenuineIntel")) {
					 cachedFeature |= FEATURE_X86;
				 } else if (!TextUtils.isEmpty(mips) && mips.contains("MIPS")) {
					 cachedFeature |= FEATURE_MIPS;
				 }
			} 
		}

		return getCachedFeature();
	}

	private static int getCachedFeature() {
		if (cachedFeatureString == null) {
			StringBuffer sb = new StringBuffer();
			if ((cachedFeature & FEATURE_ARM_V5TE) > 0)
				sb.append("V5TE ");
			if ((cachedFeature & FEATURE_ARM_V6) > 0)
				sb.append("V6 ");
			if ((cachedFeature & FEATURE_ARM_VFP) > 0)
				sb.append("VFP ");
			if ((cachedFeature & FEATURE_ARM_V7A) > 0)
				sb.append("V7A ");
			if ((cachedFeature & FEATURE_ARM_VFPV3) > 0)
				sb.append("VFPV3 ");
			if ((cachedFeature & FEATURE_ARM_NEON) > 0)
				sb.append("NEON ");
			if ((cachedFeature & FEATURE_X86) > 0)
				sb.append("X86 ");
			if ((cachedFeature & FEATURE_MIPS) > 0)
				sb.append("MIPS ");
			cachedFeatureString = sb.toString();
		}
		Log.DEBUG("GET CPU FATURE: %s", cachedFeatureString);
		return cachedFeature;
	}

	public static boolean isDroidXDroid2() {
		return (Build.MODEL.trim().equalsIgnoreCase("DROIDX") || Build.MODEL.trim().equalsIgnoreCase("DROID2") || Build.FINGERPRINT.toLowerCase().contains("shadow") || Build.FINGERPRINT.toLowerCase().contains("droid2"));
	}
}
