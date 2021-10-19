package com.gamatechno.chato.sdk.app.chatroomdetail;

import android.content.Context;
import android.util.Log;

import com.gamatechno.chato.sdk.app.chatroomdetail.model.RoomDetailUiModel;
import com.gamatechno.chato.sdk.data.constant.Api;
import com.gamatechno.chato.sdk.module.core.BasePresenter;
import com.gamatechno.chato.sdk.module.request.GGFWRest;
import com.gamatechno.chato.sdk.module.request.RequestInterface;
import com.gamatechno.chato.sdk.utils.ChatoUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UserRoomDetailPresenter extends BasePresenter implements UserRoomDetailView.Presenter {

    UserRoomDetailView.View view;

    public UserRoomDetailPresenter(Context context, UserRoomDetailView.View view) {
        super(context);
        this.view = view;
    }

    @Override
    public void requestRoomDetail(String room_id) {
        GGFWRest.GETAuth(Api.get_room_detail(room_id), getContext(), new RequestInterface.OnAuthGetRequest() {
            @Override
            public void onPreExecuted() {
                view.onLoading();
            }

            @Override
            public void onSuccess(JSONObject response) {
                view.onHideLoading();
                try {
                    if(response.getBoolean("success")){
                        view.onRequestRoomDetail(getGson().fromJson(response.getJSONObject("result").toString(), RoomDetailUiModel.class));
                    } else {
                        view.onErrorConnection(response.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String error) {
                view.onHideLoading();
                view.onErrorConnection("");
            }

            @Override
            public void onUnauthorized(String error) {
                view.onAuthFailed(error);
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
                Log.d("asdHeaders ", " requestParam: "+headers);
                return headers;
            }
        });
    }

    @Override
    public void requestRoomDetail(String room_id, String room_code) {
        GGFWRest.GETAuth(Api.get_room_detailv2(room_id, room_code), getContext(), new RequestInterface.OnAuthGetRequest() {
            @Override
            public void onPreExecuted() {
                view.onLoading();
            }

            @Override
            public void onSuccess(JSONObject response) {
                view.onHideLoading();
                try {
                    if(response.getBoolean("success")){
                        view.onRequestRoomDetail(getGson().fromJson(response.getJSONObject("result").toString(), RoomDetailUiModel.class));
                    } else {
                        view.onErrorConnection(response.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String error) {
                view.onHideLoading();
                view.onErrorConnection("");
            }

            @Override
            public void onUnauthorized(String error) {
                view.onAuthFailed(error);
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
                Log.d("asdHeaders ", " requestParam: "+headers);
                return headers;
            }
        });
    }

    @Override
    public void requestRoomBirthday(String email) {
        GGFWRest.GETAuth(Api.get_room_birthday(email), getContext(), new RequestInterface.OnAuthGetRequest() {
            @Override
            public void onPreExecuted() {
                view.onLoading();
            }

            @Override
            public void onSuccess(JSONObject response) {
                view.onHideLoading();
                try {
                    if(response.getBoolean("success")){
                        view.onRequestRoomDetail(getGson().fromJson(response.getJSONObject("result").toString(), RoomDetailUiModel.class));
                    } else {
                        view.onErrorConnection(response.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String error) {
                view.onHideLoading();
                view.onErrorConnection("");
            }

            @Override
            public void onUnauthorized(String error) {
                view.onAuthFailed(error);
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
                Log.d("asdHeaders ", " requestParam: "+headers);
                return headers;
            }
        });
    }
}
