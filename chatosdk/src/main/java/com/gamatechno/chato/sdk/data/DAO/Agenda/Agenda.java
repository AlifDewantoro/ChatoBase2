package com.gamatechno.chato.sdk.data.DAO.Agenda;

import com.gamatechno.chato.sdk.app.kontakchat.KontakModel;

import java.util.ArrayList;
import java.util.List;

public class Agenda {
    private String agenda_id;
    private String agenda_title;
    private String agenda_desc;
    private String agenda_date;
    private String agenda_place;
    private String agenda_category;
    private int agenda_is_reminder;
    private int insert_user;
    private int is_active;
    private String insert_timestamp;
    private String delete_timestamp;
    private List<KontakModel> participant = new ArrayList<>();

    public String getAgenda_category() {
        return agenda_category;
    }

    public void setAgenda_category(String agenda_category) {
        this.agenda_category = agenda_category;
    }

    public List<KontakModel> getParticipant() {
        return participant;
    }

    public void setParticipant(List<KontakModel> participant) {
        this.participant = participant;
    }

    public String getAgenda_id() {
        return agenda_id;
    }

    public void setAgenda_id(String agenda_id) {
        this.agenda_id = agenda_id;
    }

    public String getAgenda_title() {
        return agenda_title;
    }

    public void setAgenda_title(String agenda_title) {
        this.agenda_title = agenda_title;
    }

    public String getAgenda_desc() {
        return agenda_desc;
    }

    public void setAgenda_desc(String agenda_desc) {
        this.agenda_desc = agenda_desc;
    }

    public String getAgenda_date() {
        return agenda_date;
    }

    public void setAgenda_date(String agenda_date) {
        this.agenda_date = agenda_date;
    }

    public String getAgenda_place() {
        return agenda_place;
    }

    public void setAgenda_place(String agenda_place) {
        this.agenda_place = agenda_place;
    }

    public int getAgenda_is_reminder() {
        return agenda_is_reminder;
    }

    public void setAgenda_is_reminder(int agenda_remainder) {
        this.agenda_is_reminder = agenda_remainder;
    }

    public int getInsert_user() {
        return insert_user;
    }

    public void setInsert_user(int insert_user) {
        this.insert_user = insert_user;
    }

    public int getIs_active() {
        return is_active;
    }

    public void setIs_active(int is_active) {
        this.is_active = is_active;
    }

    public String getInsert_timestamp() {
        return insert_timestamp;
    }

    public void setInsert_timestamp(String insert_timestamp) {
        this.insert_timestamp = insert_timestamp;
    }

    public String getDelete_timestamp() {
        return delete_timestamp;
    }

    public void setDelete_timestamp(String delete_timestamp) {
        this.delete_timestamp = delete_timestamp;
    }

    public final static String PERSONAL = "ALL";
    public final static String GROUP = "GROUP";
    public final static String DIRECT = "DIRECT";
}
