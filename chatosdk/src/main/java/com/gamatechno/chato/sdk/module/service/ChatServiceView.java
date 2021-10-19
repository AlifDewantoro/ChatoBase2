package com.gamatechno.chato.sdk.module.service;

//import com.gamatechno.chato.sdk.app.call.model.CallRequestParam;
import com.gamatechno.chato.sdk.data.DAO.Chat.Chat;
import com.gamatechno.chato.sdk.data.DAO.Chat.NotifChat;
import com.gamatechno.chato.sdk.data.DAO.RoomChat.RoomChat;
import com.gamatechno.chato.sdk.data.model.ListentoRoomModel;
import com.gamatechno.chato.sdk.data.model.PublishToRoom;

import java.util.List;

public interface ChatServiceView {
    interface Presenter{
        void checkSocket();
        void checkTokenAvailibillity(boolean isForceToReq);
        void sendStatusMessage(List<NotifChat> notifChats, OnSendStatusMessage onSendStatusMessage);
        void sendMessage(String message, RoomChat roomChat);
        void publishToRoom(PublishToRoom publishToRoom);
        void leaveRoom(PublishToRoom publishToRoom);
        void joinRoom(PublishToRoom publishToRoom);
        void sendStatusMessage(List<Chat> chatList);
        void registerGrouptoFirebase(List<RoomChat> roomChats);
        void disconnectSocket();
//        void sendSocketData(CallRequestParam callRequestParam);
    }

    interface OnSendStatusMessage{
        void onAfterSendStatus();
    }

    interface View{
        void onCheckTokenAvailibillity(boolean isLogin);
        void onReceiveMessage(RoomChat roomChat);
        void onReceiveUpdateStatusChat(List<Chat> chatList);
        void onSendMessage(int id);
        void onFailedSendMessage(String x);
        void onListenToRoom(ListentoRoomModel model);
        void onSendSocketData(String message);
        void onListenCallSocket(RoomChat roomChat);
    }
}
