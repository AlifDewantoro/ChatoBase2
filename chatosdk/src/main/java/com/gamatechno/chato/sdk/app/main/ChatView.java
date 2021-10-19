package com.gamatechno.chato.sdk.app.main;

import com.gamatechno.chato.sdk.app.main.searchlist.SearchChatroomModel;
import com.gamatechno.chato.sdk.module.core.BaseView;

import java.util.List;

public interface ChatView {
    interface Presenter{
        void searchChat(String keyword);
    }

    interface View extends BaseView {
        void onSearchChat(List<SearchChatroomModel> list);
        void onFailedRequestChat();
    }
}
