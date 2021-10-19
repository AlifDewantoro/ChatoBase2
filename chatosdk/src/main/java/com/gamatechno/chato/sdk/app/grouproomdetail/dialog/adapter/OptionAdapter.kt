package com.gamatechno.chato.sdk.app.grouproomdetail.dialog.adapter

import android.content.Context
import android.os.Bundle
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gamatechno.chato.sdk.R
import com.gamatechno.chato.sdk.data.model.OptionModel
import kotlinx.android.synthetic.main.adapter_option.view.*

class OptionAdapter(@NonNull context: Context, optionList: MutableList<OptionModel>, onOptionAdapter: OnOptionAdapter) : RecyclerView.Adapter<OptionAdapter.Holder>() {

    var optionList: MutableList<OptionModel>? = null
    var context: Context? = null
    var onOptionAdapter: OnOptionAdapter? = null

    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindTo(optionModel: OptionModel, onOptionAdapter: OnOptionAdapter): Unit = with(itemView) {
            optionModel.let {
                tv_name.setText(it!!.name)
                setOnClickListener({
                    onOptionAdapter.onOptionClick(optionModel)
                })
            }

        }
    }

    init {
        this.optionList = optionList
        this.context = context
        this.onOptionAdapter = onOptionAdapter
    }

    interface OnOptionAdapter{
        fun onOptionClick(optionModel: OptionModel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): OptionAdapter.Holder {
        val layoutView: View
        layoutView = LayoutInflater.from(parent.context).inflate(R.layout.adapter_option, parent, false)
        return Holder(layoutView)
    }

    override fun getItemCount(): Int {
        return optionList!!.size
    }

    override fun onBindViewHolder(p0: OptionAdapter.Holder, p1: Int) {
        p0.bindTo(optionList!!.get(p1), onOptionAdapter!!)
    }

}
