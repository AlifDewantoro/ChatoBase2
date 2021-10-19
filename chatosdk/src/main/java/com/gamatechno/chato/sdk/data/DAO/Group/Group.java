package com.gamatechno.chato.sdk.data.DAO.Group;

import com.gamatechno.chato.sdk.app.kontakchat.KontakModel;
import com.gamatechno.chato.sdk.data.DAO.Chat.Chat;

import java.util.List;

public class Group {
    String room_name = "", group_desc = "", room_type = "", room_desc = "", room_icon = "", created_by = "", created_date = "", last_message = "", room_group_type = "", room_photo_name = "", room_photo_url = "", base64_avatar = "";
    int user_id, room_id, user_count, is_admin, is_exit = 0, is_deleted = 0, is_pinned_message = 0, count_shared = 0, count_star_message = 0;
    List<String> group_user_id;
    List<KontakModel> list_user;
    Chat pinned_message;

    public Group() {

    }

    public int getIs_pinned_message() {
        return is_pinned_message;
    }

    public void setIs_pinned_message(int is_pinned_message) {
        this.is_pinned_message = is_pinned_message;
    }

    public Chat getPinned_message() {
        return pinned_message;
    }

    public void setPinned_message(Chat pinned_message) {
        this.pinned_message = pinned_message;
    }

    public String getRoom_group_type() {
        return room_group_type;
    }

    public void setRoom_group_type(String room_group_type) {
        this.room_group_type = room_group_type;
    }

    public String getRoom_photo_name() {
        return room_photo_name;
    }

    public void setRoom_photo_name(String room_photo_name) {
        this.room_photo_name = room_photo_name;
    }

    public String getRoom_photo_url() {
        if(room_photo_url == null){
            return "";
        }
        return room_photo_url;
    }

    public void setRoom_photo_url(String room_photo_url) {
        this.room_photo_url = room_photo_url;
    }

    public Group(String room_name, String group_desc, int user_id, int room_id, List<String> group_user_id) {
        this.room_name = room_name;
        this.group_desc = group_desc;
        this.user_id = user_id;
        this.room_id = room_id;
        this.group_user_id = group_user_id;
    }

    public String getBase64_avatar() {
        return base64_avatar;
    }

    public int getIs_exit() {
        return is_exit;
    }

    public int getIs_deleted() {
        return is_deleted;
    }

    public void setIs_deleted(int is_deleted) {
        this.is_deleted = is_deleted;
    }

    public void setIs_exit(int is_exit) {
        this.is_exit = is_exit;
    }

    public void setBase64_avatar(String base64_avatar) {
        this.base64_avatar = base64_avatar;
    }

    public void setList_user(List<KontakModel> list_user) {
        this.list_user = list_user;
    }

    public List<KontakModel> getList_user() {
        return list_user;
    }

    public String getRoom_name() {
        return room_name;
    }

    public void setRoom_name(String room_name) {
        this.room_name = room_name;
    }

    public void setGroup_desc(String group_desc) {
        this.group_desc = group_desc;
    }

    public String getRoom_type() {
        return room_type;
    }

    public void setRoom_type(String room_type) {
        this.room_type = room_type;
    }

    public String getRoom_desc() {
        return room_desc;
    }

    public void setRoom_desc(String room_desc) {
        this.room_desc = room_desc;
    }

    public String getRoom_icon() {
        return room_icon;
    }

    public void setRoom_icon(String room_icon) {
        this.room_icon = room_icon;
    }

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }

    public String getLast_message() {
        return last_message;
    }

    public void setLast_message(String last_message) {
        this.last_message = last_message;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public void setRoom_id(int room_id) {
        this.room_id = room_id;
    }

    public int getUser_count() {
        return user_count;
    }

    public void setUser_count(int user_count) {
        this.user_count = user_count;
    }

    public int getIs_admin() {
        return is_admin;
    }

    public void setIs_admin(int is_admin) {
        this.is_admin = is_admin;
    }

    public void setGroup_user_id(List<String> group_user_id) {
        this.group_user_id = group_user_id;
    }

    public String getGroup_desc() {
        return group_desc;
    }

    public int getUser_id() {
        return user_id;
    }

    public int getRoom_id() {
        return room_id;
    }

    public List<String> getGroup_user_id() {
        return group_user_id;
    }

    public int getCount_shared() {
        return count_shared;
    }

    public int getCount_star_message() {
        return count_star_message;
    }

    public void setCount_shared(int count_shared) {
        this.count_shared = count_shared;
    }

    public void setCount_star_message(int count_star_message) {
        this.count_star_message = count_star_message;
    }
}
