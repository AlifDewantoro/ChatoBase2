package com.gamatechno.chato.sdk.app.grouproomdetail.fragment.info

import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.gamatechno.chato.sdk.R
import com.gamatechno.chato.sdk.app.chatroom.model.ChatRoomUiModel
import com.gamatechno.chato.sdk.app.chatroomdetail.UserRoomDetailActivity
import com.gamatechno.chato.sdk.app.grouproomadd.addgroup.addmember.AddMemberDialog
import com.gamatechno.chato.sdk.app.grouproomdetail.GroupInfoActivity
import com.gamatechno.chato.sdk.app.grouproomdetail.viewmodel.GroupInfoViewModel
import com.gamatechno.chato.sdk.app.kontakchat.KontakAdapter
import com.gamatechno.chato.sdk.app.kontakchat.KontakModel
import com.gamatechno.chato.sdk.app.sharedmedia.SharedMediaActivity
import com.gamatechno.chato.sdk.app.starredmessage.StarredMessageActivity
import com.gamatechno.chato.sdk.data.DAO.Group.Group
import com.gamatechno.chato.sdk.data.constant.Preferences
import com.gamatechno.chato.sdk.data.constant.StringConstant
import com.gamatechno.chato.sdk.data.model.UserModel
import com.gamatechno.chato.sdk.utils.ChatoUtils
import com.gamatechno.chato.sdk.utils.Loading
import com.gamatechno.ggfw.Activity.FragmentPermission
import com.gamatechno.ggfw.utils.AlertDialogBuilder
import com.gamatechno.ggfw.utils.GGFWUtil
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_group_info.*

class GroupInfoFragment : FragmentPermission(), GroupInfoFragmentView.View {

    lateinit var groupInfoViewModel: GroupInfoViewModel
    lateinit var chatRoomUiModel: ChatRoomUiModel
    lateinit var kontakAdapter: KontakAdapter
    lateinit var listKontak: MutableList<KontakModel>

    lateinit var loading: Loading
    lateinit var group: Group

    lateinit var presenter: GroupInfoFragmentPresenter

