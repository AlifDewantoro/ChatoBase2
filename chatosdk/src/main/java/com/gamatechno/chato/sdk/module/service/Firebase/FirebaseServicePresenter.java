package com.gamatechno.chato.sdk.module.service.Firebase;

import android.content.Context;

import com.gamatechno.chato.sdk.data.DAO.Chat.Chat;
import com.gamatechno.chato.sdk.data.DAO.Chat.NotifChat;
import com.gamatechno.chato.sdk.data.DAO.RoomChat.RoomChat;
import com.gamatechno.chato.sdk.data.model.PublishToRoom;
import com.gamatechno.chato.sdk.data.constant.Api;
import com.gamatechno.chato.sdk.data.constant.StringConstant;
import com.gamatechno.chato.sdk.module.core.BasePresenter;
import com.gamatechno.chato.sdk.module.request.GGFWRest;
import com.gamatechno.chato.sdk.module.request.RequestInterface;
import com.gamatechno.chato.sdk.module.service.ChatServiceView;
import com.gamatechno.chato.sdk.utils.ChatoUtils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FirebaseServicePresenter extends BasePresenter implements ChatServiceView.Presenter {
    public FirebaseServicePresenter(Context context) {
        super(context);
    }

    @Override
    public void checkSocket() {

    }

    @Override
    public void checkTokenAvailibillity(boolean isForceToReq) {

    }

    @Override
    public void sendStatusMessage(List<NotifChat> notifChats, ChatServiceView.OnSendStatusMessage onSendStatusMessage) {

    }

    @Override
    public void sendMessage(String message, RoomChat roomChat) {

    }

    @Override
    public void publishToRoom(PublishToRoom publishToRoom) {

    }

    @Override
    public void leaveRoom(PublishToRoom publishToRoom) {

    }

    @Override
    public void joinRoom(PublishToRoom publishToRoom) {

    }

    @Override
    public void sendStatusMessage(List<Chat> chatList) {
        if(chatList.size()>0){
            GGFWRest.POST(Api.update_status_message(), new RequestInterface.OnPostRequest() {
                @Override
                public void onPreExecuted() {

                }

                @Override
                public void onSuccess(JSONObject response) {

                }

                @Override
                public void onFailure(String error) {

                }

                @Override
                public Map<String, String> requestParam() {
                    Map<String, String> params = new HashMap<>();
                    for (int i = 0; i < chatList.size(); i++) {
                        params.put("message_id["+i+"]", ""+chatList.get(i).getMessage_id());
                    }
                    params.put("message_status", StringConstant.chat_status_delivered);
                    params.put("room_id",chatList.get(0).getRoom_id());
                    params.put("to_user_id", ""+chatList.get(0).getFrom_user_id());
                    return params;
                }

                @Override
                public Map<String, String> requestHeaders() {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Authorization", "Bearer " + ChatoUtils.getUserLogin(getContext()).getAccess_token());
                    if(customer!=null){
                        headers.put("customer_app_id", ""+customer.getCustomer_app_id());
                        headers.put("customer_secret", ""+customer.getCustomer_secret());
                    }
                    return headers;
                }
            });
        }
    }

    @Override
    public void registerGrouptoFirebase(List<RoomChat> roomChats) {

    }

    @Override
    public void disconnectSocket() {

    }
}
