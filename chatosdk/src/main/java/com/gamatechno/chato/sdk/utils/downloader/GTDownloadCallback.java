package com.gamatechno.chato.sdk.utils.downloader;

public interface GTDownloadCallback {
    void onProcess(GTDownloadRequest request);
    void onCancel(GTDownloadRequest request);
    void onSuccess(GTDownloadRequest request);
}
