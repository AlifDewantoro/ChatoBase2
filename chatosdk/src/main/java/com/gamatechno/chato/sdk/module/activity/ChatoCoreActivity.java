package com.gamatechno.chato.sdk.module.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.gamatechno.chato.sdk.data.constant.StringConstant;
import com.gamatechno.chato.sdk.module.core.ChatoBaseApplication;
import com.gamatechno.chato.sdk.module.service.ChatoService;
import com.gamatechno.chato.sdk.utils.Loading;
import com.gamatechno.chato.sdk.utils.ChatoUtils;
import com.gamatechno.ggfw.Activity.ActivityGeneral;
import com.gamatechno.ggfw.utils.GGFWUtil;

public class ChatoCoreActivity extends ActivityGeneral {

    final String TAG = this.getClass().getName();
    IntentFilter filter;
    Boolean isRegistered = false;
    public Loading loading;

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.d(TAG, "Receiving....");

            if(action.contains("activity")){
                if (action.equals(StringConstant.activity_check_stop)) {
                    sendBroadcast(new Intent(StringConstant.service_check_stop));
                }
            }
        }
    };

    @Override
    protected Context getContext() {
        return super.getContext();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/SanFransisco-Regular.otf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );*/

        filter = new IntentFilter();
        filter.addAction(StringConstant.activity_check_stop);

        loading = new Loading(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                ChatoBaseApplication.Companion.getInstance().cancelPendingChatoRequest();
                this.onBackPressed();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        ChatoBaseApplication.Companion.getInstance().cancelPendingChatoRequest();
        super.onBackPressed();
    }

    /*@Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }*/

    @Override
    protected void onResume() {
        super.onResume();
        try {
            if(ChatoUtils.isUserLogin(getContext())){
                registerReceiver(receiver, filter);
                isRegistered = true;
                if(!GGFWUtil.isServiceRunning(getContext(), ChatoService.class)){
                    stopService(new Intent(getContext(), ChatoService.class));
                    startService(new Intent(getContext(), ChatoService.class));
                } else {
                    sendBroadcast(new Intent(StringConstant.service_status_on));
                }
            }
        } catch (IllegalStateException e){

        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(ChatoUtils.isUserLogin(getContext())){
            if(isRegistered){
                isRegistered = false;
                unregisterReceiver(receiver);
                if(GGFWUtil.isServiceRunning(getContext(), ChatoService.class)){
                    sendBroadcast(new Intent(StringConstant.service_status_stop));
                }
            }
        }
    }
}
