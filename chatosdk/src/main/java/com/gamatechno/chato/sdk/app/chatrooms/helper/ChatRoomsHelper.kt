package com.gamatechno.chato.sdk.app.chatrooms.helper

import com.gamatechno.chato.sdk.app.chatrooms.uimodel.ChatRoomsUiModel
import com.gamatechno.chato.sdk.data.DAO.Chat.Chat
import com.gamatechno.chato.sdk.data.DAO.Label.LabelModel
import java.util.*

class ChatRoomsHelper {


    companion object {
        @JvmStatic
        fun sampleChat(): MutableList<Chat> {
            val sampleChat: MutableList<Chat> = ArrayList()
            var chat = Chat(1, 1, 2, "ini Aku", "message", "", "DELIVERED", "2019-04-09", "00:00:01")
            sampleChat.add(chat)
            chat = Chat(2, 2, 1, "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.", "message", "", "DELIVERED", "2019-04-09", "00:00:01")
            sampleChat.add(chat)
            chat = Chat(3, 2, 1, "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.", "image", "https://www.gamatechno.com/images/2016_07_11_General_Meeting_1.JPG", "DELIVERED", "2019-04-09", "00:00:01")
            sampleChat.add(chat)
            return sampleChat
        }

        @JvmStatic
        fun filtered_labels(): MutableList<LabelModel> {
            val filtered_labels: MutableList<LabelModel> = ArrayList()
            filtered_labels.add(LabelModel(1, "Task", true, LabelModel.TASK))
            filtered_labels.add(LabelModel(2, "Team", true, LabelModel.TEAM))
            return filtered_labels
        }

        @JvmStatic
        fun totalPinnedChatRoom(list: MutableList<ChatRoomsUiModel>): Int {
            var total = 0
            for (m in list) {
                if (m.roomChat.is_pined == 1) {
                    total = total + 1
                }
            }
            return total
        }

        @JvmStatic
        fun isChatRoomClicked(list: MutableList<ChatRoomsUiModel>): Boolean {
            for (m in list) {
                if (m.is_checked) {
                    return true
                }
            }
            return false
        }

        @JvmStatic
        fun getChatRoomClicked(lists: MutableList<ChatRoomsUiModel>): ChatRoomsUiModel? {
            for (m in lists) {
                if (m.is_checked) {
                    return m
                }
            }
            return null
        }

        @JvmStatic
        fun getChatListRoomClicked(lists: MutableList<ChatRoomsUiModel>): MutableList<ChatRoomsUiModel> {
            val listChecked: MutableList<ChatRoomsUiModel> = arrayListOf()
            for (m in lists) {
                if (m.is_checked) {
                    listChecked.add(m)
                }
            }
            return listChecked
        }

        @JvmStatic
        fun getIndexChatRoom(chatRoomUiModelList: MutableList<ChatRoomsUiModel>, chatRoomUiModel: ChatRoomsUiModel): Int {
            for (i in chatRoomUiModelList.indices) {
                if (chatRoomUiModel.roomChat.room_id == chatRoomUiModelList[i].roomChat.room_id) {
                    return i
                }
            }
            return -1
        }
    }
}