    companion object{
        fun newInstance(groupInfoViewModel: GroupInfoViewModel, chatRoomUiModel: ChatRoomUiModel): GroupInfoFragment {
            val fragment = GroupInfoFragment()
            fragment.groupInfoViewModel = groupInfoViewModel
            fragment.chatRoomUiModel = chatRoomUiModel
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_group_info, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter = GroupInfoFragmentPresenter(context, this)
        loading = Loading(context)

        initViewModel()
        listKontak = ArrayList()
        val model = Gson().fromJson(GGFWUtil.getStringFromSP(context, Preferences.USER_LOGIN), UserModel::class.java)
        kontakAdapter = KontakAdapter(context,  model, listKontak, object : KontakAdapter.OnKontakAdapter{
            override fun onKontakClick(kontakModel: KontakModel?, position: Int) {

                if(kontakModel!!.room_id==0){
                    presenter.createRoomId(kontakModel)
                }else {
                    onCreateRoomId(kontakModel)
                }


                /*if(!kontakModel!!.user_id.equals(ChatoUtils.getUserLogin(context).user_id)){
                    GroupActionDialog(context, group, kontakModel!!, object : GroupActionDialog.OnGroupActionListener{
                        override fun onChat(dialog: Dialog, kontakModel: KontakModel) {
                            if(kontakModel.user_id != ChatoUtils.getUserLogin(getContext()).user_id){
                                if(kontakModel.room_id==0) {
                                    presenter.createRoomId(kontakModel)
                                } else {
                                    onCreateRoomId(kontakModel)
                                }
                            }
                        }

                        override fun onDelete(d: Dialog, kontakModel: KontakModel) {
                            if(kontakModel.user_id != ChatoUtils.getUserLogin(getContext()).user_id){
                                AlertDialogBuilder(context, "Apakah Anda menghapus "+kontakModel.user_name+" dari grup?", "Ya", "Tidak", object : AlertDialogBuilder.OnAlertDialog {
                                    override fun onPositiveButton(dialog: DialogInterface) {
                                        d.dismiss()
                                        presenter!!.removeFromGroup(""+group.room_id, kontakModel)
                                    }

                                    override fun onNegativeButton(dialog: DialogInterface) {

                                    }
                                })
                            }
                        }

                        override fun onRemoveFromAdmin(dialog: Dialog, kontakModel: KontakModel) {
                            if(kontakModel.user_id != ChatoUtils.getUserLogin(getContext()).user_id){
                                presenter!!.updateAdminRole(""+group.room_id, kontakModel, 0)
                                dialog.dismiss()
                            }
                        }

                        override fun onAddToAdmin(dialog: Dialog, kontakModel: KontakModel) {
                            if(kontakModel.user_id != ChatoUtils.getUserLogin(getContext()).user_id){
                                presenter!!.updateAdminRole(""+group.room_id, kontakModel, 1)
                                dialog.dismiss()
                            }
                        }

                    })
                }*/
            }

            override fun onKontakLongClick(kontakModel: KontakModel?, position: Int) {

            }

            override fun onMakeGroup() {

            }
        })
        rv_user.layoutManager = LinearLayoutManager(context)
        rv_user.adapter = kontakAdapter

        lay_addmember.setOnClickListener({
            AddMemberDialog(context, object: AddMemberDialog.OnAddMember {
                override fun onAfterAddingMember(dialog: Dialog, list: MutableList<KontakModel>?) {
                    dialog.dismiss()
                    presenter.addMemberToGroup( ""+group.room_id, list!!)
                }
            })
        })

        img_edit_groupname.setOnClickListener({
            if (edt_groupname.visibility == View.VISIBLE) {
                group.room_name = edt_groupname.text.toString()
                groupInfoViewModel.updateUpdatedGroupData(group)
                ChatoUtils.hideSoftKeyboard(context, edt_groupname)
            } else {
                edt_groupname.visibility = View.VISIBLE
                tv_groupname.visibility = View.GONE
                img_edit_groupname.setImageResource(R.drawable.ic_check_unread_24dp)
                edt_groupname.setSelection(edt_groupname.text.length)
                ChatoUtils.showKeyboard(context, edt_groupname)
            }
        })

        btn_setelan_grup.setOnClickListener({
            groupInfoViewModel.updatePageController(GroupInfoActivity.tag_groupsetting)
        })

        lay_starredmessage.setOnClickListener({
//            startActivity(Intent(context, StarredMessageActivity::class.java).putExtra("room", chatRoomUiModel))
            activity!!.startActivityForResult(Intent(context, StarredMessageActivity::class.java).putExtra("room", chatRoomUiModel), (activity!! as GroupInfoActivity).REQUEST_STAR_MESSAGES)
        })

        lay_sharedcontent.setOnClickListener({
            startActivity(Intent(context, SharedMediaActivity::class.java).putExtra("room", chatRoomUiModel).putExtra("sharedMedia", true))
        })
        lay_leavegroup.setOnClickListener({
            AlertDialogBuilder(context, "Apakah Anda yakin ingin keluar dari grup ini?", "Ya", "Tidak", object : AlertDialogBuilder.OnAlertDialog {
                override fun onPositiveButton(dialog: DialogInterface) {
                    if(group != null){
                        presenter!!.exitGroup(group)
                    }
                }

                override fun onNegativeButton(dialog: DialogInterface) {

                }
            })
        })

    }

    override fun onCreateRoomId(model: KontakModel) {

        val data = ChatRoomUiModel(
            model.user_id.toString(),
            model.user_name,
            model.user_photo,
            model.room_id.toString()
        )
        val intent = Intent(context, UserRoomDetailActivity::class.java)
        intent.putExtra("data", data)
        startActivity(intent)
        /*
        val intent = Intent(context, ChatRoomActivity::class.java)
        intent.putExtra("data", model)
        startActivity(intent)
        activity!!.setResult(StringConstant.FINNISH_CHAT_ACTIVITY)
        activity!!.finish()

         */
    }

    override fun onUpdateGroupInfo(group: Group) {

    }

    override fun onUpdateAdminRole(kontakModel: KontakModel, is_admin: Int) {
        for (i in 0 until listKontak!!.size-1) {
            if(listKontak.get(i).user_id == kontakModel.user_id){
                listKontak.get(i).is_admin = is_admin
            }
        }
        notifyListUser()
        activity!!.setResult(StringConstant.REFRESH_CHAT_HISTORY)
    }

    override fun onExitGroup(message: String) {
        activity!!.finish()
    }

    override fun failedToDoSomething(message: String) {
        GGFWUtil.ToastShort(context, message)
    }

    override fun onLoading() {
        loading!!.show()
    }

    override fun onHideLoading() {
        loading!!.dismiss()
    }

    override fun onErrorConnection(message: String?) {
        GGFWUtil.ToastShort(context, message)
    }

    override fun onAuthFailed(error: String?) {

    }

    override fun onAddMemberToGroup(list: MutableList<KontakModel>) {
        listKontak!!.addAll(list)
        notifyListUser()
        activity!!.setResult(StringConstant.REFRESH_CHAT_HISTORY)
    }

    override fun onRemoveMember(kontakModel: KontakModel) {
        Log.d("GroupInfoFragment","NoM: "+listKontak.size+" Kontak detail: "+kontakModel.user_id)
//        for (i in 0 until listKontak!!.size-1) {
//            Log.d("GroupInfoFragment","Kontak ("+ i +"): "+listKontak!!.get(i).user_id)
//            if(listKontak!!.get(i).user_id == kontakModel.user_id){
//                listKontak!!.removeAt(i)
//                listKont
//            }
//        }
        for (kontak in listKontak){
            if(kontak.user_id==kontakModel.user_id){
                listKontak.remove(kontak)
                break
            }
        }
        notifyListUser()
        activity!!.setResult(StringConstant.REFRESH_CHAT_HISTORY)
    }


    private fun initData(group: Group){
        tv_groupname.setText(group.room_name)
        edt_groupname.setText(group.room_name)
        tv_shared_content.setText(""+group.count_shared)
        tv_starred_message.setText(""+group.count_star_message)

        edt_groupname.visibility = View.GONE
        tv_groupname.visibility = View.VISIBLE

        if(group.room_group_type.equals("BROADCAST")){
            lay_leavegroup.visibility = View.GONE
        }

        if(group.is_admin != 0){
            img_edit_groupname.visibility = View.VISIBLE
            img_edit_groupname.setImageResource(R.drawable.ic_pencil_edit_button)
            lay_addmember.visibility = View.VISIBLE
        } else {
            img_edit_groupname.visibility = View.GONE
            lay_addmember.visibility = View.GONE
        }
    }

    private fun initViewModel(){
        groupInfoViewModel.initGroupData().observe(viewLifecycleOwner, Observer {
            this.group = it!!
            initData(it!!)
        })

        groupInfoViewModel.initRefreshedListUser().observe(viewLifecycleOwner, Observer {
            this.group = it!!
            listKontak.clear()
            listKontak.addAll(it!!.list_user)
            kontakAdapter.notifyDataSetChanged()
            tv_count_user.setText(""+it!!.list_user.size)
            if(it!!.is_admin == 0){
                btn_setelan_grup.visibility = View.GONE
            } else {
                btn_setelan_grup.visibility = View.VISIBLE
            }
        })
    }

    private fun notifyListUser(){
        tv_count_user.setText(""+listKontak.size)
        kontakAdapter.notifyDataSetChanged()
    }
}
