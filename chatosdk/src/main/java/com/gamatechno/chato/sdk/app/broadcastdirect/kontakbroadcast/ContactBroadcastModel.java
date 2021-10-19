package com.gamatechno.chato.sdk.app.broadcastdirect.kontakbroadcast;

import com.gamatechno.chato.sdk.app.kontakchat.KontakModel;

public class ContactBroadcastModel {
    private int type;
    private KontakModel contact;
    private boolean isSelected;

    ContactBroadcastModel(int type, KontakModel contact) {
        this.type = type;
        this.contact = contact;
    }

    public int getType() {
        return type;
    }

    public KontakModel getContact() {
        return contact;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
