package com.gamatechno.chato.sdk.app.grouproomadd.addgroup.detailgroup;

import android.content.Context;
import android.content.Intent;

import com.gamatechno.chato.sdk.data.DAO.RoomChat.RoomChat;
import com.gamatechno.chato.sdk.data.constant.Api;
import com.gamatechno.chato.sdk.data.constant.StringConstant;
import com.gamatechno.chato.sdk.module.core.BasePresenter;
import com.gamatechno.chato.sdk.module.request.GGFWRest;
import com.gamatechno.chato.sdk.module.request.RequestInterface;
import com.gamatechno.chato.sdk.utils.ChatoUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AddDetailGroupPresenter extends BasePresenter implements AddDetailGroupView.Presenter {
    AddDetailGroupView.View view;

    public AddDetailGroupPresenter(Context context, AddDetailGroupView.View view) {
        super(context);
        this.view = view;
    }

    @Override
    public void requestAddGroup(AddDetailGroupUiModel model, String photo_url) {
        GGFWRest.POST(Api.createRoomGroup(), new RequestInterface.OnPostRequest() {
            @Override
            public void onPreExecuted() {
                view.onLoading();
            }

            @Override
            public void onSuccess(JSONObject response) {
                view.onHideLoading();
                try {
                    JSONObject obj = response.getJSONObject("result");
                    if (response.getBoolean("success")) {
                        view.onSuccessAddingGroup(getGson().fromJson(obj.toString(), RoomChat.class), response.getString("message"));
                        getContext().sendBroadcast(new Intent(StringConstant.service_requestgroup_tofirebase_register));
                    } else {
                        view.onFailedAddingGroup(response.getString("message"));
                    }

                } catch (JSONException e) {
                    view.onHideLoading();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String error) {
                view.onHideLoading();
                view.onAuthFailed(ChatoUtils.messageError(error));
            }

            @Override
            public Map<String, String> requestParam() {
                Map<String, String> params = new HashMap<>();
                params.put("group_name", model.getTitle());
                params.put("group_photo", photo_url);
//                params.put("group_photo", model.getBase64_image());
                params.put("group_desc", model.getDeskripsi());
                params.put("group_type", model.getGroup_type());
                for (int i = 0; i < model.getKontakModelList().size(); i++) {
                    params.put("user_id["+i+"]", ""+model.getKontakModelList().get(i).getUser_id());
                }
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
