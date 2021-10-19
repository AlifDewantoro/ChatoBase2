package com.gamatechno.chato.sdk.utils

import android.content.Context
import android.net.Uri
import com.gamatechno.chato.sdk.data.DAO.Customer.Customer
import com.gamatechno.chato.sdk.data.constant.Api
import com.gamatechno.chato.sdk.data.constant.Preferences
import com.gamatechno.chato.sdk.module.request.GGFWRest
import com.gamatechno.chato.sdk.module.request.RequestInterface
import com.gamatechno.chato.sdk.utils.FilePath.FilePath
import com.gamatechno.chato.sdk.utils.FilePath.IOUtils
import com.gamatechno.ggfw.utils.GGFWUtil
import com.gamatechno.ggfw.utils.VolleyMultipartRequest
import com.google.gson.Gson
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class SharingFileUploader(context: Context, loading: Loading, uri: Uri, isneedLoading: Boolean, onUploadImage: OnUploadImage) {

    lateinit var customer: Customer

    init {

        if (GGFWUtil.getStringFromSP(context, Preferences.CUSTOMER_INFO) != "") {
            customer = Gson().fromJson(GGFWUtil.getStringFromSP(context, Preferences.CUSTOMER_INFO), Customer::class.java)
        }

        GGFWRest.POSTMultipart(Api.uploadDocumentSharing(), context, object : RequestInterface.OnMultipartRequest{
            override fun requestData(): MutableMap<String, VolleyMultipartRequest.DataPart> {
                val params = HashMap<String, VolleyMultipartRequest.DataPart>()
                params["file_name"] = VolleyMultipartRequest.DataPart(uri.path, ChatoUtils.getBytesFile(context, uri), IOUtils.getTypeFile(context, uri))
                return params
            }

            override fun onPreExecuted() {
                loading.show()
            }

            override fun onSuccess(response: JSONObject?) {
                if(!isneedLoading)
                    loading.dismiss()

                try {
                    if (response!!.getBoolean("success")) {
                        val obj = response!!.getJSONObject("result")
                        onUploadImage.onSuccessUploadImage(obj.getString("file_url"))
                    } else {
                        onUploadImage.onFailedUploadImage(response.getString("message"))
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }

            override fun onFailure(error: String?) {
                loading.dismiss()
            }

            override fun requestParam(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params["file_title"] = FilePath.getFileName(context, uri)
                params["file_desc"] = ""
                return params
            }

            override fun requestHeaders(): MutableMap<String, String> {
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

    interface OnUploadImage{
        fun onSuccessUploadImage(url: String)
        fun onFailedUploadImage(message: String)
    }
}