package com.gamatechno.chato.sdk.app.starredmessage;

import android.content.Context;

import com.gamatechno.chato.sdk.data.DAO.Chat.Chat;
import com.gamatechno.chato.sdk.data.constant.Api;
import com.gamatechno.chato.sdk.module.core.BasePresenter;
import com.gamatechno.chato.sdk.module.request.GGFWRest;
import com.gamatechno.chato.sdk.module.request.RequestInterface;
import com.gamatechno.chato.sdk.utils.ChatoUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StarredMessagePresenter extends BasePresenter implements StarredMessageView.Presenter {
    StarredMessageView.View view;

    public StarredMessagePresenter(Context context, StarredMessageView.View view) {
        super(context);
        this.view = view;
    }

    @Override
    public void requestStarredMessage(String api) {
        GGFWRest.GET(api, new RequestInterface.OnGetRequest() {
            @Override
            public void onPreExecuted() {
                view.onLoading();
            }

            @Override
            public void onSuccess(JSONObject response) {
                view.onHideLoading();
                List<Chat> chatList = new ArrayList<>();
                try {
                    if(response.getBoolean("success")){
                        JSONArray list = response.getJSONArray("result");
                        for (int i = list.length()-1; i >= 0; i--) {
                            JSONObject data = list.getJSONObject(i);
                            Chat chat1 = getGson().fromJson(data.toString(), Chat.class);
                            chatList.add(chat1);
                        }
                        view.onRequestStarredMessage(chatList);
                    } else {
                        view.onFailedRequest(response.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String error) {
                view.onHideLoading();
                view.onErrorConnection(error);
            }

            @Override
            public Map<String, String> requestParam() {
                return null;
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

    @Override
    public void requestUnStarMessage(Chat chat, String roomId) {
        GGFWRest.POST(Api.star_message(), new RequestInterface.OnPostRequest() {
            @Override
            public void onPreExecuted() {
//                view.onStarChat(chat, chat.getMessage_star() == 0 ? true : false);
            }

            @Override
            public void onSuccess(JSONObject response) {
                view.onUnStarSuccess();
            }

            @Override
            public void onFailure(String error) {
                view.onUnStarFailed(error);
            }

            @Override
            public Map<String, String> requestParam() {
                Map<String, String> params = new HashMap<>();
                params.put("room_id", roomId);
                params.put("message_id", ""+chat.getMessage_id());
                params.put("status", "UNSTAR");
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
