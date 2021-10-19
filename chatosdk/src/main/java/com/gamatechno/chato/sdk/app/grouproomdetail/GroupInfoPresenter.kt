package com.gamatechno.chato.sdk.app.grouproomdetail

import android.content.Context
import com.gamatechno.chato.sdk.app.kontakchat.KontakModel
import com.gamatechno.chato.sdk.data.DAO.Group.Group
import com.gamatechno.chato.sdk.data.constant.Api
import com.gamatechno.chato.sdk.module.core.BasePresenter
import com.gamatechno.chato.sdk.module.request.GGFWRest
import com.gamatechno.chato.sdk.module.request.RequestInterface
import com.gamatechno.chato.sdk.utils.ChatoUtils
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class GroupInfoPresenter(context: Context?, view: GroupInfoView.View) : BasePresenter(context), GroupInfoView.Presenter {

    internal var view: GroupInfoView.View? = null

    init {
        this.view = view
    }

    override fun requestInfoGroup(roomid: String, isLoadingShow: Boolean) {
        GGFWRest.GET(Api.get_info_detail_group(roomid), object : RequestInterface.OnGetRequest {
            override fun onPreExecuted() {
                if(isLoadingShow)
                    view!!.onLoading()
            }

            override fun onSuccess(response: JSONObject) {
                if(isLoadingShow)
                    view!!.onHideLoading()
                try {
                    if(response.getBoolean("success")){
                        val result = response.getJSONObject("result")
                        val list = result.getJSONArray("list_user")
                        val group_detail = result.getJSONObject("group_detail")
                        val rooms = ArrayList<KontakModel>()
                        val group = gson.fromJson(group_detail.toString(), Group::class.java!!)
                        group.count_shared = result.getInt("count_shared")
                        group.count_star_message = result.getInt("count_star_message")


                        for (i in 0 until list.length()) {
                            val data = list.getJSONObject(i)
                            val roomChat = gson.fromJson(data.toString(), KontakModel::class.java!!)
                            rooms.add(roomChat)
                        }
                        group.list_user = rooms

                        view!!.onRequestInfoGroup(group, true)
                    } else {
                        view!!.onFailedRequestData(response.getString("message"))
                    }

                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }

            override fun onFailure(error: String) {
                if(isLoadingShow)
                    view!!.onHideLoading()

                view!!.onFailedRequestData(error)
            }

            override fun requestParam(): Map<String, String> {
                return HashMap()
            }

            override fun requestHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Authorization"] = "Bearer " + ChatoUtils.getUserLogin(context).access_token
                if (customer != null) {
                    headers["customer_app_id"] = "" + customer.customer_app_id
                    headers["customer_secret"] = "" + customer.customer_secret
                }
                return headers
            }
        })
    }

    override fun updateAvatarGroup(group: Group) {
        GGFWRest.POST(Api.update_detail_group(), object : RequestInterface.OnPostRequest {
            override fun onPreExecuted() {
                view!!.onLoading()
            }

            override fun onSuccess(response: JSONObject) {
                view!!.onHideLoading()
                try {
                    if(response.getBoolean("success")){
                        val result = response.getJSONObject("result")
                        val group = gson.fromJson(result.toString(), Group::class.java!!)

                        view!!.onRequestInfoGroup(group, false)
                    } else {
                        view!!.onFailedRequestData(response.getString("message"))
                    }

                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }

            override fun onFailure(error: String) {
                view!!.onHideLoading()
                view!!.onFailedRequestData(error)
            }

            override fun requestParam(): Map<String, String> {
                val params = HashMap<String, String>()
                params["room_id"] = ""+group.room_id
                params["room_name"] = group.room_name
                params["room_desc"] = group.group_desc
                params["room_group_type"] = group.room_group_type
                params["room_photo_name"] = group.room_photo_name
                if(!group.room_photo_url.equals("")){
                    params["room_photo_url"] = group.room_photo_url
                }
                return params
            }

            override fun requestHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Authorization"] = "Bearer " + ChatoUtils.getUserLogin(context).access_token
                if (customer != null) {
                    headers["customer_app_id"] = "" + customer.customer_app_id
                    headers["customer_secret"] = "" + customer.customer_secret
                }
                return headers
            }
        })
    }

    override fun updateInfoGroup(group: Group) {
        GGFWRest.POST(Api.update_detail_group(), object : RequestInterface.OnPostRequest {
            override fun onPreExecuted() {
                view!!.onLoading()
            }

            override fun onSuccess(response: JSONObject) {
                view!!.onHideLoading()
                try {
                    if(response.getBoolean("success")){
                        val result = response.getJSONObject("result")
                        val group = gson.fromJson(result.toString(), Group::class.java!!)

                        view!!.onRequestInfoGroup(group, false)
                    } else {
                        view!!.onFailedRequestData(response.getString("message"))
                    }

                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }

            override fun onFailure(error: String) {
                view!!.onHideLoading()
                view!!.onFailedRequestData(error)
            }

            override fun requestParam(): Map<String, String> {
                val params = HashMap<String, String>()
                params["room_id"] = ""+group.room_id
                params["room_name"] = group.room_name
                params["room_desc"] = group.group_desc
                params["room_group_type"] = group.room_group_type
                params["room_photo_name"] = group.room_photo_name
                params["room_photo_url"] = group.room_photo_url
                return params
            }

            override fun requestHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Authorization"] = "Bearer " + ChatoUtils.getUserLogin(context).access_token
                if (customer != null) {
                    headers["customer_app_id"] = "" + customer.customer_app_id
                    headers["customer_secret"] = "" + customer.customer_secret
                }
                return headers
            }
        })
    }

    override fun removeMember(roomid: String, kontakModel: KontakModel) {

    }

    override fun removeFromAdmin(roomid: String, kontakModel: KontakModel) {

    }

    override fun addToAdmin(roomid: String, kontakModel: KontakModel) {

    }

}