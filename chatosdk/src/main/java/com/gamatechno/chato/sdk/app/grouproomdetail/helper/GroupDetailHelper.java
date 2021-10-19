package com.gamatechno.chato.sdk.app.grouproomdetail.helper;

import com.gamatechno.chato.sdk.data.model.OptionModel;

import java.util.ArrayList;
import java.util.List;

public class GroupDetailHelper {
    public static String id_chat = "1";
    public static String id_addtoadmin = "2";
    public static String id_removefromadmin = "3";
    public static String id_deletemember = "4";

    public static List<OptionModel> list_foradmin(String name, String idUser, String idUserCliked){
        List<OptionModel> datas = new ArrayList<>();
        if (!idUser.equals(idUserCliked)) {
            datas.add(new OptionModel(id_chat, "Mulai Obrolan dengan " + name, ""));
        }
        datas.add(new OptionModel(id_removefromadmin, "Hapus akses Admin milik "+name, ""));
        return datas;
    }

    public static List<OptionModel> list_foruser(String name, String idUser, String idUserCliked){
        List<OptionModel> datas = new ArrayList<>();
        if (!idUser.equals(idUserCliked)) {
            datas.add(new OptionModel(id_chat, "Mulai Obrolan dengan " + name, ""));
        }
        return datas;
    }
    public static List<OptionModel> list_forotheradmin(String name, String idUser, String idUserCliked){
        List<OptionModel> datas = new ArrayList<>();
        if (!idUser.equals(idUserCliked)) {
            datas.add(new OptionModel(id_chat, "Mulai Obrolan dengan " + name, ""));
        }
        datas.add(new OptionModel(id_addtoadmin, "Tambah akses Admin untuk "+name, ""));
        datas.add(new OptionModel(id_deletemember, "Hapus member untuk "+name, ""));
        return datas;
    }
}
