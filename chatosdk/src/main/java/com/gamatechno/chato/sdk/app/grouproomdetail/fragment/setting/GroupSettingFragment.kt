package com.gamatechno.chato.sdk.app.grouproomdetail.fragment.setting

import androidx.lifecycle.Observer
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import com.gamatechno.chato.sdk.R
import com.gamatechno.chato.sdk.app.chatroom.model.ChatRoomUiModel
import com.gamatechno.chato.sdk.app.grouproomdetail.GroupInfoActivity
import com.gamatechno.chato.sdk.app.grouproomdetail.viewmodel.GroupInfoViewModel
import com.gamatechno.chato.sdk.data.DAO.Group.Group
import com.gamatechno.ggfw.Activity.FragmentPermission
import com.gamatechno.ggfw.utils.AlertDialogBuilder
import kotlinx.android.synthetic.main.fragment_group_setting.*

class GroupSettingFragment : FragmentPermission() {

    lateinit var groupInfoViewModel: GroupInfoViewModel
    lateinit var chatRoomUiModel: ChatRoomUiModel
    lateinit var group: Group

    companion object {
        fun newInstance(groupInfoViewModel: GroupInfoViewModel, chatRoomUiModel: ChatRoomUiModel): GroupSettingFragment {
            val fragment = GroupSettingFragment()
            fragment.groupInfoViewModel = groupInfoViewModel
            fragment.chatRoomUiModel = chatRoomUiModel
            return fragment
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_group_setting, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()

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



        lay_admin.setOnClickListener({
            groupInfoViewModel.updatePageController(GroupInfoActivity.tag_groupadmin)
        })
    }

    private fun initViewModel(){
        groupInfoViewModel.initGroupData().observe(viewLifecycleOwner, Observer {
            group = it!!
            if(it!!.room_group_type.equals("OPEN")){
                switch_broadcast.isChecked = false
            } else {
                switch_broadcast.isChecked = true
            }
        })
    }
}