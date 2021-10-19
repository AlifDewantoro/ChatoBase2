package com.gamatechno.chato.sdk.app.filesharing

import android.content.Context
import com.gamatechno.chato.sdk.data.DAO.File.FileModel
import com.gamatechno.chato.sdk.data.constant.Api
import com.gamatechno.chato.sdk.module.core.BasePresenter
import com.gamatechno.chato.sdk.module.request.GGFWRest
import com.gamatechno.chato.sdk.module.request.RequestInterface.OnGetRequest
import com.gamatechno.chato.sdk.utils.ChatoUtils
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class MyDocumentPresenter(context: Context, view: MyDocumentView.View): BasePresenter(context), MyDocumentView.Presenter{

    var view : MyDocumentView.View
    init {
        this.view = view
    }
    override fun requestDocument(isRefresh: Boolean, type: String, keyword: String) {
        GGFWRest.GET(Api.getFileSharing()+"?page=1&keyword=&directory"+type, object : OnGetRequest {
            override fun onPreExecuted() {

            }
            override fun onSuccess(response: JSONObject) {
                try {
                    if (response.getBoolean("success")) {
                        val list = ArrayList<FileModel>()
                        val result = response.getJSONObject("result")
                        val array = result.getJSONArray("list_document")
                        for (i in 0 until array.length()) {
                            list.add(gson.fromJson(array[i].toString(), FileModel::class.java))
                        }
//                        view.onGetAllSchedule(list)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(error: String) {}
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
}