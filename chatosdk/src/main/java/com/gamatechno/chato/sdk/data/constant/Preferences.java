package com.gamatechno.chato.sdk.data.constant;

import com.gamatechno.chato.sdk.BuildConfig;

public class Preferences {
    private static String package_name = BuildConfig.LIBRARY_PACKAGE_NAME;

    public static final String USER_NAME                  = package_name+"_user_name";
    public static final String ACCESS_TOKEN			= package_name+"access_token";
    public static final String FIREBASE_TOKEN			= package_name+"firebase_token";
    public static final String USER_ID                  = package_name+"_user_id";
    public static final String USER_NIP                  = package_name+"_user_nip";
    public static final String USER_NIP_LAMA                  = package_name+"_user_nip_lama";
    public static final String USER_USERNAME                  = package_name+"_user_username";
    public static final String USER_JABATAN                  = package_name+"_user_jabatan";
    public static final String USER_PHOTO                  = package_name+"_user_photo";
    public static final String USER_REFRESH_TOKEN                  = package_name+"_user_refresh_token";
    public static final String USER_TOKEN_IAT                  = package_name+"_user_token_iat";
    public static final String USER_TOKEN_EXP                  = package_name+"_user_token_exp";
    public static final String USER_LOGIN                  = package_name+"_user_login";
    public static final String USER_GROUP                  = package_name+"_user_group";
    public static final String INTRO_APP                  = package_name+"_intro_app";
    public static final String CUSTOMER_INFO                  = package_name+"_customer_info";

    // Penanda bahwa room sedang dibuka
    public static final String CHATROOM_STATE                  = package_name+"_chatroom_state";
    // Penanda id room yang ternotif pesan baru, sehingga dapat refresh otomatis ketika user buka kembali room tsb
    public static final String CHATROOM_ID_FROM_NOTIF                  = package_name+"_chatroom_ID_notif";
    // Penanda id room yang sedang dibuka
    public static final String OPENED_CHATROOM_ID                  = package_name+"opened_chatroom_id";

    public static final String CHATROOM_SORTING = package_name+"_chatroom_sorting";


}
