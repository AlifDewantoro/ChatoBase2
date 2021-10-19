package com.gamatechno.chato.sdk.app.kontakchat;

import android.content.Context;

import com.gamatechno.chato.sdk.data.constant.Api;
import com.gamatechno.chato.sdk.module.core.BasePresenter;
import com.gamatechno.chato.sdk.module.request.GGFWRest;
import com.gamatechno.chato.sdk.module.request.RequestInterface;
import com.gamatechno.chato.sdk.utils.ChatoUtils;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KontakPresenter extends BasePresenter implements KontakView.Presenter {

    KontakView.View view;
    int page = 0;
    Gson gson;

    public KontakPresenter(Context context, KontakView.View view) {
        super(context);
        this.view = view;
        this.gson = new Gson();
    }

    @Override
    public void requestKontak(Boolean isRefresh, Boolean isForwarded) {

        if(isRefresh)
            page = 0;
        page++;

        GGFWRest.GETAuth(Api.list_kontak(""+page), getContext(), new RequestInterface.OnAuthGetRequest() {
            @Override
            public void onPreExecuted() {
                if(!isRefresh)
                    view.onLoadMore();
                else
                    view.onLoading();
            }

            @Override
            public void onSuccess(JSONObject response) {
                view.onHideLoading();
                try {
                    if(response.getBoolean("success")){
                        List<KontakModel> models = new ArrayList<>();
                        List<KontakModel> groupmodels = new ArrayList<>();
                        JSONObject result = response.getJSONObject("result");
                        JSONArray list_user = result.getJSONArray("list_user");
                        JSONArray list_group = result.getJSONArray("list_group");
                        for (int i = 0; i < list_user.length(); i++) {
                            models.add(gson.fromJson(list_user.get(i).toString(), KontakModel.class));
                        }
                        if(isForwarded){
                            for (int i = 0; i < list_group.length(); i++) {
                                groupmodels.add(gson.fromJson(list_group.get(i).toString(), KontakModel.class));
                            }
                        }
                        view.onRequestKontak(models, groupmodels, isRefresh);
                    } else {
                        view.onFailedRequestKontak(isRefresh);
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
                return headers;
            }
        });
    }

    @Override
    public void createRoomId(KontakModel model) {
        GGFWRest.POST(Api.createRoom(), new RequestInterface.OnPostRequest(){

            @Override
            public void onPreExecuted() {
                view.onLoading();
            }

            @Override
            public void onSuccess(JSONObject response) {
                try {
                    if(response.getBoolean("success")) {
                        JSONObject result = response.getJSONObject("result");
                        model.setRoom_id(result.has("room_id") ? result.getInt("room_id") : 0);
                        view.onHideLoading();
                        view.onCreateRoomId(model);
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
                Map<String, String> headers = new HashMap<>();
                headers.put("to_user_id", ""+model.user_id);
                return headers;
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
    public void searchUser(String keyword, Boolean isRefresh) {
        if(isRefresh)
            page = 0;
        page++;

        GGFWRest.GET(Api.search_user(keyword, ""+page), new RequestInterface.OnGetRequest() {
            @Override
            public void onPreExecuted() {
                if(!isRefresh)
                    view.onLoadMore();
                else
                    view.onLoading();
            }

            @Override
            public void onSuccess(JSONObject response) {
                view.onHideLoading();
                try {
                    if(response.getBoolean("success")){
                        List<KontakModel> models = new ArrayList<>();
                        JSONObject result = response.getJSONObject("result");
                        JSONArray list_user = result.getJSONArray("list_user");
                        for (int i = 0; i < list_user.length(); i++) {
                            models.add(gson.fromJson(list_user.get(i).toString(), KontakModel.class));
                        }
                        view.onRequestKontak(models, new ArrayList(), isRefresh);
                    } else {
                        view.onFailedRequestKontak(isRefresh);
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
}
