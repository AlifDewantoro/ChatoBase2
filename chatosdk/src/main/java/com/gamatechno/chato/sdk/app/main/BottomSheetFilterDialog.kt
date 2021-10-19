package com.gamatechno.chato.sdk.app.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.gamatechno.chato.sdk.R
import com.gamatechno.chato.sdk.utils.BaseBottomSheet
import kotlinx.android.synthetic.main.fragment_bottom_sheet_filter_dialog.view.*

class BottomSheetFilterDialog(val listener: onFilterSelectedListener) : BaseBottomSheet() {
    var rootView: View? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_bottom_sheet_filter_dialog, container, false)

        rootView!!.btn_close.setOnClickListener {
            dismiss()
        }

        rootView!!.btn_apply.setOnClickListener {
            if (rootView!!.rbSortAZ.isChecked){
                listener.onSelectedFilter(1)
                dismiss()
            }else if (rootView!!.rbSortZA.isChecked){
                listener.onSelectedFilter(2)
                dismiss()
            }else if (rootView!!.rbSortNormal.isChecked){
                listener.onSelectedFilter(3)
                dismiss()
            } else{
                Toast.makeText(context, "filter belum dipilih", Toast.LENGTH_LONG).show()
            }
        }

        return rootView
    }

    interface onFilterSelectedListener {
        fun onSelectedFilter(type: Int)
    }
}