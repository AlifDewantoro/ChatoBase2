package com.gamatechno.chato.sdk.app.grouproomdetail

import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.gamatechno.chato.sdk.R
import com.gamatechno.chato.sdk.app.chatroom.BaseChatRoomActivity
import com.gamatechno.chato.sdk.app.chatroom.model.ChatRoomUiModel
import com.gamatechno.chato.sdk.app.grouproomdetail.fragment.info.GroupInfoFragment
import com.gamatechno.chato.sdk.app.grouproomdetail.fragment.listadmin.ListAdminFragment
import com.gamatechno.chato.sdk.app.grouproomdetail.fragment.setting.GroupSettingFragment
import com.gamatechno.chato.sdk.app.grouproomdetail.viewmodel.GroupInfoViewModel
import com.gamatechno.chato.sdk.app.photopreview.ImageViewActivity
import com.gamatechno.chato.sdk.data.DAO.Group.Group
import com.gamatechno.chato.sdk.module.core.ChatoBaseApplication.Companion.instance
import com.gamatechno.chato.sdk.module.dialogs.DialogImagePicker.DialogImagePicker
import com.gamatechno.chato.sdk.utils.ChatoUtils
import com.gamatechno.chato.sdk.utils.ImageUploader
import com.gamatechno.chato.sdk.utils.Loading
import com.gamatechno.ggfw.Activity.Interfaces.PermissionResultInterface
import com.gamatechno.ggfw.easyphotopicker.DefaultCallback
import com.gamatechno.ggfw.easyphotopicker.EasyImage
import com.gamatechno.ggfw.utils.GGFWUtil
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.activity_group_info.*
import java.io.File

class GroupInfoActivity : BaseChatRoomActivity(), GroupInfoView.View {

    val REQUEST_STAR_MESSAGES = 200

    lateinit var groupInfoViewModel: GroupInfoViewModel

    lateinit var groupInfoFragment: GroupInfoFragment
    lateinit var groupSettingFragment: GroupSettingFragment
    lateinit var listAdminFragment: ListAdminFragment

    var group = Group()
    lateinit var presenter: GroupInfoPresenter
    lateinit var chatRoomUiModel : ChatRoomUiModel

    internal var mFragmentManager = supportFragmentManager

    lateinit var loading: Loading

    companion object{
        var tag_groupinfo = 1
        var tag_groupsetting = 2
        var tag_groupadmin = 3
    }

    var tag_recent = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_info)

        setSupportActionBar(toolbar)
