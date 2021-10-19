package com.gamatechno.chato.sdk.app.broadcastdirect.kontakbroadcast

import com.gamatechno.chato.sdk.app.kontakchat.KontakModel
import com.gamatechno.chato.sdk.module.core.BaseView

interface ContactBroadcastView {
    interface Presenter {
        fun getContact(isRefresh:Boolean)
        fun getSearchContact(keyword:String, isRefresh: Boolean)
    }
    interface View : BaseView {
        fun onListContact(listUser:ArrayList<KontakModel>, listGroup: ArrayList<KontakModel>, isRefresh: Boolean)
        fun onFailedListContact(isRefresh: Boolean)
    }
}