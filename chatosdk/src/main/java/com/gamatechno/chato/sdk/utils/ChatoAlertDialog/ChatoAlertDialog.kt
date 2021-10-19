package com.gamatechno.chato.sdk.utils.ChatoAlertDialog

import android.app.Dialog
import android.content.Context
import com.gamatechno.chato.sdk.R
import com.gamatechno.ggfw.utils.DialogBuilder
import kotlinx.android.synthetic.main.dialog_chato_alert.*

class ChatoAlertDialog (context: Context?, title: String, desc: String, onAlerDialog: ChatoAlertDialog.OnAlertDialog) : DialogBuilder(context, R.layout.dialog_chato_alert) {

    init {
        with(dialog){
            tv_title.setText(title)
            tv_desc.setText(desc)
            tv_ok.setOnClickListener({
                onAlerDialog.onOk(this)
            })
        }

        show()
    }

    public interface OnAlertDialog{
        fun onOk(dialog: Dialog)
    }

}
