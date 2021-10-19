package com.gamatechno.chato.sdk.app.chatroomdetail

import android.Manifest
import android.app.ActivityOptions
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.core.view.ViewCompat
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gamatechno.chato.sdk.R
import com.gamatechno.chato.sdk.app.chatroom.ChatRoomActivity
import com.gamatechno.chato.sdk.app.chatroom.model.ChatRoomUiModel
import com.gamatechno.chato.sdk.app.chatroomdetail.adapter.GroupCommonAdapter
import com.gamatechno.chato.sdk.app.chatroomdetail.model.RoomDetailUiModel
import com.gamatechno.chato.sdk.app.chatrooms.uimodel.ChatRoomsUiModel
import com.gamatechno.chato.sdk.app.photopreview.ImageViewActivity
import com.gamatechno.chato.sdk.app.sharedmedia.fragment.sharedfile.SharedPersonFileFragment
import com.gamatechno.chato.sdk.app.sharedmedia.fragment.sharedimage.SharedPersonMediaFragment
import com.gamatechno.chato.sdk.data.DAO.RoomChat.RoomChat
import com.gamatechno.chato.sdk.module.activity.ChatoPermissionActivity
import com.gamatechno.chato.sdk.utils.ChatoUtils
import com.gamatechno.chato.sdk.utils.Loading
import com.gamatechno.chato.sdk.utils.ViewPagerAdapter
import com.gamatechno.ggfw.Activity.Interfaces.PermissionResultInterface
import com.gamatechno.ggfw.utils.GGFWUtil
import com.gamatechno.ggfw_ui.avatarview.AvatarPlaceholder
import com.gamatechno.ggfw_ui.avatarview.loader.PicassoLoader
import kotlinx.android.synthetic.main.activity_group_info_new.*
import kotlinx.android.synthetic.main.activity_user_room_detail_new.*
import kotlinx.android.synthetic.main.activity_user_room_detail_new.avatarView
import kotlinx.android.synthetic.main.activity_user_room_detail_new.avatarViewCard
import kotlinx.android.synthetic.main.activity_user_room_detail_new.tlAttachment
import kotlinx.android.synthetic.main.activity_user_room_detail_new.toolbar
import kotlinx.android.synthetic.main.activity_user_room_detail_new.tvLainnya
import kotlinx.android.synthetic.main.activity_user_room_detail_new.tv_last_seen
import kotlinx.android.synthetic.main.activity_user_room_detail_new.tv_name
import kotlinx.android.synthetic.main.activity_user_room_detail_new.vpAttachment

