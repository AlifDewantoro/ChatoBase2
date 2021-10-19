package com.gamatechno.chato.sdk.app.chatroom;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import com.gamatechno.chato.sdk.R;
import com.gamatechno.chato.sdk.app.chatroom.model.ChatReactionResponse;
import com.gamatechno.chato.sdk.app.chatroom.model.ChatRoomUiModel;
import com.gamatechno.chato.sdk.app.kontakchat.KontakModel;
import com.gamatechno.chato.sdk.data.DAO.Chat.Chat;
import com.gamatechno.chato.sdk.data.DAO.Group.Group;
import com.gamatechno.chato.sdk.data.constant.Api;
import com.gamatechno.chato.sdk.data.constant.StringConstant;
import com.gamatechno.chato.sdk.data.model.UserModel;
import com.gamatechno.chato.sdk.module.core.BasePresenter;
import com.gamatechno.chato.sdk.module.request.GGFWRest;
import com.gamatechno.chato.sdk.module.request.RequestInterface;
import com.gamatechno.chato.sdk.utils.ChatoUtils;
import com.gamatechno.chato.sdk.utils.FilePath.FilePath;
import com.gamatechno.chato.sdk.utils.FilePath.IOUtils;
import com.gamatechno.chato.sdk.utils.downloader.Exception.GTDownloadException;
import com.gamatechno.chato.sdk.utils.downloader.GTDownloadCallback;
import com.gamatechno.chato.sdk.utils.downloader.GTDownloadManager;
import com.gamatechno.chato.sdk.utils.downloader.GTDownloadRequest;
import com.gamatechno.ggfw.utils.GGFWUtil;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatRoomPresenter extends BasePresenter implements ChatRoomView.Presenter {

    ChatRoomView.View view;
    Gson gson;
    int page = 1;



    String TAG = "ChatRoomPresenter";
    final ClipboardManager clipboard;

    public ChatRoomPresenter(Context context, ChatRoomView.View view) {
        super(context);
        this.view = view;
        gson = new Gson();
        clipboard = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
    }

    @Override
    public void requestHistoryChat(String userid, String from_chatid, String room_id, boolean isRefresh) {
        String api = "";
        if(isRefresh)
            api = Api.get_history_chat("" + userid, "" + page, (room_id.equals("0") ? "" : room_id));
        else
            api = Api.get_history_chat("" + userid, from_chatid, "" + page, (room_id.equals("0") ? "" : room_id), (room_id.equals("0") ? "" : room_id));

        GGFWRest.GET(api, new RequestInterface.OnGetRequest() {
            @Override
            public void onPreExecuted() {
                view.onLoadingChat();
            }

            @Override
            public void onSuccess(JSONObject response) {
                view.onHideLoadingChat();
                List<Chat> chatList = new ArrayList<>();
                try {
                    if(response.getBoolean("success")){
                        JSONObject result = response.getJSONObject("result");
                        JSONArray list = result.getJSONArray("list_user");
                        if(isRefresh){

                            UserModel userModel = getGson().fromJson(result.getJSONObject("detail_user").toString(), UserModel.class);

//                            view.onCheckStatusRoom(userModel.getIs_online() == 1 ? "Sedang Online" : "");
                            if(userModel.getIs_online() == 1)
                                view.onCheckStatusRoom("Online");
                            else if(userModel.getLast_seen()!=null) {
                                Log.d(TAG, "onSuccess: " + userModel.getLast_seen());
                                view.onCheckStatusRoom(userModel.getLast_seen());
                            } else {
                                view.onCheckStatusRoom("");
                            }


                            for (int i = list.length()-1; i >= 0; i--) {
                                JSONObject data = list.getJSONObject(i);
                                Chat chat1 = gson.fromJson(data.toString(), Chat.class);
                                chatList.add(chat1);
                            }
                        } else {
                            for (int i = 0; i < list.length(); i++) {
                                JSONObject data = list.getJSONObject(i);
                                Chat chat1 = gson.fromJson(data.toString(), Chat.class);
                                chatList.add(chat1);
                            }
                        }
                        view.onRequestHistoryChat(chatList, isRefresh);
                    } else {
                        if(isRefresh){
                            view.onFailedRequestHistoryChat("Belum ada obrolan");
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String error) {
                view.onHideLoadingChat();
                view.onErrorConnection("");
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
    public void requestHistoryGruopChat(String id, String from_idchat, boolean isRefresh) {
        String api = "";
        if(isRefresh)
            api = Api.get_history_groupchat("" + id, "" + page);
        else
            api = Api.get_history_groupchat("" + id, from_idchat, "" + page);

        GGFWRest.GET(api, new RequestInterface.OnGetRequest() {
            @Override
            public void onPreExecuted() {

            }

            @Override
            public void onSuccess(JSONObject response) {
                List<Chat> chatList = new ArrayList<>();
                try {
                    if(response.getBoolean("success")){
//                        JSONObject result = response.getJSONObject("result");
                        JSONArray list = response.getJSONArray("result");
                        if(isRefresh){
                            for (int i = list.length()-1; i >= 0; i--) {
                                JSONObject data = list.getJSONObject(i);
                                Chat chat1 = gson.fromJson(data.toString(), Chat.class);
                                chatList.add(chat1);
                            }
                        } else {
                            for (int i = 0; i < list.length(); i++) {
                                JSONObject data = list.getJSONObject(i);
                                Chat chat1 = gson.fromJson(data.toString(), Chat.class);
                                chatList.add(chat1);
                            }
                        }
                        view.onRequestHistoryChat(chatList, isRefresh);
                    } else {
//                        view.onFailedRequestHistoryChat(response.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String error) {
                view.onErrorConnection("");
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
    public void sendMessage(Chat chat) {
        GGFWRest.POST(Api.send_message(), new RequestInterface.OnPostRequest() {
            @Override
            public void onPreExecuted() {

            }

            @Override
            public void onSuccess(JSONObject response) {
                try {
                    if(response.getBoolean("success")){
                        Chat resultchat = gson.fromJson(response.getJSONObject("result").toString(), Chat.class);

                        if(chat.getMessage_type().equals(Chat.chat_type_image)){
                            resultchat.setBitmap_image(chat.getBitmap_image());
                            view.onSendMessage(resultchat);
                        } else if(!chat.getMessage_type().equals(Chat.chat_type_message)){
                            Log.d(TAG, "asd onSuccess: "+resultchat.getMessage_attachment_name());

                            if(chat.getMessage_type().equals(Chat.chat_type_video)){
                                IOUtils.copyFile(chat.getMessage_attachment_name(), chat.getMessage_type(), resultchat.getMessage_attachment_name());
                            } else {
                                IOUtils.savefile(Uri.parse(chat.getUri_attachment()), chat.getMessage_type(), resultchat.getMessage_attachment_name(), getContext());
                            }
                            view.onSendMessage(resultchat);
                        } else {
                            view.onSendMessage(resultchat);
                        }
                    } else {
                        chat.setMessage_status(StringConstant.chat_status_failed);
                        view.onFailedSendMessage(chat, response.getString("message"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String error) {
                chat.setMessage_status(StringConstant.chat_status_failed);
                view.onFailedSendMessage(chat);
            }

            @Override
            public Map<String, String> requestParam() {
                Map<String, String> params = new HashMap<>();
                params.put("to_user_id", ""+chat.getTo_user_id());
                params.put("message", ""+chat.getMessage_text());
                params.put("room_id", chat.getRoom_id() == "0" ? "" : chat.getRoom_id() );
                params.put("room_code", chat.getRoom_code().equals("") ? "" : chat.getRoom_code() );
                params.put("message_type", ""+chat.getMessage_type());
                params.put("message_file", ""+chat.getMessage_attachment());
                params.put("payload", ""+chat.getPayload());

                if(chat.getMessage_is_forward() == 1) params.put("message_is_forward", "1");
                if(chat.getMessage_is_replay() == 1){
                    params.put("message_is_replay", "1");
                    params.put("message_id", ""+chat.getMessage_id());
                }
                if(!chat.getMessage_type().equals(Chat.chat_type_message)){
                    String file = "";
                    if(chat.getMessage_is_forward() == 1){
                        file = chat.getMessage_attachment_name();
                    } else {
                        file = FilePath.getFileName(getContext(), chat.getFileModel().getUri());
                    }
                    params.put("message_file_name", file);
                    try {
                        params.put("message_file_type", "."+file.split("\\.")[file.split("\\.").length-1]);
                    } catch (IndexOutOfBoundsException e){
                        e.printStackTrace();
                    }
                }
                if(chat.getMessage_type().equals(Chat.chat_type_video)){
                    params.put("message_file_thumbnail", ""+chat.getThumb_video());
                    params.put("message_file_duration", ""+chat.getDuration());
                }
                if (chat.getMessage_type().equals(Chat.chat_type_audio)){
                    String file = chat.getFileModel().getNamefile();

                    params.put("message_file_duration", ""+chat.getMessage_file_duration());
                    try {
                        params.put("message_file_type", "."+file.split("\\.")[file.split("\\.").length-1]);
                    } catch (IndexOutOfBoundsException e){
                        e.printStackTrace();
                    }
                }

                params.put("mention_user", chat.getMentionId().toString());
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
    public void sendGroupMessage(Chat chat) {
        GGFWRest.POST(Api.send_group_message(), new RequestInterface.OnPostRequest() {
            @Override
            public void onPreExecuted() {

            }

            @Override
            public void onSuccess(JSONObject response) {
                try {
                    if(response.getBoolean("success")){
                        Chat chat1 = gson.fromJson(response.getJSONObject("result").toString(), Chat.class);

                        if(chat.getMessage_type().equals(Chat.chat_type_image)){
                            chat1.setBitmap_image(chat.getBitmap_image());
                        }

                        view.onSendMessage(chat1);
                    } else {
                        chat.setMessage_status(StringConstant.chat_status_failed);
                        view.onFailedSendMessage(chat, response.getString("message"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String error) {
                chat.setMessage_status(StringConstant.chat_status_failed);
                view.onFailedSendMessage(chat);
            }

            @Override
            public Map<String, String> requestParam() {
                Map<String, String> params = new HashMap<>();
                params.put("group_id", ""+chat.getTo_user_id());
                params.put("message", ""+chat.getMessage_text());
                params.put("message_type", ""+chat.getMessage_type());
                params.put("message_file", ""+chat.getMessage_attachment());
                params.put("payload", ""+chat.getPayload());
                if(chat.getMessage_type().equals(Chat.chat_type_file)){
                    String file = FilePath.getFileName(getContext(), chat.getFileModel().getUri());
                    params.put("message_file_name", file);
                    try {
                        params.put("message_file_type", "."+file.split("\\.")[file.split("\\.").length-1]);
                    } catch (IndexOutOfBoundsException e){
                        e.printStackTrace();
                    }
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

    @Override
    public void sendStatusMessage(List<Chat> chatList) {
        if(chatList.size()>0){
            GGFWRest.POST(Api.update_status_message(), new RequestInterface.OnPostRequest() {
                @Override
                public void onPreExecuted() {

                }

                @Override
                public void onSuccess(JSONObject response) {
                    view.onSendStatusMessage();
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
                    params.put("message_status", StringConstant.chat_status_read);
                    params.put("room_id", ""+chatList.get(0).getRoom_id());
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
    public void checkSelectedChat(List<Chat> chatList) {
        int total = ChatroomHelper.totalSelected(chatList);
        if(total>0){
            switch (total){
                case 1:
                    view.onAppBarAction(StringConstant.status_messageselected_one);
                    break;
                default:
                    view.onAppBarAction(StringConstant.status_messageselected_multiple);
                    break;
            }
        } else {
            view.onAppBarAction(StringConstant.status_messageselected_none);
        }
    }

    @Override
    public void copyChat(List<Chat> chatList) {
        ClipData clip = null;
        String val = "";
        for (Chat chat: chatList){
            val = val+chat.getMessage_text()+"\n";
        }
        Log.d(TAG, "copyChat: "+chatList.size());
        Log.d(TAG, "copyChat: "+val);
        clip = ClipData.newPlainText("chatoxpakaryo", val);
        clipboard.setPrimaryClip(clip);
        GGFWUtil.ToastShort(getContext(), getContext().getResources().getString(R.string.label_copiedchat));
    }

    @Override
    public void reactionChat(Chat chat, String chatroom_id, String emojiId) {
        GGFWRest.POST(Api.reaction_message(), new RequestInterface.OnPostRequest() {
            @Override
            public void onPreExecuted() {

            }

            @Override
            public void onSuccess(JSONObject response) {

                try {
                    if (response.getBoolean("success")) {
                        JSONObject result = response.getJSONObject("result");
                        ChatReactionResponse responseModel = gson.fromJson(result.toString(), ChatReactionResponse.class);
                        Log.d("respon emoji reaction", response.getString("message")
                                + ", emoticon = " + responseModel.getReaction_data());
                        view.onSuccessReaction(responseModel);
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
                params.put("room_id", chatroom_id);
                params.put("message_id", ""+chat.getMessage_id());
                params.put("reaction", emojiId);
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
    public void starChat(Chat chat, String chatroom_id) {
        GGFWRest.POST(Api.star_message(), new RequestInterface.OnPostRequest() {
            @Override
            public void onPreExecuted() {
                view.onStarChat(chat, chat.getMessage_star() == 0 ? true : false);
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
                params.put("room_id", chatroom_id);
                params.put("message_id", ""+chat.getMessage_id());
                params.put("status", chat.getMessage_star() == 1 ? "STAR" : "UNSTAR");
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
    public void downloadFileFromChatToForward(Chat chat, KontakModel kontakModel) {
        String pathsegment = "";

        switch (chat.getMessage_type()){
            case Chat.chat_type_video:
                pathsegment = "SmartOffice/Video/";
                break;
            case Chat.chat_type_file:
                pathsegment = "SmartOffice/Document/";
                break;
            case Chat.chat_type_image:
                pathsegment = "SmartOffice/Images/";
                break;
            default:
                pathsegment = "SmartOffice/";
                break;
        }
        Uri destination = Uri.withAppendedPath(Uri.fromFile(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)), pathsegment);
        Uri file_uri = Uri.withAppendedPath(Uri.fromFile(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)), pathsegment+chat.getMessage_attachment_name());

        Log.d("asdFile "+chat.getMessage_type(), " -- "+chat.getMessage_attachment_name());
        Log.d("asdFile "+chat.getMessage_type(), ""+file_uri);
        if(IOUtils.isFileExist(file_uri.toString().replace("file:/", ""))){
            chat.setUri_attachment(file_uri.toString());
            view.onSuccessDownloadFileToForward(chat, kontakModel);
        } else {
            try {
                new GTDownloadManager(getContext(), new GTDownloadCallback(){
                    @Override
                    public void onProcess(GTDownloadRequest request) {
                        view.onLoading();
                    }

                    @Override
                    public void onCancel(GTDownloadRequest request) {

                    }

                    @Override
                    public void onSuccess(GTDownloadRequest request) {
                        view.onHideLoading();
                        chat.setUri_attachment(request.getDestinationUri().toString());
                        view.onSuccessDownloadFileToForward(chat, kontakModel);
                    }
                }).startRequest(new GTDownloadRequest(Uri.parse(chat.getMessage_attachment()), chat.getMessage_attachment_name()).setDestinationUri(destination));
            } catch (GTDownloadException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void requestDeleteMessage(List<Chat> chats, String own_message) {
        String api_delete = Api.delete_message();
        if(own_message.equals("1")){
            api_delete = Api.force_delete_message();
        }
        GGFWRest.POST(api_delete, new RequestInterface.OnPostRequest() {
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
                for (int i = 0; i < chats.size(); i++) {
                    params.put("message_id["+i+"]", ""+chats.get(i).getMessage_id());
                }
                params.put("own_message", own_message);
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
    public void searchChat(String keyword, String room_id) {
        GGFWRest.GET(Api.search_message((room_id.equals("0") ? "" : room_id), keyword), new RequestInterface.OnGetRequest() {
            @Override
            public void onPreExecuted() {

            }

            @Override
            public void onSuccess(JSONObject response) {
                List<Chat> chatList = new ArrayList<>();
                try {
                    if(response.getBoolean("success")){
                        JSONArray list = response.getJSONArray("result");
                        for (int i = list.length()-1; i >= 0; i--) {
                            JSONObject data = list.getJSONObject(i);
                            Chat chat1 = gson.fromJson(data.toString(), Chat.class);
                            chatList.add(chat1);
                        }
                        view.onSearchChat(chatList);
                    } else {
                        view.onErrorConnection("Kata kunci tidak ditemukan");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String error) {
                view.onErrorConnection("");
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
    public void getMessageInfo(Chat chat) {
        GGFWRest.GET(Api.message_info(""+chat.getMessage_id()), new RequestInterface.OnGetRequest() {
            @Override
            public void onPreExecuted() {
                view.onLoading();
            }

            @Override
            public void onSuccess(JSONObject response) {
                view.onHideLoading();
                try {
                    if(response.getBoolean("success")){
                        JSONObject result = response.getJSONObject("result");
                        view.onGetMessageInfo(chat, result.getString("message_delivered_date"), result.getString("message_read_date"));
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
            }

            @Override
            public Map<String, String> requestParam() {
                Map<String, String> params = new HashMap<>();
                params.put("message_id", ""+chat.getMessage_id());
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
    public void getGroupMessageInfo(Chat chat, String roomId) {
        GGFWRest.GET(Api.message_group_info(""+chat.getMessage_id(), roomId), new RequestInterface.OnGetRequest() {
            @Override
            public void onPreExecuted() {
                view.onLoading();
            }

            @Override
            public void onSuccess(JSONObject response) {
                view.onHideLoading();
                List<KontakModel> data_terkirim = new ArrayList<>();
                List<KontakModel> data_terbaca = new ArrayList<>();

                try {
                    if(response.getBoolean("success")){
                        JSONObject result = response.getJSONObject("result");
                        JSONArray list_terkirim = result.getJSONArray("delivered");
                        JSONArray list_terbaca = result.getJSONArray("read");
                        for (int i = list_terkirim.length()-1; i >= 0; i--) {
                            JSONObject data = list_terkirim.getJSONObject(i);
                            KontakModel kontakModel = gson.fromJson(data.toString(), KontakModel.class);
                            data_terkirim.add(kontakModel);
                        }

                        for (int i = list_terbaca.length()-1; i >= 0; i--) {
                            JSONObject data = list_terbaca.getJSONObject(i);
                            KontakModel kontakModel = gson.fromJson(data.toString(), KontakModel.class);
                            data_terbaca.add(kontakModel);
                        }
                        view.onGetGroupMessageInfo(chat, data_terkirim, data_terbaca);
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
            }

            @Override
            public Map<String, String> requestParam() {
                Map<String, String> params = new HashMap<>();
                params.put("message_id", ""+chat.getMessage_id());
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
    public void getUpdatedGroupInfo(ChatRoomUiModel chatRoomUiModel) {
        GGFWRest.GET(Api.get_update_room_info(""+chatRoomUiModel.getRoom_code()), new RequestInterface.OnGetRequest() {
            @Override
            public void onPreExecuted() {

            }

            @Override
            public void onSuccess(JSONObject response) {
                try {
                    if(response.getBoolean("success")){
                        JSONObject result = response.getJSONObject("result");
                        Group group = gson.fromJson(result.toString(), Group.class);
                        view.onUpdateGroupInfo(group);
                    } else {
                        view.onErrorConnection(response.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String error) {
                view.onErrorConnection("Bermasalah dengan server");
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
                if(customer!=null){
                    headers.put("customer_app_id", ""+customer.getCustomer_app_id());
                    headers.put("customer_secret", ""+customer.getCustomer_secret());
                }
                return headers;
            }
        });
    }

    @Override
    public void pinMessageGroup(Chat chat, Chat groupchat, String room_id) {
        int ispinned = 1;
        if(chat.getMessage_id() == groupchat.getMessage_id()){
            ispinned = 0;
        }

        int finalIspinned = ispinned;
        GGFWRest.POST(Api.pin_message(), new RequestInterface.OnPostRequest() {
            @Override
            public void onPreExecuted() {
                view.onLoading();
            }

            @Override
            public void onSuccess(JSONObject response) {
                view.onHideLoading();
                try {
                    if(response.getBoolean("success")){
                        view.onPinMessageGroup(finalIspinned, chat);
                        view.onErrorConnection(response.getString("message"));
                    } else {
                        chat.setMessage_status(StringConstant.chat_status_failed);
                        view.onErrorConnection(response.getString("message"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String error) {
                view.onHideLoading();
                chat.setMessage_status(StringConstant.chat_status_failed);
                view.onFailedSendMessage(chat);
            }

            @Override
            public Map<String, String> requestParam() {
                Map<String, String> params = new HashMap<>();
                params.put("message_id", ""+chat.getMessage_id());
                params.put("room_id", chat.getRoom_id() == null ? room_id : ""+chat.getRoom_id() );
                params.put("status", ""+(finalIspinned == 0 ? "UNPIN" : "PIN"));
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
