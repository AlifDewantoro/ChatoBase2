package com.gamatechno.chato.sdk.app.broadcastdirect.roombroadcast

import android.Manifest
import android.net.Uri
import android.os.Bundle
import android.view.View
import com.chato.chato_emoticon.Actions.EmojIconActions
import com.chato.chato_emoticon.Helper.EmojiconsPopup
import com.gamatechno.chato.sdk.R
import com.gamatechno.chato.sdk.module.activity.ChatoPermissionActivity
import com.gamatechno.chato.sdk.module.core.ChatoBaseApplication
import com.gamatechno.chato.sdk.utils.Loading
import com.gamatechno.ggfw_ui.avatarview.AvatarPlaceholder
import com.gamatechno.ggfw_ui.avatarview.loader.PicassoLoader
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.activity_chat_room.*
import kotlinx.android.synthetic.main.toolbar_chatroom.*

open class BaseRoomBroadcastActivity: ChatoPermissionActivity() {
    //    Replied Content
    protected var RequiredPermissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
    protected var FileRequiredPermission = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
    protected var TYPE_ATTACHMENT = 0
    protected val TYPE_IMAGE = 1
    protected val TYPE_FILE = 2
    protected val VIDEO_FILE = 3
    protected val TYPE_MESSAGE = 0

    var loading: Loading? = null

    var emojIcon: EmojIconActions? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_room)
        hideSendMessage()

        setupEmoji()
    }

    protected open fun setToolbar() {
        toolbar.setOverflowIcon(resources.getDrawable(R.drawable.ic_more_vert))
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar!!.setDisplayHomeAsUpEnabled(false)
        card_bar.setOnClickListener(View.OnClickListener {
            finish()
            ChatoBaseApplication.instance.cancelPendingChatoRequest()
        })
        val imageLoader = PicassoLoader()
        val refreshableAvatarPlaceholder = AvatarPlaceholder("B")
        imageLoader.loadImage(img_profile, refreshableAvatarPlaceholder, "google.com")
        txt_title.setText("Broadcast Pesan")
        appbar_action.setInOutAnimation(R.anim.pull_in_bottom, R.anim.push_out_top)
    }

    protected fun disableAutoOpenEmoji(emojActions: EmojIconActions) {
        try {
            var field = emojActions.javaClass.getDeclaredField("popup")
            field.isAccessible = true
            val emojiconsPopup = field[emojActions] as EmojiconsPopup
            field = emojiconsPopup.javaClass.getDeclaredField("pendingOpen")
            field.isAccessible = true
            field[emojiconsPopup] = false
        } catch (exception: Exception) {
        }
    }

    protected fun onTextChangeInput(input: String) {
//        Log.d(TAG, "onTextChangeInput: $input")
        if (input == "") {
            hideSendMessage()
        } else {
            showSendMessage()
        }
    }

    fun setupEmoji() {
        emojIcon = EmojIconActions(this, window.decorView.rootView, edt_message, img_emoticon, "#495C66", "#DCE1E2", "#E6EBEF")
        emojIcon!!.setIconsIds(R.drawable.ic_keyboard_black_24dp, R.drawable.ic_emoji)
        emojIcon!!.setUseSystemEmoji(true)
        edt_message.setUseSystemDefault(true)
        emojIcon!!.ShowEmojIcon()
        disableAutoOpenEmoji(emojIcon!!)
    }

    protected open fun onTextChangeInput(uri_attachment: Uri) {
//        Log.d(TAG, "onTextChangeInput: $uri_attachment")
        if (!uri_attachment.toString().equals("null")) {
            showSendMessage()
        } else {
            hideSendMessage()
        }
    }

    protected fun hideSendMessage() {
        lay_action.display(lay_attachment)
    }

    protected fun showSendMessage() {
        lay_action.display(img_send)
    }

    protected open fun startTrueCropActivity(uri: Uri?) {
        CropImage.activity(uri)
//                .setCropShape(CropImageView.CropShape.RECTANGLE)
                .start(this)
    }

}