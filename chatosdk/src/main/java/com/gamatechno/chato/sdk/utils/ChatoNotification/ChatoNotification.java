package com.gamatechno.chato.sdk.utils.ChatoNotification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.RemoteInput;
import androidx.core.content.ContextCompat;

import com.gamatechno.chato.sdk.BuildConfig;
import com.gamatechno.chato.sdk.R;
import com.gamatechno.chato.sdk.app.chatroom.ChatRoomActivity;
import com.gamatechno.chato.sdk.data.DAO.Chat.Chat;
import com.gamatechno.chato.sdk.data.DAO.Chat.NotifChat;
import com.gamatechno.chato.sdk.data.DAO.Chat.dbaccess.NotifChatDatabase;
import com.gamatechno.chato.sdk.data.DAO.RoomChat.RoomChat;
import com.gamatechno.chato.sdk.data.constant.StringConstant;
import com.gamatechno.chato.sdk.data.model.UserModel;
import com.gamatechno.chato.sdk.module.service.ChatoService;
import com.gamatechno.chato.sdk.utils.ChatoUtils;
import com.gamatechno.chato.sdk.utils.DetectHtml;
import com.gamatechno.ggfw.utils.GGFWUtil;

public class ChatoNotification {
    private Context context;
    private ChatoNotification instance;
    private PendingIntent pendingIntent;
    private UserModel userModel;
    NotificationManager notificationManager;
    NotificationCompat.Builder notificationBuilder;
    boolean isIconModified = false;
    int image_notif;

    NotifChatDatabase chatNotifDatabase;

    public ChatoNotification(Context context) {
        this.context = context;
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        userModel = ChatoUtils.getUserLogin(context);
        chatNotifDatabase = new NotifChatDatabase(context);
    }

    public ChatoNotification(Context context, int image_notif) {
        this.context = context;
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        userModel = ChatoUtils.getUserLogin(context);
        this.isIconModified = true;
        this.image_notif = image_notif;
        chatNotifDatabase = new NotifChatDatabase(context);
    }

    public void cancelNotif(int id){
        notificationManager.cancel(id);
    }

    private String messageText(RoomChat roomChat){
        Chat chat = roomChat.getDetail_last_message();
        String text = "";
        if(roomChat.getRoom_type().equals(RoomChat.group_room_type) &&
                roomChat.getDetail_last_message().getFrom_user_id() != userModel.getUser_id())
            text += chat.getFrom_username() + " : ";

        switch (chat.getMessage_type()){
            case RoomChat.image_type:
                text = text+context.getResources().getString(R.string.emoji_photo);
                if(chat.getMessage_text().equals(" ")){
                    text = text+"(Foto)";
                } else {
                    text = text+" "+ DetectHtml.convertHtmlToPlain(chat.getMessage_text());
                }
                break;
            case RoomChat.file_type:
                text = text+context.getResources().getString(R.string.emoji_file);
                if(chat.getMessage_text().equals(" ")){
                    text = text+"(Berkas)";
                } else {
                    text = text+" "+DetectHtml.convertHtmlToPlain(chat.getMessage_text());
                }
                break;
            case RoomChat.video_type:
                text = text+context.getResources().getString(R.string.emoji_video);
                if(chat.getMessage_text().equals(" ")){
                    text = text+"(Video)";
                } else {
                    text = text+" "+DetectHtml.convertHtmlToPlain(chat.getMessage_text());
                }
                break;
            default:
                text = text+DetectHtml.convertHtmlToPlain(chat.getMessage_text());
                break;
        }
        return text;
    }

    private String messageText(NotifChat chat){
        String text = "";
        if(chat.getRoom_type().equals(RoomChat.group_room_type) &&
                chat.getFrom_user_id() != userModel.getUser_id())
            text += chat.getFrom_username() + " : ";

        switch (chat.getMessage_type()){
            case RoomChat.image_type:
                text = text+context.getResources().getString(R.string.emoji_photo);
                if(chat.getMessage_text().equals(" ")){
                    text = text+"(Foto)";
                } else {
                    text = text+" "+DetectHtml.convertHtmlToPlain(chat.getMessage_text());
                }
                break;
            case RoomChat.file_type:
                text = text+context.getResources().getString(R.string.emoji_file);
                if(chat.getMessage_text().equals(" ")){
                    text = text+"(Berkas)";
                } else {
                    text = text+" "+DetectHtml.convertHtmlToPlain(chat.getMessage_text());
                }
                break;
            case RoomChat.video_type:
                text = text+context.getResources().getString(R.string.emoji_video);
                if(chat.getMessage_text().equals(" ")){
                    text = text+"(Video)";
                } else {
                    text = text+" "+DetectHtml.convertHtmlToPlain(chat.getMessage_text());
                }
                break;
            default:
                text = text+DetectHtml.convertHtmlToPlain(chat.getMessage_text());
                break;
        }
        return text;
    }


