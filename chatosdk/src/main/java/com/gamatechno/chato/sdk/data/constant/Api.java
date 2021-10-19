package com.gamatechno.chato.sdk.data.constant;

import com.gamatechno.chato.sdk.BuildConfig;

public class Api {

    private static String base_api = BuildConfig.base_api;

    public static String login() {
        return base_api + BuildConfig.stringLogin;
    }


    public static String signin() {
        return base_api + BuildConfig.stringSignin;
    }


    public static String profile_all() {
        return base_api + BuildConfig.stringProfile;
    }

    public static String customer_info() {
        return base_api + BuildConfig.stringCustomerInfo;
    }

    public static String register_user() {
        return base_api + BuildConfig.stringRegister;
    }

    public static String profile_by(String type) {
        return base_api + BuildConfig.stringProfileBy + type;
    }

    public static String list_kontak(String page) {
        return base_api + BuildConfig.stringKontak+"?page="+page;
    }

    public static String message_info(String messageid) {
        return base_api + BuildConfig.stringMessageInfo+"?message_id="+messageid;
    }

    public static String list_conversation(String page, String order_by) {
        return base_api + BuildConfig.stringListConversation+"?page="+page+"&sort_by="+order_by;
    }

    public static String get_room_detail(String room_id) {
        return base_api + BuildConfig.stringRoomDetail+"?room_id="+room_id;
    }

    public static String get_room_detail(String room_id, String room_code) {
        return base_api + BuildConfig.stringRoomDetail+"?room_code="+room_code;
    }

    public static String get_room_detailv2(String room_id, String room_code) {
        return base_api + BuildConfig.stringRoomDetailv2+"?room_code="+room_code;
    }

    public static String get_room_birthday(String email) {
        return base_api + BuildConfig.stringRoomEmail+"?email="+email;
    }

    public static String list_groupConversation(String page, String order_by) {
        return base_api + BuildConfig.stringListGroup+"?page="+page+"&sort_by="+order_by;
    }

    public static String search_Conversation(String keyword) {
        return base_api + BuildConfig.stringSearchConversation+"?keyword="+keyword;
    }

    public static String create_group() {
        return base_api + BuildConfig.stringCreateGroup;
    }


    public static String search_user(String keyword, String page) {
        return base_api + BuildConfig.stringSearchUser+"?page="+page+"&keyword="+keyword;
    }

    public static String add_member_group() {
        return base_api + BuildConfig.stringAddMemberGroup;
    }

    public static String get_history_chat(String to_user_id, String page, String room_code) {
        return base_api + BuildConfig.stringHistoryChat+"?page="+page+"&to_user_id="+to_user_id+"&room_code="+room_code;
    }

    public static String get_history_chat(String to_user_id, String last_message_id, String page, String room_id, String room_code) {
        return base_api + BuildConfig.stringHistoryChat+"?page="+page+"&to_user_id="+to_user_id+"&last_message_id="+last_message_id+"&room_id="+room_id+"&room_code="+room_code;
    }

    public static String get_history_groupchat(String to_user_id, String last_message_id, String page) {
        return base_api + BuildConfig.stringHistoryGroupChat+"?page="+page+"&group_id="+to_user_id+"&last_message_id="+last_message_id;
    }

    public static String get_history_groupchat(String to_user_id, String page) {
        return base_api + BuildConfig.stringHistoryGroupChat+"?page="+page+"&group_id="+to_user_id;
    }


    public static String send_message() {
        return base_api + BuildConfig.stringSendMessage;
    }


    public static String broadcast_message() {
        return base_api + BuildConfig.stringBroadcastMessage;
    }


    public static String send_group_message() {
        return base_api + BuildConfig.stringSendGroupMessage;
    }


    public static String update_status_message() {
        return base_api + BuildConfig.stringUpdateStatusMessage;
    }


    public static String string_list_roomgroup() {
        return base_api + BuildConfig.stringlistRoomGroup;
    }



    public static String update_device_token() {
        return base_api + BuildConfig.stringUpdateDeviceToken;
    }

    public static String customer_token() {
        return base_api + BuildConfig.stringGetCustomerToken;
    }

    public static String pin_chat_room() {
        return base_api + BuildConfig.stringPinnedChatRoom;
    }

    public static String pin_multiple_chat_room() {
        return base_api + BuildConfig.stringPinnedMultipleChatRoom;
    }


    public static String star_message() {
        return base_api + BuildConfig.stringStarMessage;
    }


    public static String search_message(String roomid, String keyword) {
        return base_api + BuildConfig.stringSearchMessage+"?room_id="+roomid+"&keyword="+keyword;
    }


    public static String get_info_detail_group(String roomid) {
        return base_api + BuildConfig.stringInfoDetailGroup+"?room_id="+roomid;
    }


    public static String delete_message() {
        return base_api + BuildConfig.stringDeleteMessage;
    }


