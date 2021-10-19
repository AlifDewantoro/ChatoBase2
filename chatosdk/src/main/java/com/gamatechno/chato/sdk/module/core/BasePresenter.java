package com.gamatechno.chato.sdk.module.core;

import android.content.Context;
import android.util.Log;

import com.gamatechno.chato.sdk.data.DAO.Customer.Customer;
import com.gamatechno.chato.sdk.data.constant.Preferences;
import com.gamatechno.ggfw.utils.GGFWUtil;
import com.google.gson.Gson;

public class BasePresenter {
    Context context;

    public Gson gson;
    public Customer customer;

    public BasePresenter(Context context) {
        this.context = context;
        this.gson = new Gson();

        Log.d("BasePresenter", "BasePresenter: "+Preferences.CUSTOMER_INFO);
        Log.d("BasePresenter", "BasePresenter: "+GGFWUtil.getStringFromSP(context, Preferences.CUSTOMER_INFO));
        if(!GGFWUtil.getStringFromSP(context, Preferences.CUSTOMER_INFO).equals("")){
            customer = gson.fromJson(GGFWUtil.getStringFromSP(context, Preferences.CUSTOMER_INFO), Customer.class);
        }
    }

    public Context getContext() {
        return context;
    }

    public Gson getGson() {
        return gson;
    }
}
