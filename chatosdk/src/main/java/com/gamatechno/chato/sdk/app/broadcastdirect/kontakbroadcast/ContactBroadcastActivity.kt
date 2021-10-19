package com.gamatechno.chato.sdk.app.broadcastdirect.kontakbroadcast

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import com.gamatechno.chato.sdk.R
import com.gamatechno.chato.sdk.app.broadcastdirect.roombroadcast.RoomBroadcastActivity
import com.gamatechno.chato.sdk.app.kontakchat.KontakModel
import com.gamatechno.chato.sdk.app.kontakchat.ListKontakModel
import com.gamatechno.chato.sdk.data.DAO.RoomChat.RoomChat
import com.gamatechno.chato.sdk.module.activity.ChatoCoreActivity
import com.gamatechno.chato.sdk.utils.ChatoUtils
import kotlinx.android.synthetic.main.dialog_add_member.*
import java.util.*
import kotlin.collections.ArrayList

class ContactBroadcastActivity : ChatoCoreActivity(), ContactBroadcastView.View {

    lateinit var addedKontakModels: ArrayList<KontakModel>
    lateinit var layoutManager: LinearLayoutManager
    lateinit var cbAdapter: ContactBroadcastAdapter

    lateinit var presenter: ContactBroadcastPresenter
    internal var isSearch = false
    internal var isLoadMore = true
    internal var isGroupTitleAvailable = false
    internal var isContactTitleAvailable = false

    private var timer = Timer()
    private val DELAY: Long = 1000 // milliseconds
    private var isFromDetailBroadcast = false
//    internal var isSelectAll = false
//    internal var isSelectAllGroup = false
//    internal var isSelectAllContact = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_add_member)

        presenter = ContactBroadcastPresenter(this,this)
        addedKontakModels = ArrayList()
        toggle_kontak.display(lay_toolbar)

        if(intent.hasExtra("data")){
            addedKontakModels.addAll((intent.getSerializableExtra("data") as ListKontakModel).contacts)
            isFromDetailBroadcast = true
        }

        initKontakAdapter()
        img_search.setOnClickListener { enableSearch() }
        img_back.setOnClickListener {
            if(isSearch) {
                disableSearch(true)
            } else {
                finish()
            }
        }

