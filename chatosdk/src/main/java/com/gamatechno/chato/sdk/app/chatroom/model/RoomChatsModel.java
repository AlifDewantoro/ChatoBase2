package com.gamatechno.chato.sdk.app.chatroom.model;

import com.gamatechno.chato.sdk.data.DAO.RoomChat.RoomChat;

import java.util.List;

public class RoomChatsModel {
    List<RoomChat> roomChats;

    public RoomChatsModel(List<RoomChat> roomChats) {
        this.roomChats = roomChats;
    }

    public List<RoomChat> getRoomChats() {
        return roomChats;
    }
}
