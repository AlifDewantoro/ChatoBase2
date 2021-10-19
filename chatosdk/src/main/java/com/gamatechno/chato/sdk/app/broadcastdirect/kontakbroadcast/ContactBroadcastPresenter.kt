package com.gamatechno.chato.sdk.app.broadcastdirect.kontakbroadcast

import android.content.Context
import android.util.Log
import com.gamatechno.chato.sdk.app.kontakchat.KontakModel
import com.gamatechno.chato.sdk.data.constant.Api
import com.gamatechno.chato.sdk.module.core.BasePresenter
import com.gamatechno.chato.sdk.module.request.GGFWRest
import com.gamatechno.chato.sdk.module.request.RequestInterface
import com.gamatechno.chato.sdk.utils.ChatoUtils
import org.json.JSONException
import org.json.JSONObject
import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.set

class ContactBroadcastPresenter(context: Context, view: ContactBroadcastView.View) :
        BasePresenter(context), ContactBroadcastView.Presenter {

    var view : ContactBroadcastView.View
    internal var page: Int = 0
    init {
        this.view = view
    }

    override fun getContact(isRefresh: Boolean) {
        if (isRefresh)
            page = 0
        page++
        GGFWRest.GETAuth(Api.list_kontak("" + page),context, object:RequestInterface.OnAuthGetRequest{
            override fun onPreExecuted() {

            }

            override fun onSuccess(response: JSONObject?) {
                try {
                    if (response!!.getBoolean("success")) {
                        val models = ArrayList<KontakModel>()
                        val groupmodels = ArrayList<KontakModel>()
                        val result = response.getJSONObject("result")
                        val list_user = result.getJSONArray("list_user")
                        val list_group = result.getJSONArray("list_group")
                        for (i in 0 until list_user.length()) {
                            models.add(gson.fromJson(list_user.get(i).toString(), KontakModel::class.java))
                        }
                        for (i in 0 until list_group.length()) {
                            groupmodels.add(gson.fromJson(list_group.get(i).toString(), KontakModel::class.java))
                        }

                        view.onListContact(models, groupmodels, isRefresh)
                    } else {
                        view.onFailedListContact(isRefresh)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }

            override fun onFailure(error: String?) {

            }

            override fun onUnauthorized(error: String?) {

            }

            override fun requestParam(): Map<String, String> {
                return HashMap()
            }

            override fun requestHeaders(): Map<String, String> {
                val headers = java.util.HashMap<String, String>()
                headers["Authorization"] = "Bearer " + ChatoUtils.getUserLogin(context).access_token
                if (customer != null) {
                    headers["customer_app_id"] = "" + customer.customer_app_id
                    headers["customer_secret"] = "" + customer.customer_secret
                }
                return headers
            }

        })
    }

    override fun getSearchContact(keyword: String, isRefresh: Boolean) {
        if (isRefresh)
            page = 0
        page++

        GGFWRest.GET(Api.search_user(keyword, "" + page), object : RequestInterface.OnGetRequest {
            override fun onPreExecuted() {

            }

            override fun onSuccess(response: JSONObject) {
                try {
                    if (response.getBoolean("success")) {
                        val models = ArrayList<KontakModel>()
                        val result = response.getJSONObject("result")
                        val list_user = result.getJSONArray("list_user")
                        for (i in 0 until list_user.length()) {
                            models.add(gson.fromJson(list_user.get(i).toString(), KontakModel::class.java))
                        }
                        Log.d("ContactBroadcast","size:" + models.size)
                        view.onListContact(models, ArrayList(), isRefresh)
                    } else {
                        view.onFailedListContact(isRefresh)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }

            override fun onFailure(error: String) {
                view.onHideLoading()
                view.onErrorConnection(error)
            }

            override fun requestParam(): Map<String, String>? {
                return null
            }

            override fun requestHeaders(): Map<String, String> {
                val headers = java.util.HashMap<String, String>()
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