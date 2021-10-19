package com.gamatechno.chato.sdk.app.login;

import android.content.Context;

import com.gamatechno.chato.sdk.data.model.UserModel;
import com.gamatechno.chato.sdk.data.constant.Api;
import com.gamatechno.chato.sdk.module.core.BasePresenter;
import com.gamatechno.chato.sdk.module.request.GGFWRest;
import com.gamatechno.chato.sdk.module.request.RequestInterface;
import com.gamatechno.chato.sdk.utils.ChatoUtils;
import com.gamatechno.ggfw.utils.GGFWUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginPresenter extends BasePresenter implements LoginView.Presenter{

    LoginView.View view;


    public LoginPresenter(Context context, LoginView.View view) {
        super(context);
        this.view = view;
    }

    @Override
    public void doLogin(final String username, final String password) {

        String api = Api.signin();

        GGFWRest.POST(api, new RequestInterface.OnPostRequest() {
            @Override
            public void onPreExecuted() {
                view.onLoading();
            }

            @Override
            public void onSuccess(JSONObject response) {
                view.onHideLoading();
                try {
                    if (response.getBoolean("success")) {
                        JSONObject obj = response.getJSONObject("result");
                        ChatoUtils.setUserLogin(getContext(), gson.fromJson(obj.toString(), UserModel.class));
                        view.onSucces();
                    } else {
                        view.onAuthFailed(response.getString("message"));
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
                params.put("username", username);
                params.put("password", password);
//                params.put("type", "MANUAL");
                return params;
            }

            @Override
            public Map<String, String> requestHeaders() {
                Map<String, String> headers = new HashMap<>();

                headers.put("Content-Type", "application/x-www-form-urlencoded");

                if(customer!=null){
                    headers.put("customer_app_id", ""+customer.getCustomer_app_id());
                    headers.put("customer_secret", ""+customer.getCustomer_secret());
                }

                return headers;
            }
        });
    }

    @Override
    public void getOfficialToken(String token, LoginView.ClientView clientView) {
        GGFWRest.POST(Api.customer_token(), new RequestInterface.OnPostRequest() {
            @Override
            public void onPreExecuted() {

            }

            @Override
            public void onSuccess(JSONObject response) {

                try {
                    if(response.getBoolean("success")){
                        JSONObject obj = response.getJSONObject("result");
                        ChatoUtils.setUserLogin(getContext(), gson.fromJson(obj.toString(), UserModel.class));
                        clientView.onAuthResult(true, "Sukses autentikasi Chato");
                    } else {
                        clientView.onAuthResult(false, response.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String error) {
                clientView.onAuthResult(false, "Gagal autentikasi Chato");
            }

            @Override
            public Map<String, String> requestParam() {
                Map<String, String> params = new HashMap<>();
                params.put("jwt_client", token);
                if(customer!=null){
                    params.put("customer_app_id", ""+customer.getCustomer_app_id());
                    params.put("customer_secret", ""+customer.getCustomer_secret());
                }
                return params;
            }

            @Override
            public Map<String, String> requestHeaders() {
                Map<String, String> headers = new HashMap<>();
                if(customer!=null){
                    headers.put("customer_app_id", ""+customer.getCustomer_app_id());
                    headers.put("customer_secret", ""+customer.getCustomer_secret());
                }
                return headers;
            }
        });
    }

    @Override
    public void updateTokenFirebase(final String token) {
        GGFWRest.POST(Api.update_device_token(), new RequestInterface.OnPostRequest() {
            @Override
            public void onPreExecuted() {

            }

            @Override
            public void onSuccess(JSONObject response) {
                view.onHideLoading();
                try {
                    if(response.getBoolean("success")){
                        view.onSuccessUpdateTokenFirebase();
                    } else {
                        GGFWUtil.ToastShort(getContext(), "Return false but Success");
                        view.onSuccessUpdateTokenFirebase();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String error) {
                view.onHideLoading();
                view.onFailedUpdateTokenFirebase();
            }

            @Override
            public Map<String, String> requestParam() {
                Map<String, String> params = new HashMap<>();
                params.put("device_token", token);
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
