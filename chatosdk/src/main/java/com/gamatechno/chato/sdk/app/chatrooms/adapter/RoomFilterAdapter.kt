package com.gamatechno.chato.sdk.app.chatrooms.adapter

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.gamatechno.chato.sdk.R
import com.gamatechno.chato.sdk.app.chatroom.model.FileModel
import com.gamatechno.chato.sdk.data.DAO.Label.LabelModel
import com.gamatechno.ggfw_ui.autolabel.Label
import kotlinx.android.synthetic.main.adapter_room_filter.view.*

class RoomFilterAdapter(val context: Context, val onRoomFilter: OnRoomFilter) : RecyclerView.Adapter<RoomFilterAdapter.Holder>() {

    var labels : MutableList<LabelModel> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val layoutView: View
        layoutView = LayoutInflater.from(parent.context).inflate(R.layout.adapter_room_filter, parent, false)
        return Holder(layoutView)
    }

    fun setDatas(x : MutableList<LabelModel>){
        labels.clear()
        labels.addAll(x)
        notifyDataSetChanged()
    }

    fun getSelectedDatas() : MutableList<LabelModel>{
        val x = ArrayList<LabelModel>()
        for (y in labels){
            if(y.is_checked) x.add(y)
        }
        return x
    }

    fun getSortBy(): String{
        var x = ""
        var list = getSelectedDatas()
        if(list.size == 1){
            /*for(y in 0 until list.size){
                if(y == (list.size-1)){
                    x = x+list.get(y).name
                } else {
                    x = x+list.get(y).name+"|"
                }
            }*/
            x = x+list.get(0).name
        }
        return x
    }

    override fun getItemCount(): Int {
        return labels.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val label = labels.get(position)
        with(holder.itemView){
            tv_name.setText(label.label_title)
            if(label.is_checked){
                tv_name.setTextColor(context.getResources().getColor(R.color.colorPrimary))
                tv_name.setBackground(context.getResources().getDrawable(R.drawable.filter_filled))
            } else {
                tv_name.setTextColor(context.getResources().getColor(R.color.grey_700))
                tv_name.setBackground(context.getResources().getDrawable(R.drawable.filter_filled_empty))
            }
            tv_name.setOnClickListener({
                labels.get(position).is_checked = !label.is_checked
                onRoomFilter.onSetRoomFilter(labels)
                notifyDataSetChanged()
            })
        }
    }

    class Holder(v: View) : RecyclerView.ViewHolder(v) {
        init {
            setIsRecyclable(false)
        }
    }

    interface OnRoomFilter{
        fun onSetRoomFilter(list : MutableList<LabelModel>)
    }
}