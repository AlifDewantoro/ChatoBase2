package com.gamatechno.chato.sdk.app.chatroom.dialog

import android.content.Context
import com.gamatechno.chato.sdk.R
import com.gamatechno.ggfw.utils.DialogBuilder
import kotlinx.android.synthetic.main.dialog_add_member.*
import kotlinx.android.synthetic.main.dialog_file_source.*
import kotlinx.android.synthetic.main.dialog_picker.*
import kotlinx.android.synthetic.main.dialog_picker.btn_cancel

class FileShareDialog(context: Context, onAction: OnAction):DialogBuilder(context, R.layout.dialog_file_source) {
    init {
        with(dialog){
            btn_cancel.setOnClickListener {
                dismiss()
            }

            btn_ok.setOnClickListener {
                if(rbt_my_document.isChecked)
                    onAction.onMyDocumentSelected()
                else
                    onAction.onInternalStorageSelected()
                dismiss()
            }
            show()
        }
    }

    interface OnAction{
        fun onMyDocumentSelected()
        fun onInternalStorageSelected()
    }
}