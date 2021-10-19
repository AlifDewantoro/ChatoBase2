package com.gamatechno.chato.sdk.app.sharedmedia.fragment.sharedimage

import android.content.Context
import android.util.Log
import com.gamatechno.chato.sdk.data.DAO.Chat.Chat
import com.gamatechno.chato.sdk.module.core.BasePresenter
import com.gamatechno.chato.sdk.module.request.GGFWRest
import com.gamatechno.chato.sdk.module.request.RequestInterface
import com.gamatechno.chato.sdk.utils.ChatoUtils
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.Map
import kotlin.collections.set

class SharedMediaPresenter(context: Context?, val view: SharedMediaView.View) : BasePresenter(context), SharedMediaView.Presenter {
//    lateinit var list: ArrayList<String>
    override fun getList(api:String) {
        Log.d("MediaShared",api)
        GGFWRest.GET(api,object : RequestInterface.OnGetRequest{
            override fun onPreExecuted() {

            }

            override fun onSuccess(response: JSONObject?) {
                Log.d("MediaShared",response.toString())
                if(response!!.getString("message") == "success"){
                    val array = response.getJSONArray("result")
                    val listChat = ArrayList<Chat>()
                    Log.d("MediaShared","lenght: " + array.length())
                    for(i in 0 until array.length()){
                        Log.d("MediaShared","content: " + array.getJSONObject(i).toString())
                        val message = gson.fromJson(array.getJSONObject(i).toString(), Chat::class.java)
                        listChat.add(message)
                    }
                    if(listChat.size>0)
                        view.setListView(listChat)
                    else
                        view.setEmptyView()
                } else {
                    view.setEmptyView()
                }
            }

            override fun onFailure(error: String?) {
                view.setEmptyView()
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

}