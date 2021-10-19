package com.gamatechno.chato.sdk.data.DAO.Chat.dbaccess;

import android.content.Context;
import android.util.Log;

import androidx.room.Room;

import com.gamatechno.chato.sdk.data.DAO.Chat.Chat;

import com.gamatechno.chato.sdk.data.DAO.Chat.NotifChat;
import com.gamatechno.chato.sdk.data.DAO.Chat.NotifDB;
import com.gamatechno.chato.sdk.data.constant.StringConstant;

import java.util.List;

public class NotifChatDatabase {
    NotifDB notifDB;
    public NotifChatDatabase(Context context) {
        notifDB = Room.databaseBuilder(context, NotifDB.class, StringConstant.DB_NAME).fallbackToDestructiveMigration().build();
    }

    public List<NotifChat> getNotifbyRoomid(String room_id){
        List<NotifChat> getListMessage = notifDB.db_notif().getChatNotifs(room_id);
        Log.d("MainHelper", "getListMessage: " + getListMessage.size());
        return getListMessage;
    }

    public void addNotif(NotifChat chat){
        notifDB.db_notif().insertChatNotif(chat);
    }

    public void deleteNotifbyRoomId(String room_id){
        new Thread(new Runnable() {
            @Override
            public void run() {
                notifDB.db_notif().deleteRoomChatNotif(room_id);
            }
        }).start();
    }

    public void deleteAllNotif(){
        notifDB.db_notif().deleteAllRoomChatNotif();
    }
}
