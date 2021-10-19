package com.gamatechno.ggfw.string;

import android.graphics.Bitmap;
import android.text.TextUtils;

import com.gamatechno.ggfw.string.prettytime.TimeConversion;
import com.gamatechno.ggfw.utils.CustomBase64;


import java.io.ByteArrayOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Map;

public class GGFWString {

    public static TimeConversion TimeConvertion;

    public static String CompleteMonthName(int month) {
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

    public static String ShortMonthName(int month) {
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

    public static boolean isEmailValid(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public static String md5(final String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = MessageDigest
                    .getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String encodeImageToBASE64(Bitmap bmp) {
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, bao);
        byte[] byteArray = bao.toByteArray();
        return CustomBase64.encodeBytes(byteArray);
    }

    public static String DateInMilis() {
        Calendar cal = Calendar.getInstance();
        String calender = String.valueOf(cal.get(Calendar.MILLISECOND));
        return calender;
    }

    public static String ParamConversion(Map<String, String> x){
        String myval = "";
        if(x!=null){
            if(x.size()>0){
                myval = "?";
                int i = 0;
                for (String key : x.keySet() ){
                    myval = myval+key+"="+x.get(key);

                    if(i<x.size()-1){
                        myval = myval+"&";
                    }
                    i++;
                }
            }
        }

        return myval;
    }

}
