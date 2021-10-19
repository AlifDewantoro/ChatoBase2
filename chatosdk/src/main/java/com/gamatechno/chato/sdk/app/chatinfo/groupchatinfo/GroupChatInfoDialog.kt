package com.gamatechno.chato.sdk.app.chatinfo.groupchatinfo

import android.content.Context
import android.net.Uri
import android.view.Gravity
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.gamatechno.chato.sdk.R
import com.gamatechno.chato.sdk.app.chatinfo.groupchatinfo.adapter.ChatgroupInfoAdapter
import com.gamatechno.chato.sdk.app.chatroom.adapter.ChatRoomAdapter
import com.gamatechno.chato.sdk.app.kontakchat.KontakModel
import com.gamatechno.chato.sdk.data.DAO.Chat.Chat
import com.gamatechno.ggfw.utils.DialogBuilder
import kotlinx.android.synthetic.main.dialog_group_chat_info.*

class GroupChatInfoDialog(context: Context?, chat: Chat, list_terkirim: MutableList<KontakModel>, list_dibaca: MutableList<KontakModel>) : DialogBuilder(context, R.layout.dialog_group_chat_info) {

    var adapter_terbaca : ChatgroupInfoAdapter
    var adapter_terkirim : ChatgroupInfoAdapter

    internal var adapter: ChatRoomAdapter
    internal var chatlist: MutableList<Chat> = ArrayList()

    init {
        setAnimation(R.style.DialogAnimationRight)
        setFullScreen(dialog.lay_dialog)
        setGravity(Gravity.BOTTOM)


        with(dialog){
            rv_dibacaoleh.layoutManager = LinearLayoutManager(context)
            rv_terkirimke.layoutManager = LinearLayoutManager(context)
            rv_info.layoutManager = LinearLayoutManager(context)

            img_back.setOnClickListener({
                dismiss()
            })

            adapter_terbaca = ChatgroupInfoAdapter(context!!, list_dibaca!!)
            adapter_terkirim = ChatgroupInfoAdapter(context!!, list_terkirim!!)

            rv_dibacaoleh.adapter = adapter_terbaca
            rv_terkirimke.adapter = adapter_terkirim

            chatlist.add(chat)
            adapter = ChatRoomAdapter(getContext(), chatlist, object : ChatRoomAdapter.OnChatRoomClick{
                override fun onImageClick(view: View?, chat:Chat?) {

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
            rv_info.adapter = adapter
        }

        show()
    }
}
