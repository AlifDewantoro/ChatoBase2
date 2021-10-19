package com.gamatechno.chato.sdk.app.broadcastdirect.kontakbroadcast

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gamatechno.chato.sdk.R
import com.gamatechno.chato.sdk.app.kontakchat.KontakModel
import com.gamatechno.chato.sdk.data.DAO.RoomChat.RoomChat
import com.gamatechno.ggfw_ui.avatarview.AvatarPlaceholder
import com.gamatechno.ggfw_ui.avatarview.loader.PicassoLoader
import kotlinx.android.synthetic.main.adapter_kontak.view.*
import kotlinx.android.synthetic.main.header_contact_broadcast.view.*
import kotlinx.android.synthetic.main.item_message.view.*
import kotlinx.android.synthetic.main.item_message.view.tv_name

class ContactBroadcastAdapter(var onAction: OnAction): RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    var list: ArrayList<ContactBroadcastModel>

    init {
        this.list = ArrayList()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, type: Int): RecyclerView.ViewHolder {
        if(type==0) {
            val v = LayoutInflater.from(viewGroup.context).inflate(R.layout.header_contact_broadcast,viewGroup,false)
            return HeaderHolder(v)
        } else {
            val v = LayoutInflater.from(viewGroup.context).inflate(R.layout.adapter_kontak,viewGroup,false)
            return ItemHolder(v)
        }
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is HeaderHolder){
            holder.binding(list[position], onAction)
        } else if(holder is ItemHolder){
            holder.binding(list[position], onAction)
        }
    }

    override fun getItemViewType(position: Int) = list[position].type

    fun addAll(list: ArrayList<ContactBroadcastModel>){
        this.list.addAll(list)
        notifyDataSetChanged()
    }

    fun clear(){
        this.list.clear()
        notifyDataSetChanged()
    }

    class HeaderHolder(val v: View):RecyclerView.ViewHolder(v){
        private val tvTitle = v.tv_title
        private val btnSelectAll = v.btn_selectAll
        private val tvSelectAll = v.tv_selectAll
        private val ivSelectAll = v.iv_selectAll
        fun binding(item: ContactBroadcastModel, onAction: OnAction){
            val contact = item.contact
            if(contact.isHeader) {
                tvTitle.text = "Daftar "+contact.user_name
                tvTitle.visibility = View.VISIBLE
            } else
                tvTitle.visibility = View.GONE

            tvSelectAll.text = "Pilih Semua " + contact.user_name
            if(item.isSelected){
                ivSelectAll.setColorFilter(v.resources.getColor(R.color.green_500))
            } else {
                ivSelectAll.setColorFilter(v.resources.getColor(R.color.grey_500))
            }
            btnSelectAll.setOnClickListener {
                onAction.onClickSelectAll(item)
            }
            btnSelectAll.visibility = View.GONE
        }
    }
    class ItemHolder(val v: View):RecyclerView.ViewHolder(v){
        private val tvName = v.tv_name
        private val avatarView = v.avatarView
        private val cvIndicator = v.card_indicator
        private val ivSelected = v.iv_select
        private fun getName(contact:KontakModel):String {
            return if(contact.room_type.equals(RoomChat.user_room_type))
                contact.user_name
            else
                contact.group_name
        }
        fun binding(item: ContactBroadcastModel, onAction: OnAction){
            val contact = item.contact
            cvIndicator.visibility = View.GONE
            ivSelected.visibility = View.VISIBLE
            tvName.text = getName(contact)

            val picassoLoader = PicassoLoader()
            val avatarPlaceholder = AvatarPlaceholder(tvName.text.toString())

            picassoLoader.loadImage(avatarView,avatarPlaceholder,contact.user_photo)

            v.setOnClickListener({
                onAction.onClickItem(item)
            })

            if(item.isSelected){
                ivSelected.setColorFilter(v.resources.getColor(R.color.green_500))
            } else {
                ivSelected.setColorFilter(v.resources.getColor(R.color.grey_500))
            }
        }
    }

    interface OnAction{
        fun onClickItem(cbm: ContactBroadcastModel)
        fun onClickSelectAll(cbm: ContactBroadcastModel)
    }
}