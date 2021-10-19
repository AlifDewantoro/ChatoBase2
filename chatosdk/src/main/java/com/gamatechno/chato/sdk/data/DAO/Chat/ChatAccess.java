package com.gamatechno.chato.sdk.data.DAO.Chat;



import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;

import java.util.List;

@Dao
public interface ChatAccess {
    @Insert
    void insertRoomChat(Chat chat);
    @Delete
    void deleteRoomChat(Chat chat);
    @Insert
    void insertMultiple(List<Chat> chats);

//    @Query("SELECT * from Chat")
//    List<Chat> getAllChat();
}
