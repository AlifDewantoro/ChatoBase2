package com.gamatechno.chato.sdk.app.chatinfo.groupchatinfo.adapter

import android.content.Context
import android.os.Bundle
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gamatechno.chato.sdk.R
import com.gamatechno.chato.sdk.app.kontakchat.KontakModel
import com.gamatechno.chato.sdk.data.model.OptionModel
import kotlinx.android.synthetic.main.adapter_chatgroup_info.view.*

class ChatgroupInfoAdapter(@NonNull context: Context, list: MutableList<KontakModel>) : RecyclerView.Adapter<ChatgroupInfoAdapter.Holder>() {

    var list: MutableList<KontakModel>
    var context: Context

    init {
        this.list = list
        this.context = context
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ChatgroupInfoAdapter.Holder {
        val layoutView: View
        layoutView = LayoutInflater.from(p0.context).inflate(R.layout.adapter_chatgroup_info, p0, false)
        return Holder(layoutView)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(p0: ChatgroupInfoAdapter.Holder, p1: Int) {
        p0.bindTo(list.get(p1))
    }

    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindTo(kontakModel: KontakModel): Unit = with(itemView) {
            with(kontakModel){
                tv_name.setText(user_name)
            }
        }

    }

}
