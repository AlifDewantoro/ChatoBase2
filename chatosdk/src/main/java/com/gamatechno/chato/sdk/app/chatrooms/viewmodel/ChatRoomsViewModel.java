package com.gamatechno.chato.sdk.app.chatrooms.viewmodel;

import androidx.lifecycle.LiveData;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.MutableLiveData;

import com.gamatechno.chato.sdk.app.chatrooms.uimodel.ChatRoomsUiModel;

public class ChatRoomsViewModel extends ViewModel {
    MutableLiveData<String> keyword;
    MutableLiveData<Boolean> scrollStatus;
    MutableLiveData<Boolean> requestRefreshRoom;
    MutableLiveData<Boolean> requestPinned;
    MutableLiveData<Boolean> requestDelete;
    MutableLiveData<Boolean> startChat;
    MutableLiveData<Boolean> labelCheck;
    MutableLiveData<ChatRoomsUiModel> chatroomsLongPress;
    MutableLiveData<ChatRoomsUiModel> chatRoomClickFromSearch;
    MutableLiveData<ChatRoomsUiModel> chatRoomLongPressFromSearch;

    MutableLiveData<Boolean> backpressedInit;
    MutableLiveData<Boolean> backpressedUpdate;

    public LiveData<Boolean> initStartChat(){
        if(startChat == null){
            startChat = new MutableLiveData<>();
        }
        return startChat;
    }

    public void updateStartChat(boolean isBackPressed){
        if(startChat!=null){
            startChat.postValue(isBackPressed);
        } else {
            startChat = new MutableLiveData<>();
            startChat.postValue(isBackPressed);
        }
    }

    public LiveData<Boolean> initBackPressed(){
        if(backpressedInit == null){
            backpressedInit = new MutableLiveData<>();
        }
        return backpressedInit;
    }

    public void updateBackPressed(boolean isBackPressed){
        if(backpressedInit!=null){
            backpressedInit.postValue(isBackPressed);
        } else {
            backpressedInit = new MutableLiveData<>();
            backpressedInit.postValue(isBackPressed);
        }
    }

    public LiveData<Boolean> initBackPressedUpdate(){
        if(backpressedUpdate == null){
            backpressedUpdate = new MutableLiveData<>();
        }
        return backpressedUpdate;
    }

    public void updateBackPressedUpdate(boolean isSearch){
        if(backpressedUpdate!=null){
            backpressedUpdate.postValue(isSearch);
        } else {
            backpressedUpdate = new MutableLiveData<>();
            backpressedUpdate.postValue(isSearch);
        }
    }


    public LiveData<Boolean> initRequestDelete(){
        if(requestDelete == null){
            requestDelete = new MutableLiveData<>();
        }
        return requestDelete;
    }

    public void updateRequestDelete(boolean isDelete){
        if(requestDelete!=null){
            requestDelete.postValue(isDelete);
        } else {
            requestDelete = new MutableLiveData<>();
            requestDelete.postValue(isDelete);
        }
    }

    public LiveData<Boolean> initRequestPin(){
        if(requestPinned == null){
            requestPinned = new MutableLiveData<>();
//            requestPinned.postValue(false);
            requestPinned.postValue(null);
        }
        return requestPinned;
    }

    public void updateRequestPin(boolean isRoom){
        if(requestPinned!=null){
            requestPinned.postValue(isRoom);
        }
    }

    public LiveData<ChatRoomsUiModel> initChatRoomsLongPress(){
        if(chatroomsLongPress == null){
            chatroomsLongPress = new MutableLiveData<>();
            chatroomsLongPress.postValue(new ChatRoomsUiModel());
        }
        return chatroomsLongPress;
    }

    public LiveData<ChatRoomsUiModel> initChatRoomClickFromSearch(){
        if(chatRoomClickFromSearch == null){
            chatRoomClickFromSearch = new MutableLiveData<>();
//            chatRoomClickFromSearch.postValue(new ChatRoomsUiModel());
        }
        return chatRoomClickFromSearch;
    }

    public LiveData<ChatRoomsUiModel> initChatRoomLongPressFromSearch(){
        if(chatRoomLongPressFromSearch == null){
            chatRoomLongPressFromSearch = new MutableLiveData<>();
//            chatRoomLongPressFromSearch.postValue(new ChatRoomsUiModel());
        }
        return chatRoomLongPressFromSearch;
    }

    public LiveData<String> initKeyword(){
        if(keyword == null){
            keyword = new MutableLiveData<>();
            keyword.postValue("");
        }
        return keyword;
    }

    public LiveData<Boolean> initLabel(){
        if(labelCheck == null){
            labelCheck = new MutableLiveData<>();
            labelCheck.postValue(false);
        }
        return labelCheck;
    }


    public LiveData<Boolean> getScroolStatus(){
        if(scrollStatus == null){
            scrollStatus = new MutableLiveData<>();
            scrollStatus.postValue(true);
        }
        return scrollStatus;
    }

    public void updateScrollStatus(boolean isShow){
        if(scrollStatus!=null){
            scrollStatus.postValue(isShow);
        }
    }

    public LiveData<Boolean> initRefreshRoom(){
        if(requestRefreshRoom == null){
            requestRefreshRoom = new MutableLiveData<>();
//            requestRefreshRoom.postValue(true);
        }
        return requestRefreshRoom;
    }

    public void updateRefreshRoom(boolean isShow){
        if(requestRefreshRoom!=null){
            requestRefreshRoom.postValue(isShow);
        }
    }

    public void updateChatRoomsLongPress(ChatRoomsUiModel chatRoomUiModel){
        if(chatroomsLongPress!=null){
            chatroomsLongPress.postValue(chatRoomUiModel);
        }
    }

    public void updateChatRoomsClickFromSearch(ChatRoomsUiModel chatRoomUiModel){
        if(chatRoomClickFromSearch!=null){
            chatRoomClickFromSearch.postValue(chatRoomUiModel);
        }
    }

    public void updateChatRoomsLongPressFromSearch(ChatRoomsUiModel chatRoomUiModel){
        if(chatRoomLongPressFromSearch!=null){
            chatRoomLongPressFromSearch.postValue(chatRoomUiModel);
        }
    }

    public void updateKeyword(String key){
        if(keyword!=null){
            keyword.postValue(key);
        }
    }

    public void updateLabel(Boolean label){
        if(labelCheck!=null){
            labelCheck.postValue(label);
        }
    }
}
