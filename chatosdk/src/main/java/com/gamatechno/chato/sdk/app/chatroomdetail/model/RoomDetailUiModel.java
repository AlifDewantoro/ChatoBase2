package com.gamatechno.chato.sdk.app.chatroomdetail.model;

import com.gamatechno.chato.sdk.data.DAO.RoomChat.RoomChat;

import java.util.List;

public class RoomDetailUiModel extends RoomChat {

    List<RoomChat> common_group;
    int count_shared;
    int count_star_message;
    String position;
    RoomChat group_detail;

    public RoomChat getGroup_detail() {
        return group_detail;
    }

    public List<RoomChat> getCommon_group() {
        return common_group;
    }

    public int getCount_shared() {
        return count_shared;
    }

    public int getCount_star_message() {
        return count_star_message;
    }

    public String getPosition() {
        return position;
    }
}
