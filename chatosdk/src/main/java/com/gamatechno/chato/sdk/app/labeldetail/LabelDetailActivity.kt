package com.gamatechno.chato.sdk.app.labeldetail

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.gamatechno.chato.sdk.R
import com.gamatechno.chato.sdk.app.chatroom.ChatRoomActivity
import com.gamatechno.chato.sdk.app.chatrooms.adapter.RoomAdapter
import com.gamatechno.chato.sdk.app.chatrooms.uimodel.ChatRoomsUiModel
import com.gamatechno.chato.sdk.app.grouproomadd.addgroup.addmember.AddMemberDialog
import com.gamatechno.chato.sdk.app.kontakchat.KontakModel

import com.gamatechno.chato.sdk.data.DAO.Label.LabelModel
import com.gamatechno.chato.sdk.module.activity.ChatoCoreActivity
import kotlinx.android.synthetic.main.activity_label_detail.*
import kotlinx.android.synthetic.main.layout_helper.*
import java.lang.Exception

class LabelDetailActivity : ChatoCoreActivity(), LabelDetailView.View, View.OnClickListener {
    lateinit var roomAdapter : RoomAdapter
    lateinit var chatRoomUiModelList: MutableList<ChatRoomsUiModel>
    lateinit var presenter: LabelDetailPresenter
    var labelModel : LabelModel? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_label_detail)
        setSupportActionBar(toolbar)
        chatRoomUiModelList = ArrayList()
        presenter = LabelDetailPresenter(context, this)

        if (supportActionBar != null) {
            supportActionBar!!.setHomeButtonEnabled(true)
            supportActionBar!!.setDisplayShowHomeEnabled(true)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        }

        if(intent.hasExtra("data")){
            try {
                labelModel = intent.getSerializableExtra("data") as LabelModel
                supportActionBar!!.title = labelModel!!.label_title
            } catch (e : Exception){
                finish()
            }
        } else {
            finish()
        }

        roomAdapter = RoomAdapter(context, chatRoomUiModelList, object : RoomAdapter.OnObrolanAdapter{
            override fun onClickObrolan(model: ChatRoomsUiModel?) {
                val intent = Intent(context, ChatRoomActivity::class.java)
                intent.putExtra("chatroom", model)
                startActivity(intent)
            }

            override fun onLongClick(model: ChatRoomsUiModel?) {

            }
        })
        rv.layoutManager = LinearLayoutManager(context)
        rv.adapter = roomAdapter


        tv_startnewconversation.setOnClickListener(this)
        btn_tryservererror.setOnClickListener(this)
        btn_tryconnection.setOnClickListener(this)
        fab_add.setOnClickListener(this)

        swipe.setOnRefreshListener ({
            swipe.isRefreshing = false
            presenter.requestObrolan(labelModel!!)
        })
        tv_startnewconversation.setOnClickListener(this)
        presenter.requestObrolan(labelModel!!)
    }

    override fun onLoading() {

    }
    override fun onHideLoading() {

    }

    override fun onRequestRoom(list: MutableList<ChatRoomsUiModel>) {
        helper_noconversation.visibility = View.GONE
        chatRoomUiModelList.clear()
        chatRoomUiModelList.addAll(list)
        roomAdapter.notifyDataSetChanged()

        helper_noconversation.setVisibility(View.GONE)
    }

    override fun onFailedRequestObrolan() {
        helper_noconversation.visibility = View.VISIBLE
        tv_startnewconversation.setText("Klik disini untuk menambahkan pesan baru")
    }

    override fun successPinnedChatRoom(message: String?) {

    }

    override fun onDeleteRoom(isSuccess: Boolean, message: String?) {

    }

    override fun onErrorConnection(message: String) {
        if(chatRoomUiModelList.size == 0){
            helper_servererror.visibility = View.VISIBLE
        }
    }
    override fun onAuthFailed(error: String) {

    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_label, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onClick(p0: View?) {
        when(p0!!.id){
            R.id.tv_startnewconversation -> {
                AddMemberDialog(context, "Tambah Anggota Label", true, object : AddMemberDialog.OnAddMember{
                    override fun onAfterAddingMember(dialog: Dialog?, list: MutableList<KontakModel>?) {
                        presenter.requestObrolan(labelModel!!)
                    }
                }).setAddedList(chatRoomUiModelList)
            }
            R.id.btn_tryservererror -> {
                presenter.requestObrolan(labelModel!!)
            }
            R.id.btn_tryconnection -> {
                presenter.requestObrolan(labelModel!!)
            }
            R.id.fab_add -> {
                AddMemberDialog(context, "Tambah Anggota Label", true, object : AddMemberDialog.OnAddMember{
                    override fun onAfterAddingMember(dialog: Dialog?, list: MutableList<KontakModel>?) {
                        presenter.requestObrolan(labelModel!!)
                    }
                }).setAddedList(chatRoomUiModelList)
            }
        }
    }
}