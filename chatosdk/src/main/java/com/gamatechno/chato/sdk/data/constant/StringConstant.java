package com.gamatechno.chato.sdk.data.constant;

import com.gamatechno.chato.sdk.BuildConfig;
import com.gamatechno.chato.sdk.module.core.ChatoBaseApplication;

public class StringConstant {
    private static String package_name = ChatoBaseApplication.getInstance().getKEY()+"_"+BuildConfig.LIBRARY_PACKAGE_NAME;

    public static final String DB_NAME = "chato";

    /*Chat Type Constant*/
    public static final int type_item_message           = 1;
    public static final int type_reply_message          = 2;
    public static final int type_image_attachment       = 3;
    public static final int type_video_attachment       = 4;
    public static final int type_audio_attachment       = 5;
    public static final int type_file_attachment        = 6;
    public static final int type_message_delete         = 7;
    public static final int type_item_label             = 8;
    public static final int type_item_schedule          = 9;

    public static final String chat_send_message      = package_name+"_chat_send_message";
    public static final String chat_retreive_person_message      = package_name+"_chat_retreive_person_message";

    //    Flag untuk mengirim adanya listen status room baru
    public static final String broadcast_listen_to_room      = package_name+"_broadcast_listen_to_room";

    public static final String broadcast_get_update_group_info = package_name+"_broadcast_get_update_room_info";

    // Flag untuk menandai adanya pesan masuk, status baru masuk
    final public static String broadcast_receive_status_chat      = package_name+"_broadcast_receive_status_chat";
    final public static String broadcast_receive_chat      = package_name+"_broadcast_receive_chat";
    final public static String broadcast_refresh_chat      = package_name+"_broadcast_refresh_chat";
    final public static String broadcast_receive_chat_reaction      = package_name+"_receive_chat_reaction";

    // Flag untuk meminta status service aktif atau nonaktif
    public static final String service_status_on = package_name+"_service_status_on";
    public static final String service_status_stop = package_name+"_service_status_stop";
    public static final String service_check_stop = package_name+"_service_check_stop";

    //    Flag untuk meminta request service group list
    public static final String service_requestgroup_tofirebase_register = package_name+"_service_requestgroup_firebase_register";

    //    Flag untuk mematikan service
    public static final String service_killself = package_name+"_killself";

    // Flag untuk menandai activity sedang stop atau resume
    public static final String activity_check_stop = package_name+"_activity_check_stop";



    // Tipe status pesan
    public static final String chat_status_delivered = "DELIVERED";
    public static final String chat_status_pending = "PENDING";
    public static final String chat_status_read = "READ";
    public static final String chat_status_failed = "FAILED";
    public static final String chat_status_sending = "SENDING";

    // Tipe chatroom ketika sedang open atau close
    public static final String chatroom_state_open = "opened";
    public static final String chatroom_state_close = "closed";
    public static final String chatroom_state_close_notif = package_name+"_chatroom_state_close_notif";
    public static final String chatroom_state_send_notif = package_name+"_chatroom_state_send_notif";
    public static final String chatroom_state_publish_to_room = package_name+"_chatroom_state_listen_to_room";
    public static final String chatroom_state_open_to_room = package_name+"_chatroom_state_open_to_room";
    public static final String chatroom_state_out_from_room = package_name+"_chatroom_state_out_from_room";

    //    public static final String SERVICECHAT_RECEIVE_MESSAGE      = "received message";
    public static final String SERVICECHAT_RECEIVE_MESSAGE      = "listen";
    public static final String SERVICECHAT_RECEIVE_UPDATESTATUS = "listen_update_status";
    public static final String SERVICECHAT_LISTEN_TO_ROOM = "listen_to_room";
    public static final String SERVICECHAT_PUBLISH_TO_ROOM = "publish_to_room";
    public static final String SERVICECHAT_LEAVE_ROOM = "leave_room";
    public static final String SERVICECHAT_JOIN_ROOM = "join_room";
    public static final String SERVICECHAT_CALL = "call";

    // Flag untuk Sorting chatroom
    public static final String chatroom_sorting_alphabetic = "ASC";
    public static final String chatroom_sorting_newest = "LAST_MESSAGE";

    // Flag untuk status selected message : none of selected, one selected atau multiple selected message
    public static final int status_messageselected_none = 0;
    public static final int status_messageselected_one = 1;
    public static final int status_messageselected_multiple = 2;

    // Flag untuk tipe action appbar chatroom
    public static final String appbar_forward = "appbar_forward";
    public static final String appbar_reply = "appbar_reply";
    public static final String appbar_delete = "appbar_delete";
    public static final String appbar_star = "appbar_star";
    public static final String appbar_copy = "appbar_copy";
    public static final String appbar_reaction = "appbar_reaction";
    public static final String appbar_info = "appbar_info";
    public static final String appbar_pinmessage = "appbar_pinmessage";

    public static final String[] mimeTypes =
            {"image/*","application/pdf","application/msword","application/vnd.ms-powerpoint","application/vnd.ms-excel","text/plain"};

    public static final String[] mimeTypesFile =
            {"application/pdf",
                    "application/msword",
                    "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
                    "application/vnd.ms-powerpoint",
                    "application/vnd.ms-excel",
                    "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                    "application/vnd.ms-powerpoint",
                    "application/vnd.openxmlformats-officedocument.presentationml.presentation",
                    "audio/x-mpeg-3",
                    "audio/mpeg3"};

    public static final String imagepicker_profilechatsaya = "imagepicker_profilechatsaya";
    public static final String imagepicker_addgroup = "imagepicker_addgroup";

    public static final String notification_message = "notification_message";

    public static final String call_broadcast = package_name+"call_broadcast";
    public static final String call_broadcast_response = package_name+"call_broadcast_response";
    public static final String call_outcoming_audio = package_name+"_call_outcoming_audio";
    public static final String call_outcoming_video = package_name+"_call_outcoming_video";
    public static final String call_incoming_audio = package_name+"_call_outcoming_audio";
    public static final String call_incoming_video = package_name+"_call_outcoming_video";

    public static String call_streaming = package_name+"_call_streaming";
    public static String call_end = package_name+"_call_end";

    //Activity Result Code
    public static final int REFRESH_CHAT_HISTORY = 98;
    public static final int FINNISH_CHAT_ACTIVITY = 99;
}
