package com.gamatechno.chato.sdk.module.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.IBinder;
import androidx.core.app.RemoteInput;
import android.util.Log;

import com.gamatechno.chato.sdk.app.chatroom.model.ChatListModel;
import com.gamatechno.chato.sdk.data.DAO.Chat.Chat;
import com.gamatechno.chato.sdk.data.DAO.RoomChat.RoomChat;
import com.gamatechno.chato.sdk.data.model.ListentoRoomModel;
import com.gamatechno.chato.sdk.data.model.PublishToRoom;
import com.gamatechno.chato.sdk.data.constant.Preferences;
import com.gamatechno.chato.sdk.data.constant.StringConstant;
import com.gamatechno.chato.sdk.utils.ChatoNotification.ChatoNotification;
import com.gamatechno.chato.sdk.utils.ChatoUtils;
import com.gamatechno.ggfw.utils.GGFWUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.annotation.Nullable;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

public class ChatoService extends Service implements ChatoServiceView.View, ChatServiceView.View {

    final String TAG = "MainService";

    ChatServicePresenter chatServicePresenter;
    ChatoServicePresenter chatoServicePresenter;
    IntentFilter filter;

    ChatoNotification chatoNotification;
    private PublishSubject<String> publish_state;

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            Log.d(TAG, "Receiving....");

