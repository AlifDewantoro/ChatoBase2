package com.gamatechno.chato.sdk.data.model;

import java.io.Serializable;
import java.util.List;

public class ListentoRoomModel implements Serializable {
    String username, type, date_time;
    int room_id;;
    List<Integer> message_id;

    final public static String STATE_OFFLINE = "offline";
    final public static String STATE_TYPING = "typing";
    final public static String STATE_ONLINE = "online";
    final public static String STATE_LAST_SEEN = "last_seen";
    final public static String STATE_DELETE = "delete_message";

    public String getUsername() {
        return username;
    }

    public String getType() {
        return type;
    }

    public String getDate_time() {
        return date_time;
    }

    public int getRoom_id() {
        return room_id;
    }

    public List<Integer> getMessage_id() {
        return message_id;
    }
}
