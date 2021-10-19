package com.gamatechno.chato.sdk.app.grouproomdetail

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.Menu
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.CompoundButton
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gamatechno.chato.sdk.R
import com.gamatechno.chato.sdk.app.chatroom.ChatRoomActivity
import com.gamatechno.chato.sdk.app.chatroom.model.ChatRoomUiModel
import com.gamatechno.chato.sdk.app.grouproomadd.addgroup.addmember.AddMemberDialog
import com.gamatechno.chato.sdk.app.grouproomdetail.dialog.GroupActionDialog
import com.gamatechno.chato.sdk.app.grouproomdetail.fragment.info.GroupInfoFragmentPresenter
import com.gamatechno.chato.sdk.app.grouproomdetail.fragment.info.GroupInfoFragmentView
import com.gamatechno.chato.sdk.app.grouproomdetail.viewmodel.GroupInfoViewModel
import com.gamatechno.chato.sdk.app.kontakchat.KontakAdapter
import com.gamatechno.chato.sdk.app.kontakchat.KontakModel
import com.gamatechno.chato.sdk.app.sharedmedia.fragment.sharedfile.SharedGroupFileFragment
import com.gamatechno.chato.sdk.app.sharedmedia.fragment.sharedimage.SharedGroupMediaFragment
import com.gamatechno.chato.sdk.data.DAO.Group.Group
import com.gamatechno.chato.sdk.data.constant.Preferences
import com.gamatechno.chato.sdk.data.constant.StringConstant
import com.gamatechno.chato.sdk.data.model.UserModel
import com.gamatechno.chato.sdk.module.activity.ChatoPermissionActivity
import com.gamatechno.chato.sdk.module.core.ChatoBaseApplication.Companion.instance
import com.gamatechno.chato.sdk.module.dialogs.DialogImagePicker.DialogImagePicker
import com.gamatechno.chato.sdk.utils.ChatoUtils
import com.gamatechno.chato.sdk.utils.ImageUploader
import com.gamatechno.chato.sdk.utils.Loading
import com.gamatechno.chato.sdk.utils.ViewPagerAdapter
import com.gamatechno.ggfw.Activity.Interfaces.PermissionResultInterface
import com.gamatechno.ggfw.easyphotopicker.DefaultCallback
import com.gamatechno.ggfw.easyphotopicker.EasyImage
import com.gamatechno.ggfw.utils.AlertDialogBuilder
import com.gamatechno.ggfw.utils.GGFWUtil
import com.gamatechno.ggfw_ui.avatarview.AvatarPlaceholder
import com.gamatechno.ggfw_ui.avatarview.loader.PicassoLoader
import com.google.gson.Gson
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.activity_group_info_new.*
import java.io.File

