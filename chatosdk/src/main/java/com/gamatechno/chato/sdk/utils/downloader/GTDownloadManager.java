package com.gamatechno.chato.sdk.utils.downloader;

import android.app.DownloadManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.gamatechno.chato.sdk.R;
import com.gamatechno.chato.sdk.utils.downloader.Exception.GTDownloadException;

import java.util.ArrayList;


public class GTDownloadManager {

    private Context mContext;
    private DownloadManager dm;
    private ArrayList<GTDownloadRequest> mList;
    private GTDownloadCallback mCallback;
    private BroadcastReceiver receiver;

    private int notificationId = 1;

    private static final String CHANNEL_ID = "GTDOWNLOADER";
    private static final String GROUP_GTDOWNLOADER = "NOTIFICATION_GTDOWNLOADER";

    public GTDownloadManager(Context context, GTDownloadCallback callback) {
        this.mContext = context;
        this.mCallback = callback;
        this.receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);

                if (id != -1) {

                    try {
                        GTDownloadRequest downloadRequest = getRequestById(id);
                        mList.remove(downloadRequest);
                        if (mCallback != null) {
                            if (isSuccessful(id)) {
//                                addNotification(downloadRequest,
//                                        mContext.getResources().getString(R.string.download_complete));
                                mCallback.onSuccess(downloadRequest);
                            } else {
//                                addNotification(downloadRequest,
//                                        mContext.getResources().getString(R.string.download_canceled));
                                mCallback.onCancel(downloadRequest);
                            }
                        }
                    } catch (GTDownloadException e) {
                        e.printStackTrace();
                    }

                }
            }
        };
        mContext.registerReceiver(receiver,new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        this.mList = new ArrayList<>();
        this.dm = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
    }



    public void onDestroy(){
        if(mList!=null) {
            mList.clear();
            mList = null;
        }
        mContext.unregisterReceiver(receiver);
    }

    public void startRequest(GTDownloadRequest request) throws GTDownloadException {
        if(checkUriInProcess(request)) {
            throw new GTDownloadException(GTDownloadException.REQUEST_IN_PROCESS);
        } else {
            Long id = dm.enqueue(request.getDownloadRequest());
            request.setId(id);
            mList.add(request);
            if (mCallback != null)
                mCallback.onProcess(request);
        }
    }

    public void cancelDownload(GTDownloadRequest request){
        try {
            GTDownloadRequest downloadRequest = getRequestByUri(request);
            dm.remove(downloadRequest.getId());
        } catch (GTDownloadException e) {
            e.printStackTrace();
        }
    }

    private boolean isSuccessful(long id){
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(id).setFilterByStatus(DownloadManager.STATUS_SUCCESSFUL);
        Cursor cursor = dm.query(query);
        return cursor.moveToFirst();
    }

    private boolean checkUriInProcess(GTDownloadRequest request){
        for(GTDownloadRequest downloadRequest : mList){
            if (downloadRequest.getUri().equals(request.getUri())){
                return true;
            }
        }
        return false;
    }

    private GTDownloadRequest getRequestByUri(GTDownloadRequest request) throws GTDownloadException {
        for(GTDownloadRequest downloadRequest : mList){
            if (downloadRequest.getUri().equals(request.getUri())){
                return downloadRequest;
            }
        }
        throw new GTDownloadException(GTDownloadException.REQUEST_NOT_IN_PROCESS);
    }

    private GTDownloadRequest getRequestById(Long id) throws GTDownloadException {
        for(GTDownloadRequest downloadRequest : mList){
            if (downloadRequest.getId().equals(id)){
                return downloadRequest;
            }
        }
        throw new GTDownloadException(GTDownloadException.REQUEST_NOT_IN_PROCESS);
    }

    private void addNotification(GTDownloadRequest request, String status) {
        int summaryId = 0;
        createNotificationChannel();
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(mContext,CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_app_space)
                        .setContentTitle(request.getFileName())
                        .setContentText(status)
                        .setGroup(GROUP_GTDOWNLOADER);

        Notification summaryNotification =
                new NotificationCompat.Builder(mContext,CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_app_space)
                        .setGroup(GROUP_GTDOWNLOADER)
                        .setGroupSummary(true)
                        .build();

        Intent intent = new Intent(mContext, GTDownloadManager.class);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setDataAndType(request.getDestinationUri(),"*/*");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent contentIntent = PendingIntent.getActivity(mContext, 0, intent, 0);
        builder.setContentIntent(contentIntent);

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(mContext);
        managerCompat.notify(notificationId, builder.build());
        managerCompat.notify(summaryId,summaryNotification);
        notificationId++;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "GT_NOTIFICATION";
            String description = "CHANNEL_DESC";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager manager = mContext.getSystemService(NotificationManager.class);
            assert manager != null;
            manager.createNotificationChannel(channel);
        }
    }
}
