package com.gamatechno.chato.sdk.app.broadcastdirect.roombroadcast

import com.gamatechno.chato.sdk.data.DAO.Chat.Chat

interface RoomBroadcastView {
    interface Presenter{
        fun sendMessage(chat: Chat?)
        fun checkSelectedChat(chatList: MutableList<Chat>)
    }

    interface View {
        fun onSendMessage(chat: Chat?)
        fun onFailedSendMessage(chat: Chat?, message: String?)
    }
}