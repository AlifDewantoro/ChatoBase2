package com.gamatechno.chato.sdk.app.main

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.widget.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.gamatechno.chato.sdk.R
import com.gamatechno.chato.sdk.app.chatroom.ChatRoomActivity
import com.gamatechno.chato.sdk.app.chatrooms.ChatRoomsFragmentNew
import com.gamatechno.chato.sdk.app.chatrooms.adapter.RoomAdapter.OnObrolanAdapter
import com.gamatechno.chato.sdk.app.chatrooms.uimodel.ChatRoomsUiModel
import com.gamatechno.chato.sdk.app.grouproomadd.addgroup.addmember.AddMemberDialog
import com.gamatechno.chato.sdk.app.grouproomadd.addgroup.addmember.AddMemberDialog.OnAddMember
import com.gamatechno.chato.sdk.app.grouproomadd.addgroup.detailgroup.AddDetailGroupDialog
import com.gamatechno.chato.sdk.app.grouproomadd.addgroup.detailgroup.AddDetailGroupDialog.OnAddDetailGroup
import com.gamatechno.chato.sdk.app.kontakchat.KontakChatDialog
import com.gamatechno.chato.sdk.app.kontakchat.KontakChatDialog.OnKontakChatDialog
import com.gamatechno.chato.sdk.app.kontakchat.KontakModel
import com.gamatechno.chato.sdk.app.main.searchlist.AdapterSearchList
import com.gamatechno.chato.sdk.app.main.searchlist.SearchChatroomModel
import com.gamatechno.chato.sdk.data.DAO.RoomChat.RoomChat
import com.gamatechno.chato.sdk.data.constant.StringConstant
import com.gamatechno.chato.sdk.module.dialogs.DialogImagePicker.DialogImagePicker
import com.gamatechno.chato.sdk.module.dialogs.DialogImagePicker.DialogImagePicker.OnDialogImagePicker
import com.gamatechno.ggfw.Activity.Interfaces.PermissionResultInterface
import com.gamatechno.ggfw.easyphotopicker.DefaultCallback
import com.gamatechno.ggfw.easyphotopicker.EasyImage
import com.gamatechno.ggfw.easyphotopicker.EasyImage.ImageSource
import com.gamatechno.ggfw.utils.AlertDialogBuilder
import com.gamatechno.ggfw.utils.GGFWUtil
import com.google.android.material.tabs.TabLayout
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.activity_chato_new.*
import kotlinx.android.synthetic.main.layout_helper.*
import java.io.File
import java.util.*

class ChatoFragmentNew(var listener: ChatBadgeView) : ChatRoomsFragmentNew(), ChatView.View, View.OnClickListener, BottomSheetFilterDialog.onFilterSelectedListener {
    var kontakChatDialog: KontakChatDialog? = null
    var presenter: ChatPresenter? = null
    var mSearch: MenuItem? = null
    var mSearchView: SearchView? = null
    var isObrolan = true

    var isPinnedRoom = true

    var list_searchlist: MutableList<SearchChatroomModel> = ArrayList()
    var adapterSearchList: AdapterSearchList? = null

    private val RequiredPermissions = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    var addMemberDialog: AddMemberDialog? = null
    var addDetailGroupDialog: AddDetailGroupDialog? = null

    var imagepicker_code = ""

    fun newInstance(): ChatoFragmentNew {
        return ChatoFragmentNew(object : ChatBadgeView{
            override fun onShowBadgeChat() {
                //TODO("Not yet implemented")
            }

            override fun onHideBadgeChat() {
                //TODO("Not yet implemented")
            }
        })
    }

    private var timer = Timer()
    private val DELAY: Long = 1000 // milliseconds


    private var rootview: View? = null

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated: Tes here")
        //        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
//        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("");
        initSearchChatroom()
        presenter = ChatPresenter(context!!, this)
        wrapper_top.setInOutAnimation(R.anim.pull_in_bottom, R.anim.push_out_top)
        toggle_fab.setInOutAnimation(R.anim.pull_in_top, R.anim.push_out_bottom)

//        setHasOptionsMenu(true)