        edt_search.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    ChatoUtils.hideSoftKeyboard(context, edt_search)
                    return true
                }
                return false
            }
        })

        edt_search.addTextChangedListener(object:TextWatcher{
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (isSearch) {
                    timer.cancel()
                    timer = Timer()
                    timer.schedule(
                            object : TimerTask() {
                                override fun run() {
                                    if (s.toString() == "") {
                                        presenter.getContact(true)
                                    } else {
                                        presenter.getSearchContact(s.toString(), true)
                                    }
                                }
                            },
                            DELAY
                    )
                }
            }
        })

        fab.setOnClickListener {
            if(addedKontakModels.size>0) {
                //TODO: Send selected data to next activity
                /*for (contact in addedKontakModels) {
                    if (contact.room_type.equals(RoomChat.user_room_type))
                        Log.d("ContactBroadcastAct", "user_name: " + contact.user_name)
                    else
                        Log.d("ContactBroadcastAct", "group_name: " + contact.group_name)
                }*/

//                TODO : Here the detail
                if(isFromDetailBroadcast){
                    setResult(99,Intent(context, RoomBroadcastActivity::class.java).putExtra("data", ListKontakModel(addedKontakModels)))
                    finish()
                } else {
                    startActivity(Intent(context, RoomBroadcastActivity::class.java).putExtra("data", ListKontakModel(addedKontakModels)))
                    finish()
                }
            } else {
                Toast.makeText(this,
                        "Tidak ada data yg ditambahkan.\nMohon diperiksa kembali.",
                        Toast.LENGTH_SHORT).show()
            }
        }

        presenter.getContact(true)
    }

    override fun onListContact(listUser: ArrayList<KontakModel>, listGroup: ArrayList<KontakModel>, isRefresh: Boolean) {
        val listData = ArrayList<ContactBroadcastModel>()
        if (isRefresh) {
            clearList()
            if(listGroup.size>0||listUser.size>0) {
                if (listGroup.size > 0) {
//                    val cbm = ContactBroadcastModel(0, KontakModel("", false, 0))
//                    cbm.isSelected = isSelectAll
//                    listData.add(cbm)
                    if (!isGroupTitleAvailable) {
                        val cbm = ContactBroadcastModel(0, KontakModel("Grup", true, 0))
//                        cbm.isSelected = isSelectAllGroup
                        listData.add(cbm)
                        isGroupTitleAvailable = true
                    }
                    for (km in listGroup) {
                        val cbm = ContactBroadcastModel(1, km)
                        cbm.isSelected = checkSelected(km)
                        listData.add(cbm)
                    }
                }
                if (listUser.size > 0) {
                    if (!isContactTitleAvailable) {
                        val cbm = ContactBroadcastModel(0, KontakModel("Kontak", true, 0))
//                        cbm.isSelected = isSelectAllContact
                        listData.add(cbm)
                        isContactTitleAvailable = true
                    }
                    for (km in listUser) {
                        val cbm = ContactBroadcastModel(1, km)
                        cbm.isSelected = checkSelected(km)
                        listData.add(cbm)

                    }
                }
            }
            cbAdapter.addAll(listData)
        } else {
            for(km in listUser){
                val cbm = ContactBroadcastModel(1, km)
                cbm.isSelected = checkSelected(km)
                listData.add(cbm)
            }
            cbAdapter.addAll(listData)
        }

        isLoadMore = true
    }

    private fun clearList(){
        cbAdapter.clear()
        isGroupTitleAvailable = false
        isContactTitleAvailable = false
    }

    private fun checkSelected(km: KontakModel):Boolean{
        for(contact in addedKontakModels){
            if(contact.room_type==RoomChat.user_room_type) {
                if (contact.user_id.equals(km.user_id)) {
                    return true
                }
            } else {
                if (contact.room_id.equals(km.room_id)) {
                    return true
                }
            }
        }
        return false
    }

    override fun onFailedListContact(isRefresh: Boolean) {

    }

    private fun enableSearch() {
        isSearch = true
        toggle_kontak.display(edt_search)
        ChatoUtils.showKeyboard(context, edt_search)
    }

    private fun disableSearch(isNeedToRefresh: Boolean) {
        isSearch = false
        toggle_kontak.display(lay_toolbar)

        if (edt_search.text.toString() != "") {
            edt_search.setText("")
        }
        ChatoUtils.hideSoftKeyboard(context, edt_search)

        if (isNeedToRefresh)
            presenter.getContact(true)
    }

    private fun initKontakAdapter() {
        cbAdapter = ContactBroadcastAdapter(object:ContactBroadcastAdapter.OnAction {
            override fun onClickSelectAll(cbm: ContactBroadcastModel) {
//                if(!cbm.contact.isHeader){
//                    cbm.isSelected = !isSelectAll
//                    isSelectAllGroup = !isSelectAll
//                    isSelectAllContact= !isSelectAll
//                    isSelectAll = !isSelectAll
//                } else {
//                    if(cbm.contact.user_name.equals("Grup")) {
//                        cbm.isSelected = !isSelectAllGroup
//                        isSelectAllGroup = !isSelectAllGroup
//                    } else {
//                        cbm.isSelected = !isSelectAllGroup
//                        isSelectAllContact = !isSelectAllContact
//                    }
//                    isSelectAll = isSelectAllGroup&&isSelectAllContact
//                }
//                cbAdapter.notifyDataSetChanged()
            }

            override fun onClickItem(cbm: ContactBroadcastModel) {
                if(cbm.isSelected){
                    for(contact in addedKontakModels) {
                        if(contact.room_type==cbm.contact.room_type) {
                            if (contact.room_type == RoomChat.user_room_type) {
                                if (contact.user_id.equals(cbm.contact.user_id)) {
                                    addedKontakModels.remove(contact)
                                    break
                                }
                            } else {
                                if (contact.room_id.equals(cbm.contact.room_id)) {
                                    addedKontakModels.remove(contact)
                                    break
                                }
                            }
                        }
                    }
                    cbm.isSelected=false
                } else {
                    addedKontakModels.add(cbm.contact)
                    cbm.isSelected=true
                }

                cbAdapter.notifyDataSetChanged()
            }
        })
        layoutManager = LinearLayoutManager(context)
        rv.layoutManager = layoutManager
        rv.adapter = cbAdapter

        rv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0)
                //check for scroll down
                {
                    val visibleItemCount = layoutManager.childCount
                    val totalItemCount = layoutManager.itemCount
                    val pastVisiblesItems = layoutManager.findFirstVisibleItemPosition()

                    if (isLoadMore) {
                        if (visibleItemCount + pastVisiblesItems >= totalItemCount) {
                            isLoadMore = false
                            println("Last Item Bro $totalItemCount")
                            if (edt_search.text.toString() == "") {
                                presenter.getContact(false)
                            } else {
//                                presenter.searchUser(edt_search.text.toString(), false)
                            }
                        }
                    }
                }
            }
        })
    }


    override fun onLoading() {

    }

    override fun onHideLoading() {

    }

    override fun onErrorConnection(message: String?) {

    }

    override fun onAuthFailed(error: String?) {

    }
}