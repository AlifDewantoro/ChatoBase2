package com.gamatechno.chato.sdk.module.service.Firebase;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.gamatechno.chato.sdk.data.DAO.Chat.Chat;
import com.gamatechno.chato.sdk.data.DAO.RoomChat.RoomChat;
import com.gamatechno.chato.sdk.data.constant.Preferences;
import com.gamatechno.chato.sdk.data.constant.StringConstant;
import com.gamatechno.chato.sdk.data.model.PublishToRoom;
import com.gamatechno.chato.sdk.utils.ChatoNotification.ChatoNotification;
import com.gamatechno.chato.sdk.utils.ChatoUtils;
import com.gamatechno.ggfw.utils.GGFWUtil;

import java.util.ArrayList;
import java.util.List;

public class FirebaseForSDK {

    private static final String TAG = "ChatFirebaseService";
    ChatoNotification chatoNotification;
    Context context;
    FirebaseServicePresenter presenter;

    public FirebaseForSDK(Context context) {
        this.chatoNotification = new ChatoNotification(getApplicationContext());
        this.context = context;
        presenter = new FirebaseServicePresenter(getApplicationContext());
    }

    private Context getApplicationContext(){
        return context;
    }

    private void showPushNotification(RoomChat roomChat){
        if(!GGFWUtil.getStringFromSP(getApplicationContext(), Preferences.CHATROOM_STATE).equals(StringConstant.chatroom_state_open)){
            if(!roomChat.getDetail_last_message().getFrom_username().equals(ChatoUtils.getUserLogin(context).getUser_name())) {
                chatoNotification.showPersonChatNotif(roomChat);
                Chat chat = roomChat.getDetail_last_message();
                Log.d(TAG, "is here(Chatroom_state): " + chat.getMessage_text() + " cekroom:" + roomChat.getRoom_id() +
                        " Chatroom_state:" + GGFWUtil.getStringFromSP(getApplicationContext(), Preferences.CHATROOM_STATE));
                if (chat.getMessage_type_num() == StringConstant.type_item_label &&
                        chat.getTo_user_id() == ChatoUtils.getUserLogin(getApplicationContext()).getUser_id()) {
//                    turnOnService();
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
            if(GGFWUtil.getStringFromSP(getApplicationContext(), Preferences.OPENED_CHATROOM_ID).equals(String.valueOf(roomChat.getRoom_id()))){
                Chat chat = roomChat.getDetail_last_message();
                chat.setRoom_id(String.valueOf(roomChat.getRoom_id()));
                chat.setMessage_status(StringConstant.chat_status_read);
                Log.d(TAG, "is here(Open_chatroom_id): " + chat.getMessage_text());
                if(chat.getFrom_user_id() != ChatoUtils.getUserLogin(getApplicationContext()).getUser_id()){
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
                        chat.getTo_user_id() == ChatoUtils.getUserLogin(getApplicationContext()).getUser_id()){
//                    turnOnService();
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

    private void sendStatusMessage(Chat chat){
        Log.d(TAG, "deliveredStatus: " + chat.getMessage_text());
        List<Chat> chats = new ArrayList<>();
        chat.setMessage_status(StringConstant.chat_status_delivered);
        chats.add(chat);
        presenter.sendStatusMessage(chats);
    }
}
