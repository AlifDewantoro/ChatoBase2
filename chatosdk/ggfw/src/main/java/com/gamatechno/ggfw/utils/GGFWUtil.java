package com.gamatechno.ggfw.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import com.google.android.material.snackbar.Snackbar;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.gamatechno.ggfw.BuildConfig;
import com.gamatechno.ggfw.R;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.prefs.Preferences;

public class GGFWUtil {
    private static SharedPreferences sp;
    private static final String SP_NOTATION = "GGFW" + "_sharedPreference_";

    /*public static void SnackLong(View view, String text) {
        Snackbar.make(view, text, Snackbar.LENGTH_LONG).show();
    }

    public static void SnackShort(View view, String text) {
        Snackbar.make(view, text, Snackbar.LENGTH_SHORT).show();
    }*/

    public static void ToastLong(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show();
    }

    public static void ToastShort(Context context, String text) {
        if (context != null) {
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
        }
    }

    public static String volleyError(Context context, VolleyError error) {
        if (error instanceof NetworkError) {
            return context.getResources().getString(R.string.txt_network_error);
        } else if (error instanceof ServerError) {
            return context.getResources().getString(R.string.txt_server_error);
        } else if (error instanceof NoConnectionError) {
            return context.getResources().getString(R.string.txt_NoConnection);
        } else if (error instanceof TimeoutError) {
            return context.getResources().getString(R.string.txt_server_time_out);
        } else if (error instanceof AuthFailureError) {
            return context.getResources().getString(R.string.txt_auth_error);
        }
        return "There's something wrong";
    }

    public static void showVolleyError(Context context, VolleyError error) {
        if (error instanceof NetworkError) {
            ToastShort(context, context.getResources().getString(R.string.txt_network_error));
        } else if (error instanceof ServerError) {
            ToastShort(context, context.getResources().getString(R.string.txt_server_error));
        } else if (error instanceof NoConnectionError) {
            ToastShort(context, context.getResources().getString(R.string.txt_NoConnection));
        } else if (error instanceof TimeoutError) {
            ToastShort(context, context.getResources().getString(R.string.txt_server_time_out));
        } else if (error instanceof AuthFailureError) {
            ToastShort(context, context.getResources().getString(R.string.txt_auth_error));
        }
    }

    public static int getRealDP(Context context, Double value) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        Double fpixels = metrics.density * value;
        int pixels = Double.valueOf((fpixels + 0.5f)).intValue();
        return pixels;
    }

    public static boolean isFormComplete(Context context, View view, List<Integer> forms) {
        boolean isValid = true;
        for (int id : forms) {
            EditText et = view.findViewById(id);
            if (TextUtils.isEmpty(et.getText().toString())) {
                et.setError(context.getResources().getString(R.string.form_empty));
                isValid = false;
            }
        }
        return isValid;
    }

    public static void setBooleanToSP(Context context, String constant, Boolean val) {
        sp = context.getSharedPreferences(SP_NOTATION, 0);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(constant, val);
        editor.commit();
    }

    public static void setStringToSP(Context context, String constant, String val) {
        sp = context.getSharedPreferences(SP_NOTATION, 0);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(constant, val);
        editor.commit();
    }

    public static void setIntToSP(Context context, String constant, int val) {
        sp = context.getSharedPreferences(SP_NOTATION, 0);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(constant, val);
        editor.commit();
    }

    public static int getIntFromSP(Context context, String from) {
        sp = context.getSharedPreferences(SP_NOTATION, 0);
        return sp.getInt(from, 0);
    }

    public static String getStringFromSP(Context context, String from) {
        sp = context.getSharedPreferences(SP_NOTATION, 0);
        return sp.getString(from, "");
    }

    public static void removeStringFromSP(Context context, String to) {
        sp = context.getSharedPreferences(SP_NOTATION, 0);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(to);
        editor.commit();
    }

    public static boolean getBooleanFromSP(Context context, String from) {
        if (context != null) {
            sp = context.getSharedPreferences(SP_NOTATION, 0);
            return sp.getBoolean(from, false);
        } else {
            return false;
        }
    }

    public static boolean getBooleanTrueFromSP(Context context, String from) {
        if (context != null) {
            sp = context.getSharedPreferences(SP_NOTATION, 0);
            return sp.getBoolean(from, true);
        } else {
            return true;
        }
    }

    public static boolean isServiceRunning(Context context, Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                android.util.Log.i("isMyServiceRunning?", true + "");
                return true;
            }
        }
        android.util.Log.i("isMyServiceRunning?", false + "");
        return false;
    }

    public static String convertToBase64(Bitmap bitmap) {
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bao);
        byte[] byteArray = bao.toByteArray();
        return Base64Util.encodeBytes(byteArray);
    }

    public static Bitmap getBitmapFromUri(Context context, Uri uri){
        Bitmap bitmap_image = null;
        try {
//            Log.d(TAG, "handleCropResult: "+uri);
            bitmap_image = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap_image;
    }

    public static boolean isValidURL(String url) {

        URL u = null;

        try {
            u = new URL(url);
        } catch (MalformedURLException e) {
            return false;
        }

        try {
            u.toURI();
        } catch (URISyntaxException e) {
            return false;
        }

        return true;
    }
}
