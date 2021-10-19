package com.gamatechno.chato.sdk.module.service;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

//import com.gamatechno.chato.sdk.app.call.model.CallRequestParam;
import com.gamatechno.chato.sdk.app.chatroom.model.RoomChatsModel;
import com.gamatechno.chato.sdk.data.DAO.Chat.Chat;
import com.gamatechno.chato.sdk.data.DAO.Chat.NotifChat;
import com.gamatechno.chato.sdk.data.DAO.Chat.dbaccess.NotifChatDatabase;
import com.gamatechno.chato.sdk.data.DAO.RoomChat.RoomChat;
import com.gamatechno.chato.sdk.data.model.ListentoRoomModel;
import com.gamatechno.chato.sdk.data.model.PublishToRoom;
import com.gamatechno.chato.sdk.data.constant.Api;
import com.gamatechno.chato.sdk.data.constant.Preferences;
import com.gamatechno.chato.sdk.data.constant.StringConstant;
import com.gamatechno.chato.sdk.module.core.BasePresenter;
import com.gamatechno.chato.sdk.module.core.SocketSingleton;
import com.gamatechno.chato.sdk.module.request.GGFWRest;
import com.gamatechno.chato.sdk.module.request.RequestInterface;
import com.gamatechno.chato.sdk.utils.ChatoUtils;
import com.gamatechno.ggfw.utils.GGFWUtil;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.subjects.PublishSubject;
import io.socket.client.Ack;
import io.socket.client.Manager;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import io.socket.engineio.client.Transport;


public class ChatServicePresenter extends BasePresenter implements ChatServiceView.Presenter {

    final String TAG = "ChatServicePresenter";

    private Socket mSocket;
    private Gson gson;
    ChatServiceView.View view;

    ListenHandler listenHandler = new ListenHandler();
    NotifChatDatabase chatNotifDatabase;

    private PublishSubject<Integer> subject_group;

    public ChatServicePresenter(Context context, ChatServiceView.View view) {
        super(context);
        this.view = view;
        SocketSingleton singleton = SocketSingleton.get(context);
        mSocket = singleton.getSocket();
        setListenHandler();
        gson = new Gson();
        chatNotifDatabase = new NotifChatDatabase(context);

        checkSocket();
    }

    @Override
    public void checkSocket() {
        if (!mSocket.connected())
            mSocket.connect();
    }

    @Override
    public void checkTokenAvailibillity(boolean isForceToReq) {
        view.onCheckTokenAvailibillity(ChatoUtils.isUserLogin(getContext()));
    }

