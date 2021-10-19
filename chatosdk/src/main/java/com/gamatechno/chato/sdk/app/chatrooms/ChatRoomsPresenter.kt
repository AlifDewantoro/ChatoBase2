package com.gamatechno.chato.sdk.app.chatrooms

import android.content.Context
import com.gamatechno.chato.sdk.app.chatrooms.ChatRoomsView.ObrolanPresenter
import com.gamatechno.chato.sdk.app.chatrooms.uimodel.ChatRoomsUiModel
import com.gamatechno.chato.sdk.data.DAO.RoomChat.RoomChat
import com.gamatechno.chato.sdk.data.constant.Api
import com.gamatechno.chato.sdk.module.core.BasePresenter
import com.gamatechno.chato.sdk.module.request.GGFWRest
import com.gamatechno.chato.sdk.module.request.RequestInterface.OnAuthGetRequest
import com.gamatechno.chato.sdk.module.request.RequestInterface.OnPostRequest
import com.gamatechno.chato.sdk.utils.ChatoUtils
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class ChatRoomsPresenter(context: Context?, var view: ChatRoomsView.View) : BasePresenter(context), ObrolanPresenter {
    var page = 0
    var isSuccess = true
    var isLoading = false
    override fun requestObrolan(isRefresh: Boolean, keyword: String?, categories: String) {
        isLoading = true
        if (isRefresh) page = 0
        page++
        GGFWRest.GETAuth(Api.list_conversation("" + page, categories), context, object : OnAuthGetRequest {
            override fun onPreExecuted() {
                view.onLoading()
            }

            override fun onSuccess(response: JSONObject) {
                view.onHideLoading()
                isLoading = false
                try {
                    isSuccess = response.getBoolean("success")
                    if (response.getBoolean("success")) {
                        val models: MutableList<ChatRoomsUiModel> = ArrayList()
                        val result = response.getJSONObject("result")
                        val list_user = result.getJSONArray("list_user")
                        for (i in 0 until list_user.length()) {
                            models.add(ChatRoomsUiModel(gson.fromJson(list_user[i].toString(), RoomChat::class.java)))
                        }
                        view.onRequestObrolan(models, isRefresh)
                    } else {
                        view.onFailedRequestObrolan()
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(error: String) {
                view.onHideLoading()
                view.onErrorConnection(error)
                isLoading = false
            }

            override fun onUnauthorized(error: String) {
                view.onAuthFailed(error)
            }

            override fun requestParam(): Map<String, String> {
                return HashMap()
            }

            override fun requestHeaders(): Map<String, String> {
                val headers: MutableMap<String, String> = HashMap()
                headers["Authorization"] = "Bearer " + ChatoUtils.getUserLogin(context).access_token
                if (customer != null) {
                    headers["customer_app_id"] = "" + customer.customer_app_id
                    headers["customer_secret"] = "" + customer.customer_secret
                }
                return headers
            }
        })
    }

    override fun pinChatRoom(chatRoomUiModel: ChatRoomsUiModel?, total: Int) {
        GGFWRest.POST(Api.pin_chat_room(), object : OnPostRequest {
            override fun onPreExecuted() {
                view.onLoading()
            }

            override fun onSuccess(response: JSONObject) {
                try {
                    if (response.getBoolean("success")) {
                        view.successPinnedChatRoom(response.getString("message"))
                    } else {
                        view.successPinnedChatRoom(response.getString("message"))
                    }
                } catch (e: JSONException) {
                    view.onHideLoading()
                    e.printStackTrace()
                }
            }

            override fun onFailure(error: String) {
                view.onHideLoading()
                view.onFailedPin(error)
            }

            override fun requestParam(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["room_id"] = "" + chatRoomUiModel!!.roomChat.room_id
                params["index"] = "" + total
                params["status"] = if (chatRoomUiModel!!.roomChat.is_pined == 0) "PIN" else "UNPIN"
                return params
            }

            override fun requestHeaders(): Map<String, String> {
                val headers: MutableMap<String, String> = HashMap()
                headers["Authorization"] = "Bearer " + ChatoUtils.getUserLogin(context).access_token
                if (customer != null) {
                    headers["customer_app_id"] = "" + customer.customer_app_id
                    headers["customer_secret"] = "" + customer.customer_secret
                }
                return headers
            }
        })
    }

    override fun pinMultipleChatRoom(chatRoomUiModels: List<ChatRoomsUiModel>?, total: Int, status: String) {
        GGFWRest.POST(Api.pin_multiple_chat_room(), object : OnPostRequest {
            override fun onPreExecuted() {
                view.onLoading()
            }

            override fun onSuccess(response: JSONObject) {
                try {
                    if (response.getBoolean("success")) {
                        view.successPinnedMultipleChatRoom(response.getString("message"))
                    } else {
                        view.successPinnedMultipleChatRoom(response.getString("message"))
                    }
                } catch (e: JSONException) {
                    view.onHideLoading()
                    e.printStackTrace()
                }
            }

            override fun onFailure(error: String) {
                view.onHideLoading()
                view.onFailedPin(error)
            }

            override fun requestParam(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                val listId: MutableList<Int> = arrayListOf()
                for (item in chatRoomUiModels!!){
                    listId.add(item.roomChat.room_id)
                }
                params["room_id"] = "" + listId.toString()
                //params["index"] = "" + total
                //params["status"] = if (chatRoomUiModel!!.roomChat.is_pined == 0) "PIN" else "UNPIN"
                params["status"] = status
                return params
            }

            override fun requestHeaders(): Map<String, String> {
                val headers: MutableMap<String, String> = HashMap()
                headers["Authorization"] = "Bearer " + ChatoUtils.getUserLogin(context).access_token
                if (customer != null) {
                    headers["customer_app_id"] = "" + customer.customer_app_id
                    headers["customer_secret"] = "" + customer.customer_secret
                }
                return headers
            }
        })
    }

    override fun deleteRoom(chatRoomUiModel: ChatRoomsUiModel?) {
        GGFWRest.POST(Api.delete_room(), object : OnPostRequest {
            override fun onPreExecuted() {
                view.onLoading()
            }

            override fun onSuccess(response: JSONObject) {
                view.onHideLoading()
                try {
                    if (response.getBoolean("success")) {
                        view.onDeleteRoom(true, response.getString("message"))
                    } else {
                        view.onDeleteRoom(false, response.getString("message"))
                    }
                } catch (e: JSONException) {
                    view.onHideLoading()
                    e.printStackTrace()
                }
            }

            override fun onFailure(error: String) {
                view.onHideLoading()
                view.onFailedDelete(error)
            }

            override fun requestParam(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["room_id"] = "" + chatRoomUiModel!!.roomChat.room_id
                return params
            }

            override fun requestHeaders(): Map<String, String> {
                val headers: MutableMap<String, String> = HashMap()
                headers["Authorization"] = "Bearer " + ChatoUtils.getUserLogin(context).access_token
                if (customer != null) {
                    headers["customer_app_id"] = "" + customer.customer_app_id
                    headers["customer_secret"] = "" + customer.customer_secret
                }
                return headers
            }
        })
    }
}