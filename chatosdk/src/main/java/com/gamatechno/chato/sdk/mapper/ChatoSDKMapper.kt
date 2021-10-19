package com.gamatechno.chato.sdk.mapper

import android.content.Context
import android.util.Log
import com.gamatechno.chato.sdk.data.DAO.Customer.Customer
import com.gamatechno.chato.sdk.data.constant.Preferences
import com.gamatechno.chato.sdk.data.model.UserModel
import com.gamatechno.chato.sdk.utils.ChatoUtils
import com.gamatechno.ggfw.utils.GGFWUtil
import com.google.gson.Gson
import java.lang.Exception

class ChatoSDKMapper {
    companion object {
        fun setToken(context: Context, token : String, refreshToken : String){
            var userModel = UserModel()
            if(!GGFWUtil.getStringFromSP(context, Preferences.USER_LOGIN).equals("")){
                userModel = Gson().fromJson(GGFWUtil.getStringFromSP(context, Preferences.USER_LOGIN), UserModel::class.java)
                userModel.apply {
                    access_token = token
                    refresh_token = refreshToken
                }
            } else {
                userModel = UserModel(token, refreshToken)
            }
            ChatoUtils.setUserLogin(context, userModel)
        }

        fun isTokenSet(context : Context) : Boolean{
            if(GGFWUtil.getStringFromSP(context, Preferences.USER_LOGIN).equals("")){
                return false
            } else {
                val userModel = Gson().fromJson(GGFWUtil.getStringFromSP(context, Preferences.USER_LOGIN), UserModel::class.java)
                try{
                    if(userModel.access_token.equals("-")){
                        return false
                    } else {
                        return true
                    }
                } catch (e : Exception){
                    return false
                }
            }
        }

        fun setCustomer(context : Context, cust_secret : String, cust_app_id : String){
            Log.d("ChatoSDKMapper", "BasePresenter: " + Preferences.CUSTOMER_INFO)
            val cust = Customer(cust_app_id, cust_secret)
            GGFWUtil.setStringToSP(context, Preferences.CUSTOMER_INFO, Gson().toJson(cust))
            Log.d("ChatoSDKMapper", "gson to json: " + Gson().toJson(cust))
            Log.d("ChatoSDKMapper", "BasePresenter: " + GGFWUtil.getStringFromSP(context, Preferences.CUSTOMER_INFO))
        }

        fun setUser(context: Context, id: Int){
            Log.d("setUser", "here : "+id)
            var userModel : UserModel
            if(!GGFWUtil.getStringFromSP(context, Preferences.USER_LOGIN).equals("")){
                userModel = Gson().fromJson(GGFWUtil.getStringFromSP(context, Preferences.USER_LOGIN), UserModel::class.java)
                userModel.apply {
                    user_id = id
                }
            } else {
                userModel = UserModel(id)
            }
            ChatoUtils.setUserLogin(context, userModel)
        }

        fun setUser(context: Context, name : String, email: String, photo : String){
            var userModel : UserModel
            if(!GGFWUtil.getStringFromSP(context, Preferences.USER_LOGIN).equals("")){
                userModel = Gson().fromJson(GGFWUtil.getStringFromSP(context, Preferences.USER_LOGIN), UserModel::class.java)
                userModel.apply {
                    user_name = name
                    user_email = email
                    user_photo = photo
                }
            } else {
                userModel = UserModel(name, email, photo)
            }
            ChatoUtils.setUserLogin(context, userModel)
        }
    }
}