    public void showPersonChatNotif(RoomChat roomChat){
        Chat chat = roomChat.getDetail_last_message();
        chat.setRoom_id(""+roomChat.getRoom_id());

        NotifChat notifChat = new NotifChat(chat.getMessage_id(), chat.getFrom_user_id(), chat.getFrom_username(), chat.getFrom_username_photo(), chat.getTo_user_id(), chat.getTo_username(),
                chat.getTo_username_photo(), chat.getMessage_text(), chat.getMessage_type(), chat.getMessage_attachment(), chat.getMessage_status(), chat.getMessage_date(),
                chat.getMessage_time(), chat.getPayload());

        notifChat.setRoom_type(""+roomChat.getRoom_type());
        notifChat.setRoom_id(""+roomChat.getRoom_id());

        String GROUP_KEY = BuildConfig.LIBRARY_PACKAGE_NAME+"_"+roomChat.getRoom_id();
        String NOTIFICATION_CHANNEL_ID = ""+chat.getChatId();
//        Uri soundUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + context.getPackageName() + "/" + R.raw.chato_sound);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build();

            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, BuildConfig.application_name+" Notifications", NotificationManager.IMPORTANCE_HIGH);
            // Configure the notification channel.
            notificationChannel.setDescription(BuildConfig.application_name);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(ContextCompat.getColor(context, R.color.colorPrimary));
//            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
//            notificationChannel.setVibrationPattern(new long[]{0, 1000}); TODO : here vibration
            notificationChannel.enableVibration(true);
//            notificationChannel.setSound(soundUri, audioAttributes);
            notificationChannel.setImportance(NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(notificationChannel);

        }

        NotificationCompat.InboxStyle inboxStyle =
                new NotificationCompat.InboxStyle();
//        inboxStyle.setBigContentTitle(messageText(notifChat));


        for(NotifChat notifChat1 : chatNotifDatabase.getNotifbyRoomid(""+roomChat.getRoom_id())){
            inboxStyle.addLine(messageText(notifChat1));
        }
        inboxStyle.addLine(messageText(notifChat));

        notificationBuilder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID);
//        .setSound(soundUri)
        notificationBuilder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_app_space)
                .setTicker(BuildConfig.application_name)
                //     .setPriority(Notification.PRIORITY_MAX)
                .setContentTitle(roomChat.getRoom_name())
                .setContentText(messageText(notifChat))
                .setGroup(GROUP_KEY)
                .setGroupSummary(true)
                .setContentInfo(BuildConfig.LIBRARY_PACKAGE_NAME)
                .setStyle(inboxStyle);

        if(isIconModified) notificationBuilder.setSmallIcon(image_notif);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            notificationBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);
        }

        String replyLabel = "Enter your reply here";

        Intent resultIntent = new Intent(context, ChatRoomActivity.class);
        resultIntent.putExtra(StringConstant.notification_message, roomChat);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        //Set a unique request code for this pending intent
        PendingIntent resultPendingIntent = PendingIntent.getActivity(context, chat.getFrom_user_id(), resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        notificationBuilder.setContentIntent(resultPendingIntent);

        if(!GGFWUtil.isServiceRunning(context, ChatoService.class)){
            context.startService(new Intent(context, ChatoService.class));
        }

        Intent replyIntent = new Intent(StringConstant.chat_retreive_person_message);
        replyIntent.putExtra("data", roomChat);
        PendingIntent replyPendingIntent =
                PendingIntent.getBroadcast(context,
                        chat.getFrom_user_id(),
                        replyIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);
        //Initialise RemoteInput
        RemoteInput remoteInput = new RemoteInput.Builder(""+chat.getFrom_user_id())
                .setLabel(replyLabel)
                .build();

        NotificationCompat.Action replyAction = new NotificationCompat.Action.Builder(
                android.R.drawable.sym_action_chat, "BALAS", replyPendingIntent)
                .addRemoteInput(remoteInput)
                .setAllowGeneratedReplies(true)
                .build();
        if(!roomChat.getRoom_type().equals(RoomChat.official_room_type)){
            notificationBuilder.addAction(replyAction);
        }


        Log.d("SmartOfficeNotification", "initData: "+chat.getRoom_id());
        notificationManager.notify(Integer.parseInt(chat.getRoom_id()),
                notificationBuilder.build());

        chatNotifDatabase.addNotif(notifChat);
    }

}