        toolbar.inflateMenu(R.menu.menu_search)
        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                //R.id.action_add -> initKontakDialog()
                R.id.action_search -> showOrHideTopView(isSHow = false, isSearch = true)
                //R.id.action_filter -> showFilterSheet() //showFilterDialog()
                R.id.action_new_group -> addGroup()
                R.id.action_linked_device -> Toast.makeText(context, ""+resources.getString(R.string.feature_not_available), Toast.LENGTH_LONG).show()
                R.id.action_stared_message -> Toast.makeText(context, ""+resources.getString(R.string.feature_not_available), Toast.LENGTH_LONG).show()
                R.id.action_pinned_message -> Toast.makeText(context, ""+resources.getString(R.string.feature_not_available), Toast.LENGTH_LONG).show()
                R.id.action_setting -> Toast.makeText(context, ""+resources.getString(R.string.feature_not_available), Toast.LENGTH_LONG).show()
            }
            true
        }
//        toggle_fab.setInOutAnimation(R.anim.pull_in_top, R.anim.slide_out_bottom);
        viewModel!!.initBackPressed().observe(
            viewLifecycleOwner,
            Observer { isBackPressed ->
                if (isBackPressed != null && isBackPressed) {
                    if (isSearchh) {
                        viewModel!!.updateKeyword("")
                        showOrHideTopView(true, false)
                    } else {
                        viewModel!!.updateBackPressedUpdate(isSearchh)
                    }
                } else if (isBackPressed != null && !isBackPressed) {
                    showOrHideTopView(true, false)
                }
            })
        viewModel!!.initKeyword()
            .observe(viewLifecycleOwner, Observer { s ->
                if (s == "") {
                    edt_search.setText("")
                    //                    showOrHideTopView(true, true);
                    lay_searchlist.setVisibility(View.GONE)
                    rv.visibility = View.VISIBLE
                    llChatTabs.visibility = View.VISIBLE
                    list_searchlist.clear()
                    adapterSearchList!!.notifyDataSetChanged()
                    helper_nodata.setVisibility(View.GONE)
                    keyword = s
                    uncheckTheChatRoom(false)
                } else {
                    lay_searchlist.setVisibility(View.VISIBLE)
                    rv.visibility = View.GONE
                    llChatTabs.visibility = View.GONE
                    presenter!!.searchChat(s)
                }
            })
        viewModel!!.initChatRoomsLongPress().observe(
            viewLifecycleOwner,
            Observer { chatRoomUiModel ->
                if (chatRoomUiModel != null) {
                    if (chatRoomUiModel.is_checked) {
                        showOrHideTopView(false, false)
                        tv_action_title.setText(chatRoomUiModel.roomChat.room_name)
                        ll_search.setVisibility(View.GONE)
                        lay_appbar.setVisibility(View.VISIBLE)
                        selectCount++
                        isPinnedRoom = true
                        //if (chatRoomUiModel.roomChat.is_pined == 0) {
                        if (!isPinOrUnpin()) {
                            img_pin.setImageResource(R.drawable.ic_pin_chat)
                        } else {
                            img_pin.setImageResource(R.drawable.ic_unpin_chat)
                        }
                    } else {
                        if (selectCount>0) {
                            selectCount--
                        }
                        if (!isPinOrUnpin()) {
                            img_pin.setImageResource(R.drawable.ic_pin_chat)
                        } else {
                            img_pin.setImageResource(R.drawable.ic_unpin_chat)
                        }
                        showOrHideTopView(true, false)
                    }
                    Log.e("count", "jumlah" +selectCount)
                    img_delete_chat.visibility = if (selectCount>1) View.GONE else View.VISIBLE
                }
            })

        tlChat.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                showOrHideTopView(true, false)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                // do nothing
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                // do nothing
            }
        })

        fab_add.setOnClickListener(View.OnClickListener { initKontakDialog() })
        edt_search.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                charSequence: CharSequence,
                i: Int,
                i1: Int,
                i2: Int
            ) {
            }

            override fun onTextChanged(
                charSequence: CharSequence,
                i: Int,
                i1: Int,
                i2: Int
            ) {
                if (charSequence.toString() != "") {
                    img_clear_search.setVisibility(View.VISIBLE)
                } else {
                    img_clear_search.setVisibility(View.GONE)
                }
                if (isSearchh) {
                    timer.cancel()
                    timer = Timer()
                    timer.schedule(
                        object : TimerTask() {
                            override fun run() {
                                viewModel!!.updateKeyword(edt_search.getText().toString())
                            }
                        },
                        DELAY
                    )
                }
            }

            override fun afterTextChanged(editable: Editable) {}
        })
        img_back_search.setOnClickListener(this)
        img_pin.setOnClickListener(this)
        img_label.setOnClickListener(this)
        img_delete_chat.setOnClickListener(this)
        img_clear_search.setOnClickListener(this)
    }

    private fun showFilterSheet() {
        val bottSheet = BottomSheetFilterDialog(this)
        bottSheet.show(actContext!!.supportFragmentManager, bottSheet.tag)
    }

    var actContext: FragmentActivity? = null

    override fun onAttach(activity: Activity) {
        actContext = activity as FragmentActivity
        super.onAttach(activity)
    }

    private fun showFilterDialog() {
        val filterDialog = Dialog(this!!.requireContext())
        filterDialog.setContentView(R.layout.dialog_filter_chat)
        filterDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        filterDialog.setCancelable(true)
        filterDialog.setCanceledOnTouchOutside(true)
        val filterAZ = filterDialog.findViewById<TextView>(R.id.filterAZ)
        val filterZA = filterDialog.findViewById<TextView>(R.id.filterZA)
        val filterReset = filterDialog.findViewById<TextView>(R.id.filterReset)

        filterAZ.setOnClickListener { v: View? ->
            roomAdapter!!.sortChat(1)
            if (tlChat.selectedTabPosition == 0){
                fragmentOfficailChat.roomAdapter!!.sortChat(1)
            }else if (tlChat.selectedTabPosition == 1){
                fragmentGroupChat.roomAdapter!!.sortChat(1)
            }else if (tlChat.selectedTabPosition == 2){
                fragmentPersonalChat.roomAdapter!!.sortChat(1)
            }
            filterDialog.dismiss()
        }
        filterZA.setOnClickListener { v: View? ->
            roomAdapter!!.sortChat(2)
            if (tlChat.selectedTabPosition == 0){
                fragmentOfficailChat.roomAdapter!!.sortChat(2)
            }else if (tlChat.selectedTabPosition == 1){
                fragmentGroupChat.roomAdapter!!.sortChat(2)
            }else if (tlChat.selectedTabPosition == 2){
                fragmentPersonalChat.roomAdapter!!.sortChat(2)
            }
            filterDialog.dismiss()
        }
        filterReset.setOnClickListener { v: View? ->
            roomAdapter!!.sortChat(3)
            if (tlChat.selectedTabPosition == 0){
                fragmentOfficailChat.roomAdapter!!.sortChat(1)
            }else if (tlChat.selectedTabPosition == 1){
                fragmentGroupChat.roomAdapter!!.sortChat(1)
            }else if (tlChat.selectedTabPosition == 2){
                fragmentPersonalChat.roomAdapter!!.sortChat(1)
            }
            filterDialog.dismiss()
        }
        filterDialog.show()
    }

    fun setViewOnClickEvent(view: View) {
        Log.d(TAG, "setViewOnClickEvent: " + view.id)
        val id = view.id
        if (id == R.id.img_back_search) {
            selectCount = 0
            viewModel!!.updateKeyword("")
            showOrHideTopView(true, false)
        } else if (id == R.id.img_pin) {
            viewModel!!.updateRequestPin(isPinnedRoom)
            showOrHideTopView(true, false)
        } else if (id == R.id.img_delete_chat) {
            AlertDialogBuilder(
                context,
                "Apakah Anda yakin ingin menghapus obrolan ini?",
                "Ya",
                "Tidak",
                object : AlertDialogBuilder.OnAlertDialog {
                    override fun onPositiveButton(dialog: DialogInterface) {
                        viewModel!!.updateRequestDelete(isPinnedRoom)
                        showOrHideTopView(true, false)
                    }

                    override fun onNegativeButton(dialog: DialogInterface) {}
                })
        } else if (id == R.id.img_label) {
            viewModel!!.updateLabel(true)
        } else if (id == R.id.img_clear_search) {
            if (edt_search.getText().toString() == "") {
                showOrHideTopView(true, false)
            } else {
                edt_search.setText("")
            }
        }


    }

    private fun initSearchChatroom() {
        adapterSearchList =
            AdapterSearchList(context, list_searchlist, object : OnObrolanAdapter {
                override fun onClickObrolan(model: ChatRoomsUiModel) {
                    viewModel!!.updateChatRoomsClickFromSearch(model)
                }

                override fun onLongClick(model: ChatRoomsUiModel) {
                    viewModel!!.updateChatRoomsLongPressFromSearch(model)
                }
            })
        rv_searchlist.setLayoutManager(LinearLayoutManager(context))
        rv_searchlist.setAdapter(adapterSearchList)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val itemId = item.itemId
        if (itemId == R.id.action_search) {
            showOrHideTopView(false, true)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSearchChat(list: MutableList<SearchChatroomModel>) {
        val bufferTypeChat: MutableList<SearchChatroomModel> = arrayListOf()

        if (tlChat.selectedTabPosition == 0){
            for (item in list) {
                if (item.chatRoomUiModel != null) {
                    Log.e(TAG, "type room "+item.chatRoomUiModel.roomChat.room_type + " name "
                            + item.chatRoomUiModel.roomChat.room_name)
                    if (item.chatRoomUiModel.roomChat.room_type.equals(RoomChat.official_room_type)) {
                        bufferTypeChat.add(item)
                    }
                }else if (item.chat != null){
                    Log.e(TAG, "type room "+item.chat.room_type + " name "
                            + item.chat.room_code)
                    if (item.chat.room_type.equals(RoomChat.official_room_type)) {
                        bufferTypeChat.add(item)
                    }
                }
            }
        }else if (tlChat.selectedTabPosition == 1){
            for (item in list) {
                if (item.chatRoomUiModel != null) {
                    Log.e(TAG, "type room "+item.chatRoomUiModel.roomChat.room_type + " name "
                            + item.chatRoomUiModel.roomChat.room_name)
                    if (item.chatRoomUiModel.roomChat.room_type.equals(RoomChat.group_room_type)) {
                        bufferTypeChat.add(item)
                    }
                }else if (item.chat != null){
                    Log.e(TAG, "type room "+item.chat.room_type + " name "
                            + item.chat.room_code)
                    if (item.chat.room_type.equals(RoomChat.group_room_type)) {
                        bufferTypeChat.add(item)
                    }
                }
            }
        }else if (tlChat.selectedTabPosition == 2){
            for (item in list) {
                if (item.chatRoomUiModel != null) {
                    Log.e(TAG, "type room "+item.chatRoomUiModel.roomChat.room_type + " name "
                            + item.chatRoomUiModel.roomChat.room_name)
                    if (item.chatRoomUiModel.roomChat.room_type.equals(RoomChat.user_room_type)) {
                        bufferTypeChat.add(item)
                    }
                }else if (item.chat != null){
                    Log.e(TAG, "type room "+item.chat.room_type + " name "
                            + item.chat.room_code)
                    if (item.chat.room_type.equals(RoomChat.user_room_type)) {
                        bufferTypeChat.add(item)
                    }
                }
            }
        }
        list_searchlist.clear()
        Log.e(TAG, "size buffer list = " +bufferTypeChat.size)
        if (bufferTypeChat.size > 0) {
            noData.visibility = View.GONE
            list_searchlist.addAll(bufferTypeChat)
        } else {
            noData.visibility = View.VISIBLE
            tv_nodata.setText("Ruang obrolan tidak ditemukan")
        }
        /*
        if (list.size > 0) {
            helper_nodata.setVisibility(View.GONE)
            list_searchlist.addAll(list)
        } else {
            helper_nodata.setVisibility(View.VISIBLE)
            tv_nodata.setText("Ruang obrolan tidak ditemukan")
        }
         */
        adapterSearchList!!.notifyDataSetChanged()
    }

    override fun onFailedRequestChat() {
        list_searchlist.clear()
        adapterSearchList!!.notifyDataSetChanged()
    }

    override fun onClick(view: View) {
        setViewOnClickEvent(view)
    }

    private fun initSearch() {
        mSearch!!.setOnMenuItemClickListener { false }
        mSearchView = mSearch!!.actionView as SearchView
        mSearchView!!.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
    }

    private fun initKontakDialog() {
        if (kontakChatDialog == null) {
            kontakChatDialog =
                KontakChatDialog(context!!, true, false, object : OnKontakChatDialog {
                    override fun onClickKontak(model: KontakModel) {
                        val intent = Intent(
                            context,
                            ChatRoomActivity::class.java
                        )
                        intent.putExtra("data", model)
                        startActivity(intent)
                    }

                    override fun onAddGroup() {
                        addGroup()
                    }
                })
        } else {
            kontakChatDialog!!.show()
        }
    }

    private fun addGroup() {
        addMemberDialog = AddMemberDialog(context,
            OnAddMember { dialog, list ->
                addDetailGroupDialog =
                    AddDetailGroupDialog(context, list, object : OnAddDetailGroup {
                        override fun onImageClick() {
                            imagepicker_code =
                                StringConstant.imagepicker_addgroup
                            askCompactPermissions(
                                RequiredPermissions,
                                object : PermissionResultInterface {
                                    override fun permissionGranted() {
                                        DialogImagePicker(
                                            context,
                                            object : OnDialogImagePicker {
                                                override fun onCameraClick() {
                                                    EasyImage.openCamera(this@ChatoFragmentNew, 0)
                                                }

                                                override fun onFileManagerClick() {
                                                    EasyImage.openGallery(this@ChatoFragmentNew, 0)
                                                }

                                                override fun onVideoCameraClick() {}
                                            })
                                    }

                                    override fun permissionDenied() {
                                        GGFWUtil.ToastShort(
                                            context,
                                            "Anda perlu memberikan akses terlebih dahulu"
                                        )
                                    }
                                })
                        }

                        override fun onSuccessAddGroup() {
                            addMemberDialog!!.dismiss()
                            viewModel!!.updateRefreshRoom(true)
                        }

                        override fun onCancelAddDetailGroup() {
                            addMemberDialog!!.show()
                        }
                    })
            })
    }

    override fun onCreateOptionsMenu(
        menu: Menu,
        inflater: MenuInflater
    ) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_search, menu)
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == Activity.RESULT_OK) {
                val resultUri = result.uri
                if (imagepicker_code == StringConstant.imagepicker_addgroup) {
                    addDetailGroupDialog!!.setImageGroup(
                        GGFWUtil.getBitmapFromUri(
                            context,
                            resultUri
                        ), resultUri
                    )
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error.toString()
                GGFWUtil.ToastShort(context, "" + error)
            }
        } else {
            EasyImage.handleActivityResult(
                requestCode,
                resultCode,
                data,
                activity,
                object : DefaultCallback() {
                    override fun onImagePickerError(
                        e: Exception,
                        source: ImageSource,
                        type: Int
                    ) {
                        e.printStackTrace()
                    }

                    override fun onImagesPicked(
                        imageFiles: List<File>,
                        source: ImageSource,
                        type: Int
                    ) {
                        Log.d(TAG, "onImagesPicked: $type")
                        startCropActivity(Uri.fromFile(imageFiles[0]))
                    }

                    override fun onCanceled(source: ImageSource, type: Int) {
                        //Cancel handling, you might wanna remove taken photo if it was canceled
                        if (source == ImageSource.CAMERA) {
                            val photoFile =
                                EasyImage.lastlyTakenButCanceledPhoto(context!!)
                            photoFile?.delete()
                        }
                    }
                })
        }
    }

    private fun startCropActivity(uri: Uri) {
        CropImage.activity(uri)
            .setCropShape(CropImageView.CropShape.RECTANGLE)
            .start(requireActivity())
    }

    override fun onRequestObrolan(list: List<ChatRoomsUiModel?>?, isRefresh: Boolean) {
        super.onRequestObrolan(list, isRefresh)

        if (checkNewMessage()){
            listener.onShowBadgeChat()
        }else {
            listener.onHideBadgeChat()
        }
    }

    override fun onSelectedFilter(type: Int) {
        when(type){
            1 ->{
                if (tlChat.selectedTabPosition == 0){
                    fragmentOfficailChat.roomAdapter!!.sortChat(1)
                }else if (tlChat.selectedTabPosition == 1){
                    fragmentGroupChat.roomAdapter!!.sortChat(1)
                }else if (tlChat.selectedTabPosition == 2){
                    fragmentPersonalChat.roomAdapter!!.sortChat(1)
                }
            }
            2 ->{
                if (tlChat.selectedTabPosition == 0){
                    fragmentOfficailChat.roomAdapter!!.sortChat(2)
                }else if (tlChat.selectedTabPosition == 1){
                    fragmentGroupChat.roomAdapter!!.sortChat(2)
                }else if (tlChat.selectedTabPosition == 2){
                    fragmentPersonalChat.roomAdapter!!.sortChat(2)
                }
            }
            3 ->{
                if (tlChat.selectedTabPosition == 0){
                    fragmentOfficailChat.roomAdapter!!.sortChat(3)
                }else if (tlChat.selectedTabPosition == 1){
                    fragmentGroupChat.roomAdapter!!.sortChat(3)
                }else if (tlChat.selectedTabPosition == 2){
                    fragmentPersonalChat.roomAdapter!!.sortChat(3)
                }
            }
        }
    }

}