package com.gamatechno.chato.sdk.module.service;

import com.gamatechno.chato.sdk.data.DAO.RoomChat.RoomChat;

import java.util.List;

public interface ChatoServiceView {
    interface Presenter{
        void resumeService();
        void requestListGroup();
        void stopService();
    }

    interface View{
        void onStopChatService();
        void onRequestListGroup(List<RoomChat> rooms);
    }
}
