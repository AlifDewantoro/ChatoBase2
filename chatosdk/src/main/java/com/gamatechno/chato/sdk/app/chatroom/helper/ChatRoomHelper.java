package com.gamatechno.chato.sdk.app.chatroom.helper;

import com.gamatechno.chato.sdk.data.DAO.Chat.Chat;

import java.util.List;

public class ChatRoomHelper {
    public static Boolean isMessageAvailable(int message_id, List<Chat> items, int total){
        for (Chat chat : items){
//            Log.d("ChatRoomHelper", "items : "+chat.getMessage_id()+", "+message_id);
            if(chat.getMessage_id() == message_id){
                return true;
            }
        }
        return false;
    }

    public static int indexRedirect(int message_id, List<Chat> items){
        for (int i = 0; i < items.size(); i++) {
//            Log.d("ChatRoomHelper", "items : "+items.get(i).getMessage_id()+", "+message_id);
            if(items.get(i).getMessage_id() == message_id){
                return i;
            }
        }
        return 0;
    }
}
