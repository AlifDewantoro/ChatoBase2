package com.gamatechno.chato.sdk.app.filesharing

import com.gamatechno.chato.sdk.data.DAO.File.FileModel
import com.gamatechno.chato.sdk.module.core.BaseView

interface MyDocumentView {
    interface Presenter {
        fun requestDocument(isRefresh : Boolean, type: String, keyword : String)
    }

    interface View : BaseView{
        fun onRequestDocument(list : MutableList<FileModel>)
    }
}