    public static String update_detail_group() {
        return base_api + BuildConfig.stringUpdateDetailGroup;
    }


    public static String force_delete_message() {
        return base_api + BuildConfig.stringForceDeleteMessage;
    }


    public static String list_starred_message(String room_id) {
        if(room_id.equalsIgnoreCase("")) {
            return base_api + BuildConfig.stringListGlobalStarredMessage;
        } else {
            return base_api + BuildConfig.stringListStarredMessage + (room_id.equals("")?room_id : ("?room_id="+room_id));

        }
    }

    public static String list_starred_message(String room_id, String user_id) {
        if(room_id.equalsIgnoreCase("")) {
            return base_api + BuildConfig.stringListGlobalStarredMessage;
        } else {
            if(room_id.equals("null")){
                return base_api + BuildConfig.stringListStarredMessage + "?user_id="+user_id;
            } else {
                return base_api + BuildConfig.stringListStarredMessage + "?room_id="+room_id;
            }


        }
    }

    public static String reaction_message() {
        return base_api + BuildConfig.stringReactionChat;
    }

    public static String list_room_media(String room_id) {
        if(room_id.equalsIgnoreCase("")) {
            return base_api + BuildConfig.stringListRoomMedia;
        } else {
            return base_api + BuildConfig.stringListRoomMedia + (room_id.equals("")?room_id : ("?room_id="+room_id));

        }
    }

    public static String createRoom() {
        return base_api + BuildConfig.stringCreateRoom;
    }

    public static String list_room_media(String room_id, String user_id, String type) {
        if(room_id.equalsIgnoreCase("")) {
            return base_api + BuildConfig.stringListRoomMedia;
        } else {
            if(room_id.equals("null")){
                return base_api + BuildConfig.stringListRoomMedia + "?user_id="+user_id+"&type="+type;
            } else {
                return base_api + BuildConfig.stringListRoomMedia + "?room_id="+room_id+"&type="+type;
            }

        }
    }

    public static String createRoomGroup() {
        return base_api + BuildConfig.stringCreateRoomGroup;
    }

    public static String updatePhotoProfile() {
        return base_api + BuildConfig.stringUpdatePhotoProfile;
    }

    public static String delete_room() {
        return base_api + BuildConfig.stringDeleteRoom;
    }

    public static String clear_room() {
        return base_api + BuildConfig.stringClearRoom;
    }

    public static String remove_from_group() {
        return base_api + BuildConfig.stringRemoveFromGroup;
    }

    public static String update_admingroup_role() {
        return base_api + BuildConfig.stringUpdateAdminGroupRole;
    }

    public static String exit_room_group() {
        return base_api + BuildConfig.stringExitRoomGroup;
    }

    public static String upload_file() {
        return base_api + BuildConfig.stringUploadFile;
    }

    public static String message_group_info(String messageId, String roomid) {
        return base_api + BuildConfig.stringMessageGroupInfo+"?message_id="+messageId+"&room_id="+roomid;
    }

    public static String get_update_room_info(String roomcode) {
        return base_api + BuildConfig.stringUpdateRoomInfo+"?room_code="+roomcode;
    }

    public static String pin_message() {
        return base_api + BuildConfig.stringPinMessage;
    }

    public static String get_list_agenda() {
        return base_api + BuildConfig.stringListAgenda;
    }

    public static String post_add_agenda() {
        return base_api + BuildConfig.stringAddAgenda;
    }

    public static String post_update_agenda() {
        return base_api + BuildConfig.stringUpdateAgenda;
    }

    public static String get_delete_agenda(String id) {
        return base_api + BuildConfig.stringDeleteAgenda + "?agenda_id="+id;
    }

    public static String get_detail_agenda(String id) {
        return base_api + BuildConfig.stringDetailAgenda + "?agenda_id="+id;
    }

    public static String get_list_label() {
        return base_api + BuildConfig.stringListLabel;
    }

    public static String post_add_label() {
        return base_api + BuildConfig.stringAddLabel;
    }

    public static String post_update_label() {
        return base_api + BuildConfig.stringUpdateLabel;
    }

    public static String get_delete_label() {
        return base_api + BuildConfig.stringDeleteLabel;
    }

    public static String get_detail_label() {
        return base_api + BuildConfig.stringDetailLabel;
    }

    public static String getFileSharing() {
        return base_api + BuildConfig.stringMyDocument;
    }

    public static String uploadDocumentSharing() {
        return base_api + BuildConfig.stringUploadDocumentSharing;
    }

    public static String room_label() {
        return base_api + BuildConfig.stringListRoomLabel;
    }

    public static String delete_room_label() {
        return base_api + BuildConfig.stringDeleteRoomLabel;
    }

    public static String add_room_label() {
        return base_api + BuildConfig.stringAddRoomLabel;
    }
}
