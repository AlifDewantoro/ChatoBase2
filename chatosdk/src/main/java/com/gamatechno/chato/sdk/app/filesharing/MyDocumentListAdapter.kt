package com.gamatechno.chato.sdk.app.filesharing

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.gamatechno.chato.sdk.R
import com.gamatechno.chato.sdk.app.chatroom.model.FileModel

class MyDocumentListAdapter : RecyclerView.Adapter<MyDocumentListAdapter.ItemHolder>() {
   var list: ArrayList<FileModel> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, type: Int): ItemHolder {
        return ItemHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_shared_file,parent,false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ItemHolder, post: Int) {
        holder.bind(list[post])
    }

    fun addAll(list:ArrayList<FileModel>){
        this.list.addAll(list)
        notifyDataSetChanged()
    }

    class ItemHolder(v: View) : RecyclerView.ViewHolder(v) {
        private var ivIcon: ImageView? = null
        private var tvName: TextView? = null
        private var tvInfo: TextView? = null
        private var tvDate: TextView? = null

        init {
            ivIcon = v.findViewById(R.id.iv_icon)
            tvName = v.findViewById(R.id.tv_file_name)
            tvInfo = v.findViewById(R.id.tv_note)
            tvDate = v.findViewById(R.id.tv_date)
        }

        fun bind(file:FileModel) {
            ivIcon!!.setImageResource(R.drawable.ic_dokumen)
            tvName!!.text = file.namefile
            tvInfo!!.visibility = View.GONE
            tvDate!!.visibility = View.GONE
        }
    }
}