package com.gamatechno.chato.sdk.app.labeldetail

import android.content.Context
import android.util.Log
import com.gamatechno.chato.sdk.app.chatrooms.uimodel.ChatRoomsUiModel
import com.gamatechno.chato.sdk.data.DAO.Label.LabelModel
import com.gamatechno.chato.sdk.data.DAO.RoomChat.RoomChat
import com.gamatechno.chato.sdk.data.constant.Api
import com.gamatechno.chato.sdk.module.core.BasePresenter
import com.gamatechno.chato.sdk.module.request.GGFWRest
import com.gamatechno.chato.sdk.module.request.RequestInterface
import com.gamatechno.chato.sdk.utils.ChatoUtils
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.set

class LabelDetailPresenter(context: Context?, view: LabelDetailView.View) : BasePresenter(context), LabelDetailView.Presenter{

    var view : LabelDetailView.View

    init {
        this.view = view
    }

    override fun requestObrolan(labelModel: LabelModel) {
        GGFWRest.GET(Api.get_detail_label()+"?label_id="+labelModel.label_id,object : RequestInterface.OnGetRequest{
            override fun onPreExecuted() {

            }

            override fun onSuccess(response: JSONObject?) {
                Log.d("MediaShared",response.toString())
                if(response!!.getBoolean("success")){
                    val result = response.getJSONObject("result")
                    val list_room = result.getJSONArray("room")
                    val listRoom = ArrayList<ChatRoomsUiModel>()
                    Log.d("MediaShared","lenght: " + list_room.length())
                    for(i in 0 until list_room.length()){
                        listRoom.add(ChatRoomsUiModel(gson.fromJson<RoomChat>(list_room.get(i).toString(), RoomChat::class.java)))
                    }

                    if(listRoom.size > 0)
                        view.onRequestRoom(listRoom)
                    else
                        view.onFailedRequestObrolan()
                } else {
                    view.onFailedRequestObrolan()
                }
            }

            override fun onFailure(error: String?) {
                view.onErrorConnection(error)
            }

            override fun requestParam(): Map<String, String> {
                return HashMap()
            }

            override fun requestHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Authorization"] = "Bearer " + ChatoUtils.getUserLogin(context).access_token
//                Log.d("MediaShared",headers.get("Authorization"))
                if (customer != null) {
                    headers["customer_app_id"] = "" + customer.customer_app_id
                    headers["customer_secret"] = "" + customer.customer_secret
                }
                return headers
            }

        })
    }

    override fun pinChatRoom(chatRoomUiModel: ChatRoomsUiModel?, total: Int) {

    }

    override fun deleteRoom(chatRoomUiModel: ChatRoomsUiModel?) {

    }

}