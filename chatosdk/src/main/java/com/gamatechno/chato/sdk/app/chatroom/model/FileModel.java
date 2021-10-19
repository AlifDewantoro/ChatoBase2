package com.gamatechno.chato.sdk.app.chatroom.model;

import android.net.Uri;

import java.io.Serializable;

public class FileModel implements Serializable {
    String uri;
    String mimetype;
    String namefile = "";

    public FileModel(String uri, String mimetype) {
        this.uri = uri;
        this.mimetype = mimetype;
    }

    public FileModel(String uri, String mimetype, String namefile) {
        this.uri = uri;
        this.mimetype = mimetype;
        this.namefile = namefile;
    }

    public String getNamefile() {
        return namefile;
    }

    public void setNamefile(String namefile) {
        this.namefile = namefile;
    }

    public Uri getUri() {
        return Uri.parse(uri);
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getMimetype() {
        return mimetype;
    }

    public void setMimetype(String mimetype) {
        this.mimetype = mimetype;
    }
}
