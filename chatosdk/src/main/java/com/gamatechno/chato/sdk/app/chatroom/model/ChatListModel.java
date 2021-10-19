package com.gamatechno.chato.sdk.app.chatroom.model;

import com.gamatechno.chato.sdk.data.DAO.Chat.Chat;

import java.io.Serializable;
import java.util.List;

public class ChatListModel implements Serializable {
    List<Chat> chatList;

    public ChatListModel(List<Chat> chatList) {
        this.chatList = chatList;
    }

    public List<Chat> getChatList() {
        return chatList;
    }
}
