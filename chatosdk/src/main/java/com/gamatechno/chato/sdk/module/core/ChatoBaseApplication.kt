package com.gamatechno.chato.sdk.module.core

import android.app.Application
import android.util.Log
import cat.ereza.customactivityoncrash.config.CaocConfig
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.gamatechno.chato.sdk.BuildConfig
import com.gamatechno.chato.sdk.R
import com.gamatechno.chato.sdk.app.login.LoginPresenter
import com.gamatechno.chato.sdk.app.login.LoginView
import com.gamatechno.chato.sdk.data.constant.Preferences
import com.gamatechno.chato.sdk.data.model.UserModel
import com.gamatechno.chato.sdk.mapper.ChatoSDKMapper
import com.gamatechno.chato.sdk.utils.ChatoUtils
import com.gamatechno.ggfw.utils.GGFWUtil
import com.google.gson.Gson


open class ChatoBaseApplication : Application() {
    private val TIMEOUT_MS = 60000 // 45second

    private var requestQueue: RequestQueue? = null

    companion object {
        @JvmStatic
        lateinit var instance: ChatoBaseApplication
            private set
    }

    private var chato_placeholder: Int = R.drawable.ic_placeholder

    public var KEY = ""

    override fun onCreate() {
        super.onCreate()
        instance = this

        CaocConfig.Builder.create()
            .backgroundMode(CaocConfig.BACKGROUND_MODE_SILENT) //default: CaocConfig.BACKGROUND_MODE_SHOW_CUSTOM
            .enabled(true) //default: true
            .showErrorDetails(true) //default: true
            .showRestartButton(true) //default: true
            .trackActivities(true) //default: false
            .minTimeBetweenCrashesMs(2000) //default: 3000
            .errorDrawable(R.drawable.new_error) //default: bug image
            .apply()
    }

    fun getChatoPlaceholder(): Int {
        return chato_placeholder
    }

    fun setChatoPlaceholder(drawable: Int) {
        chato_placeholder = drawable
    }

    fun logout(){
        GGFWUtil.setStringToSP(this, Preferences.CUSTOMER_INFO, "")
        GGFWUtil.setStringToSP(this, Preferences.USER_LOGIN, "")
    }

    fun setChatoFirebaseToken(token : String){
        LoginPresenter(this, object : LoginView.View{
            override fun onErrorConnection(message: String?) {

            }

            override fun onAuthFailed(error: String?) {

            }

            override fun onFailedUpdateTokenFirebase() {

            }

            override fun onHideLoading() {

            }

            override fun onLoading() {

            }

            override fun onSucces() {

            }

            override fun onSuccessUpdateTokenFirebase() {

            }

        }).updateTokenFirebase(token)
    }

    fun setChatoToken(token : String, authInteractor: AuthInteractor){
        ChatoSDKMapper.setCustomer(this, BuildConfig.cust_secret, BuildConfig.cust_app_id)
        LoginPresenter(this, object : LoginView.View{
            override fun onErrorConnection(message: String?) {

            }

            override fun onAuthFailed(error: String?) {

            }

            override fun onFailedUpdateTokenFirebase() {

            }

            override fun onHideLoading() {

            }

            override fun onLoading() {

            }

            override fun onSucces() {

            }

            override fun onSuccessUpdateTokenFirebase() {

            }

        }).getOfficialToken(token, object : LoginView.ClientView{
            override fun onAuthResult(isSuccess: Boolean?, message: String?) {
                authInteractor.authResult(isSuccess, message)
            }
        })
    }

    interface AuthInteractor{
        fun authResult(isSuccess: Boolean?, message: String?)
    }

    fun setUserInfo(
            id: Int,
            name: String?,
            email: String?,
            photo: String?
    ) {
        var userModel : UserModel
        if(!GGFWUtil.getStringFromSP(this, Preferences.USER_LOGIN).equals("")){
            userModel = Gson().fromJson(GGFWUtil.getStringFromSP(this, Preferences.USER_LOGIN), UserModel::class.java)
            userModel.apply {
                user_id = id
                user_name = name
                user_email = email
                user_photo = photo
            }
        } else {
            userModel = UserModel(name, email, photo)
        }
        ChatoUtils.setUserLogin(this, userModel)
    }

    fun getChatoRequestQueue(): RequestQueue? {
        Log.d("BaseApplication", "getRequestQueue : ")
        if (requestQueue == null) {
            Log.d(
                    "BaseApplication",
                    "getRequestQueue : make new instance "
            )
            requestQueue = Volley.newRequestQueue(applicationContext)
        }
        return requestQueue
    }

    fun <T> addToChatoRequestQueue(
            request: Request<T>,
            tag: String?
    ) {
        request.tag = tag

        // set retry policy
        request.retryPolicy = DefaultRetryPolicy(
                TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        request.setShouldCache(false)
        Log.d(
                "BaseApplication",
                "addToRequestQueue : " + request.url
        )
        getChatoRequestQueue()!!.add(request)
    }

    fun cancelPendingChatoRequest() {
        if (requestQueue != null) requestQueue!!.cancelAll { true }
    }

    fun cancelPendingChatoRequest(tag: Any?) {
        if (requestQueue != null) requestQueue!!.cancelAll(tag)
    }
}