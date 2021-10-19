package com.gamatechno.chato.sdk.app.grouproomadd.addgroup.detailgroup;

import android.net.Uri;

import com.gamatechno.chato.sdk.app.kontakchat.KontakModel;

import java.util.List;

public class AddDetailGroupUiModel {
    String title;
    String deskripsi;
    String base64_image;
    String group_type;
    Uri uri;
    List<KontakModel> kontakModelList;


    public AddDetailGroupUiModel() {
    }

    public AddDetailGroupUiModel(String title, String deskripsi, String base64_image, List<KontakModel> kontakModelList) {
        this.title = title;
        this.deskripsi = deskripsi;
        this.base64_image = base64_image;
        this.kontakModelList = kontakModelList;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public String getGroup_type() {
        return group_type;
    }

    public void setGroup_type(String group_type) {
        this.group_type = group_type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getBase64_image() {
        return base64_image;
    }

    public void setBase64_image(String base64_image) {
        this.base64_image = base64_image;
    }

    public List<KontakModel> getKontakModelList() {
        return kontakModelList;
    }

    public void setKontakModelList(List<KontakModel> kontakModelList) {
        this.kontakModelList = kontakModelList;
    }
}
