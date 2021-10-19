package com.gamatechno.chato.sdk.utils

import android.content.Context
import android.net.Uri
import com.gamatechno.chato.sdk.data.constant.Api
import com.gamatechno.chato.sdk.module.request.GGFWRest
import com.gamatechno.chato.sdk.module.request.RequestInterface
import com.gamatechno.chato.sdk.utils.FilePath.IOUtils
import com.gamatechno.ggfw.utils.VolleyMultipartRequest
import org.json.JSONException
import org.json.JSONObject
import java.util.HashMap

class ImageUploader(context: Context, loading: Loading, uri: Uri, isneedLoading: Boolean, onUploadImage: OnUploadImage) {
    init {
        GGFWRest.POSTMultipart(Api.upload_file(), context, object : RequestInterface.OnMultipartRequest{
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
                return params
            }

            override fun requestHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Authorization"] = "Bearer " + ChatoUtils.getUserLogin(context).access_token
                return headers
            }

        })
    }

    interface OnUploadImage{
        fun onSuccessUploadImage(url: String)
        fun onFailedUploadImage(message: String)
    }
}