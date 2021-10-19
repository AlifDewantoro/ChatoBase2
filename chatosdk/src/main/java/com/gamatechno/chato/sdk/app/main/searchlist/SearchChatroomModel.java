package com.gamatechno.chato.sdk.app.main.searchlist;

import com.gamatechno.chato.sdk.app.chatrooms.uimodel.ChatRoomsUiModel;
import com.gamatechno.chato.sdk.data.DAO.Chat.Chat;

import java.io.Serializable;

public class SearchChatroomModel implements Serializable {
    ChatRoomsUiModel chatRoomUiModel;
    Chat chat;
    int type;
    String title;

    public SearchChatroomModel(int type, String title) {
        this.type = type;
        this.title = title;
    }

    public SearchChatroomModel() {

    }

    public void setChatRoomUiModel(ChatRoomsUiModel chatRoomUiModel) {
        this.chatRoomUiModel = chatRoomUiModel;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ChatRoomsUiModel getChatRoomUiModel() {
        return chatRoomUiModel;
    }

    public Chat getChat() {
        return chat;
    }

    public int getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    final public static int chatroom_type = 1;
    final public static int message_type = 2;
    final public static int header_type = 3;

}
