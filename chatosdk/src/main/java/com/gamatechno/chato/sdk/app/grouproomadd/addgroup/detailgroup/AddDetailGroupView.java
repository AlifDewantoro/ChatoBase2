package com.gamatechno.chato.sdk.app.grouproomadd.addgroup.detailgroup;

import com.gamatechno.chato.sdk.data.DAO.RoomChat.RoomChat;
import com.gamatechno.chato.sdk.module.core.BaseView;

public interface AddDetailGroupView {
    interface Presenter{
        public void requestAddGroup(AddDetailGroupUiModel model, String photo_url);
    }

    interface View extends BaseView {
        public void onSuccessAddingGroup(RoomChat roomChat, String message);
        public void onFailedAddingGroup(String message);
    }
}
