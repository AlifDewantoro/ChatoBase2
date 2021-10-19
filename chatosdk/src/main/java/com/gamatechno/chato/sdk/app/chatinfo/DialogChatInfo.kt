package com.gamatechno.chato.sdk.app.chatinfo

import android.content.Context
import android.net.Uri
import android.view.Gravity
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.gamatechno.chato.sdk.R
import com.gamatechno.chato.sdk.app.chatroom.adapter.ChatRoomAdapter
import com.gamatechno.chato.sdk.data.DAO.Chat.Chat
import com.gamatechno.ggfw.utils.DialogBuilder
import kotlinx.android.synthetic.main.dialog_chat_info.*

class DialogChatInfo(context: Context?, chat: Chat, delivered_info : String, read_info : String) : DialogBuilder(context, R.layout.dialog_chat_info) {

    internal var adapter: ChatRoomAdapter
    internal var chatlist: MutableList<Chat> = ArrayList()

    init {
        setAnimation(R.style.DialogBottomAnimation)
        setFullWidth(dialog.lay_dialog)
        setGravity(Gravity.BOTTOM)

        dialog.rv_info.layoutManager = LinearLayoutManager(getContext())
        chatlist.add(chat)
        adapter = ChatRoomAdapter(getContext(), chatlist, object : ChatRoomAdapter.OnChatRoomClick{
            override fun onImageClick(view: View?, chat: Chat?) {

            }

            override fun onLongPress(view: View?, position: Int) {

            }

            override fun onClickItemView(view: View?, position: Int) {

            }

            override fun onClickRepliedMessage(chat: Chat?) {

            }

            override fun onClickAttachment(chat: Chat?, uri: Uri?) {

            }

            override fun onDownloadingAttachment(isDownload: Boolean, position: Int) {

            }

            override fun onOpenVideo(view: View?, chat: Chat?, uri: Uri?) {

            }

            override fun onPlayAudio(chat: Chat?, position: Int) {

            }

            override fun onReadMessage(chat: Chat?) {
                
            }
        }, false)
        dialog.rv_info.adapter = adapter


        dialog.tv_delivered.text = if(delivered_info.equals("")) "-" else delivered_info.split(" ")[1]
        dialog.tv_read.text = if(read_info.equals("")) "-" else read_info.split(" ")[1]

        show()
    }
}
