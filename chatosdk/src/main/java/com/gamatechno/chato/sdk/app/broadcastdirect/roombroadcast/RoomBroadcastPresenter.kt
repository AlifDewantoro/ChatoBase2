package com.gamatechno.chato.sdk.app.broadcastdirect.roombroadcast

import android.content.Context
import android.net.Uri
import android.util.Log
import com.gamatechno.chato.sdk.data.DAO.Chat.Chat
import com.gamatechno.chato.sdk.data.constant.Api
import com.gamatechno.chato.sdk.data.constant.StringConstant
import com.gamatechno.chato.sdk.module.core.BasePresenter
import com.gamatechno.chato.sdk.module.request.GGFWRest
import com.gamatechno.chato.sdk.module.request.RequestInterface.OnPostRequest
import com.gamatechno.chato.sdk.utils.FilePath.FilePath
import com.gamatechno.chato.sdk.utils.FilePath.IOUtils
import com.gamatechno.chato.sdk.utils.ChatoUtils
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class RoomBroadcastPresenter(context: Context, view: RoomBroadcastView.View) : BasePresenter(context), RoomBroadcastView.Presenter {
    var view: RoomBroadcastView.View? = null
    var TAG = RoomBroadcastActivity::class.java.name

    init {
        this.view = view
    }

    override fun sendMessage(chat: Chat?) {
        GGFWRest.POST(Api.broadcast_message(), object : OnPostRequest {
            override fun onPreExecuted() {}
            override fun onSuccess(response: JSONObject) {
                try {
                    if (response.getBoolean("success")) {
                        val resultchat = gson.fromJson(response.getJSONObject("result").toString(), Chat::class.java)
                        if (chat!!.message_type == Chat.chat_type_image) {
                            resultchat.bitmap_image = chat!!.bitmap_image
                            view!!.onSendMessage(resultchat)
                        } else if (chat!!.message_type != Chat.chat_type_message) {
                            Log.d(TAG, "asd onSuccess: " + resultchat.message_attachment_name)
                            if (chat!!.message_type == Chat.chat_type_video) {
                                IOUtils.copyFile(chat!!.message_attachment_name, chat!!.message_type, resultchat.message_attachment_name)
                            } else {
                                IOUtils.savefile(Uri.parse(chat!!.uri_attachment), chat!!.message_type, resultchat.message_attachment_name, context)
                            }
                            view!!.onSendMessage(resultchat)
                        } else {
                            view!!.onSendMessage(resultchat)
                        }
                    } else {
                        chat!!.message_status = StringConstant.chat_status_failed
                        view!!.onFailedSendMessage(chat, response!!.getString("message"))
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(error: String) {
                chat!!.message_status = StringConstant.chat_status_failed
                view!!.onFailedSendMessage(chat!!, "")
            }

            override fun requestParam(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                chat!!.setMessage()
                chat!!.setMessage_file()

                if (chat!!.message_type != Chat.chat_type_message) {
                    var file = ""
                    file = if (chat!!.message_is_forward == 1) {
                        chat!!.message_attachment_name
                    } else {
                        FilePath.getFileName(context, chat!!.fileModel.uri)
                    }
                    chat.message_file_name = file
                    try {
                        chat.message_file_type = "." + file.split("\\.").toTypedArray()[file.split("\\.").toTypedArray().size - 1]
                    } catch (e: IndexOutOfBoundsException) {
                        e.printStackTrace()
                    }
                }
                if (chat!!.message_type == Chat.chat_type_video) {
                    chat.message_file_thumbnail = "" + chat!!.thumb_video
                }
                params["gtfwRequest"] = gson.toJson(chat!!)
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

    override fun checkSelectedChat(chatList: MutableList<Chat>) {

    }
}