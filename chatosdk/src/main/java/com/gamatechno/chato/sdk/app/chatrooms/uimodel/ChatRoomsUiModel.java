package com.gamatechno.chato.sdk.app.chatrooms.uimodel;

import com.gamatechno.chato.sdk.data.DAO.RoomChat.RoomChat;

import java.io.Serializable;

public class ChatRoomsUiModel implements Serializable {
    int is_pined = 0;
    RoomChat roomChat;
    boolean is_checked = false;
    String keyword = "";

    public boolean getIs_checked() {
        return is_checked;
    }

    public void setIs_checked(boolean is_checked) {
        this.is_checked = is_checked;
    }

    public ChatRoomsUiModel() {

    }

    public ChatRoomsUiModel(RoomChat roomChat) {
        this.roomChat = roomChat;
    }

    public RoomChat getRoomChat() {
        return roomChat;
    }

    public void setRoomChat(RoomChat roomChat) {
        this.roomChat = roomChat;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public static String type_conversation_person = "D";
    public static String type_conversation_group = "P";
}
