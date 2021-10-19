package com.gamatechno.chato.sdk.app.chatroom;

import android.content.Context;

import com.gamatechno.chato.sdk.data.DAO.Chat.Chat;
import com.gamatechno.chato.sdk.data.constant.StringConstant;
import com.gamatechno.chato.sdk.utils.ChatoUtils;

import java.util.ArrayList;
import java.util.List;

public class ChatroomHelper {
    public static int totalSelected(List<Chat> chatList){
        int result = 0;
        for (Chat chat:chatList){
            if(chat.isClicked())
                result++;
        }
        return result;
    }

    public static int getIndexClickedChat(List<Chat> chatList){
        for (int i = 0; i < chatList.size(); i++) {
            if(chatList.get(i).isClicked())
                return i;
        }
        return -1;
    }

    public static String namaPanggilan(String s){
        try{
            if(s.split(" ").length > 0){
                return s.split(" ")[0];
            }
        } catch (Exception e){
            return s;
        }
        return s;
    }

    public static int getIndexUnread(Context context, List<Chat> chatList){
        int user_id = ChatoUtils.getUserLogin(context).getUser_id();
        if(chatList.size() > 1)
            for (int i = 0; i < chatList.size()-1; i++) {
                //            if(chatList.get(i).getMessage_status().equals(StringConstant.chat_status_read) && !chatList.get(i+1).getMessage_status().equals(StringConstant.chat_status_read))
                if(!(chatList.get(i).getMessage_status().equals(StringConstant.chat_status_read)) && chatList.get(i).getFrom_user_id() != user_id)
                    return i+1;
            }
        return -1;
    }

    public static List<Integer> getIndexListClickedChat(List<Chat> chatList){
        List<Integer> getIndexListClickedChat = new ArrayList<>();
        for (int i = 0; i < chatList.size(); i++) {
            if(chatList.get(i).isClicked())
                getIndexListClickedChat.add(i);
        }
        return getIndexListClickedChat;
    }

    public static Boolean isAllSelectedChatisMine(Context context, List<Chat> chatList){
        int user_id = ChatoUtils.getUserLogin(context).getUser_id();
        for(Chat chat: chatList){
            if(chat.getFrom_user_id() != user_id){
                return false;
            }
        }
        return true;
    }

    public static Chat getSelectedOneChat(List<Chat> chatList){
        for (Chat chat: chatList){
            if(chat.isClicked())
                return chat;
        }
        return null;
    }

    public static String typeSelectedChat(List<Chat> chatList){
        if(totalSelected(chatList) == 1){
            for (Chat chat: chatList){
                if(chat.isClicked()){
                    return chat.getMessage_type();
                }
            }
        }
        return "";
    }

    public static List<Chat> getSelectedChatList(List<Chat> chatList){
        List<Chat> list = new ArrayList<>();
        for (Chat chat: chatList){
            if(chat.isClicked())
                list.add(chat);
        }
        return list;
    }

    public static String getMessageChatSelected(List<Chat> chatList){
        String val = "";
        for (Chat chat: chatList){
            if(chat.isClicked())
                val = val+chat.getMessage_text()+"\n";
        }

        return val;
    }

    //    1 = message, 2 = attachment, 3 = multitype
    public static int getClickedChatType(List<Chat> chatList){
        boolean isMessage = false;
        boolean isAttachment = false;
        for (Chat chat: chatList){
            if(chat.isClicked()){
                if(chat.getMessage_type().equals(Chat.chat_type_message)){
                    isMessage = true;
                }

                if(chat.getMessage_type().equals(Chat.chat_type_image) ||
                        chat.getMessage_type().equals(Chat.chat_type_file) ||
                        chat.getMessage_type().equals(Chat.chat_type_audio) ||
                        chat.getMessage_type().equals(Chat.chat_type_video))
                    isAttachment = true;
            }
        }

        if(isMessage && isAttachment){
            return 3;
        } else if(isMessage && !isAttachment){
            return 1;
        } else{
            return 2;
        }
    }
}
