package com.gamatechno.chato.sdk.app.grouproomdetail.fragment.info

import android.content.Context
import android.content.Intent
import com.gamatechno.chato.sdk.app.kontakchat.KontakModel
import com.gamatechno.chato.sdk.data.DAO.Group.Group
import com.gamatechno.chato.sdk.data.constant.Api
import com.gamatechno.chato.sdk.data.constant.StringConstant
import com.gamatechno.chato.sdk.module.core.BasePresenter
import com.gamatechno.chato.sdk.module.request.GGFWRest
import com.gamatechno.chato.sdk.module.request.RequestInterface
import com.gamatechno.chato.sdk.utils.ChatoUtils
import com.gamatechno.ggfw.utils.GGFWUtil
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class GroupInfoFragmentPresenter(context: Context?, view: GroupInfoFragmentView.View) : BasePresenter(context), GroupInfoFragmentView.Presenter {


    internal var view: GroupInfoFragmentView.View? = null

    init {
        this.view = view
    }

    override fun exitGroup(group: Group) {
        GGFWRest.POST(Api.exit_room_group(), object : RequestInterface.OnPostRequest {
            override fun onPreExecuted() {
                view!!.onLoading()
            }

            override fun onSuccess(response: JSONObject) {
                view!!.onHideLoading()
                try {
                    GGFWUtil.ToastShort(context, response.getString("message"))
                    if (response.getBoolean("success")) {
                        view!!.onExitGroup("")
                        context.sendBroadcast(Intent(StringConstant.service_requestgroup_tofirebase_register))
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(error: String) {
                view!!.onHideLoading()
            }

            override fun requestParam(): Map<String, String> {
                val params = HashMap<String, String>()
                params["room_id"] = ""+group.room_id
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

    override fun createRoomId(model: KontakModel) {
        GGFWRest.POST(Api.createRoom(), object : RequestInterface.OnPostRequest {

            override fun onPreExecuted() {
                view!!.onLoading()
            }

            override fun onSuccess(response: JSONObject) {
                try {
                    if (response.getBoolean("success")) {
                        val result = response.getJSONObject("result")
                        model.room_id = if (result.has("room_id")) result.getInt("room_id") else 0
                        view!!.onHideLoading()
                        view!!.onCreateRoomId(model)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }

            override fun onFailure(error: String) {
                view!!.onHideLoading()
                view!!.onErrorConnection(error)
            }

            override fun requestParam(): Map<String, String>? {
                val headers = HashMap<String, String>()
                headers["to_user_id"] = "" + model.user_id
                return headers
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

    override fun addMemberToGroup(idroom: String, list: MutableList<KontakModel>) {
        GGFWRest.POST(Api.add_member_group(), object : RequestInterface.OnPostRequest {
            override fun onPreExecuted() {
                view!!.onLoading()
            }

            override fun onSuccess(response: JSONObject) {
                view!!.onHideLoading()
                try {
//                    GGFWUtil.ToastShort(context, response.getString("message"))
                    if (response.getBoolean("success")) {
                        view!!.onAddMemberToGroup(list)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }

            override fun onFailure(error: String) {
                view!!.onHideLoading()
            }

            override fun requestParam(): Map<String, String> {
                val params = HashMap<String, String>()
                params["room_id"] = idroom
                for (i in list.indices) {
                    params["user_id[$i]"] = "" + list.get(i).user_id
                }
                params["is_admin"] = "0"
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

    override fun removeFromGroup(idroom: String, kontakModel: KontakModel) {
        GGFWRest.POST(Api.remove_from_group(), object : RequestInterface.OnPostRequest {
            override fun onPreExecuted() {
                view!!.onLoading()
            }

            override fun onSuccess(response: JSONObject) {
                view!!.onHideLoading()
                try {
                    GGFWUtil.ToastShort(context, response.getString("message"))
                    if (response.getBoolean("success")) {
                        view!!.onRemoveMember(kontakModel)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(error: String) {
                view!!.onHideLoading()
            }

            override fun requestParam(): Map<String, String> {
                val params = HashMap<String, String>()
                params["room_id"] = ""+idroom
                params["user_id"] = ""+kontakModel.user_id
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

    override fun updateAdminRole(idroom: String, kontakModel: KontakModel, admin_role: Int) {
        GGFWRest.POST(Api.update_admingroup_role(), object : RequestInterface.OnPostRequest {
            override fun onPreExecuted() {
                view!!.onLoading()
            }

            override fun onSuccess(response: JSONObject) {
                view!!.onHideLoading()
                try {
                    GGFWUtil.ToastShort(context, response.getString("message"))
                    val result = response.getJSONObject("result")
                    if (response.getBoolean("success")) {
                        view!!.onUpdateAdminRole(kontakModel, result.getInt("is_admin"))
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }

            override fun onFailure(error: String) {
                view!!.onHideLoading()
            }

            override fun requestParam(): Map<String, String> {
                val params = HashMap<String, String>()
                params["is_admin"] = ""+admin_role
                params["room_id"] = ""+idroom
                params["user_id"] = ""+kontakModel.user_id
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
}