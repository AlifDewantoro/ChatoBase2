package com.gamatechno.chato.sdk.app.grouproomdetail

import com.gamatechno.chato.sdk.app.kontakchat.KontakModel
import com.gamatechno.chato.sdk.data.DAO.Group.Group
import com.gamatechno.chato.sdk.module.core.BaseView

interface GroupInfoView {
    interface Presenter{
        fun requestInfoGroup(roomid: String, isLoadingShow: Boolean)
        fun removeMember(roomid: String, kontakModel: KontakModel)
        fun removeFromAdmin(roomid: String, kontakModel: KontakModel)
        fun addToAdmin(roomid: String, kontakModel: KontakModel)
        fun updateInfoGroup(group: Group)
        fun updateAvatarGroup(group: Group)
    }
    interface View: BaseView{
        fun onRequestInfoGroup(group: Group, isRefresh: Boolean)
        fun onFailedRequestData(message: String)
    }
}