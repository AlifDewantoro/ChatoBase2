package com.gamatechno.chato.sdk.data.DAO.Chat;

import android.graphics.Bitmap;

import androidx.room.Entity;
import androidx.room.Ignore;

import com.gamatechno.chato.sdk.app.chatroom.model.FileModel;

@Entity
public class NotifChat extends Chat {

    public NotifChat(int message_id, int from_user_id, String from_username, String from_username_photo, int to_user_id, String to_username, String to_username_photo, String message_text, String message_type, String message_attachment, String message_status, String message_date, String message_time, String payload) {
        super(message_id, from_user_id, from_username, from_username_photo, to_user_id, to_username, to_username_photo, message_text, message_type, message_attachment, message_status, message_date, message_time, payload);
    }

    @Ignore
    public NotifChat(int message_id, int from_user_id, String room_id, String message_status) {
        super(message_id, from_user_id, room_id, message_status);
    }

    @Ignore
    public NotifChat(int message_id, int from_user_id, int to_user_id, String message_text, String message_type, String message_attachment, String message_status, String message_date, String message_time) {
        super(message_id, from_user_id, to_user_id, message_text, message_type, message_attachment, message_status, message_date, message_time);
    }

    @Ignore
    public NotifChat(int from_user_id, int to_user_id, String message_text, String message_type, String message_attachment, String message_status, String payload, String room_id, String room_code) {
        super(from_user_id, to_user_id, message_text, message_type, message_attachment, message_status, payload, room_id, room_code);
    }

    @Ignore
    public NotifChat(int from_user_id, int to_user_id, String message_text, String message_type, String message_attachment, String message_status, String payload, FileModel fileModel, Bitmap bitmap_image, String room_id, String room_code) {
        super(from_user_id, to_user_id, message_text, message_type, message_attachment, message_status, payload, fileModel, bitmap_image, room_id, room_code);
    }

    @Ignore
    public NotifChat(int from_user_id, int to_user_id, String message_text, String message_type, String message_attachment, String message_status, String payload, FileModel fileModel) {
        super(from_user_id, to_user_id, message_text, message_type, message_attachment, message_status, payload, fileModel);
    }

    @Ignore
    public NotifChat(int from_user_id, int to_user_id, String message_text, String message_type, String message_attachment, String message_status, String payload, FileModel fileModel, String room_id, String thumb_video, String duration, String room_code) {
        super(from_user_id, to_user_id, message_text, message_type, message_attachment, message_status, payload, fileModel, room_id, thumb_video, duration, room_code);
    }

    @Ignore
    public NotifChat(int from_user_id, int to_user_id, String message_text, String message_type, String message_attachment, String message_status, String payload, FileModel fileModel, String room_id, String room_code) {
        super(from_user_id, to_user_id, message_text, message_type, message_attachment, message_status, payload, fileModel, room_id, room_code);
    }
}
