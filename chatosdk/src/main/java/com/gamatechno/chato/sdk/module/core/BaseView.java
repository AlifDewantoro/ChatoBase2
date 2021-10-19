package com.gamatechno.chato.sdk.module.core;

public interface BaseView {
    void onLoading();
    void onHideLoading();
    void onErrorConnection(String message);
    void onAuthFailed(String error);
}
