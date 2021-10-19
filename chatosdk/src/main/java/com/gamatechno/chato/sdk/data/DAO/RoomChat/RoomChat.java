package com.gamatechno.chato.sdk.data.DAO.RoomChat;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.gamatechno.chato.sdk.data.DAO.Chat.Chat;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
public class RoomChat implements Serializable {
    @NonNull
    @PrimaryKey(autoGenerate = true)
    private int roomChatId;

    private int room_id;
    private int is_mention;
    private String room_type;
    private String room_name;
    private String room_nick_name;
    private String room_desc;
    private String room_code;
    private String room_photo;
    private String group_desc;
    private String group_name;
    private int user_id;
    private String user_username;
    private String user_name;
    private String user_photo;
    private int user_nip;
    private int is_online;
    private String last_message;
    private String last_date;
    private String last_time;
    private String room_category;
    private int is_pined;
    private int is_admin = 0;
    private int unread_message;
    private String message_type;
    private String detail_last_message_string;
    private String room_photo_url;
    private List<String> label_id = new ArrayList();
    private List<String> label_title = new ArrayList();
    private List<String> label_color = new ArrayList();

    public String getRoom_category() {
        return room_category;
    }

    public void setRoom_category(String room_category) {
        this.room_category = room_category;
    }

    @Ignore
    private Chat detail_last_message;

    @Ignore
    private String room_topic;

    public String getRoom_code() {
        return room_code;
    }

    public void setRoom_code(String room_code) {
        this.room_code = room_code;
    }

    @Ignore
    public RoomChat() {

    }

    public RoomChat(int room_id, int is_mention, String room_type, String room_name, String room_nick_name, String group_desc, int user_id, String user_username, String user_name, String user_photo, int user_nip, int is_online, String last_message, String last_date, String last_time, int is_pined) {
        this.room_id = room_id;
        this.is_mention = is_mention;
        this.room_type = room_type;
        this.room_name = room_name;
        this.room_nick_name = room_nick_name;
        this.group_desc = group_desc;
        this.user_id = user_id;
        this.user_username = user_username;
        this.user_name = user_name;
        this.user_photo = user_photo;
        this.user_nip = user_nip;
        this.is_online = is_online;
        this.last_message = last_message;
        this.last_date = last_date;
        this.last_time = last_time;
        this.is_pined = is_pined;
    }

    public int getIs_admin() {
        return is_admin;
    }

    public void setIs_admin(int is_admin) {
        this.is_admin = is_admin;
    }

    public int getIs_mention() {
        return is_mention;
    }

    public void setIs_mention(int is_mention) {
        this.is_mention = is_mention;
    }

    public String getRoom_photo() {
        return room_photo;
    }

    public String getRoom_topic() {
        return room_topic;
    }

    public void setRoom_topic(String room_topic) {
        this.room_topic = room_topic;
    }

    public void setRoom_photo(String room_photo) {
        this.room_photo = room_photo;
    }

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    public String getMessage_type() {
        return message_type;
    }

    public void setMessage_type(String message_type) {
        this.message_type = message_type;
    }

    public int getUnread_message() {
        return unread_message;
    }

    public void setUnread_message(int unread_message) {
        this.unread_message = unread_message;
    }

    public int getRoom_id() {
        return room_id;
    }

    public String getRoom_type() {
        return room_type;
    }

    public String getRoom_name() {
        return room_name;
    }

    public String getRoom_desc() {
        return room_desc;
    }

    public String getRoom_nick_name() {
        return room_nick_name;
    }

    public String getGroup_desc() {
        return group_desc;
    }

    public int getUser_id() {
        return user_id;
    }

    public String getUser_username() {
        return user_username;
    }

    public String getUser_name() {
        return user_name;
    }

    public String getUser_photo() {
        return user_photo;
    }

    public int getUser_nip() {
        return user_nip;
    }

    public int getIs_online() {
        return is_online;
    }

    public String getLast_message() {
        if(last_message == null){
            return "Belum ada pesan";
        } else {
            return (last_message.equals("") ? "Belum ada pesan" : last_message);
        }
    }

    public Chat getDetail_last_message() {
        return detail_last_message;
    }

    public void setDetail_last_message(Chat detail_last_message) {
        this.detail_last_message = detail_last_message;
    }

    public String getDetail_last_message_string() {
        return detail_last_message_string;
    }

    public void setDetail_last_message_string(String detail_last_message_string) {
        this.detail_last_message_string = detail_last_message_string;
    }

    public String getLast_date() {
        return last_date;
    }

    public String getLast_time() {
        return last_time;
    }

    public int getIs_pined() {
        return is_pined;
    }

    @NonNull
    public int getRoomChatId() {
        return roomChatId;
    }

    public void setRoomChatId(@NonNull int roomChatId) {
        this.roomChatId = roomChatId;
    }

    public String getRoom_photo_url() {
        if(room_photo_url == null){
            return "";
        }
        return room_photo_url;
    }

    public List<String> getLabel_id() {
        return label_id;
    }

    public void setLabel_id(List<String> label_id) {
        this.label_id = label_id;
    }

    public List<String> getLabel_title() {
        return label_title;
    }

    public void setLabel_title(List<String> label_title) {
        this.label_title = label_title;
    }

    public List<String> getLabel_color() {
        return label_color;
    }

    public void setLabel_color(List<String> label_color) {
        this.label_color = label_color;
    }

    final public static String user_room_type = "D";
    final public static String group_room_type = "G";
    final public static String official_room_type = "O";

    final public static String image_type = "image";
    final public static String file_type = "file";
    final public static String video_type = "video";
    final public static String voice_type = "voice";

}
