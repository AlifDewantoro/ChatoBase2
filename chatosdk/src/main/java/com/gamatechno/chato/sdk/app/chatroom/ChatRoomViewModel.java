package com.gamatechno.chato.sdk.app.chatroom;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.gamatechno.chato.sdk.data.DAO.Chat.Chat;

public class ChatRoomViewModel extends ViewModel {
    MutableLiveData<String> appbar_action;
    MutableLiveData<String> chat_image;
    MutableLiveData<Integer> longpress_position;
    MutableLiveData<Integer> clicked_position;
    MutableLiveData<Chat> chat_replied;

    public LiveData<String> initAppbarAction(){
        if(appbar_action == null){
            appbar_action = new MutableLiveData<>();
            appbar_action.postValue("");
        }
        return appbar_action;
    }

    public LiveData<String> initChatImageClick(){
        if(chat_image == null){
            chat_image = new MutableLiveData<>();
            chat_image.postValue("");
        }
        return chat_image;
    }

    public LiveData<Integer> initLongPress(){
        if(longpress_position == null){
            longpress_position = new MutableLiveData<>();
        }
        return longpress_position;
    }

    public LiveData<Integer> initClickChat(){
        if(clicked_position == null){
            clicked_position = new MutableLiveData<>();
        }
        return clicked_position;
    }

    public LiveData<Chat> initRepliedMessage(){
        if(chat_replied == null){
            chat_replied = new MutableLiveData<>();
        }
        return chat_replied;
    }



    public void onClickItemView(int data){
        if(longpress_position!=null){
            longpress_position.postValue(data);
        }
    }

    public void onClickRepliedMessage(Chat chat){
        if(chat_replied!=null){
            chat_replied.postValue(chat);
        }
    }

    public void onLongClick(int data){
        if(longpress_position!=null){
            longpress_position.postValue(data);
        }
    }

    public void onImageClick(String data){
        if(chat_image!=null){
            chat_image.postValue(data);
        }
    }

    public void updateAppbarAction(String data){
        if(appbar_action!=null){
            appbar_action.postValue(data);
        }
    }

}
