package com.gamatechno.chato.sdk.app.broadcastdirect.roombroadcast

import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView
import com.gamatechno.chato.sdk.R
import com.gamatechno.chato.sdk.app.broadcastdirect.detailbroadcastmember.DialogDetailBroadcast
import com.gamatechno.chato.sdk.app.chatroom.adapter.ChatRoomAdapter
import com.gamatechno.chato.sdk.app.chatroom.adapter.ChatRoomAdapter.OnChatRoomClick
import com.gamatechno.chato.sdk.app.chatroom.model.FileModel
import com.gamatechno.chato.sdk.app.kontakchat.ListKontakModel
import com.gamatechno.chato.sdk.app.photopreview.ImageViewActivity
import com.gamatechno.chato.sdk.app.playvideo.PlayVideoActivity
import com.gamatechno.chato.sdk.app.playvideo.sendvideopreview.VideoPreviewActivity
import com.gamatechno.chato.sdk.data.DAO.Chat.Chat
import com.gamatechno.chato.sdk.data.DAO.RoomChat.RoomChat
import com.gamatechno.chato.sdk.data.constant.StringConstant
import com.gamatechno.chato.sdk.utils.ChatUtils.EndlessRecyclerViewScrollListener
import com.gamatechno.chato.sdk.utils.ChatUtils.SpeedyLinearLayoutManager
import com.gamatechno.chato.sdk.utils.ChatoUtils
import com.gamatechno.chato.sdk.utils.FilePath.FilePath
import com.gamatechno.chato.sdk.utils.FilePath.IOUtils
import com.gamatechno.ggfw.Activity.Interfaces.PermissionResultInterface
import com.gamatechno.ggfw.easyphotopicker.Constants
import com.gamatechno.ggfw.easyphotopicker.DefaultCallback
import com.gamatechno.ggfw.easyphotopicker.EasyImage
import com.gamatechno.ggfw.easyphotopicker.EasyImage.ImageSource
import com.gamatechno.ggfw.utils.GGFWUtil
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.activity_chat_room.*
import kotlinx.android.synthetic.main.toolbar_chatroom.*
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class RoomBroadcastActivity : BaseRoomBroadcastActivity(), View.OnClickListener, RoomBroadcastView.View {

    val TAG = RoomBroadcastActivity::class.java.name
    var file_attachment = ""
    var name_attachment = ""
    var fileModel: FileModel? = null
    var thumb_file_attachment: Bitmap? = null

    var bitmap_image: Bitmap? = null
    var stream_attachment: InputStream? = null

    var uri_attachment: Uri? = Uri.parse("null")
    var isAppBarShow = false

    var layoutManager: SpeedyLinearLayoutManager? = null
    var endlessRecyclerViewScrollListener: EndlessRecyclerViewScrollListener? = null

    var chatList: MutableList<Chat>? = null
    var adapter: ChatRoomAdapter? = null

    var presenter: RoomBroadcastPresenter? = null
    var detail_broadcast: DialogDetailBroadcast? = null

    lateinit var listKontakModel: ListKontakModel

    private val REQUEST_CODE_ATTACHMENT = 212
    private val REQUEST_GROUP_INFO = 100
    private val PREPARE_SEND_VIDEO = 300

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        chatList = ArrayList()
        presenter = RoomBroadcastPresenter(context, this)
        listKontakModel = intent.getSerializableExtra("data") as ListKontakModel
        tv_statusbar.visibility = View.VISIBLE
        tv_statusbar.setText(getNameRooms(listKontakModel))
        setupRecyclerView()

        edt_message.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                onTextChangeInput(charSequence.toString())
            }

            override fun afterTextChanged(editable: Editable) {}
        })

        setToolbar()

        img_camera.setOnClickListener(this)
        img_send.setOnClickListener(this)
        card_bar.setOnClickListener(this)
        img_attach.setOnClickListener(this)
        img_attachment_close.setOnClickListener(this)
        lay_gallery.setOnClickListener(this)
        lay_record_video.setOnClickListener(this)
        lay_document.setOnClickListener(this)
        lay_detail_room.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        val id: Int = p0!!.getId()
        if (id == R.id.img_camera) {
            askCompactPermissions(RequiredPermissions, object : PermissionResultInterface {
                override fun permissionGranted() {
                    EasyImage.openCamera(this@RoomBroadcastActivity, 0)
                }

                override fun permissionDenied() {
                    GGFWUtil.ToastShort(context, "Anda perlu memberikan akses terlebih dahulu")
                }
            })
        } else if (id == R.id.img_attachment_close) {
            onShowAttachment(null, TYPE_IMAGE, "")
        }   else if (id == R.id.img_send) {
            prepareToSendMessage()
        }   else if (id == R.id.card_bar) {
            finish()
        } else if (id == R.id.lay_detail_room) {
            initDialogDetailBroadcast()
        } else if (id == R.id.img_attach) {
            if (lay_menu_attach.visibility == View.VISIBLE) {
                lay_menu_attach.visibility = View.GONE
                edt_message.requestFocus()
            } else {
                lay_menu_attach.visibility = View.VISIBLE
                ChatoUtils.hideSoftKeyboard(this, edt_message)
            }
        } else if (id == R.id.lay_document) {
            askCompactPermissions(RequiredPermissions, object : PermissionResultInterface {
                override fun permissionGranted() {
                    val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
                    intent.putExtra(Intent.EXTRA_MIME_TYPES, StringConstant.mimeTypesFile)
                    intent.type = "*/*"
                    intent.addCategory(Intent.CATEGORY_OPENABLE)
                    startActivityForResult(intent, REQUEST_CODE_ATTACHMENT)
                }

                override fun permissionDenied() {
                    GGFWUtil.ToastShort(context, "Anda perlu memberikan akses terlebih dahulu")
                }
            })
        } else if (id == R.id.lay_gallery) {
            askCompactPermissions(RequiredPermissions, object : PermissionResultInterface {
                override fun permissionGranted() {
                    EasyImage.openGalleryVideo(this@RoomBroadcastActivity, 0)
                }

                override fun permissionDenied() {
                    GGFWUtil.ToastShort(context, "Anda perlu memberikan akses terlebih dahulu")
                }
            })
        } else if (id == R.id.lay_record_video) {
            askCompactPermissions(RequiredPermissions, object : PermissionResultInterface {
                override fun permissionGranted() {
                    EasyImage.openVideo(this@RoomBroadcastActivity, 0)
                }

                override fun permissionDenied() {
                    GGFWUtil.ToastShort(context, "Anda perlu memberikan akses terlebih dahulu")
                }
            })
        } else if (id == R.id.img_camera) {
            askCompactPermissions(RequiredPermissions, object : PermissionResultInterface {
                override fun permissionGranted() {
                    EasyImage.openCamera(this@RoomBroadcastActivity, 0)
                    //                        new DialogImagePicker(getContext(), true, new DialogImagePicker.OnDialogImagePicker() {
//                            @Override
//                            public void onCameraClick() {
////                                EasyImage.openCamera(ChatRoomActivity.this, 0);
//                                EasyImage.openCamera(ChatRoomActivity.this, 0);
//                            }
//
//                            @Override
//                            public void onFileManagerClick() {
//                                EasyImage.openGalleryVideo(ChatRoomActivity.this, 0);
//                            }
//
//                            @Override
//                            public void onVideoCameraClick() {
//                                EasyImage.openVideo(ChatRoomActivity.this, 0);
//                            }
//                        });
                }

                override fun permissionDenied() {
                    GGFWUtil.ToastShort(context, "Anda perlu memberikan akses terlebih dahulu")
                }
            })
        } else if (id == R.id.img_attachment_close) {
            onShowAttachment(null, TYPE_IMAGE, "")
        }
    }

    private fun onShowAttachment(o: Any?, type: Int, name: String) {
        TYPE_ATTACHMENT = type
        if (o == null) {
            emptyAttachment()
            if (type != TYPE_MESSAGE) {
                lay_action_attachment!!.hide()
                TYPE_ATTACHMENT = TYPE_MESSAGE
            }
            onTextChangeInput(uri_attachment!!)
            file_attachment = ""
        } else {
            tv_attachment!!.setText(name)
            when (type) {
                TYPE_IMAGE -> {
                    img_attachment!!.setImageBitmap(o as Bitmap?)
                    file_attachment = GGFWUtil.convertToBase64(o as Bitmap?)
                }
                TYPE_FILE -> {
                    img_attachment!!.setImageResource(R.drawable.ic_arsip)
                    file_attachment = IOUtils.convertToBase64(o as InputStream?)
                }
            }
            Log.d(TAG, "onShowAttachment: $file_attachment")
            lay_action_attachment!!.show()
            onTextChangeInput(uri_attachment!!)
        }
    }

    private fun emptyAttachment() {
        name_attachment = ""
        uri_attachment = Uri.parse("null")
    }

    private fun prepareToSendMessage() {
        var ch: Chat? = null
        when (TYPE_ATTACHMENT) {
            TYPE_MESSAGE -> {
                ch = Chat(ChatoUtils.getUserLogin(context).user_id, 0, edt_message.text.toString(), Chat.chat_type_message, "", StringConstant.chat_status_sending, "" + chatList!!.size, "", "")
                onShowAttachment(null, TYPE_MESSAGE, "")
            }
            TYPE_IMAGE -> {
                ch = Chat(ChatoUtils.getUserLogin(context).user_id, 0, edt_message.text.toString(), Chat.chat_type_image, file_attachment, StringConstant.chat_status_sending, "" + chatList!!.size, fileModel, bitmap_image, "", "")
                onShowAttachment(null, TYPE_IMAGE, "")
            }
            TYPE_FILE -> {
                ch = Chat(ChatoUtils.getUserLogin(context).user_id, 0, edt_message.text.toString(), Chat.chat_type_file, file_attachment, StringConstant.chat_status_sending, "" + chatList!!.size, fileModel, "", "")
                onShowAttachment(null, TYPE_IMAGE, "")
                ch.uri_attachment = fileModel!!.uri.toString()
            }
            VIDEO_FILE -> {
                ch = Chat(ChatoUtils.getUserLogin(context).user_id, 0, edt_message.text.toString(), Chat.chat_type_video, file_attachment, StringConstant.chat_status_sending, "" + chatList!!.size, fileModel, "", GGFWUtil.convertToBase64(thumb_file_attachment), "", "")
                ch!!.isVideoDownloding = true
                ch.uri_attachment = fileModel!!.uri.toString()
                ch.message_attachment_name = fileModel!!.namefile
                onShowAttachment(null, TYPE_IMAGE, "")
            }
        }
        sendMessage(ch!!)
        if (lay_menu_attach.visibility == View.VISIBLE) {
            lay_menu_attach.visibility = View.GONE
        }
    }

    private fun sendMessage(ch: Chat) {
        ch.room = listKontakModel!!.contacts
        presenter!!.sendMessage(ch)
        updateDataChat(ch)
    }

    private fun updateDataChat(ch: Chat) {
        chatList!!.add(ch)
        adapter!!.notifyDataSetChanged()
        rv.scrollToPosition(chatList!!.size - 1)
        sterilizeChat()
        edt_message.setText("")

        Log.d(TAG, ""+chatList!!.size)
    }

    private fun sterilizeChat() {
        for (i in chatList!!.indices) {
            chatList!![i].isClicked = false
        }
        adapter!!.notifiyListChanged()
        presenter!!.checkSelectedChat(chatList!!)
        if (edt_search.text.toString() != "") {
            appbar_action.show()
            isAppBarShow = true
            toolbar.visibility = View.GONE
            appbar_action.displaying(lay_searchbar)
        }
    }

    override fun onSendMessage(chat: Chat?) {
        for (i in chatList!!.indices) {
            if (chat!!.payload == chatList!![i].payload) {
                chatList!![i] = chat
                adapter!!.notifiyListChanged()
            }
        }
    }

    override fun onFailedSendMessage(chat: Chat?, message: String?) {
        for (i in chatList!!.indices) {
            if (chat!!.payload == chatList!![i].payload) {
                chatList!![i] = chat
                adapter!!.notifiyListChanged()
            }
        }
    }

    private fun onActionBarBack() {
        if (lay_searchbar.visibility == View.VISIBLE) {
            edt_search.setText("")
            appbar_action.hide(lay_searchbar)
            appbar_action.hide()
            lay_action_chat.visibility = View.VISIBLE
            toolbar.visibility = View.VISIBLE
            ChatoUtils.hideSoftKeyboard(context, edt_search)
        } else {
            finish()
        }
    }

    private fun getNameRooms(kontaks: ListKontakModel): String{
        var x = "";
        for (i in kontaks!!.contacts.indices) {
            x += (if (kontaks!!.contacts.get(i).room_type.equals(RoomChat.user_room_type)) kontaks!!.contacts.get(i).user_name else kontaks!!.contacts.get(i).group_name) + (if(i < kontaks!!.contacts.size-1) ", " else "")
        }
        return x
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == Activity.RESULT_OK) {
                val resultUri = result.uri
                handleCropResult(resultUri)
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error.toString()
                GGFWUtil.ToastShort(context, "" + error)
            }
        } else if (requestCode == REQUEST_CODE_ATTACHMENT) {
            try {
                val resultUri = data!!.data
                handleFileResult(resultUri)
            } catch (e: NullPointerException) {
            }
        } else if (requestCode == Constants.RequestCodes.TAKE_VIDEO) {
            Log.d(TAG, "herevideo: hehehe")
            if (data != null) {
                startActivityForResult(Intent(this, VideoPreviewActivity::class.java).putExtra("uri", FilePath.uriFileOrOther(context, data.data)), PREPARE_SEND_VIDEO)
            }
            lay_menu_attach.visibility = View.GONE
        } else if(requestCode == PREPARE_SEND_VIDEO) {
            TYPE_ATTACHMENT = VIDEO_FILE
            if (resultCode == 100 && data != null) {
                edt_message.setText(data.getStringExtra("text"))
                file_attachment = IOUtils.convertToBase64(IOUtils.getInputStream(context, data.data))
                fileModel = FileModel(data.data!!.toString(), FilePath.getMimeType(context, data.data!!), data.getStringExtra("videoName"))
//                Log.d(TAG, "onPrepareVideoToSend: " + videoPreview.getUri() + " - " + videoPreview.getRealUri().getPath());
//                thumb_file_attachment = ThumbnailUtils.createVideoThumbnail(data.data!!.path, MediaStore.Video.Thumbnails.MINI_KIND)
                Log.d(TAG, "onPrepareVideoToSend: $thumb_file_attachment")
                prepareToSendMessage()
            }
        } else {
            EasyImage.handleActivityResult(requestCode, resultCode, data, this, object : DefaultCallback() {
                override fun onImagePickerError(e: Exception, source: ImageSource, type: Int) {
                    e.printStackTrace()
                }

                override fun onImagesPicked(imageFiles: List<File>, source: ImageSource, type: Int) {
                    val mimetype = FilePath.getMimeType(context, Uri.fromFile(imageFiles[0]))
                    Log.d(TAG, "onImagesPicked: $mimetype")
                    if (mimetype == "image/jpeg" || mimetype == "image/png") {
                        name_attachment = FilePath.getFileName(context, Uri.fromFile(imageFiles[0]))
                        startTrueCropActivity(Uri.fromFile(imageFiles[0]))
                    } else {
                        startActivityForResult(Intent(context!!, VideoPreviewActivity::class.java).putExtra("uri", FilePath.uriFileOrOther(context, data!!.data)), PREPARE_SEND_VIDEO)
                    }
                }

                override fun onCanceled(source: ImageSource, type: Int) {
                    //Cancel handling, you might wanna remove taken photo if it was canceled
                    if (source == ImageSource.CAMERA) {
                        val photoFile = EasyImage.lastlyTakenButCanceledPhoto(context)
                        photoFile?.delete()
                    }
                }
            })
        }
        if (requestCode == REQUEST_GROUP_INFO) {
            if (resultCode == 99) {
                listKontakModel = data!!.getSerializableExtra("data") as ListKontakModel
                Log.d("RoomBroadcastActivity", "data receive: " + listKontakModel)
                tv_statusbar.setText(getNameRooms(listKontakModel))
                detail_broadcast!!.updateList(listKontakModel)
//                finish()
            }
        }
    }

    fun handleCropResult(uri: Uri?) {
        if (uri != null) {
            try {
                Log.d(TAG, "handleCropResult: $uri")
                bitmap_image = MediaStore.Images.Media.getBitmap(contentResolver, uri)
                uri_attachment = uri
                fileModel = FileModel(uri.toString(), FilePath.getMimeType(context, uri))
                onShowAttachment(bitmap_image, TYPE_IMAGE, name_attachment)
                if (lay_menu_attach.visibility == View.VISIBLE) {
                    lay_menu_attach.visibility = View.GONE
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        } else {
            GGFWUtil.ToastShort(context, "Cannot Retrieve Cropped Image")
        }
    }

    fun handleFileResult(uri: Uri?) {
        if (uri != null) {
            uri_attachment = uri
            Log.d(TAG, "handleFileResult: " + FilePath.getMimeType(context, uri))
            name_attachment = FilePath.getFileName(context, uri)
            stream_attachment = IOUtils.getInputStream(context, uri)
            fileModel = FileModel(uri.toString(), FilePath.getMimeType(context, uri))
            onShowAttachment(stream_attachment, TYPE_FILE, name_attachment)
            if (lay_menu_attach.visibility == View.VISIBLE) {
                lay_menu_attach.visibility = View.GONE
                //                    edt_message.requestFocus();
            }
        }
    }

    private fun setupRecyclerView() {
        layoutManager = SpeedyLinearLayoutManager(this, SpeedyLinearLayoutManager.VERTICAL, false)
        layoutManager!!.stackFromEnd = true
        rv.layoutManager = layoutManager
        chatList = ArrayList()
        adapter = ChatRoomAdapter(context, chatList, object : OnChatRoomClick {
            override fun onLongPress(view: View, position: Int) {
                chatList!!.get(position).isClicked = true
                adapter!!.notifyDataSetChanged()
                presenter!!.checkSelectedChat(chatList!!)
            }

            override fun onClickItemView(view: View, position: Int) {
                if (chatList!!.get(position).isClicked) {
                    chatList!!.get(position).isClicked = false
                    adapter!!.notifyDataSetChanged()
                    presenter!!.checkSelectedChat(chatList!!)
                } else {
                    if (adapter!!.checkIsListClicked()) {
                        chatList!!.get(position).isClicked = true
                        adapter!!.notifyDataSetChanged()
                        presenter!!.checkSelectedChat(chatList!!)
                    }
                }
            }

            override fun onClickRepliedMessage(chat: Chat) {

            }

            override fun onClickAttachment(chat: Chat, uri: Uri) {
                try {
                    IOUtils.openFile(context, File(uri.toString().replace("file:/", "")))
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }

            override fun onDownloadingAttachment(isdownload: Boolean, position: Int) {
                askCompactPermissions(RequiredPermissions, object : PermissionResultInterface {
                    override fun permissionGranted() {
                        chatList!!.get(position).isVideoDownloding = isdownload
                        adapter!!.notifiyListChanged()
                    }

                    override fun permissionDenied() {
                        GGFWUtil.ToastShort(context, "Anda perlu memberikan akses terlebih dahulu")
                    }
                })
            }

            override fun onOpenVideo(view: View, chat: Chat, uri: Uri) {
                val data = Bundle()
                data.putSerializable("chat", chat)
                data.putString("uri", uri.toString())
                if (!ChatoUtils.isPreLolipop()) {
                    val options = ActivityOptions.makeSceneTransitionAnimation(this@RoomBroadcastActivity, view, ViewCompat.getTransitionName(view))
                    startActivity(Intent(context, PlayVideoActivity::class.java).putExtras(data), options.toBundle())
                } else {
                    startActivity(Intent(context, PlayVideoActivity::class.java).putExtras(data))
                }
            }

            override fun onPlayAudio(chat: Chat?, position: Int) {

            }

            override fun onImageClick(view: View, chat: Chat) {
                val data = Bundle()
                try {
                    data.putString("title", chat.from_username)
                    val dateTime = chat.message_date + " " + chat.message_time
                    val date = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).parse(dateTime)
                    data.putString("subtitle", SimpleDateFormat("dd-MMMM-yyyy, HH:mm", Locale.getDefault()).format(date))
                    data.putString("image", chat.message_attachment)
                    if (!ChatoUtils.isPreLolipop()) {
                        val options = ActivityOptions.makeSceneTransitionAnimation(this@RoomBroadcastActivity, view, ViewCompat.getTransitionName(view))
                        startActivity(Intent(context, ImageViewActivity::class.java).putExtras(data), options.toBundle())
                    } else {
                        startActivity(Intent(context, ImageViewActivity::class.java).putExtras(data))
                    }
                } catch (e: ParseException) {
                    e.printStackTrace()
                }
            }

            override fun onReadMessage(chat: Chat?) {

            }
        }, false)
        endlessRecyclerViewScrollListener = object : EndlessRecyclerViewScrollListener(layoutManager) {
            override fun onLoadMore(var1: Int, var2: Int, var3: RecyclerView) {

            }
        }
        val fabScrollListener: RecyclerView.OnScrollListener = object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (!rv.canScrollVertically(1)) {
                    fab_down.hide()
                } else {
                    if (dy < 0 && fab_down.visibility == View.GONE) {
                        fab_down.show()
                    }
                }
            }
        }
        rv.addOnScrollListener(endlessRecyclerViewScrollListener!!)
        rv.addOnScrollListener(fabScrollListener)
        rv.itemAnimator = DefaultItemAnimator()
        rv.adapter = adapter
        fab_down.setOnClickListener {
            rv.scrollToPosition(chatList!!.size - 1)
            fab_down.hide()
        }
    }

    private fun initDialogDetailBroadcast(){
        try {
            detail_broadcast!!.updateList(listKontakModel)
            detail_broadcast!!.show()
        } catch (e : KotlinNullPointerException){
            detail_broadcast = DialogDetailBroadcast(context,this@RoomBroadcastActivity,  listKontakModel!!)
        }
    }
}
