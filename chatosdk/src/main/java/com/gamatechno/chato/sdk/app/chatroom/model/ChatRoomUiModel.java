package com.gamatechno.chato.sdk.app.chatroom.model;

import com.gamatechno.chato.sdk.data.DAO.RoomChat.RoomChat;

import java.io.Serializable;

public class ChatRoomUiModel implements Serializable {
    String user_id;
    String title;
    String avatar;
    String room_id = "";
    String room_code = "";
    String type = RoomChat.user_room_type;
    int is_admin = 0;

    public ChatRoomUiModel(String user_id, String title, String avatar) {
        this.user_id = user_id;
        this.title = title;
        this.avatar = avatar;
    }

    public ChatRoomUiModel(String user_id, String title, String avatar, String room_id) {
        this.user_id = user_id;
        this.title = title;
        this.avatar = avatar;
        this.room_id = room_id;
    }

    public ChatRoomUiModel(String user_id, String title, String avatar, String room_id, String type) {
        this.user_id = user_id;
        this.title = title;
        this.avatar = avatar;
        this.room_id = room_id;
        this.type = type;
    }

    public ChatRoomUiModel(String user_id, String title, String avatar, String room_id, String type, int is_admin) {
        this.user_id = user_id;
        this.title = title;
        this.avatar = avatar;
        this.room_id = room_id;
        this.type = type;
        this.is_admin = is_admin;
    }

    public String getRoom_code() {
        return room_code;
    }

    public void setRoom_code(String room_code) {
        this.room_code = room_code;
    }

    public void setRoom_id(String room_id) {
        this.room_id = room_id;
    }

    public int getIs_admin() {
        return is_admin;
    }

    public void setIs_admin(int is_admin) {
        this.is_admin = is_admin;
    }

    public String getType() {
        return type;
    }

    public String getRoom_id() {
        return room_id;
    }

    public void setId(String user_id) {
        this.user_id = user_id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getTitle() {
        return title;
    }

    public String getAvatar() {
        if(avatar == null)
            return "google.com";
        return (avatar.equalsIgnoreCase("") ? "google.com" : avatar);
    }

    public String getUser_id() {
        return user_id;
    }

    public void setType(String type) {
        this.type = type;
    }
}