//        supportActionBar!!.setTitle("Info Grup")
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        with(intent){
            if(hasExtra("data")){
                chatRoomUiModel = getSerializableExtra("data") as ChatRoomUiModel
                group.room_photo_url = chatRoomUiModel.avatar
//                Log.d("imageView", "imagePrev: " + group.room_photo_url)
                Picasso.get()
                        .load((if(group.room_photo_url.equals("")) "" else group.room_photo_url))
                        .placeholder(instance.getChatoPlaceholder())
                        .into(avatar_header)
            } else {
                finish()
            }
        }

        loading = Loading(context)
        presenter = GroupInfoPresenter(context, this)

        setupViewModel()
        initFragments()

        presenter.requestInfoGroup(chatRoomUiModel.room_id, true)

        with(avatar_header){
            setOnClickListener {
                val data = Bundle()
                data.putString("title", "Detail")
                data.putBoolean("isDownload", false)
                data.putString("image", chatRoomUiModel.avatar)

                if (!ChatoUtils.isPreLolipop()) {
                    val options = ActivityOptions.makeSceneTransitionAnimation(this@GroupInfoActivity, this, ViewCompat.getTransitionName(this))
                    startActivity(Intent(context, ImageViewActivity::class.java).putExtras(data), options.toBundle())
                } else {
                    startActivity(Intent(context, ImageViewActivity::class.java).putExtras(data))
                }
            }
        }

        img_camera.setOnClickListener({
            askCompactPermissions(RequiredPermissions, object : PermissionResultInterface {
                override fun permissionGranted() {

                    DialogImagePicker(context, false, object : DialogImagePicker.OnDialogImagePicker {
                        override fun onCameraClick() {
                            photo_progress.visibility = View.VISIBLE
                            EasyImage.openCamera(this@GroupInfoActivity, 0)
                        }

                        override fun onFileManagerClick() {
                            photo_progress.visibility = View.VISIBLE
                            EasyImage.openGallery(this@GroupInfoActivity, 0)
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
        })
    }

    private fun setupViewModel(){
        groupInfoViewModel = ViewModelProviders.of(this).get(GroupInfoViewModel::class.java!!)
        groupInfoViewModel.initPageController().observe(this, Observer {
            tag_recent = it!!
            when(tag_recent){
                tag_groupsetting -> {
                    reinitializeFragment(groupSettingFragment)
                }
                tag_groupadmin -> {
                    reinitializeFragment(listAdminFragment)
                }
                tag_groupinfo -> {
                    reinitializeFragment(groupInfoFragment)
                }
            }
        })
        groupInfoViewModel.initLeaveGroup().observe(this, Observer {
            if(it == true){

            }
        })
        groupInfoViewModel.initUpdatedGroupData().observe(this, Observer {
            presenter.updateInfoGroup(it!!)
        })

    }

    private  fun initFragments(){
        groupInfoFragment = GroupInfoFragment.newInstance(groupInfoViewModel, chatRoomUiModel)
        groupSettingFragment = GroupSettingFragment.newInstance(groupInfoViewModel, chatRoomUiModel)
        listAdminFragment = ListAdminFragment.newInstance(groupInfoViewModel, chatRoomUiModel)

        reinitializeFragment(groupInfoFragment)
    }

    private fun reinitializeFragment(curFragment: Fragment){
        val transaction = mFragmentManager.beginTransaction()
        if (mFragmentManager.findFragmentByTag(curFragment.javaClass.getSimpleName()) == null) {
            transaction.add(R.id.framelayout, curFragment, curFragment.javaClass.getSimpleName())
        }

        val tagGroupInfoFragment = mFragmentManager.findFragmentByTag(groupInfoFragment.javaClass.getSimpleName())
        val tagSettingGroupFragment = mFragmentManager.findFragmentByTag(groupSettingFragment.javaClass.getSimpleName())
        val tagListAdminGroupFragment = mFragmentManager.findFragmentByTag(listAdminFragment.javaClass.getSimpleName())

        when(curFragment.javaClass.simpleName){
            groupInfoFragment.javaClass.getSimpleName() -> {
                if (tagGroupInfoFragment != null) {
                    transaction.show(tagGroupInfoFragment)
                }

                if (tagSettingGroupFragment != null) {
                    transaction.hide(tagSettingGroupFragment)
                }

                if (tagListAdminGroupFragment != null) {
                    transaction.hide(tagListAdminGroupFragment)
                }
                appBarLayout.setExpanded(true, true)
                supportActionBar!!.setTitle("Info Grup")
            }
            groupSettingFragment.javaClass.getSimpleName() -> {
                if (tagSettingGroupFragment != null) {
                    transaction.show(tagSettingGroupFragment)
                }

                if (tagGroupInfoFragment != null) {
                    transaction.hide(tagGroupInfoFragment)
                }

                if (tagListAdminGroupFragment != null) {
                    transaction.hide(tagListAdminGroupFragment)
                }
                appBarLayout.setExpanded(false, true)
                supportActionBar!!.setTitle("Setelan Grup")
            }
            listAdminFragment.javaClass.getSimpleName() -> {
                if (tagSettingGroupFragment != null) {
                    transaction.hide(tagSettingGroupFragment)
                }

                if (tagGroupInfoFragment != null) {
                    transaction.hide(tagGroupInfoFragment)
                }

                if (tagListAdminGroupFragment != null) {
                    transaction.show(tagListAdminGroupFragment)
                }
                appBarLayout.setExpanded(false, true)
                supportActionBar!!.setTitle("Daftar Admin")
            }
        }
        transaction.commitAllowingStateLoss()
    }

    override fun onRequestInfoGroup(group: Group, isRefresh: Boolean) {
        this.group = group
        if(group.is_admin == 1){
            img_camera.visibility = View.VISIBLE
        } else {
            img_camera.visibility = View.GONE
        }
        groupInfoViewModel.updateGroupData(group)

        if(isRefresh){
            groupInfoViewModel.updateRefreshedListUser(group)
        }
    }

    override fun onFailedRequestData(message: String) {

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item!!.itemId){
            android.R.id.home -> {
                when(tag_recent){
                    tag_groupsetting -> {
                        reinitializeFragment(groupInfoFragment)
                        tag_recent = tag_groupinfo
                    }
                    tag_groupadmin -> {
                        reinitializeFragment(groupSettingFragment)
                        tag_recent = tag_groupsetting
                    }
                    tag_groupinfo -> {
                        super.onBackPressed()
                    }
                }
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }



    override fun onBackPressed() {
        when(tag_recent){
            tag_groupsetting -> {
                reinitializeFragment(groupInfoFragment)
                tag_recent = tag_groupinfo
            }
            tag_groupadmin -> {
                reinitializeFragment(groupSettingFragment)
                tag_recent = tag_groupsetting
            }
            tag_groupinfo -> {
                super.onBackPressed()
            }
        }
    }

    override fun onLoading() {
        loading!!.show()
    }

    override fun onHideLoading() {
        loading!!.dismiss()
    }

    override fun onErrorConnection(message: String?) {

    }

    override fun onAuthFailed(error: String?) {

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == Activity.RESULT_OK) {
                val resultUri = result.uri

                val bitmap = GGFWUtil.getBitmapFromUri(context, resultUri)

                group.base64_avatar = GGFWUtil.convertToBase64(bitmap!!)
                avatar_header.setImageBitmap(bitmap)

                ImageUploader(context, loading, resultUri, false, object: ImageUploader.OnUploadImage{
                    override fun onSuccessUploadImage(url: String) {
                        group.room_photo_url = url
                        Picasso.get()
                                .load((if(group.room_photo_url.equals("")) "" else group.room_photo_url))
                                .placeholder(instance.getChatoPlaceholder())
                                .into(avatar_header, object : Callback {
                                    override fun onSuccess() {
                                        photo_progress.visibility = View.GONE
                                    }

                                    override fun onError(e: Exception) {
                                        photo_progress.visibility = View.GONE
                                    }
                                })
                        presenter.updateAvatarGroup(group)

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
            presenter.requestInfoGroup(chatRoomUiModel.room_id, true)
        } else {
            EasyImage.handleActivityResult(requestCode, resultCode, data, this@GroupInfoActivity, object : DefaultCallback() {
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
}
