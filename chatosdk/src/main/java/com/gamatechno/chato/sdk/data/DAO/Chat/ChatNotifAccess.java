package com.gamatechno.chato.sdk.data.DAO.Chat;



import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ChatNotifAccess {
    @Insert
    void insertChatNotif(NotifChat chat);

    @Delete
    void deleteChatNotif(NotifChat chat);

    @Insert
    void insertMultiple(List<NotifChat> chats);

    @Query("DELETE FROM NotifChat WHERE room_id = :rm_id")
    void deleteRoomChatNotif(String rm_id);

    @Query("DELETE FROM NotifChat")
    void deleteAllRoomChatNotif();

    @Query("SELECT * FROM NotifChat WHERE room_id = :rm_id")
    List<NotifChat> getChatNotifs(String rm_id);
}
