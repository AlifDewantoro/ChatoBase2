package com.gamatechno.chato.sdk.app.login;

import com.gamatechno.chato.sdk.module.core.BaseView;

public interface LoginView {
    interface Presenter{
        void doLogin(String username, String password);
        void getOfficialToken(String token, ClientView clientView);
        void updateTokenFirebase(String token);
    }
    interface View extends BaseView {
        void onSucces();
        void onSuccessUpdateTokenFirebase();
        void onFailedUpdateTokenFirebase();
    }
    interface ClientView {
        void onAuthResult(Boolean isSuccess, String message);
    }
}
