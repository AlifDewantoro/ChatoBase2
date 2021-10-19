package com.gamatechno.chato.sdk.app.sharedmedia.fragment.sharedimage

import com.gamatechno.chato.sdk.data.DAO.Chat.Chat
import com.gamatechno.chato.sdk.module.core.BaseView

interface SharedMediaView {
    interface Presenter{
        fun getList(api:String)
    }
    interface View: BaseView {
        fun setListView(list : ArrayList<Chat>)
        fun setEmptyView()
    }
}