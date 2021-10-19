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

package com.gamatechno.ggfw_ui.utils;

import android.app.Dialog;
import android.content.Context;
import androidx.core.content.ContextCompat;
import android.view.Window;
import android.widget.ImageView;

import com.gamatechno.ggfw_ui.R;

/**
 * Project     : SafeTravel
 * Company     : PT. Gamatechno Indonesia
 * File        : XUtils.java
 * User        : Shendy Aditya S. a.k.a xcod
 * Date        : 25 October 2017
 * Time        : 10:09 AM
 */
public class XUtils {

    static Dialog dialog;

    public static void CreateDialog(Context context){
        OpenLoadingDialog(context, R.drawable.ic_logo_gt);
    }

    public static void CreateDialog(Context context, int logo){
        OpenLoadingDialog(context, logo);

    }

    public static void OpenLoadingDialog(Context context, int logo) {
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_loading_default);
        final ImageView imgLogo = dialog.findViewById(R.id.logo);
        imgLogo.setImageResource(logo);
        //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(context,R.drawable.rounded_bg));
        dialog.show();

    }

    public static void CloseLoadingDialog(Context context) {
        if (dialog!=null && dialog.isShowing()){
            dialog.dismiss();
        }


    }
}
