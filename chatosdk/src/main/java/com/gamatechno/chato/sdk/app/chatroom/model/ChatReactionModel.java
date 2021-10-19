package com.gamatechno.chato.sdk.app.chatroom.model;

import java.io.Serializable;

public class ChatReactionModel implements Serializable {
    String name;
    String emoji;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmoji() {
        return emoji;
    }

    public void setEmoji(String emoji) {
        this.emoji = emoji;
    }
}
