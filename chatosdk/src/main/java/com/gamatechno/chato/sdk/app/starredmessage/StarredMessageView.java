package com.gamatechno.chato.sdk.app.starredmessage;

import com.gamatechno.chato.sdk.data.DAO.Chat.Chat;
import com.gamatechno.chato.sdk.module.core.BaseView;

import java.util.List;

public interface StarredMessageView {
    interface Presenter{
        void requestStarredMessage(String api);
        void requestUnStarMessage(Chat chat, String roomId);
    }
    interface View extends BaseView {
        void onRequestStarredMessage(List<Chat> chatList);
        void onFailedRequest(String message);
        void onUnStarSuccess();
        void onUnStarFailed(String text);
    }
}
