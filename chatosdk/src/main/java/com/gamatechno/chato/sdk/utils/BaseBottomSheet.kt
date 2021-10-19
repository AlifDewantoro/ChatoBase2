package com.gamatechno.chato.sdk.utils

import android.app.Dialog
import android.os.Bundle
import android.widget.FrameLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

abstract class BaseBottomSheet : BottomSheetDialogFragment(){
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bottomSheetDialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        bottomSheetDialog.setOnShowListener { dialog ->
            val dialogg = dialog as BottomSheetDialog
            val bottomSheet = dialogg.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.let { BottomSheetBehavior.from(it).state = BottomSheetBehavior.STATE_EXPANDED }
            bottomSheet?.let { BottomSheetBehavior.from(it).skipCollapsed = true }
            bottomSheet?.let { BottomSheetBehavior.from(it).isHideable = true }
        }
        return bottomSheetDialog
    }

    /*
    fun View.setSafeOnClickListener(onSafeClick: (View) -> Unit) {
        val safeClickListener = SafeClickListener {
            onSafeClick(it)
        }
        setOnClickListener(safeClickListener)
    }

     */
}