            if(action.contains("chat")){
                if(chatServicePresenter != null){
                    chatServicePresenter.checkTokenAvailibillity(false);
                }

                if (action.equals(StringConstant.chat_send_message)) {
                    String param = intent.getStringExtra("param");
                    Log.d(TAG, "onReceive Param: "+param);
                } else if (action.equals(StringConstant.chat_retreive_person_message)) {
                    Log.d(TAG, "onReceive: chat_retreive_person_message");
                    RoomChat roomChat = (RoomChat) intent.getSerializableExtra("data");
                    Chat chat = roomChat.getDetail_last_message();
                    Bundle remoteInput = RemoteInput.getResultsFromIntent(intent);

                    if (remoteInput != null) {
                        CharSequence charSequence = remoteInput.getCharSequence(
                                "" + chat.getFrom_user_id());
                        if (charSequence != null) {
                            Log.d(TAG, "onReceive: "+charSequence.toString());
                            chatServicePresenter.sendMessage(charSequence.toString(), roomChat);
                        }
                    }

                } else if(action.equals(StringConstant.chatroom_state_close_notif)){
                    int param = Integer.valueOf(intent.getStringExtra("data"));
                    chatoNotification.cancelNotif(param);
                }/* else if(action.equals(StringConstant.chatroom_state_send_notif)){
                    Chat chat = (Chat) intent.getSerializableExtra("data");
                    chatoNotification.showPersonChatNotif(chat);
                }*/ else if(action.equals(StringConstant.chatroom_state_publish_to_room)){
                    PublishToRoom publishToRoom = (PublishToRoom) intent.getSerializableExtra("data");
                    initPublishToRoom(publishToRoom);
                } else if(action.equals(StringConstant.chatroom_state_open_to_room)){
                    PublishToRoom publishToRoom = (PublishToRoom) intent.getSerializableExtra("data");
                    chatServicePresenter.joinRoom(publishToRoom);
                }
            } else if(action.contains("service")){
                if (action.equals(StringConstant.service_status_on)) {
                    Log.d(TAG, "onReceive: im on running");
                    chatoServicePresenter.resumeService();
                    if(chatServicePresenter != null)
                        chatServicePresenter.checkSocket();
                } else if(action.equals(StringConstant.service_status_stop)){
                    Log.d(TAG, "onReceive: im on stopping");
                    chatoServicePresenter.stopService();
                } else if(action.equals(StringConstant.service_check_stop)){
                    Log.d(TAG, "onReceive: im on resuming");
                    chatoServicePresenter.resumeService();
                } else if(action.equals(StringConstant.service_requestgroup_tofirebase_register)){
                    Log.d(TAG, "onReceive: requesting list groupexist");
                    chatoServicePresenter.requestListGroup();
                } else if(action.equals(StringConstant.service_requestgroup_tofirebase_register)){
                    Log.d(TAG, "onReceive: requesting list groupexist");
                    chatoServicePresenter.requestListGroup();
                }
            } else if(action.contains("call")){
//                GTCallModel param = (GTCallModel) intent.getSerializableExtra(StringConstant.call_broadcast);
//                chatServicePresenter.sendSocketData(new CallRequestParam(param.getRoom_id(), param));
            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        chatServicePresenter = new ChatServicePresenter(this, this);
        chatoServicePresenter = new ChatoServicePresenter(this, this);

        filter = new IntentFilter();
        filter.addAction(StringConstant.chat_send_message);
        filter.addAction(StringConstant.chat_retreive_person_message);
        filter.addAction(StringConstant.service_status_on);
        filter.addAction(StringConstant.service_status_stop);
        filter.addAction(StringConstant.service_check_stop);
        filter.addAction(StringConstant.service_requestgroup_tofirebase_register);
        filter.addAction(StringConstant.chatroom_state_close_notif);
        filter.addAction(StringConstant.chatroom_state_send_notif);
        filter.addAction(StringConstant.chatroom_state_publish_to_room);
        filter.addAction(StringConstant.chatroom_state_open_to_room);
        filter.addAction(StringConstant.service_killself);
        filter.addAction(StringConstant.chatroom_state_out_from_room);
        filter.addAction(StringConstant.call_broadcast);

        chatoNotification = new ChatoNotification(this);

        publish_state = PublishSubject.create();
        publish_state.debounce(4, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                // Be notified on the main thread
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String s) {
//                        Log.d(TAG, "onNextText2: "+s);
                        publish.setType(s);
                        chatServicePresenter.publishToRoom(publish);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

        registerReceiver(receiver, filter);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
//        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
        Log.d(TAG, "SocketService OnDestroy()");
    }

    @Override
    public void onCheckTokenAvailibillity(boolean isLogin) {
        if(isLogin){
            chatServicePresenter.checkSocket();
        }
    }

    @Override
    public void onReceiveMessage(RoomChat roomChat) {
        Chat chat = roomChat.getDetail_last_message();
        if (!GGFWUtil.getStringFromSP(this, Preferences.CHATROOM_STATE).equals(StringConstant.chatroom_state_open)) {
            if(chat.getMessage_type_num() == StringConstant.type_item_label &&
                    chat.getTo_user_id() == ChatoUtils.getUserLogin(getApplicationContext()).getUser_id()){
                switch (chat.getMessage_action()){
                    case "ADD":
                        break;
                    case "DELETE":
                        chatServicePresenter.leaveRoom(new PublishToRoom(""+roomChat.getRoom_id()));
                        break;
                }
                chatoServicePresenter.requestListGroup();
            }
            chatoNotification.showPersonChatNotif(roomChat);
            GGFWUtil.setStringToSP(this, Preferences.CHATROOM_ID_FROM_NOTIF, "" + chat.getRoom_id());
            sendBroadcast(new Intent(StringConstant.broadcast_refresh_chat));
        } else {
            if (GGFWUtil.getStringFromSP(this, Preferences.OPENED_CHATROOM_ID).equals(String.valueOf(roomChat.getRoom_id()))) {
                if (chat.getFrom_user_id() != ChatoUtils.getUserLogin(getApplicationContext()).getUser_id()) {
                    if(chat.getMessage_type_num() == StringConstant.type_item_label &&
                            chat.getTo_user_id() == ChatoUtils.getUserLogin(getApplicationContext()).getUser_id()){
                        switch (chat.getMessage_action()){
                            case "ADD":
                                break;
                            case "DELETE":
                                chatServicePresenter.leaveRoom(new PublishToRoom(""+roomChat.getRoom_id()));
                                break;
                        }
                        sendBroadcast(new Intent(StringConstant.broadcast_get_update_group_info));
                        chatoServicePresenter.requestListGroup();
                    }
                    sendBroadcast(new Intent(StringConstant.broadcast_receive_chat).putExtra("data", chat));
                }
            } else {
                if(chat.getMessage_type_num() == StringConstant.type_item_label &&
                        chat.getTo_user_id() == ChatoUtils.getUserLogin(getApplicationContext()).getUser_id()){
                    switch (chat.getMessage_action()){
                        case "ADD":
                            break;
                        case "DELETE":
                            chatServicePresenter.leaveRoom(new PublishToRoom(""+roomChat.getRoom_id()));
                            break;
                    }
                    chatoServicePresenter.requestListGroup();
                }
                chatoNotification.showPersonChatNotif(roomChat);
                sendBroadcast(new Intent(StringConstant.broadcast_refresh_chat));
            }
        }
        sendStatusMessage(chat);
    }

    private void sendStatusMessage(Chat chat){
        Log.d(TAG, "deliveredStatus: " + chat.getMessage_text());
        List<Chat> chats = new ArrayList<>();
        chat.setMessage_status(StringConstant.chat_status_delivered);
        chats.add(chat);
        chatServicePresenter.sendStatusMessage(chats);
    }

    @Override
    public void onReceiveUpdateStatusChat(List<Chat> chatList) {
        sendBroadcast(new Intent(StringConstant.broadcast_receive_status_chat).putExtra("data", new ChatListModel(chatList)));
    }

    @Override
    public void onSendMessage(int id) {
        GGFWUtil.ToastShort(this, "Pesan terkirim");
        chatoNotification.cancelNotif(id);
        sendBroadcast(new Intent(StringConstant.broadcast_refresh_chat));
    }

    @Override
    public void onFailedSendMessage(String x) {
        GGFWUtil.ToastShort(this, x);
    }

    @Override
    public void onListenToRoom(ListentoRoomModel model) {
        sendBroadcast(new Intent(StringConstant.broadcast_listen_to_room).putExtra("data", model));
    }

    @Override
    public void onSendSocketData(String message) {

    }

    @Override
    public void onListenCallSocket(RoomChat roomChat) {
        sendBroadcast(new Intent(StringConstant.call_broadcast_response).putExtra("data", roomChat));
    }

    @Override
    public void onStopChatService() {
        Log.d(TAG, "onStopService: i'm trying to stop");
        if(chatServicePresenter!=null){
            chatServicePresenter.disconnectSocket();

        }
    }

    @Override
    public void onRequestListGroup(List<RoomChat> rooms) {
        Log.d(TAG, "onRequestListGroup: groupexist "+rooms.size());
        chatServicePresenter.registerGrouptoFirebase(rooms);
    }

    PublishToRoom publish;
    private void initPublishToRoom(PublishToRoom publishToRoom){
        publish = publishToRoom;
//        Log.d(TAG, "onNextText1: "+publish.getType());
        chatServicePresenter.publishToRoom(publish);
        publish_state.onNext(ListentoRoomModel.STATE_ONLINE);
    }
}
