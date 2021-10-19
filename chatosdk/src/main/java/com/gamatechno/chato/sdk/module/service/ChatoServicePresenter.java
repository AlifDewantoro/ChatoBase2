package com.gamatechno.chato.sdk.module.service;

import android.content.Context;
import android.content.Intent;

import com.gamatechno.chato.sdk.data.DAO.RoomChat.RoomChat;
import com.gamatechno.chato.sdk.data.constant.Api;
import com.gamatechno.chato.sdk.data.constant.StringConstant;
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
import java.util.Map;
import java.util.Timer;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
//import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;

public class ChatoServicePresenter extends BasePresenter implements ChatoServiceView.Presenter {

    private boolean serviceIsRunning = true;
//    private final long DELAY = 1000; // milliseconds
//
//    private final CompositeDisposable disposables = new CompositeDisposable();

    ChatoServiceView.View view;
    private Timer timer;

    Gson gson = new Gson();

    public ChatoServicePresenter(Context context, ChatoServiceView.View view) {
        super(context);
        this.view = view;
        timer = new Timer();
    }

    @Override
    public void resumeService() {
        timer.cancel();
        serviceIsRunning = true;
    }

    @Override
    public void requestListGroup() {
        GGFWRest.GET(Api.string_list_roomgroup(), new RequestInterface.OnGetRequest() {
            @Override
            public void onPreExecuted() {

            }

            @Override
            public void onSuccess(JSONObject response) {
                try {
                    JSONArray list = response.getJSONArray("result");
                    ArrayList<RoomChat> rooms = new ArrayList<>();
                    if(response.getBoolean("success")){
                        for (int i = 0; i < list.length(); i++) {
                            JSONObject data = list.getJSONObject(i);
                            RoomChat roomChat = gson.fromJson(data.toString(), RoomChat.class);
                            rooms.add(roomChat);
                        }
                        view.onRequestListGroup(rooms);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String error) {

            }

            @Override
            public Map<String, String> requestParam() {
                Map<String, String> params = new HashMap<>();
                return params;
            }

            @Override
            public Map<String, String> requestHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + ChatoUtils.getUserLogin(getContext()).getAccess_token());
                return headers;
            }
        });
    }

    @Override
    public void stopService() {
        Observable.interval(1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .take(2)
                .subscribeWith(new DisposableObserver<Long>() {
                    @Override
                    public void onNext(Long aLong) {
                        if(!serviceIsRunning){
//                            serviceIsRunning = false;
                            view.onStopChatService();
                        } else {
                            serviceIsRunning = false;
                            getContext().sendBroadcast(new Intent(StringConstant.activity_check_stop));
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
//        timer = new Timer();
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                if(!serviceIsRunning){
//                    serviceIsRunning = false;
//                    view.onStopChatService();
//                } else {
//                    serviceIsRunning = false;
//                    getContext().sendBroadcast(new Intent(StringConstant.activity_check_stop));
//                    timer = new Timer();
//                    timer.schedule(new TimerTask() {
//                        @Override
//                        public void run() {
//                            if(!serviceIsRunning){
//                                view.onStopChatService();
//                            }
//                        }
//                    }, DELAY);
//                }
//            }
//        }, DELAY);
    }
}
