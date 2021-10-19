package com.gamatechno.chato.sdk.app.grouproomdetail.fragment.listadmin

import androidx.lifecycle.Observer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gamatechno.chato.sdk.R
import com.gamatechno.chato.sdk.app.chatroom.model.ChatRoomUiModel
import com.gamatechno.chato.sdk.app.grouproomdetail.viewmodel.GroupInfoViewModel
import com.gamatechno.chato.sdk.data.DAO.Group.Group
import com.gamatechno.ggfw.Activity.FragmentPermission

class ListAdminFragment : FragmentPermission() {

    lateinit var groupInfoViewModel: GroupInfoViewModel
    lateinit var chatRoomUiModel: ChatRoomUiModel
    lateinit var group: Group

    companion object {
        fun newInstance(groupInfoViewModel: GroupInfoViewModel, chatRoomUiModel: ChatRoomUiModel): ListAdminFragment {
            val fragment = ListAdminFragment()
            fragment.groupInfoViewModel = groupInfoViewModel
            fragment.chatRoomUiModel = chatRoomUiModel
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_list_admin, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
    }

    private fun initViewModel(){
        groupInfoViewModel.initGroupData().observe(viewLifecycleOwner, Observer {
            group = it!!
        })
    }
}
