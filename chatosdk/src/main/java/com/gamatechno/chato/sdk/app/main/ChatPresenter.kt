package com.gamatechno.chato.sdk.app.main

import android.content.Context
import com.gamatechno.chato.sdk.app.chatrooms.uimodel.ChatRoomsUiModel
import com.gamatechno.chato.sdk.app.main.searchlist.SearchChatroomModel
import com.gamatechno.chato.sdk.data.DAO.Chat.Chat
import com.gamatechno.chato.sdk.data.DAO.RoomChat.RoomChat
import com.gamatechno.chato.sdk.data.constant.Api
import com.gamatechno.chato.sdk.module.core.BasePresenter
import com.gamatechno.chato.sdk.module.request.GGFWRest
import com.gamatechno.chato.sdk.module.request.RequestInterface.OnAuthGetRequest
import com.gamatechno.chato.sdk.utils.ChatoUtils
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class ChatPresenter(context: Context, val view: ChatView.View?) : BasePresenter(context), ChatView.Presenter {

    override fun searchChat(keyword: String?) {
        GGFWRest.GETAuth(Api.search_Conversation(keyword), getContext(), object : OnAuthGetRequest {
            override fun onPreExecuted() {
                view!!.onLoading()
                /*if(!isRefresh)
                    view.onLoadMore();
                else
                    view.onLoading();*/
            }

            override fun onSuccess(response: JSONObject) {
                view!!.onHideLoading()
                try {
                    if (response.getBoolean("success")) {
                        val list: MutableList<SearchChatroomModel> = ArrayList<SearchChatroomModel>()
                        //                        List<ChatRoomsUiModel> models = new ArrayList<>();
                        val result = response.getJSONObject("result")
                        val list_user = result.getJSONArray("user")
                        val list_chats = result.getJSONArray("message")
                        for (i in 0 until list_user.length()) {
                            val searchChatroomModel = SearchChatroomModel(SearchChatroomModel.chatroom_type, keyword)
                            val chatRoomUiModel = ChatRoomsUiModel(getGson().fromJson(list_user[i].toString(), RoomChat::class.java))
                            chatRoomUiModel.keyword = keyword
                            searchChatroomModel.setChatRoomUiModel(chatRoomUiModel)
                            list.add(searchChatroomModel)
                        }
                        for (i in 0 until list_chats.length()) {
                            val searchChatroomModel = SearchChatroomModel(SearchChatroomModel.message_type, keyword)
                            val chatModel = getGson().fromJson(list_chats[i].toString(), Chat::class.java)
                            //chatRoomUiModel.keyword = keyword
                            searchChatroomModel.setChat(chatModel)
                            list.add(searchChatroomModel)
                        }

                        /*JSONArray list_message = result.getJSONArray("message");
                        if(list_message.length() > 0){
                            list.add(new SearchChatroomModel(SearchChatroomModel.header_type, "Perpesanan"));
                        }
                        for (int i = 0; i < list_message.length(); i++) {
                            SearchChatroomModel searchChatroomModel = new SearchChatroomModel(SearchChatroomModel.message_type, keyword);
                            Chat chat = getGson().fromJson(list_message.get(i).toString(), Chat.class);
                            searchChatroomModel.setChat(chat);
                            list.add(searchChatroomModel);
                        }*/view!!.onSearchChat(list)
                    } else {
                        view!!.onFailedRequestChat()
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(error: String) {
                view!!.onHideLoading()
                view!!.onErrorConnection(error)
            }

            override fun onUnauthorized(error: String) {
                view!!.onAuthFailed(error)
            }

            override fun requestParam(): Map<String, String> {
                return HashMap()
            }

            override fun requestHeaders(): Map<String, String> {
                val headers: MutableMap<String, String> = HashMap()
                headers["Authorization"] = "Bearer " + ChatoUtils.getUserLogin(getContext()).access_token
                if (customer != null) {
                    headers["customer_app_id"] = "" + customer.getCustomer_app_id()
                    headers["customer_secret"] = "" + customer.getCustomer_secret()
                }
                return headers
            }
        })
    }
}