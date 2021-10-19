package com.gamatechno.chato.sdk.app.filesharing

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.gamatechno.chato.sdk.R
import com.gamatechno.chato.sdk.app.chatroom.model.FileModel
import com.gamatechno.chato.sdk.module.activity.ChatoCoreActivity
import kotlinx.android.synthetic.main.activity_my_document.*

class MyDocumentActivity: ChatoCoreActivity(), MyDocumentView.View {

    companion object{
        const val OPEN_TYPE = "OPEN_TYPE"
        const val OPEN_DOCUMENT = 1
        const val OPEN_IMAGE = 2
        const val OPEN_VIDEO = 3
        const val OPEN_GALLERY = 4
    }

    var openType = 0

    lateinit var adapter:MyDocumentListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_document)

        setSupportActionBar(toolbar)
        if(supportActionBar!=null){
            supportActionBar!!.setHomeButtonEnabled(true)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.title = "Dokumen Saya"
        }

        adapter = MyDocumentListAdapter()
        rv_list.layoutManager = LinearLayoutManager(this)
        rv_list.adapter = adapter

        openType = intent.getIntExtra(OPEN_TYPE,0)

        if(openType == OPEN_DOCUMENT){
            supportActionBar!!.title = "Dokumen"
        } else if(openType == OPEN_IMAGE){
            supportActionBar!!.title = "Gambar"
        } else if(openType == OPEN_VIDEO) {
            supportActionBar!!.title = "Video"
        } else if(openType == OPEN_GALLERY) {
            supportActionBar!!.title = "Gallery"
            galleryFolder()
        } else {
            supportActionBar!!.title = "Dokumen Saya"
            defaultFolder()
        }
    }

    override fun onLoading() {

    }

    override fun onHideLoading() {

    }

    override fun onRequestDocument(list: MutableList<com.gamatechno.chato.sdk.data.DAO.File.FileModel>) {

    }

    override fun onErrorConnection(message: String?) {

    }

    override fun onAuthFailed(error: String?) {

    }

    private fun defaultFolder(){
        val list:ArrayList<FileModel> = ArrayList()
        list.add(FileModel(null,"folder","Document"))
        list.add(FileModel(null,"folder","Gambar"))
        list.add(FileModel(null,"folder","Video"))
        adapter.addAll(list)
    }

    private fun galleryFolder(){
        val list:ArrayList<FileModel> = ArrayList()
//        list.add(FileModel(null,"folder","Gambar"))
//        list.add(FileModel(null,"folder","Video"))
//        adapter.addAll(list)
    }
}