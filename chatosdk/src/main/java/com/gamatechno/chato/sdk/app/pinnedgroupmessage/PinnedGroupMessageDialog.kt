package com.gamatechno.chato.sdk.app.pinnedgroupmessage

import android.app.Dialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.PopupMenu
import com.gamatechno.chato.sdk.R
import com.gamatechno.chato.sdk.data.DAO.Chat.Chat
import com.gamatechno.chato.sdk.utils.DetectHtml
import com.gamatechno.ggfw.utils.DialogBuilder
import com.gamatechno.ggfw.utils.GGFWUtil
import kotlinx.android.synthetic.main.dialog_pinned_group_message.*


class PinnedGroupMessageDialog(context: Context?, chat: Chat, isAdmin: Boolean, onPinnedGroupMessage: OnPinnedGroupMessage) : DialogBuilder(context, R.layout.dialog_pinned_group_message) {
    lateinit var popup: PopupMenu
    var clipboard: ClipboardManager
    init {
        clipboard = context!!.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

        setAnimation(R.style.DialogBottomAnimation)
        setFullWidth(dialog.lay_dialog)
        setGravity(Gravity.BOTTOM)

        with(dialog){
            var type = ""
            if (chat.message_type == "file")
                type = "(Berkas) "
            else if (chat.message_type == "image")
                type = "(Foto) "
            else if (chat.message_type == "video")
                type = "(Video) "
            else if (chat.message_type == "voice")
                type = "(Voice) "
            tv_pinnedmessage.setText(DetectHtml.convertHtmlToPlain(type+chat.message_text))

            img_back.setOnClickListener({
                dismiss()
            })

            showPopupMenu(img_option, chat, isAdmin, onPinnedGroupMessage)

            img_option.setOnClickListener {
                popup.show()
            }
        }
        show()
    }

    interface OnPinnedGroupMessage{
        fun onUnpinMessage(chat: Chat, dialog: Dialog)
    }

    fun showPopupMenu(v: View, chat: Chat, isAdmin: Boolean, onPinnedGroupMessage: OnPinnedGroupMessage){
        popup = PopupMenu(context, v)
        // Inflate the menu from xml
        popup.getMenuInflater().inflate(R.menu.popup_pinnedmessage, popup.getMenu())
        if(!isAdmin){
            popup.menu.findItem(R.id.action_unpin).setEnabled(false);
        }
        if (chat.message_type.equals(Chat.chat_type_file) ||
            chat.message_type.equals(Chat.chat_type_image) ||
            chat.message_type.equals(Chat.chat_type_video) ||
            chat.message_type.equals(Chat.chat_type_audio) ){
            popup.menu.findItem(R.id.action_copy).setEnabled(false);
        }

        // Setup menu item selection
        popup.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener {
            override fun onMenuItemClick(item: MenuItem): Boolean {
                when (item.getItemId()) {
                    R.id.action_unpin -> {
                        onPinnedGroupMessage.onUnpinMessage(chat, dialog)
                        return true
                    }
                    R.id.action_copy-> {
                        var clip: ClipData? = null

                        clip = ClipData.newPlainText("com.gamatechno.chato", chat.message_text)
                        clipboard.setPrimaryClip(clip!!)
                        GGFWUtil.ToastShort(getContext(), getContext().getResources().getString(R.string.label_copiedchat))
                        return true
                    }
                    else -> return false
                }
            }
        })
    }
}
