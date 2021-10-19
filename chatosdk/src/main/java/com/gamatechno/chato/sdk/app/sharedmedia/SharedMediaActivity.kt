package com.gamatechno.chato.sdk.app.sharedmedia

import android.Manifest
import android.os.Bundle
import com.gamatechno.chato.sdk.R
import com.gamatechno.chato.sdk.app.chatroom.model.ChatRoomUiModel
import com.gamatechno.chato.sdk.app.sharedmedia.adapter.SharedMediaFragmentAdapter
import com.gamatechno.chato.sdk.module.activity.ChatoPermissionActivity
import com.gamatechno.ggfw.Activity.Interfaces.PermissionResultInterface
import com.gamatechno.ggfw.utils.GGFWUtil
import kotlinx.android.synthetic.main.activity_shared_media.*

class SharedMediaActivity : ChatoPermissionActivity() {

    var requiredPermissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
    lateinit var chatRoomUiModel: ChatRoomUiModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shared_media)

        setSupportActionBar(toolbar)
        supportActionBar!!.title = "Konten Bersama"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        if (intent.hasExtra("room")) {
            chatRoomUiModel = intent.getSerializableExtra("room") as ChatRoomUiModel
        }

        view_pager.adapter = SharedMediaFragmentAdapter(supportFragmentManager)
        tabLayout.setupWithViewPager(view_pager)
    }

    fun permission(permission : AskPermission){
        askCompactPermissions(requiredPermissions, object : PermissionResultInterface {
            override fun permissionGranted() {
                permission.permission()
            }

            override fun permissionDenied() {
                GGFWUtil.ToastShort(context, "Anda perlu memberikan akses terlebih dahulu")
            }
        })
    }

    interface AskPermission{
        fun permission()
    }
}
