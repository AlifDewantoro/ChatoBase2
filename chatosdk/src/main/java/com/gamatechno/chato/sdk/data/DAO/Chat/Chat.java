package com.gamatechno.chato.sdk.data.DAO.Chat;

import android.graphics.Bitmap;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.gamatechno.chato.sdk.app.chatroom.model.ChatReactionModel;
import com.gamatechno.chato.sdk.app.chatroom.model.FileModel;
import com.gamatechno.chato.sdk.app.kontakchat.KontakModel;
import com.gamatechno.chato.sdk.data.constant.StringConstant;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


@Entity
public class Chat implements Serializable {
    @NonNull
    @PrimaryKey(autoGenerate = true)
    private int chatId;

    private int message_id;
    private int from_user_id;
    String from_username;
    String user_name;
    String user_username;
    String from_username_photo;
    int to_user_id;
    int is_deleted = 0;
    String to_username;
    String to_username_photo;
    String message_text;

    String message;
    String message_type;

    String message_attachment;
    String message_file;
    String message_status;
    String message_date = "";
    String message_file_type = "";
    String message_time;

    int message_is_forward = 0;
    int message_is_broadcast = 0;
    String payload;
    int message_star;

    int message_is_replay = 0;
    String message_replay_id;
    String message_replay_text;
    String message_replay_username;
    String message_replay_type = "";
    String message_attachment_name;
    String message_attachment_thumbnail = "";
    String message_file_name = "";
    String message_file_thumbnail = "";
    int message_attachment_duration = 0;

    @Ignore
    List<Integer> mention_user;

    @Ignore
    List<ChatReactionModel> message_reaction;

    @Ignore
    List<KontakModel> room;


    @Ignore
    Chat replay_message;

    @Ignore
    String thumb_video;

    @Ignore
    String duration;

    @Ignore
    int message_file_duration;

    @Ignore
    String last_time;

    @Ignore
    Bitmap bitmap_image;

    @Ignore
    FileModel fileModel;

    @Ignore
    String uri_attachment;

    @Ignore
    boolean isClicked = false;

    @Ignore
    boolean isVideoDownloding = false;

    String room_id = "";
    String room_code = "";
    String room_type = "";

    @Ignore
    String message_action = "";

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public int getMessage_file_duration() {
        return message_file_duration;
    }

    public void setMessage_file_duration(int message_file_duration) {
        this.message_file_duration = message_file_duration;
    }

    public List<Integer> getMentionId() {
        return mention_user;
    }

    public void setMentionId(List<Integer> mentions) {
        this.mention_user = mentions;
    }

    public List<ChatReactionModel> getReactionList() {
        return message_reaction;
    }

