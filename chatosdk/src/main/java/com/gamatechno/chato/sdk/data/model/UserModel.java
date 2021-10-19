package com.gamatechno.chato.sdk.data.model;

public class UserModel {
    int user_id;
    String user_name;
    String user_email = "";
    String user_username;
    String user_photo;
    String access_token = "";
    String refresh_token;
    String token_iat;
    String token_exp;
    int is_online;
    String last_seen;

    public UserModel(String access_token, String refresh_token) {
        this.access_token = access_token;
        this.refresh_token = refresh_token;
    }

    public UserModel() {

    }

    public UserModel(int user_id) {
        this.user_id = user_id;
    }

    public UserModel(String user_name, String user_email, String user_photo) {
        this.user_name = user_name;
        this.user_email = user_email;
        this.user_photo = user_photo;
    }

    public UserModel(int user_id, String user_name, String user_username, String user_photo, String access_token, String refresh_token, String token_iat, String token_exp) {
        this.user_id = user_id;
        this.user_name = user_name;
        this.user_username = user_username;
        this.user_photo = user_photo;
        this.access_token = access_token;
        this.refresh_token = refresh_token;
        this.token_iat = token_iat;
        this.token_exp = token_exp;
    }

    public String getUser_email() {
        return user_email;
    }

    public int getIs_online() {
        return is_online;
    }

    public void setIs_online(int is_online) {
        this.is_online = is_online;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getUser_name() {
        return (user_name != null && !user_name.isEmpty() && !user_name.equalsIgnoreCase("null")) ? user_name : "-";
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_username() {
        return (user_username != null && !user_username.isEmpty() && !user_username.equalsIgnoreCase("null")) ? user_username : "-";
    }

    public void setUser_username(String user_username) {
        this.user_username = user_username;
    }

    public String getUser_photo() {
        return (user_photo != null && !user_photo.isEmpty() && !user_photo.equalsIgnoreCase("null")) ? user_photo : "-";
    }

    public void setUser_photo(String user_photo) {
        this.user_photo = user_photo;
    }

    public String getAccess_token() {
        return (access_token != null && !access_token.isEmpty() && !access_token.equalsIgnoreCase("null")) ? access_token : "-";
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getRefresh_token() {
        return (refresh_token != null && !refresh_token.isEmpty() && !refresh_token.equalsIgnoreCase("null")) ? refresh_token : "-";
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public String getToken_iat() {
        return (token_iat != null && !token_iat.isEmpty() && !token_iat.equalsIgnoreCase("null")) ? token_iat : "-";
    }

    public void setToken_iat(String token_iat) {
        this.token_iat = token_iat;
    }

    public String getToken_exp() {
        return (token_exp != null && !token_exp.isEmpty() && !token_exp.equalsIgnoreCase("null")) ? token_exp : "-";
    }

    public void setToken_exp(String token_exp) {
        this.token_exp = token_exp;
    }

    public String getLast_seen() {
        return last_seen;
    }

    public void setLast_seen(String last_seen) {
        this.last_seen = last_seen;
    }
}