class GroupInfoNewActivity : ChatoPermissionActivity(), GroupInfoView.View, GroupInfoFragmentView.View {
    val REQUEST_STAR_MESSAGES = 200
    var requiredPermissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)

    lateinit var loading: Loading
    lateinit var groupInfoViewModel: GroupInfoViewModel

    var userLoggedin = UserModel()
    var isFirstInit = true
    var group = Group()
    lateinit var infoPresenter: GroupInfoPresenter
    lateinit var actionPresenter: GroupInfoFragmentPresenter
    //lateinit var groupInfoViewModel: GroupInfoViewModel
    lateinit var chatRoomUiModel : ChatRoomUiModel

    lateinit var kontakAdapter: KontakAdapter
    lateinit var kontakAllAdapter: KontakAdapter
    lateinit var listKontak: MutableList<KontakModel>
    lateinit var listAllKontak: MutableList<KontakModel>
    lateinit var mediaFragment : SharedGroupMediaFragment
    lateinit var documentFragment : SharedGroupFileFragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_info_new)

        toolbar.title = "Info Grup"
        toolbar.navigationIcon = resources.getDrawable(R.drawable.ic_chat_back)
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        toolbar.navigationIcon!!.setColorFilter(resources.getColor(R.color.colorMonocrome100), PorterDuff.Mode.SRC_ATOP)

        actionPresenter = GroupInfoFragmentPresenter(this, this)

        initListener()
        initViewModel()

        loading = Loading(context)
        infoPresenter = GroupInfoPresenter(context, this)

        with(intent){
            if(hasExtra("data")){
                chatRoomUiModel = getSerializableExtra("data") as ChatRoomUiModel
                group.room_photo_url = chatRoomUiModel.avatar
                //Log.d("imageView", "imagePrev: " + group.room_photo_url)
                var imageLoader = PicassoLoader()
                var refreshableAvatarPlaceholder = AvatarPlaceholder(chatRoomUiModel.getTitle(), Color.parseColor("#42B6B2"), Color.parseColor("#F9F9FC"))
                imageLoader.loadImage(avatarView, refreshableAvatarPlaceholder, if(group.room_photo_url.equals("")) "" else group.room_photo_url)

                tv_name.setText(chatRoomUiModel.title)
                tv_last_seen.visibility = View.GONE
                userLoggedin = Gson().fromJson(GGFWUtil.getStringFromSP(this@GroupInfoNewActivity, Preferences.USER_LOGIN), UserModel::class.java)
                //Log.e("Check", "user loggedin = "+ userLoggedin.user_name + " "+ userLoggedin.user_id)
                infoPresenter.requestInfoGroup(chatRoomUiModel.room_id, true)

            } else {
                finish()
            }
        }
        initMember()

        initAttachment()
    }

    private fun initViewModel(){
        groupInfoViewModel = ViewModelProviders.of(this).get(GroupInfoViewModel::class.java!!)
        groupInfoViewModel.initUpdatedGroupData().observe(this, Observer {
            group = it!!
            if(it.room_group_type.equals("OPEN")){
                switch_broadcast.isChecked = false
            } else {
                switch_broadcast.isChecked = true
            }
            infoPresenter.updateInfoGroup(it)
        })
    }

    private fun initAttachment() {
        val pagerAdapter = ViewPagerAdapter(supportFragmentManager,
            FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT)

        mediaFragment = SharedGroupMediaFragment()
        documentFragment = SharedGroupFileFragment()

        pagerAdapter.addFragments(mediaFragment, "Media")
        pagerAdapter.addFragments(documentFragment, "Document")

        vpAttachment.adapter = pagerAdapter
        tlAttachment.setupWithViewPager(vpAttachment)
        vpAttachment.setCurrentItem(0, false)
    }

    private fun initListener() {
        ivDoneEditName.setOnClickListener {
            group.room_name = edt_groupname.text.toString()
            showEditName(false)
            infoPresenter.updateInfoGroup(group)
        }
        btnAddMember.setOnClickListener {
            if (group.is_admin==1) {
                AddMemberDialog(context, object : AddMemberDialog.OnAddMember {
                    override fun onAfterAddingMember(
                        dialog: Dialog,
                        list: MutableList<KontakModel>?
                    ) {
                        dialog.dismiss()
                        actionPresenter.addMemberToGroup("" + group.room_id, list!!)
                    }
                })
            }else{
                GGFWUtil.ToastShort(context, resources.getString(R.string.only_admin_restriction_notif))
            }
        }
        btnExitGroup.setOnClickListener {
            showInfoNew(resources.getString(R.string.out_from_group),
                resources.getString(R.string.out_from_group_question),
                false,
                null)
            /*
            AlertDialogBuilder(context, "Apakah Anda yakin ingin keluar dari grup ini?", "Ya", "Tidak", object : AlertDialogBuilder.OnAlertDialog {
                override fun onPositiveButton(dialog: DialogInterface) {
                    if(group != null){
                        actionPresenter!!.exitGroup(group)
                    }
                }

                override fun onNegativeButton(dialog: DialogInterface) {

                }
            })

             */
        }
        tvLainnya.setOnClickListener {
            showDialogAllMember()
        }

        switch_broadcast.setOnCheckedChangeListener(object: CompoundButton.OnCheckedChangeListener{
            override fun onCheckedChanged(p0: CompoundButton?, p1: Boolean) {
                if(p1){
                    tv_keterangansetting.setText("Hanya admin yang dapat mengirimkan pesan")
                } else {
                    tv_keterangansetting.setText("Semua anggota grup dapat mengirimkan pesan")
                }
            }
        })

        switch_broadcast.setOnClickListener({
            AlertDialogBuilder(context, "Apakah Anda yakin ingin mengubah status grup?", "Ya", "Tidak", object : AlertDialogBuilder.OnAlertDialog {
                override fun onPositiveButton(dialog: DialogInterface) {
                    group.room_group_type = if(switch_broadcast.isChecked){
                        "BROADCAST"
                    } else {
                        "OPEN"
                    }

                    groupInfoViewModel.updateUpdatedGroupData(group)
                }

                override fun onNegativeButton(dialog: DialogInterface) {
                    switch_broadcast.isChecked = !switch_broadcast.isChecked
                }
            })
        })
    }

    private fun editGroup() {
        if (llEditName.isShown){
            showEditName(false)
        }else{
            showEditName(true)
        }
    }

    private fun showEditName(isShow: Boolean){
        llEditName.visibility = if (isShow) View.VISIBLE else View.GONE
        tv_name.visibility = if (isShow) View.GONE else View.VISIBLE
        if (isShow){
            ChatoUtils.showKeyboard(context, edt_groupname)
            edt_groupname.setText(tv_name.text.toString())
        }else{
            ChatoUtils.hideSoftKeyboard(context, edt_groupname)
            edt_groupname.setText("")
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_chatroom_group_info, menu)
        return super.onCreateOptionsMenu(menu)
    }

    lateinit var dialogListMember: Dialog

    fun showDialogAllMember() {
        dialogListMember = Dialog(this)
        dialogListMember.setContentView(R.layout.dialog_member_more)
        dialogListMember.window!!.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.colorMonocrome60_80)))
        dialogListMember.window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
        dialogListMember.window!!.attributes.windowAnimations = R.style.DialogAnimationRight
        dialogListMember.setCancelable(true)
        dialogListMember.setCanceledOnTouchOutside(false)

        val btnAdd: CardView = dialogListMember.findViewById(R.id.btnAddMember)
        val rvMember: RecyclerView = dialogListMember.findViewById(R.id.rvMember)

        rvMember.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = kontakAllAdapter
        }

        btnAdd.setOnClickListener {
            if (group.is_admin==1) {
                AddMemberDialog(context, object : AddMemberDialog.OnAddMember {
                    override fun onAfterAddingMember(
                        dialog: Dialog,
                        list: MutableList<KontakModel>?
                    ) {
                        dialog.dismiss()
                        actionPresenter.addMemberToGroup("" + group.room_id, list!!)
                    }
                })
            }
        }
        dialogListMember.show()
    }

    private fun initMember() {
        listKontak = ArrayList()
        listAllKontak = ArrayList()
        val model = Gson().fromJson(GGFWUtil.getStringFromSP(context, Preferences.USER_LOGIN), UserModel::class.java)
        kontakAdapter = KontakAdapter(context, model, listKontak, object : KontakAdapter.OnKontakAdapter{
            override fun onKontakClick(kontakModel: KontakModel?, position: Int) {

                if(kontakModel!!.room_id==0){
                    actionPresenter.createRoomId(kontakModel)
                }else {
                    onCreateRoomId(kontakModel)
                }
            }

            override fun onKontakLongClick(kontakModel: KontakModel?, position: Int) {
                GroupActionDialog(context, userLoggedin.user_id.toString(), group, kontakModel!!, object:
                    GroupActionDialog.OnGroupActionListener {
                    override fun onChat(dialog: Dialog, kontakModel: KontakModel) {
                        if(kontakModel.room_id==0){
                            actionPresenter.createRoomId(kontakModel)
                        }else {
                            onCreateRoomId(kontakModel)
                        }
                    }

                    override fun onDelete(dialog: Dialog, kontakModel: KontakModel) {
                        actionPresenter.removeFromGroup(group.room_id.toString(), kontakModel)
                    }

                    override fun onRemoveFromAdmin(dialog: Dialog, kontakModel: KontakModel) {
                        actionPresenter.updateAdminRole(group.room_id.toString(), kontakModel, 0)
                    }

                    override fun onAddToAdmin(dialog: Dialog, kontakModel: KontakModel) {
                        actionPresenter.updateAdminRole(group.room_id.toString(), kontakModel, 1)
                    }

                })
            }

            override fun onMakeGroup() {

            }
        })

        kontakAllAdapter = KontakAdapter(context,  model, listAllKontak, object : KontakAdapter.OnKontakAdapter{
            override fun onKontakClick(kontakModel: KontakModel?, position: Int) {

                if(kontakModel!!.room_id==0){
                    actionPresenter.createRoomId(kontakModel)
                }else {
                    onCreateRoomId(kontakModel)
                }
            }

            override fun onKontakLongClick(kontakModel: KontakModel?, position: Int) {
                GroupActionDialog(context, userLoggedin.user_id.toString(), group, kontakModel!!, object:
                    GroupActionDialog.OnGroupActionListener {
                    override fun onChat(dialog: Dialog, kontakModel: KontakModel) {
                        if(kontakModel.room_id==0){
                            actionPresenter.createRoomId(kontakModel)
                        }else {
                            onCreateRoomId(kontakModel)
                        }
                    }

                    override fun onDelete(dialog: Dialog, kontakModel: KontakModel) {
                        actionPresenter.removeFromGroup(group.room_id.toString(), kontakModel)
                    }

                    override fun onRemoveFromAdmin(dialog: Dialog, kontakModel: KontakModel) {
                        actionPresenter.updateAdminRole(group.room_id.toString(), kontakModel, 0)
                    }

                    override fun onAddToAdmin(dialog: Dialog, kontakModel: KontakModel) {
                        actionPresenter.updateAdminRole(group.room_id.toString(), kontakModel, 1)
                    }

                })
            }

            override fun onMakeGroup() {

            }
        })

        rvMember.layoutManager = LinearLayoutManager(context)
        rvMember.adapter = kontakAdapter
    }

    override fun onExitGroup(message: String) {
        finish()
    }

    override fun failedToDoSomething(message: String) {
        GGFWUtil.ToastShort(context, message)
    }

    override fun onCreateRoomId(model: KontakModel) {

        val data = ChatRoomUiModel(
            model.user_id.toString(),
            model.user_name,
            model.user_photo,
            model.room_id.toString()
        )
        val intent = Intent(context, ChatRoomActivity::class.java)
        intent.putExtra("data", model)
        startActivity(intent)
    }

    override fun onAddMemberToGroup(list: MutableList<KontakModel>) {
        if (listKontak.size<3) {
            listKontak!!.addAll(list)
        }
        listAllKontak!!.addAll(list)
        notifyListUser()
        setResult(StringConstant.REFRESH_CHAT_HISTORY)
    }

    override fun onRemoveMember(kontakModel: KontakModel) {
        Log.d("GroupInfoFragment","NoM: "+listKontak.size+" Kontak detail: "+kontakModel.user_id)
        /*
        for (kontak in listKontak){
            if(kontak.user_id==kontakModel.user_id){
                listKontak.remove(kontak)
                break
            }
        }

         */
        for (kontak in listAllKontak){
            if(kontak.user_id==kontakModel.user_id){
                listAllKontak.remove(kontak)
                break
            }
        }
        listKontak.clear()
        for (i in 0..2 ) {
            if (i < listAllKontak.size) {
                listKontak.add(listAllKontak[i])
            }
        }
        notifyListUser()
        setResult(StringConstant.REFRESH_CHAT_HISTORY)
    }

    override fun onUpdateAdminRole(kontakModel: KontakModel, is_admin: Int) {
        for (item in listKontak) {
            if(item.user_id == kontakModel.user_id){
                item.is_admin = is_admin
            }
        }
        for (item in listAllKontak) {
            if(item.user_id == kontakModel.user_id){
                item.is_admin = is_admin
            }
        }
        notifyListUser()
        setResult(StringConstant.REFRESH_CHAT_HISTORY)
    }

    override fun onUpdateGroupInfo(group: Group) {

    }

    override fun onRequestInfoGroup(group: Group, isRefresh: Boolean) {
        this.group = group
        tv_name.setText(group.room_name)
        if(group.room_group_type.equals("OPEN")){
            switch_broadcast.isChecked = false
        } else {
            switch_broadcast.isChecked = true
        }

        if(group.is_admin == 1){
            llBoardcastSwitch.visibility = View.VISIBLE
            avatarViewCard.setOnClickListener {

                askCompactPermissions(requiredPermissions, object : PermissionResultInterface {
                    override fun permissionGranted() {

                        DialogImagePicker(context, false, object : DialogImagePicker.OnDialogImagePicker {
                            override fun onCameraClick() {
                                photo_progress.visibility = View.VISIBLE
                                EasyImage.openCamera(this@GroupInfoNewActivity, 0)
                            }

                            override fun onFileManagerClick() {
                                photo_progress.visibility = View.VISIBLE
                                EasyImage.openGallery(this@GroupInfoNewActivity, 0)
                            }

                            override fun onVideoCameraClick() {
                                //                                        EasyImage.openVideo(ChatFragment.this, 0);
                            }
                        })
                    }

                    override fun permissionDenied() {
                        GGFWUtil.ToastShort(context, "Anda perlu memberikan akses terlebih dahulu")
                    }
                })
            }

            if (isFirstInit){
                toolbar.inflateMenu(R.menu.menu_chatroom_group_info)
                toolbar.setOnMenuItemClickListener {
                    when(it.itemId){
                        R.id.action_edit -> editGroup()
                    }
                    true
                }
                isFirstInit = false
            }
        }else {
            llBoardcastSwitch.visibility = View.GONE
        }

        //groupInfoViewModel.updateGroupData(group)

        if(isRefresh){
            for (i in 0..2 ) {
                if (i < group.list_user.size) {
                    listKontak.add(group.list_user[i])
                }
            }
            listAllKontak.addAll(group.list_user)
            kontakAdapter.notifyDataSetChanged()
            kontakAllAdapter.notifyDataSetChanged()
            if (listAllKontak.size<=3){
                tvLainnya.visibility =View.GONE
            }else{
                tvLainnya.visibility =View.VISIBLE
            }
            //groupInfoViewModel.updateRefreshedListUser(group)
        }
    }

    override fun onFailedRequestData(message: String) {
        //do nothing
    }
    override fun onLoading() {
        loading!!.show()
    }

    override fun onHideLoading() {
        loading!!.dismiss()
    }

    override fun onErrorConnection(message: String?) {
        //do nothing
    }

    override fun onAuthFailed(error: String?) {
        //do nothing
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == Activity.RESULT_OK) {
                val resultUri = result.uri

                val bitmap = GGFWUtil.getBitmapFromUri(context, resultUri)

                group.base64_avatar = GGFWUtil.convertToBase64(bitmap!!)
                avatarView.setImageBitmap(bitmap)

                ImageUploader(context, loading, resultUri, false, object: ImageUploader.OnUploadImage{
                    override fun onSuccessUploadImage(url: String) {
                        group.room_photo_url = url
                        Picasso.get()
                            .load((if(group.room_photo_url.equals("")) "" else group.room_photo_url))
                            .placeholder(instance.getChatoPlaceholder())
                            .into(avatarView, object : Callback {
                                override fun onSuccess() {
                                    photo_progress.visibility = View.GONE
                                }

                                override fun onError(e: Exception) {
                                    photo_progress.visibility = View.GONE
                                }
                            })
                        infoPresenter.updateAvatarGroup(group)

                    }

                    override fun onFailedUploadImage(message: String) {
                        photo_progress.visibility = View.GONE
                    }
                })

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error.toString()
                GGFWUtil.ToastShort(context, "" + error)
                photo_progress.visibility = View.GONE
            }
        } else if(requestCode==REQUEST_STAR_MESSAGES){
            setResult(resultCode)
            infoPresenter.requestInfoGroup(chatRoomUiModel.room_id, true)
        } else {
            EasyImage.handleActivityResult(requestCode, resultCode, data, this@GroupInfoNewActivity, object : DefaultCallback() {
                override fun onImagePickerError(e: Exception, source: EasyImage.ImageSource, type: Int) {
                    e.printStackTrace()
                    photo_progress.visibility = View.GONE
                }

                override fun onImagesPicked(imageFiles: List<File>, source: EasyImage.ImageSource, type: Int) {
//                    Log.d(TAG, "onImagesPicked: $type")
                    startCropActivity(Uri.fromFile(imageFiles[0]))
                }

                override fun onCanceled(source: EasyImage.ImageSource, type: Int) {
                    //Cancel handling, you might wanna remove taken photo if it was canceled
                    if (source == EasyImage.ImageSource.CAMERA) {
                        val photoFile = EasyImage.lastlyTakenButCanceledPhoto(context)
                        photoFile?.delete()
                    }
                    photo_progress.visibility = View.GONE
                }
            })
        }
    }

    private fun notifyListUser(){
        //tv_count_user.setText(""+listKontak.size)
        kontakAdapter.notifyDataSetChanged()
        kontakAllAdapter.notifyDataSetChanged()

        if (listAllKontak.size> 3){
            tvLainnya.visibility = View.VISIBLE
        }else{
            tvLainnya.visibility = View.GONE
        }
    }

    fun showInfoNew(title: String, msg: String, hideHeader: Boolean, intent: Intent?) {
        val customDialog = Dialog(this)
        customDialog.setContentView(R.layout.dialog_yes_no)
        customDialog.window!!.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.colorMonocrome60_80)))
        customDialog.window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
        customDialog.setCancelable(true)
        customDialog.setCanceledOnTouchOutside(true)

        val btnCancel: Button = customDialog.findViewById(R.id.btnCancel);
        val btnYes: Button = customDialog.findViewById(R.id.btnYes);
        val tvMsg: TextView = customDialog.findViewById(R.id.tvMsg);
        val tvHeader: TextView = customDialog.findViewById(R.id.tvHeader)

        if (hideHeader) {
            tvHeader.visibility = View.GONE
        }
        else{
            tvHeader.visibility = View.VISIBLE
        }

        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tvHeader.text = Html.fromHtml(title, Html.FROM_HTML_MODE_LEGACY)
            tvMsg.text = Html.fromHtml(msg, Html.FROM_HTML_MODE_LEGACY)
        } else {
            tvHeader.text = Html.fromHtml(title)
            tvMsg.text = Html.fromHtml(msg)
        }
        btnCancel.setOnClickListener {
            customDialog.dismiss()
        }
        btnYes.setOnClickListener {
            actionPresenter!!.exitGroup(group)
            customDialog.dismiss()
        }

        customDialog.show()
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