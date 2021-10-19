package com.gamatechno.chato.sdk.app.grouproomdetail.fragment.listadmin

import com.gamatechno.chato.sdk.module.core.BaseView

interface ListAdminView {
    interface Presenter{
        fun addAdmin()
    }
    interface View: BaseView{

    }
}