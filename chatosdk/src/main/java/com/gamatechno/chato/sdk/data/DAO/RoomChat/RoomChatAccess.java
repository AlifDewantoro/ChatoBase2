package com.gamatechno.chato.sdk.data.DAO.RoomChat;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface RoomChatAccess {
    @Insert
    void insertRoomChat(RoomChat roomChat);
    @Delete
    void deleteRoomChat(RoomChat roomChat);
    @Insert
    void insertMultiple(List<RoomChat> roomChats);
    @Query("SELECT * from RoomChat")
    List<RoomChat> getAllRoomChat();
}
