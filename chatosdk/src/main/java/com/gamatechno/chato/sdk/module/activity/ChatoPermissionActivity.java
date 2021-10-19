package com.gamatechno.chato.sdk.module.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.gamatechno.chato.sdk.data.constant.StringConstant;
import com.gamatechno.chato.sdk.module.core.ChatoBaseApplication;
import com.gamatechno.chato.sdk.module.service.ChatoService;
import com.gamatechno.chato.sdk.utils.ChatoUtils;
import com.gamatechno.ggfw.Activity.ActivityPermission;
import com.gamatechno.ggfw.utils.GGFWUtil;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class ChatoPermissionActivity extends ActivityPermission {

    final String TAG = this.getClass().getName();
    IntentFilter filter;

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
    public Context getContext() {
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
    }

    protected void startCropActivity(Uri uri){
        CropImage.activity(uri)
                .setCropShape(CropImageView.CropShape.RECTANGLE)
                .start(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(ChatoUtils.isUserLogin(getContext())){
            unregisterReceiver(receiver);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                ChatoBaseApplication.Companion.getInstance().cancelPendingChatoRequest();
                super.onBackPressed();
//                finish();
//                this.onBackPressed();
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
                if(!GGFWUtil.isServiceRunning(getContext(), ChatoService.class)){
                    startService(new Intent(getContext(), ChatoService.class));
                } else {
                    sendBroadcast(new Intent(StringConstant.service_status_on));
                }
            }
        } catch (Exception e){

        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(ChatoUtils.isUserLogin(getContext())){
            if(GGFWUtil.isServiceRunning(getContext(), ChatoService.class)){
                sendBroadcast(new Intent(StringConstant.service_status_stop));
            }
        }
    }
}