    public void setReactionList(List<ChatReactionModel> reactionList) {
        this.message_reaction = reactionList;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setMessage() {
        this.message = message_text;
    }

    public String getMessage_file() {
        return message_file;
    }

    public void setMessage_file(String message_file) {
        this.message_file = message_file;
    }

    public void setMessage_file() {
        this.message_file = message_attachment;
    }

    public List<KontakModel> getRoom() {
        return room;
    }

    public void setRoom(List<KontakModel> room) {
        this.room = room;
    }

    public void setMessage_star(int message_star) {
        this.message_star = message_star;
    }

    public String getRoom_code() {
        return room_code;
    }

    public void setRoom_code(String room_code) {
        this.room_code = room_code;
    }

    public String getRoom_type() {
        return room_type;
    }

    public void setRoom_type(String room_type) {
        this.room_type = room_type;
    }

    public Chat(int message_id, int from_user_id, String from_username, String from_username_photo, int to_user_id, String to_username, String to_username_photo, String message_text, String message_type, String message_attachment, String message_status, String message_date, String message_time, String payload) {
        this.message_id = message_id;
        this.from_user_id = from_user_id;
        this.from_username = from_username;
        this.from_username_photo = from_username_photo;
        this.to_user_id = to_user_id;
        this.to_username = to_username;
        this.to_username_photo = to_username_photo;
        this.message_text = message_text;
        this.message_type = message_type;
        this.message_attachment = message_attachment;
        this.message_status = message_status;
        this.message_date = message_date;
        this.message_time = message_time;
        this.payload = payload;
    }

    @Ignore
    public Chat(int message_id, int from_user_id, String room_id, String message_status) {
        this.message_id = message_id;
        this.from_user_id = from_user_id;
        this.room_id = room_id;
        this.message_status = message_status;
    }

    @Ignore
    public Chat(int message_id, int from_user_id, int to_user_id, String message_text, String message_type, String message_attachment, String message_status, String message_date, String message_time) {
        this.message_id = message_id;
        this.from_user_id = from_user_id;
        this.to_user_id = to_user_id;
        this.message_text = message_text;
        this.message_type = message_type;
        this.message_attachment = message_attachment;
        this.message_status = message_status;
        this.message_date = message_date;
        this.message_time = message_time;
    }

    @Ignore
    public Chat(int from_user_id, int to_user_id, String message_text, String message_type, String message_attachment, String message_status, String payload, String room_id, String room_code) {
        this.from_user_id = from_user_id;
        this.to_user_id = to_user_id;
        this.message_text = message_text;
        this.message_type = message_type;
        this.message_attachment = message_attachment;
        this.message_status = message_status;
        this.payload = payload;
        this.room_id = room_id;
        this.room_code = room_code;
    }


    @Ignore
    public void setReplyComponent(Chat component) {
        this.message_id = component.getMessage_id();
        this.message_replay_text = component.getMessage_text();
        this.message_replay_id = ""+component.getMessage_id();
//        this.message_replay_username = ""+component.getMessage_id();
//        this.message_replay_username = ""+component.getUser_name();
        this.message_replay_username = ""+component.getFrom_username();
        this.message_is_replay = 1;
    }

    @Ignore
    public Chat(int from_user_id, int to_user_id, String message_text, String message_type, String message_attachment, String message_status, String payload, FileModel fileModel, Bitmap bitmap_image, String room_id, String room_code) {
        this.from_user_id = from_user_id;
        this.to_user_id = to_user_id;
        this.message_text = message_text;
        this.message_type = message_type;
        this.message_attachment = message_attachment;
        this.message_status = message_status;
        this.payload = payload;
        this.bitmap_image = bitmap_image;
        this.room_id = room_id;
        this.room_code = room_code;
        this.fileModel = fileModel;
    }

    @Ignore
    public Chat(int from_user_id, int to_user_id, String message_text, String message_type, String message_attachment, String message_status, String payload, FileModel fileModel) {
        this.from_user_id = from_user_id;
        this.to_user_id = to_user_id;
        this.message_text = message_text;
        this.message_type = message_type;
        this.message_attachment = message_attachment;
        this.message_status = message_status;
        this.payload = payload;
        this.fileModel = fileModel;
    }

    @Ignore
    public Chat(int from_user_id, int to_user_id, String message_text, String message_type, String message_attachment, String message_status, String payload, FileModel fileModel, String room_id, String thumb_video, String duration, String from) {
        this.from_user_id = from_user_id;
        this.to_user_id = to_user_id;
        this.message_text = message_text;
        this.thumb_video = thumb_video;
        this.duration = duration;
        this.message_type = message_type;
        this.message_attachment = message_attachment;
        this.message_status = message_status;
        this.payload = payload;
        this.fileModel = fileModel;
        this.room_id = room_id;
        this.from_username = from;
    }

    @Ignore
    public Chat(int from_user_id, int to_user_id, String message_text, String message_type, String message_attachment, String message_status, String payload, FileModel fileModel, String room_id, String room_code) {
        this.from_user_id = from_user_id;
        this.to_user_id = to_user_id;
        this.message_text = message_text;
        this.message_type = message_type;
        this.message_attachment = message_attachment;
        this.message_status = message_status;
        this.payload = payload;
        this.fileModel = fileModel;
        this.room_id = room_id;
        this.room_code = room_code;
    }

    @Ignore
    public void setForwardComponent(int to_user_id, int from_user_id){
        this.from_user_id = from_user_id;
        this.to_user_id = to_user_id;
        this.message_status = StringConstant.chat_status_sending;
        this.message_is_forward = 1;
        this.payload = "forwardingaqoe";
        this.room_id = "";
        this.bitmap_image = null;
    }

    public String getMessage_file_thumbnail() {
        return message_file_thumbnail;
    }

    public void setMessage_file_thumbnail(String message_file_thumbnail) {
        this.message_file_thumbnail = message_file_thumbnail;
    }

    public String getMessage_file_name() {
        return message_file_name;
    }

    public void setMessage_file_name(String message_file_name) {
        this.message_file_name = message_file_name;
    }

    public String getMessage_file_type() {
        return message_file_type;
    }

    public void setMessage_file_type(String message_file_type) {
        this.message_file_type = message_file_type;
    }

    @Ignore
    public String getMessage_attachment_thumbnail() {
        return message_attachment_thumbnail;
    }

    public boolean isVideoDownloding() {
        return isVideoDownloding;
    }

    public void setVideoDownloding(boolean videoDownloding) {
        isVideoDownloding = videoDownloding;
    }

    public void setIs_deleted(int is_deleted) {
        this.is_deleted = is_deleted;
    }

    public String getUri_attachment() {
        return uri_attachment;
    }

    public Uri convertUri_attachment() {
        return Uri.parse(uri_attachment);
    }

    public int getIs_deleted() {
        return is_deleted;
    }

    public void setUri_attachment(String uri_attachment) {
        this.uri_attachment = uri_attachment;
    }

    public void setRoom_id(String room_id) {
        this.room_id = room_id;
    }

    public void setMessage_is_replay(int message_is_replay) {
        this.message_is_replay = message_is_replay;
    }

    public void setMessage_is_forward(int message_is_forward) {
        this.message_is_forward = message_is_forward;
    }

    public int getMessage_is_replay() {
        return message_is_replay;
    }

    public int getMessage_is_forward() {
        return message_is_forward;
    }

    public int getMessage_is_broadcast() {
        return message_is_broadcast;
    }

    public void setMessage_is_broadcast(int message_is_broadcast) {
        this.message_is_broadcast = message_is_broadcast;
    }

    public int getMessage_star() {
        return message_star;
    }

    public String getRoom_id() {
        return room_id;
    }

    public String getLast_time() {
        return last_time;
    }

    @Ignore
    public String getUser_name() {
        return user_name;
    }

    @Ignore
    public String getUser_username() {
        return user_username;
    }

    public Bitmap getBitmap_image() {
        return bitmap_image;
    }

    public void setBitmap_image(Bitmap bitmap_image) {
        this.bitmap_image = bitmap_image;
    }

    public FileModel getFileModel() {
        return fileModel;
    }

    public void setFileModel(FileModel fileModel) {
        this.fileModel = fileModel;
    }

    @NonNull
    public int getChatId() {
        return chatId;
    }

    public void setChatId(@NonNull int chatId) {
        this.chatId = chatId;
    }

    public String getMessage_attachment_name() {
        return message_attachment_name;
    }

    public void setMessage_attachment_name(String message_attachment_name) {
        this.message_attachment_name = message_attachment_name;
    }

    public int getMessage_id() {
        return message_id;
    }

    public void setMessage_id(int message_id) {
        this.message_id = message_id;
    }

    public int getFrom_user_id() {
        return from_user_id;
    }

    public void setFrom_user_id(int from_user_id) {
        this.from_user_id = from_user_id;
    }

    public String getFrom_username() {
        return from_username;
    }

    public void setFrom_username(String from_username) {
        this.from_username = from_username;
    }

    public String getFrom_username_photo() {
        return from_username_photo;
    }

    public void setFrom_username_photo(String from_username_photo) {
        this.from_username_photo = from_username_photo;
    }

    public int getTo_user_id() {
        return to_user_id;
    }

    public void setTo_user_id(int to_user_id) {
        this.to_user_id = to_user_id;
    }

    public String getTo_username() {
        return to_username;
    }

    public void setTo_username(String to_username) {
        this.to_username = to_username;
    }

    public String getTo_username_photo() {
        return to_username_photo;
    }

    public void setTo_username_photo(String to_username_photo) {
        this.to_username_photo = to_username_photo;
    }

    @Ignore
    public String getMessage_replay_type() {
        if(message_replay_type == null){
            return "";
        }
        return message_replay_type;
    }

    @SerializedName("message")
    public String getMessage_text() {
        return message_text;
    }

    public void setMessage_text(String message_text) {
        this.message_text = message_text;
    }

    public String getMessage_type() {
        return message_type;
    }

    public int getMessage_type_num() {
        if(is_deleted == 1){
            return StringConstant.type_item_message;
        } else {
            switch (message_type){
                case chat_type_message:
                    return StringConstant.type_item_message;
                case chat_type_label:
                    return StringConstant.type_item_label;
                case chat_type_image:
                    return StringConstant.type_image_attachment;
                case chat_type_file:
                    return StringConstant.type_file_attachment;
                case chat_type_video:
                    return StringConstant.type_video_attachment;
                case chat_type_audio:
                    return StringConstant.type_audio_attachment;
            }
        }
        return StringConstant.type_item_message;
    }

    public void setMessage_type(String message_type) {
        this.message_type = message_type;
    }

    public String getMessage_attachment() {
        return message_attachment;
    }

    public void setMessage_attachment(String message_attachment) {
        this.message_attachment = message_attachment;
    }

    public String getMessage_status() {
        return message_status;
    }

    public void setMessage_status(String message_status) {
        this.message_status = message_status;
    }

    public String getMessage_date() {
        if((""+message_date).equals("") || (""+message_date).equals("null")){
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date();
            return ""+dateFormat.format(date);
//            2020-03-19
        }
        return message_date;
    }

    public void setMessage_date(String message_date) {
        this.message_date = message_date;
    }

    public String getMessage_time() {
        return message_time;
    }

    public void setMessage_time(String message_time) {
        this.message_time = message_time;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public boolean isClicked() {
        return isClicked;
    }

    public void setClicked(boolean clicked) {
        isClicked = clicked;
    }

    @Ignore
    public String getMessage_replay_id() {
        return message_replay_id;
    }

    @Ignore
    public String getMessage_replay_text() {
        if(message_replay_text == null)
            return "";
        return message_replay_text;
    }

    public Chat getReplay_message() {
        return replay_message;
    }

    public void setReplay_message(Chat replay_message) {
        this.replay_message = replay_message;
    }

    @Ignore
    public String getMessage_replay_username() {
        return message_replay_username;
    }

    //    Tipe message untuk parameter pengiriman
    @Ignore
    public static final String chat_type_message = "text";
    @Ignore
    public static final String chat_type_label = "label";
    @Ignore
    public static final String chat_type_image = "image";
    @Ignore
    public static final String chat_type_file = "file";
    @Ignore
    public static final String chat_type_audio = "voice";
    @Ignore
    public static final String chat_type_video = "video";

    public String getThumb_video() {
        return thumb_video;
    }

    public void setThumb_video(String thumb_video) {
        this.thumb_video = thumb_video;
    }

    public int getMessage_attachment_duration() {
        return message_attachment_duration;
    }

    void setMessage_attachment_duration(int message_attachment_duration) {
        this.message_attachment_duration = message_attachment_duration;
    }

    public String getMessage_action() {
        return message_action;
    }
}
