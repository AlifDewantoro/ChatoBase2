package com.gamatechno.chato.sdk.app.chatrooms

import com.gamatechno.chato.sdk.app.chatrooms.uimodel.ChatRoomsUiModel
import com.gamatechno.chato.sdk.module.core.BaseView

interface ChatRoomsView {
    interface ObrolanPresenter {
        fun requestObrolan(isRefresh: Boolean, keyword: String?, categories: String)
        fun pinChatRoom(chatRoomUiModel: ChatRoomsUiModel?, total: Int)
        fun pinMultipleChatRoom(chatRoomUiModels: List<ChatRoomsUiModel>?, total: Int, status: String)
        fun deleteRoom(chatRoomUiModel: ChatRoomsUiModel?)
    }

    interface View : BaseView {
        fun onRequestObrolan(list: List<ChatRoomsUiModel?>?, isRefresh: Boolean)
        fun onFailedRequestObrolan()
        fun onFailedPin(message: String?)
        fun onFailedDelete(message: String?)
        fun successPinnedChatRoom(message: String?)
        fun successPinnedMultipleChatRoom(message: String?)
        fun onDeleteRoom(isSuccess: Boolean, message: String?)
    }
}