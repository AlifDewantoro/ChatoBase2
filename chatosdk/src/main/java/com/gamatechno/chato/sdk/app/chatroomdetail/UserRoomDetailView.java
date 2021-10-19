package com.gamatechno.chato.sdk.app.chatroomdetail;

import com.gamatechno.chato.sdk.app.chatroomdetail.model.RoomDetailUiModel;
import com.gamatechno.chato.sdk.module.core.BaseView;

public interface UserRoomDetailView {
    interface Presenter{
        void requestRoomDetail(String room_id);
        void requestRoomDetail(String room_id, String room_code);
        void requestRoomBirthday(String email);
    }

    interface View extends BaseView {
        void onRequestRoomDetail(RoomDetailUiModel model);
    }
}
