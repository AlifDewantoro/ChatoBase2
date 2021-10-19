package com.gamatechno.chato.sdk.app.kontakchat;

import java.io.Serializable;

import static com.gamatechno.chato.sdk.data.DAO.RoomChat.RoomChat.user_room_type;

public class KontakModel implements Serializable {
    int user_id = 0, room_id = 0;
    String user_name = "", group_name = "";
    String room_code = "";
    String room_type = user_room_type;
    String user_username = "";
    String nick_name = "";
    String user_photo = "";
    String user_nip = "";
    String user_jabatan = "";
    String foto = "";
    String last_seen = "";
    int is_online;
    int is_clicked;
    int is_admin = 0;
    boolean is_group_add = false;
    boolean is_header = false;

    public KontakModel(int user_id, String user_name, String user_username, String user_nip, String user_jabatan, String foto, int is_online) {
        this.user_id = user_id;
        this.user_name = user_name;
        this.user_username = user_username;
        this.user_nip = user_nip;
        this.user_jabatan = user_jabatan;
        this.foto = foto;
        this.is_online = is_online;
    }

    public KontakModel(int room_id, String room_type, String room_name, String room_photo_url){
        this.room_id = room_id;
        this.room_type = room_type;
        this.group_name = room_name;
        this.user_photo = room_photo_url;
    }

    public KontakModel(int user_id, String user_name) {
        this.user_id = user_id;
        this.user_name = user_name;
    }

    public KontakModel(String user_name, boolean is_group_add) {
        this.user_name = user_name;
        this.is_group_add = is_group_add;
    }

    public KontakModel(String user_name, boolean is_header, int is_online) {
        this.user_name = user_name;
        this.is_online = is_online;
        this.is_header = is_header;
    }

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    public String getRoom_type() {
        if(room_type == null){
            return user_room_type;
        }
        return room_type;
    }

    public void setRoom_type(String room_type) {
        this.room_type = room_type;
    }

    public int getRoom_id() {
        return room_id;
    }

    public void setRoom_id(int room_id) {
        this.room_id = room_id;
    }

    public String getUser_photo() {
        if(user_photo == null){
            return "google.com";
        } else {
            if(user_photo.equals("")){
                return "google.com";
            }
        }
        return user_photo;
    }

    public int getIs_admin() {
        return is_admin;
    }

    public void setIs_admin(int is_admin) {
        this.is_admin = is_admin;
    }

    public String getRoom_code() {
        return room_code;
    }

    public void setRoom_code(String room_code) {
        this.room_code = room_code;
    }

    public void setUser_photo(String user_photo) {
        this.user_photo = user_photo;
    }

    public boolean isIs_group_add() {
        return is_group_add;
    }

    public void setIs_group_add(boolean is_group_add) {
        this.is_group_add = is_group_add;
    }

    public int getIs_clicked() {
        return is_clicked;
    }

    public void setIs_clicked(int is_clicked) {
        this.is_clicked = is_clicked;
    }

    public int getIs_online() {
        return is_online;
    }

    public void setIs_online(int is_online) {
        this.is_online = is_online;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    public String getNick_name() {
        return nick_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_username() {
        return user_username;
    }

    public void setUser_username(String user_username) {
        this.user_username = user_username;
    }

    public String getUser_nip() {
        return user_nip;
    }

    public void setUser_nip(String user_nip) {
        this.user_nip = user_nip;
    }

    public String getUser_jabatan() {
        return user_jabatan;
    }

    public void setUser_jabatan(String user_jabatan) {
        this.user_jabatan = user_jabatan;
    }

    public String getLast_seen() {
        return last_seen;
    }

    public void setLast_seen(String last_seen) {
        this.last_seen = last_seen;
    }

    public String getFoto() {
        if(foto.equals(""))
            return "google.com";
        else
            return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public boolean isHeader() {
        return is_header;
    }

    public void setHeader(boolean is_header) {
        this.is_header = is_header;
    }
}
