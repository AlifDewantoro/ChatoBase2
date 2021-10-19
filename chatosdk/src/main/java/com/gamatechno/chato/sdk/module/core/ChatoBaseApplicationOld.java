package com.gamatechno.chato.sdk.module.core;

import android.app.Application;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.gamatechno.chato.sdk.R;

public class ChatoBaseApplicationOld extends Application {
    private static final int TIMEOUT_MS = 60000; // 45second

    private RequestQueue requestQueue;
    private static ChatoBaseApplicationOld instance;

    public ChatoBaseApplicationOld() {
        super();
    }
    private int chato_placeholder = R.drawable.ic_placeholder;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public int getChatoPlaceholder(){
        return chato_placeholder;
    }

    public void setChatoPlaceholder(int drawable){
        this.chato_placeholder = drawable;
    }

    public void setUserInfo(int id, String name, String email, String photo){

    }

    public static synchronized ChatoBaseApplicationOld getInstance() {
        return instance;
    }

    public RequestQueue getChatoRequestQueue() {
        Log.d("BaseApplication", "getRequestQueue : ");
        if (requestQueue == null) {
            Log.d("BaseApplication",
                    "getRequestQueue : make new instance ");
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return requestQueue;
    }

    public <T> void addToChatoRequestQueue(Request<T> request, String tag) {
        request.setTag(tag);

        // set retry policy
        request.setRetryPolicy(new DefaultRetryPolicy(TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        request.setShouldCache(false);

        Log.d("BaseApplication",
                "addToRequestQueue : " + request.getUrl());
        getChatoRequestQueue().add(request);
    }

    public void cancelPendingChatoRequest() {
        if (requestQueue != null)
            requestQueue.cancelAll(new RequestQueue.RequestFilter() {
                @Override
                public boolean apply(Request<?> request) {
                    return true;
                }
            });
    }

    public void cancelPendingChatoRequest(Object tag) {
        if (requestQueue != null)
            requestQueue.cancelAll(tag);
    }
}