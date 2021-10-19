package com.gamatechno.chato.sdk.data.DAO.Chat;


import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = NotifChat.class, version = 5, exportSchema = false)
public abstract class NotifDB extends RoomDatabase {
    public abstract  ChatNotifAccess db_notif();
}
