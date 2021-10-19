package com.gamatechno.chato.sdk.module.service.Firebase;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.gamatechno.chato.sdk.app.chatroom.model.ChatReactionNotifModel;
import com.gamatechno.chato.sdk.data.DAO.Chat.Chat;
import com.gamatechno.chato.sdk.data.DAO.RoomChat.RoomChat;
import com.gamatechno.chato.sdk.data.constant.Preferences;
import com.gamatechno.chato.sdk.data.constant.StringConstant;
import com.gamatechno.chato.sdk.data.model.PublishToRoom;
import com.gamatechno.chato.sdk.utils.ChatoNotification.ChatoNotification;
import com.gamatechno.chato.sdk.utils.ChatoUtils;
import com.gamatechno.ggfw.utils.GGFWUtil;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class ChatoFirebaseService extends FirebaseMessagingService {

    private static final String TAG = "FirebaseMService";
    Gson gson = new Gson();
    ChatoNotification chatoNotification;
    FirebaseServicePresenter presenter;

    Context context;
    int imageNotif;
    ChatReactionNotifModel reactNotif;
    RoomChat roomChat;

    public ChatoFirebaseService(Context context, int imageNotif) {
        this.context = context;
        this.imageNotif = imageNotif;
    }

    public ChatoFirebaseService() {
        this.context = getApplicationContext();
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "onMessageReceived: "+remoteMessage.getData().get("data"));
        String reactionData = remoteMessage.getData().get("reaction_data");

//        Chat chat = gson.fromJson(remoteMessage.getData().get("data"), Chat.class);
        if(presenter == null){
            presenter = new FirebaseServicePresenter(getApplicationContext());
        }

        chatoNotification = new ChatoNotification(getApplicationContext());

        if (reactionData!=null){
            reactNotif = gson.fromJson(remoteMessage.getData().get("data"), ChatReactionNotifModel.class);
            showNewReaction(reactNotif);
        }else {
            roomChat = gson.fromJson(remoteMessage.getData().get("data"), RoomChat.class);
            showPushNotification(roomChat);
        }
    }

    // NOTE : ini dipakai di project eoviz tolong jangan dihapus
    public void onMessageReceived(String s){
        try {
            JSONObject jsonObject = new JSONObject(s);

            if (s.contains("reaction_data")){
                reactNotif = gson.fromJson(jsonObject.getJSONObject("data").toString(), ChatReactionNotifModel.class);
                showNewReaction(reactNotif);
            }else{
                RoomChat roomChat = gson.fromJson(jsonObject.getJSONObject("data").toString(), RoomChat.class);
//        Chat chat = gson.fromJson(remoteMessage.getData().get("data"), Chat.class);

                if(ChatoUtils.getUserLogin(context) != null && roomChat.getDetail_last_message().getFrom_user_id() != ChatoUtils.getUserLogin(context).getUser_id()){
                    if(presenter == null){
                        presenter = new FirebaseServicePresenter(context);
                    }

                    chatoNotification = new ChatoNotification(context, imageNotif);
                    showPushNotification(roomChat);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void showNewReaction(ChatReactionNotifModel data){
        context.sendBroadcast(new Intent(StringConstant.broadcast_receive_chat_reaction).putExtra("data", data));
    }


    private void showPushNotification(RoomChat roomChat){
        if(!GGFWUtil.getStringFromSP(context, Preferences.CHATROOM_STATE).equals(StringConstant.chatroom_state_open)){
            Log.d(TAG, "is " + roomChat.getDetail_last_message().getFrom_username() + " checkusername:" + ChatoUtils.getUserLogin(context).getUser_name() +
                    " Chatroom_state:" + GGFWUtil.getStringFromSP(context, Preferences.CHATROOM_STATE));
            if(!roomChat.getDetail_last_message().getFrom_username().equals(ChatoUtils.getUserLogin(context).getUser_name())) {
                chatoNotification.showPersonChatNotif(roomChat);
                Chat chat = roomChat.getDetail_last_message();
                Log.d(TAG, "is here(Chatroom_state): " + chat.getMessage_text() + " cekroom:" + roomChat.getRoom_id() +
                        " Chatroom_state:" + GGFWUtil.getStringFromSP(context, Preferences.CHATROOM_STATE));
                if (chat.getMessage_type_num() == StringConstant.type_item_label &&
                        chat.getTo_user_id() == ChatoUtils.getUserLogin(context).getUser_id()) {
                    turnOnService();
                    switch (chat.getMessage_action()) {
                        case "ADD":
                            break;
                        case "CHANGE":
                            break;
                        case "DELETE":
                            PublishToRoom publishToRoom = new PublishToRoom("" + roomChat.getRoom_id());
                            context.sendBroadcast(new Intent(StringConstant.chatroom_state_out_from_room).putExtra("data", publishToRoom));
                            break;
                    }
//                sendBroadcast(new Intent(StringConstant.broadcast_refresh_chat));
                    context.sendBroadcast(new Intent(StringConstant.service_requestgroup_tofirebase_register));
                }

                context.sendBroadcast(new Intent(StringConstant.broadcast_refresh_chat));
//            GGFWUtil.setStringToSP(getApplicationContext(), Preferences.CHATROOM_ID_FROM_NOTIF, ""+roomChat.getDetail_last_message().getFrom_user_id());
                sendStatusMessage(chat);
            }
        } else {
            if(GGFWUtil.getStringFromSP(context, Preferences.OPENED_CHATROOM_ID).equals(String.valueOf(roomChat.getRoom_id()))){
                Chat chat = roomChat.getDetail_last_message();
                chat.setRoom_id(String.valueOf(roomChat.getRoom_id()));
                chat.setMessage_status(StringConstant.chat_status_read);
                Log.d(TAG, "is here(Open_chatroom_id): " + chat.getMessage_text());
                if(chat.getFrom_user_id() != ChatoUtils.getUserLogin(context).getUser_id()){
//                    sendStatusReceiveMessage(chat);
                    context.sendBroadcast(new Intent(StringConstant.broadcast_receive_chat).putExtra("data", chat));

                }if(chat.getMessage_type_num() == StringConstant.type_item_label){
                    switch (chat.getMessage_action()){
                        case "ADD":
                            break;
                        case "CHANGE":
                            context.sendBroadcast(new Intent(StringConstant.broadcast_get_update_group_info));
                            break;
                        case "DELETE":
                            break;
                    }
                }
//                sendBroadcast(new Intent(StringConstant.broadcast_receive_chat).putExtra("data", roomChat.getDetail_last_message()));
            } else {
                chatoNotification.showPersonChatNotif(roomChat);
                Chat chat = roomChat.getDetail_last_message();
                Log.d(TAG, "is here: " + chat.getMessage_text() + " cekroom:" + roomChat.getRoom_id());
                if(chat.getMessage_type_num() == StringConstant.type_item_label &&
                        chat.getTo_user_id() == ChatoUtils.getUserLogin(context).getUser_id()){
                    turnOnService();
                    switch (chat.getMessage_action()){
                        case "ADD":
                            break;
                        case "CHANGE":
                            break;
                        case "DELETE":
                            PublishToRoom publishToRoom = new PublishToRoom(""+roomChat.getRoom_id());
                            context.sendBroadcast(new Intent(StringConstant.chatroom_state_out_from_room).putExtra("data",publishToRoom));
                            break;
                    }
                    context.sendBroadcast(new Intent(StringConstant.broadcast_refresh_chat));
                    context.sendBroadcast(new Intent(StringConstant.service_requestgroup_tofirebase_register));
                }
                sendStatusMessage(chat);
            }
        }
    }

    private void turnOnService(){
        if(!GGFWUtil.isServiceRunning(context, ChatoFirebaseService.class)){
            stopService(new Intent(context, ChatoFirebaseService.class));
            startService(new Intent(context, ChatoFirebaseService.class));
        }
//        else {
//            sendBroadcast(new Intent(StringConstant.service_status_on));
//        }
    }

    private void sendStatusMessage(Chat chat){
        Log.d(TAG, "deliveredStatus: " + chat.getMessage_text());
        List<Chat> chats = new ArrayList<>();
        chat.setMessage_status(StringConstant.chat_status_delivered);
        chats.add(chat);
        presenter.sendStatusMessage(chats);
    }

    private void sendStatusReceiveMessage(Chat chat){
        Log.d(TAG, "sendStatus: " + chat.getMessage_text());
        List<Chat> chats = new ArrayList<>();
        chat.setMessage_status(StringConstant.chat_status_read);
        chats.add(chat);
        presenter.sendStatusMessage(chats);
    }
}
