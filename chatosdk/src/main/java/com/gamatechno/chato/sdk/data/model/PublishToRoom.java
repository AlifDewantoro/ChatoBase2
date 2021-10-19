package com.gamatechno.chato.sdk.data.model;

import java.io.Serializable;
import java.util.List;

public class PublishToRoom implements Serializable {
    String room_id;
    String room_code;
    String type, user_name;
    List<Integer> message_id;

    public PublishToRoom() {

    }

    public PublishToRoom(String room_id) {
        this.room_id = room_id;
    }

    public PublishToRoom(String room_id, String room_code) {
        this.room_id = room_id;
        this.room_code = room_code;
    }

    public PublishToRoom(String  room_id, String type, String user_name) {
        this.room_id = room_id;
        this.type = type;
        this.user_name = user_name;
    }

    public PublishToRoom(String  room_id, String type, String user_name, List<Integer> message_id) {
        this.room_id = room_id;
        this.type = type;
        this.user_name = user_name;
        this.message_id = message_id;
    }



    public String getRoom_id() {
        return room_id;
    }

    public String getType() {
        return type;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setType(String type) {
        this.type = type;
    }
}