class UserRoomDetailNewActivity : ChatoPermissionActivity(), UserRoomDetailView.View {
    lateinit var chatRoomUiModel : ChatRoomUiModel
    var requiredPermissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)


    lateinit var commonGroupAdapter: GroupCommonAdapter
    var commonGroupList: MutableList<RoomChat> = arrayListOf()
    lateinit var commonAllGroupAdapter: GroupCommonAdapter
    var commonAllGroupList: MutableList<RoomChat> = arrayListOf()
    lateinit var loading: Loading

    lateinit var detaiPresenter: UserRoomDetailPresenter
    lateinit var mediaFragment : SharedPersonMediaFragment
    lateinit var documentFragment : SharedPersonFileFragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_room_detail_new)

        loading = Loading(context)
        initRecycler()
        detaiPresenter = UserRoomDetailPresenter(this, this)
        initData()

        toolbar.title = "Info Kontak"
        toolbar.navigationIcon = resources.getDrawable(R.drawable.ic_chat_back)
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        toolbar.navigationIcon!!.setColorFilter(resources.getColor(R.color.colorMonocrome100), PorterDuff.Mode.SRC_ATOP)

        initListener()

        initAttachment()
    }

    private fun initAttachment() {
        val pagerAdapter = ViewPagerAdapter(supportFragmentManager,
            FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT)

        mediaFragment = SharedPersonMediaFragment()
        documentFragment = SharedPersonFileFragment()

        pagerAdapter.addFragments(mediaFragment, "Media")
        pagerAdapter.addFragments(documentFragment, "Document")

        vpAttachment.adapter = pagerAdapter
        tlAttachment.setupWithViewPager(vpAttachment)
        vpAttachment.setCurrentItem(0, false)
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

    private fun initListener() {
        avatarViewCard.setOnClickListener {
            val data = Bundle()
            data.putString("title", "Detail")
            data.putBoolean("isDownload", false)
            data.putString(
                "image",
                if (chatRoomUiModel.avatar == "") chatRoomUiModel.title else chatRoomUiModel.avatar
            )

            if (!ChatoUtils.isPreLolipop()) {
                val options = ActivityOptions.makeSceneTransitionAnimation(
                    this@UserRoomDetailNewActivity,
                    avatarView,
                    ViewCompat.getTransitionName(avatarView)
                )
                startActivity(
                    Intent(context, ImageViewActivity::class.java).putExtras(data),
                    options.toBundle()
                )
            } else {
                startActivity(Intent(context, ImageViewActivity::class.java).putExtras(data))
            }
        }
        tvLainnya.setOnClickListener {
            showDialogAllCommonGroup()
        }
    }

    private fun initData() {
        with(intent){
            if(hasExtra("data")){
                chatRoomUiModel = getSerializableExtra("data") as ChatRoomUiModel
                //Log.d("imageView", "imagePrev: " + group.room_photo_url)
                var imageLoader = PicassoLoader()
                var refreshableAvatarPlaceholder = AvatarPlaceholder(chatRoomUiModel.getTitle(), Color.parseColor("#42B6B2"), Color.parseColor("#F9F9FC"))
                imageLoader.loadImage(avatarView, refreshableAvatarPlaceholder, if(chatRoomUiModel.avatar.equals("")) "" else chatRoomUiModel.avatar)

                tv_name.text = chatRoomUiModel.title
                tv_last_seen.visibility = View.GONE
                detaiPresenter.requestRoomDetail(chatRoomUiModel.getRoom_id())

            } else {
                finish()
            }
        }
    }

    private fun initRecycler() {
        commonGroupList = ArrayList()
        commonGroupAdapter = GroupCommonAdapter(commonGroupList, object: GroupCommonAdapter.OnActionItem{
            override fun onClickItem(roomChat: RoomChat?) {

                    val model = ChatRoomsUiModel(roomChat)

                    val intent = Intent(context, ChatRoomActivity::class.java)
                    intent.putExtra("chatroom", model)
                    startActivity(intent)


                }
        })
        commonAllGroupList = ArrayList()
        commonAllGroupAdapter = GroupCommonAdapter(commonAllGroupList, object: GroupCommonAdapter.OnActionItem{
            override fun onClickItem(roomChat: RoomChat?) {

                    val model = ChatRoomsUiModel(roomChat)

                    val intent = Intent(context, ChatRoomActivity::class.java)
                    intent.putExtra("chatroom", model)
                    startActivity(intent)

                }
        })

        rvGroup.layoutManager = LinearLayoutManager(context)
        rvGroup.adapter = commonGroupAdapter
    }

    lateinit var dialogListCommonGroup: Dialog

    fun showDialogAllCommonGroup() {
        dialogListCommonGroup = Dialog(this)
        dialogListCommonGroup.setContentView(R.layout.dialog_common_group_more)
        dialogListCommonGroup.window!!.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.colorMonocrome60_80)))
        dialogListCommonGroup.window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
        dialogListCommonGroup.window!!.attributes.windowAnimations = R.style.DialogAnimationRight
        dialogListCommonGroup.setCancelable(true)
        dialogListCommonGroup.setCanceledOnTouchOutside(false)

        val rvCommGroup: RecyclerView = dialogListCommonGroup.findViewById(R.id.rvCommGroup)

        rvCommGroup.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = commonAllGroupAdapter
        }

        dialogListCommonGroup.show()
    }

    override fun onLoading() {
        loading.show()
    }

    override fun onHideLoading() {
        loading.dismiss()
    }

    override fun onErrorConnection(message: String?) {
        GGFWUtil.ToastShort(getContext(), message)
    }

    override fun onAuthFailed(error: String?) {
        // do nothing
    }

    override fun onRequestRoomDetail(model: RoomDetailUiModel?) {
        commonGroupList.clear()
        for (i in 0..2 ) {
            if (i < model!!.common_group.size) {
                commonGroupList.add(model.common_group[i])
            }
        }
        commonGroupAdapter.notifyDataSetChanged()

        commonAllGroupList.clear()
        commonAllGroupList.addAll(model!!.common_group)
        commonAllGroupAdapter.notifyDataSetChanged()

        if (commonAllGroupList.size<=3){
            tvLainnya.visibility =View.GONE
        }else{
            tvLainnya.visibility =View.VISIBLE
        }
    }
}