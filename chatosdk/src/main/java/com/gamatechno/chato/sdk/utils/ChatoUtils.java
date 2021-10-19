package com.gamatechno.chato.sdk.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.Animation;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.gamatechno.chato.sdk.data.constant.Preferences;
import com.gamatechno.chato.sdk.data.model.UserModel;
import com.gamatechno.chato.sdk.utils.animation.ListenableFuture;
import com.gamatechno.chato.sdk.utils.animation.SettableFuture;
import com.gamatechno.ggfw.utils.GGFWUtil;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.LabelVisibilityMode;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static android.media.MediaMetadataRetriever.METADATA_KEY_DURATION;
import static com.gamatechno.chato.sdk.module.request.GGFWRest.CODE_NETWORKERROR;
import static com.gamatechno.chato.sdk.module.request.GGFWRest.CODE_NOCONNECTIONERROR;
import static com.gamatechno.chato.sdk.module.request.GGFWRest.CODE_SERVERERROR;
import static com.gamatechno.chato.sdk.module.request.GGFWRest.CODE_TIMEOUTERROR;

public class ChatoUtils {
    /*public static void disableShiftMode(BottomNavigationView view) {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);

        try {
            Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
            shiftingMode.setAccessible(true);
            shiftingMode.setBoolean(menuView, false);
            shiftingMode.setAccessible(false);

            for (int i = 0; i < menuView.getChildCount(); i++) {

                BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
                //noinspection RestrictedApi
                item.setShiftingMode(false);
                //noinspection RestrictedApi
                item.setChecked(item.getItemData().isChecked());


            }
        } catch (NoSuchFieldException ex) {
//            Log.ERROR("BottomNavForceTitle", "Unable to get shift mode field", ex);
        } catch (IllegalAccessException e) {
//            Log.ERROR("BottomNavForceTitle", "Unable to change value of shift mode", e);
        }
    }*/

    static Gson gson = new Gson();

    public static String messageError(String error_code){
        switch (error_code){
            case CODE_NETWORKERROR:
                return "Terjadi masalah dengan jaringan Anda";
            case CODE_SERVERERROR:
                return "Terjadi masalah dengan server";
            case CODE_NOCONNECTIONERROR:
                return "Terjadi masalah dengan koneksi Anda";
            case CODE_TIMEOUTERROR:
                return "Koneksi Anda sepertinya sangat lambat";
        }
        return "Sepertinya ada yang bermasalah, silahkan hubungi ";
    }

    @SuppressLint("RestrictedApi")
    public static void removeNavigationShiftMode(BottomNavigationView view) {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
        menuView.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_LABELED);
        menuView.buildMenuView();
    }

    public static int getDP(Context context, Double value){
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        Double fpixels = metrics.density * value;
        int pixels = (int) (fpixels + 0.5f);
        return pixels;
    }

    public static ListenableFuture<Boolean> animateOut(final View view, Animation animation, final int i) {
        final SettableFuture settableFuture = new SettableFuture();
        if (view.getVisibility() == i) {
            settableFuture.set(Boolean.valueOf(true));
        } else {
            view.clearAnimation();
            animation.reset();
            animation.setStartTime(0);
            animation.setAnimationListener(new Animation.AnimationListener() {
                public void onAnimationStart(Animation animation) {
                }

                public void onAnimationRepeat(Animation animation) {
                }

                public void onAnimationEnd(Animation animation) {
                    view.setVisibility(i);
                    settableFuture.set(Boolean.valueOf(true));
                }
            });
            view.startAnimation(animation);
        }
        return settableFuture;
    }

    public static void animateOut(View view, Animation animation) {
        view.clearAnimation();
        animation.reset();
        animation.setStartTime(0);
        animation.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationStart(Animation animation) {
            }

            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationEnd(Animation animation) {

            }
        });
        view.setVisibility(View.GONE);
        view.startAnimation(animation);
    }

    public static void animateIn(View view, Animation animation) {
        if (view.getVisibility() != View.VISIBLE) {
            view.clearAnimation();
            animation.reset();
            animation.setStartTime(0);
            view.setVisibility(View.VISIBLE);
            view.startAnimation(animation);
        }
    }

    /*public static boolean isUserLoggedIn(Context context){
        if(GGFWUtil.getStringFromSP(context, Preferences.ACCESS_TOKEN).equals("")){
            return false;
        } else {
            return true;
        }
    }*/

    public static boolean isPreLolipop(){
        return Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP;
    }

    public static void showKeyboard(final Context context, final EditText ettext){
        ettext.requestFocus();
        ettext.postDelayed(new Runnable(){
                               @Override public void run(){
                                   InputMethodManager keyboard = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
                                   keyboard.showSoftInput(ettext,0);
                               }
                           }
                ,200);
    }

    public static void hideSoftKeyboard(Context context, EditText ettext){
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(ettext.getWindowToken(), 0);
    }

    public static void setUserLogin(Context context, UserModel userModel){
        GGFWUtil.setStringToSP(context, Preferences.USER_LOGIN, gson.toJson(userModel));
    }

    public static UserModel getUserLogin(Context context){
        if (isUserLogin(context)){
            return gson.fromJson(GGFWUtil.getStringFromSP(context, Preferences.USER_LOGIN), UserModel.class);
        }else {
            return null;
        }
    }

    public static boolean isUserLogin(Context context){
        if(GGFWUtil.getStringFromSP(context, Preferences.USER_LOGIN).equals("")){
            return false;
        } else {
            return true;
        }
    }

    public static boolean isUserGroupExist(Context context){
        if(GGFWUtil.getStringFromSP(context, Preferences.USER_GROUP).equals("")){
            return false;
        } else {
            return true;
        }
//        return !GGFWUtil.getStringFromSP(context, Preferences.USER_LOGIN).equals("");
    }

    private static byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    public static byte[] getBytesFile(Context context, Uri uri){
        InputStream iStream = null;
        byte[] inputData = null;
        try {
            iStream = context.getContentResolver().openInputStream(uri);
            inputData = getBytes(iStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return inputData;
    }

    public static boolean isFormValid(Context context, View view, List<Integer> forms, String error){
        boolean isValid = true;
        for(int id: forms)
        {
            EditText et = view.findViewById(id);
            if(TextUtils.isEmpty(et.getText().toString()))
            {
                et.setError(error);
                isValid = false;
            }
        }
        return isValid;
    }

    public static String getVideoDuration(Context context, Uri uri){
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(context, uri);
        String duration = retriever.extractMetadata(METADATA_KEY_DURATION);
        retriever.release();

        if(duration == null){
            return ""+0;
        } else {
            try {
                Double parse = Double.parseDouble(duration)/1000;
//                return Integer.parseInt(""+parse);
                if((""+parse).split("\\.").length > 0){
                    return (""+parse).split("\\.")[0];
                } else {
                    return ""+parse;
                }
            } catch (Exception e){
                return ""+0;
            }
        }
    }
}
