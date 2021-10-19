package com.gamatechno.chato.sdk.app.labeldetail

import com.gamatechno.chato.sdk.app.chatrooms.uimodel.ChatRoomsUiModel
import com.gamatechno.chato.sdk.data.DAO.Label.LabelModel
import com.gamatechno.chato.sdk.module.core.BaseView

interface LabelDetailView {
    interface Presenter{
        fun requestObrolan(labelModel: LabelModel)
        fun pinChatRoom(chatRoomUiModel: ChatRoomsUiModel?, total: Int)
        fun deleteRoom(chatRoomUiModel: ChatRoomsUiModel?)
    }
    interface View : BaseView{
        fun onRequestRoom(list : MutableList<ChatRoomsUiModel>)
        fun onFailedRequestObrolan()
        fun successPinnedChatRoom(message: String?)
        fun onDeleteRoom(isSuccess: Boolean, message: String?)
    }
}