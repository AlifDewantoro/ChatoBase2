package com.gamatechno.chato.sdk.app.playvideo.sendvideopreview.model;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;

import com.gamatechno.chato.sdk.utils.FilePath.FilePath;
import com.gamatechno.chato.sdk.utils.FilePath.IOUtils;

import java.io.File;

public class VideoPreview{

    public VideoPreview(Context context, File file, String messageVideo, String video_name) {
        this.file = file;
        this.base64Video = IOUtils.convertToBase64(IOUtils.getInputStream(context, Uri.fromFile(file)));
        this.messageVideo = messageVideo;
        this.video_name = video_name;
    }

    public VideoPreview() {

    }

    private String base64Video, messageVideo, video_name;
    Uri compressedPath;
    private File file;

    public File getFile() {
        return file;
    }

    public String getMimeType(Context context){
        return FilePath.getMimeType(context, Uri.fromFile(file));
    }

    public String getUri(){
        return Uri.fromFile(file).toString();
    }

    public Uri getRealUri(){
        return Uri.fromFile(file);
    }


    public String getBase64Video() {
        return base64Video;
    }

    public String getVideo_name() {
        return video_name;
    }

    public Uri getUriPath() {
        return Uri.withAppendedPath(Uri.fromFile(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)), "/Chato/Video/"+video_name);
    }

    public String getMessageVideo() {
        return messageVideo;
    }

    public void setBase64Video(File file) {
        this.base64Video = base64Video;
    }

    public void setMessageVideo(String messageVideo) {
        this.messageVideo = messageVideo;
    }
}
