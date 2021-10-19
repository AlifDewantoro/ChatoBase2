package com.gamatechno.chato.sdk.app.kontakchat;

import com.gamatechno.chato.sdk.module.core.BaseView;

import java.util.List;

public interface KontakView {
    interface Presenter{
        void requestKontak(Boolean isRefresh, Boolean isForwarded);
        void createRoomId(KontakModel model);
        void searchUser(String keyword, Boolean isRefresh);
    }

    interface View extends BaseView {
        void onRequestKontak(List<KontakModel> models, List<KontakModel> group_models, boolean isRefresh);
        void onCreateRoomId(KontakModel model);
        void onFailedRequestKontak(boolean isRefresh);
        void onLoadMore();
    }
}
