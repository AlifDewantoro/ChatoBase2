package com.gamatechno.chato.sdk.app.grouproomdetail.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gamatechno.chato.sdk.R
import com.gamatechno.ggfw.Activity.FragmentPermission

class MediaFragment : FragmentPermission() {
    var rootView: View? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_media, container, false)
        return rootView
    }
}