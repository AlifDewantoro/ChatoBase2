package com.gamatechno.chato.sdk.app.grouproomdetail.fragment.info

import com.gamatechno.chato.sdk.app.kontakchat.KontakModel
import com.gamatechno.chato.sdk.data.DAO.Group.Group
import com.gamatechno.chato.sdk.module.core.BaseView

interface GroupInfoFragmentView {
    interface Presenter{
        fun exitGroup(group: Group)
        fun addMemberToGroup(idroom : String, list: MutableList<KontakModel>)
        fun createRoomId(model : KontakModel)
        fun removeFromGroup(idroom : String, kontakModel: KontakModel)
        fun updateAdminRole(idroom : String, kontakModel: KontakModel, admin_role: Int)
    }

    interface View : BaseView{
        fun onExitGroup(message: String)
        fun failedToDoSomething(message: String)
        fun onCreateRoomId(model : KontakModel)
        fun onAddMemberToGroup(list: MutableList<KontakModel>)
        fun onRemoveMember(kontakModel: KontakModel)
        fun onUpdateAdminRole(kontakModel: KontakModel, is_admin: Int)
        fun onUpdateGroupInfo(group: Group)

    }
}