    @Override
    public void sendStatusMessage(List<NotifChat> notifChats, ChatServiceView.OnSendStatusMessage onSendStatusMessage) {
//        +"?message_id="+message_id+"&message_status="+message_status+"&to_user_id="+to_user_id
        GGFWRest.POST(Api.update_status_message(), new RequestInterface.OnPostRequest() {
            @Override
            public void onPreExecuted() {

            }

            @Override
            public void onSuccess(JSONObject response) {
                if(onSendStatusMessage!=null){
                    onSendStatusMessage.onAfterSendStatus();
                }
            }

            @Override
            public void onFailure(String error) {

            }

            @Override
            public Map<String, String> requestParam() {
                Map<String, String> params = new HashMap<>();
                for (int i = 0; i < notifChats.size(); i++) {
                    params.put("message_id["+i+"]", ""+notifChats.get(i).getMessage_id());
                }
                params.put("message_status", StringConstant.chat_status_read);
                params.put("room_id", notifChats.get(0).getRoom_id());
                params.put("to_user_id", ""+notifChats.get(0).getFrom_user_id());

                /*if(chat.getMessage_status().equals(StringConstant.chat_status_read)){
                    params.put("message_status", StringConstant.chat_status_read);
                } else {
                    if(GGFWUtil.getStringFromSP(getContext(), Preferences.CHATROOM_STATE).equals(StringConstant.chatroom_state_open)){
                        params.put("message_status", StringConstant.chat_status_read);
                    } else {
                        params.put("message_status", StringConstant.chat_status_delivered);
                    }
                }
                params.put("room_id", chat.getRoom_id());
                params.put("to_user_id", ""+chat.getFrom_user_id());*/
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

    @Override
    public void sendMessage(String message, RoomChat roomChat) {
//        Chat chat = roomChat.getDetail_last_message();
//        chat.setMessage_status(StringConstant.chat_status_read);

        new Thread(new Runnable() {
            @Override
            public void run() {
                List<NotifChat> notifChats = chatNotifDatabase.getNotifbyRoomid(""+roomChat.getRoom_id());

                sendStatusMessage(notifChats, new ChatServiceView.OnSendStatusMessage() {
                    @Override
                    public void onAfterSendStatus() {
                        chatNotifDatabase.deleteNotifbyRoomId(""+roomChat.getRoom_id());
                        GGFWRest.POST(Api.send_message(), new RequestInterface.OnPostRequest() {
                            @Override
                            public void onPreExecuted() {

                            }

                            @Override
                            public void onSuccess(JSONObject response) {
                                try {
                                    if(response.getBoolean("success")){
                                        view.onSendMessage(Integer.parseInt(""+roomChat.getRoom_id()));
                                    } else {
                                        view.onFailedSendMessage(response.getString("message"));
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
                                params.put("to_user_id", ("" + roomChat.getRoom_type()).equals(RoomChat.group_room_type) ? ""+roomChat.getRoom_id():""+roomChat.getDetail_last_message().getFrom_user_id());
                                params.put("message", message);
                                params.put("room_id", ""+roomChat.getRoom_id() == null ? "" : ""+roomChat.getRoom_id() );
                                params.put("room_code", ""+roomChat.getRoom_code() == null ? "" : ""+roomChat.getRoom_code() );
                                params.put("message_type", ""+Chat.chat_type_message);
                                params.put("payload", "reply");
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
                });
            }
        }).start();
    }

    @Override
    public void publishToRoom(PublishToRoom publishToRoom) {
        Log.d(TAG, "emitGetListUser publishToRoom: "+gson.toJson(publishToRoom));
        mSocket.emit(StringConstant.SERVICECHAT_PUBLISH_TO_ROOM, getGson().toJson(publishToRoom), new Ack() {
            @Override
            public void call(Object... args) {
                Log.d(TAG, "call: getDatas "+args.length);
                if (args.length > 0) {
                    Log.d(TAG, "emitGetListUser() " + "ACK :\n" + args[0]);
                }
            }
        });
    }

    @Override
    public void leaveRoom(PublishToRoom publishToRoom) {
        mSocket.emit(StringConstant.SERVICECHAT_LEAVE_ROOM, getGson().toJson(publishToRoom), new Ack() {
            @Override
            public void call(Object... args) {
                Log.d(TAG, "call: LeaveRoom "+args.length);
                if (args.length > 0) {
                    Log.d(TAG, "emitLeaveRoom() " + "ACK :\n" + args[0]);
                }
            }
        });
    }

    @Override
    public void joinRoom(PublishToRoom publishToRoom) {
        mSocket.emit(StringConstant.SERVICECHAT_JOIN_ROOM, getGson().toJson(publishToRoom), new Ack() {
            @Override
            public void call(Object... args) {
                Log.d(TAG, "call: getDatas "+args.length);
                if (args.length > 0) {
                    Log.d(TAG, "emitGetListUser() " + "ACK :\n" + args[0]);
                }
            }
        });
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
                    params.put("room_id", chatList.get(0).getRoom_id());
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

    public void sendStatusNotifMessage(List<NotifChat> chatList) {
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
                    params.put("room_id", chatList.get(0).getRoom_id());
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
//        if(!GGFWUtil.getStringFromSP(getContext(), Preferences.USER_GROUP).equals("")){
//            try {
//                RoomChatsModel roomchats = getGson().fromJson(GGFWUtil.getStringFromSP(getContext(), Preferences.USER_GROUP), RoomChatsModel.class);
//                Log.d(TAG, "registerGrouptoFirebase: oldRoomChat: " + roomchats.getRoomChats().size());
//                for (int i = 0; i < roomchats.getRoomChats().size(); i++) {
//                    Log.d(TAG, "registerGrouptoFirebase: roomName: " + roomchats.getRoomChats().get(i).getRoom_topic());
//                    FirebaseMessaging.getInstance().unsubscribeFromTopic(roomChats.get(i).getRoom_topic());
//                }
//
//            } catch (Exception e){
//                GGFWUtil.setStringToSP(getContext(), Preferences.USER_GROUP, "");
//            }
//        }
//
//        Log.d(TAG, "registerGrouptoFirebase: newRoomChat: " + roomChats.size());
//        for (int i = 0; i < roomChats.size(); i++) {
//            Log.d(TAG, "registerGrouptoFirebase: roomName: " + roomChats.get(i).getRoom_topic());
//            FirebaseMessaging.getInstance().subscribeToTopic(roomChats.get(i).getRoom_topic());
//        }

        GGFWUtil.setStringToSP(getContext(), Preferences.USER_GROUP, getGson().toJson(new RoomChatsModel(roomChats)));

        /*subject_group = PublishSubject.create();
        subject_group.subscribeOn(Schedulers.io()).debounce(200, TimeUnit.MILLISECONDS)
                // Be notified on the main thread
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Integer i) {
                        if(i >= roomChats.size()){
                            Log.d(TAG, "onFinish groupexist: "+i);
                            subject_group.onComplete();
                        } else {
                            if(roomChats.get(i).getRoom_topic() != null){
                                Log.d(TAG, "onNext groupexist: "+roomChats.get(i).getRoom_topic());
                                FirebaseMessaging.getInstance().subscribeToTopic(roomChats.get(i).getRoom_topic());
                            }
                            subject_group.onNext(++i);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete: Subscribe groupexist done");
                        GGFWUtil.setStringToSP(getContext(), Preferences.USER_GROUP, getGson().toJson(new RoomChatsModel(roomChats)));
                    }
                });*/
//        if(roomChats.size() > 0){
//            Log.d(TAG, "registerGrouptoFirebase: groupexist nice");
//            subject_group.onNext(0);
//        }
    }

    @Override
    public void disconnectSocket() {
        Log.d(TAG, "disconnectSocket: try disconnecting");
        if(mSocket!=null)
            mSocket.disconnect();
//            if (mSocket.connected()){
//
//            }
    }

    private void setListenHandler(){
        mSocket.io().on(Manager.EVENT_TRANSPORT, listenHandler.onEventTransPort);

        mSocket.on(Socket.EVENT_CONNECT_ERROR, listenHandler.onConnectError);

        mSocket.on(Socket.EVENT_ERROR, listenHandler.onEventError);

        mSocket.on(Socket.EVENT_CONNECT, listenHandler.onEventConnect);

        mSocket.on(Socket.EVENT_DISCONNECT, listenHandler.onEventDisconnect);

        mSocket.on(StringConstant.SERVICECHAT_RECEIVE_MESSAGE, listenHandler.onReceiveMessage);

        mSocket.on(StringConstant.SERVICECHAT_RECEIVE_UPDATESTATUS, listenHandler.onUpdateStatusMessage);

        mSocket.on(StringConstant.SERVICECHAT_LISTEN_TO_ROOM, listenHandler.onListenToRoom);

        mSocket.on(StringConstant.SERVICECHAT_CALL, listenHandler.onListenToRoom);
    }

    public class ListenHandler{
        public ListenHandler() {

        }

        private Emitter.Listener onEventDisconnect = new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Log.d(TAG, "SOCKET DISCONNECTED");
//                mSocket.connect();
            }
        };

        private Emitter.Listener onReceiveMessage = new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Log.d(TAG, "received message called " + String.valueOf(args[0]));
                try {
                    JSONObject responseJsonObject = new JSONObject(String.valueOf(args[0]));
                    JSONObject body = responseJsonObject.getJSONObject("body");
                    RoomChat roomChat = gson.fromJson(body.toString(), RoomChat.class);

                    Chat chat = roomChat.getDetail_last_message();
                    chat.setRoom_id(""+roomChat.getRoom_id());
                    chat.setMessage_status(StringConstant.chat_status_delivered);
                    List<Chat> chats = new ArrayList<>();
                    chats.add(chat);
                    sendStatusMessage(chats);
                    view.onReceiveMessage(roomChat);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        private Emitter.Listener onUpdateStatusMessage = new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Log.d(TAG, "received update status " + String.valueOf(args[0]));
                try {
                    JSONObject responseJsonObject = new JSONObject(String.valueOf(args[0]));
                    JSONObject body = responseJsonObject.getJSONObject("body");
                    JSONArray array = body.getJSONArray("message_id");

                    List<Chat> chatList = new ArrayList<>();
                    for (int i = 0; i < array.length(); i++) {
                        Chat chat = new Chat(Integer.valueOf((String) array.get(i)), body.getInt("from_user_id"), body.getString("room_id"),body.getString("message_status"));
//                        Log.d(TAG, "call: "+chat.getFrom_user_id()+" "+chat.getMessage_id());
                        chatList.add(chat);
                    }

                    view.onReceiveUpdateStatusChat(chatList);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        private Emitter.Listener onListenToRoom = new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Log.d(TAG, "emitGetListUser() received listen to room called " + String.valueOf(args[0]));
                view.onListenToRoom(getGson().fromJson(String.valueOf(args[0]), ListentoRoomModel.class));
            }
        };

        private Emitter.Listener onListenCall = new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Log.d(TAG, "onListenCall() received listen to room called " + String.valueOf(args[0]));
                view.onListenCallSocket(getGson().fromJson(String.valueOf(args[0]), RoomChat.class));
            }
        };

        private Emitter.Listener onEventConnect = new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Log.d(TAG, "SOCKET CONNECTED");
//                t.schedule(new ClassEmitNotifNews(), 0, 5000);
            }
        };

        private Emitter.Listener onEventError = new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Log.d(TAG, "EVENT ERROR\n" + args[0]);
                if (args[0].equals("JsonWebTokenError")) {
                    Log.d(TAG, "JsonWebTokenError");
//                    mSocket.disconnect();
                }else{
//                    mSocket.disconnect();
                }
            }
        };

        private Emitter.Listener onConnectError = new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Log.d(TAG, "CONNECT ERROR\n" + args[0]);
                Intent getNewMessage = new Intent("safetravel.SOCKET_CONNECT_ERROR");
                mSocket.disconnect();
            }
        };

        private Emitter.Listener onEventTransPort = new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Transport transport = (Transport) args[0];

                transport.on(Transport.EVENT_REQUEST_HEADERS, new Emitter.Listener() {
                    @Override
                    public void call(Object... args) {
//                        @SuppressWarnings("unchecked")
                        if(ChatoUtils.getUserLogin(getContext()) != null){
                            Map<String, List<String>> headers = (Map<String, List<String>>) args[0];
                            headers.put("Authorization", Arrays.asList("Bearer " + ChatoUtils.getUserLogin(getContext()).getAccess_token()));
                        }
                    }
                });
            }
        };
    }
}
