package com.gamatechno.chato.sdk.app.chatroom.menu;

public class MenuModel {
    int icon;
    String title;
    Action action;

    public MenuModel(int icon, String title, Action action) {
        this.icon = icon;
        this.title = title;
        this.action = action;
    }

    public int getIcon() {
        return icon;
    }

    public String getTitle() {
        return title;
    }

    public Action getAction() {
        return action;
    }

    public interface Action{
        void onClick();
    }
}
