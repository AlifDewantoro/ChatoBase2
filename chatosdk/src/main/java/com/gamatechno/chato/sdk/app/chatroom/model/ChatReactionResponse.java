package com.gamatechno.chato.sdk.app.chatroom.model;

import java.io.Serializable;

public class ChatReactionResponse implements Serializable {
    String user_id;
    String room_id;
    String message_id;
    String reaction_data;
    String reaction;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getRoom_id() {
        return room_id;
    }

    public void setRoom_id(String room_id) {
        this.room_id = room_id;
    }

    public String getMessage_id() {
        return message_id;
    }

    public void setMessage_id(String message_id) {
        this.message_id = message_id;
    }

    public String getReaction_data() {
        return reaction_data;
    }

    public void setReaction_data(String reaction_data) {
        this.reaction_data = reaction_data;
    }

    public String getReaction() {
        return reaction;
    }

    public void setReaction(String reaction) {
        this.reaction = reaction;
    }

    public String getRoom_code() {
        return room_code;
    }

    public void setRoom_code(String room_code) {
        this.room_code = room_code;
    }

    String room_code;
}
