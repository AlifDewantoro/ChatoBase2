package com.gamatechno.chato.sdk.app.broadcastdirect.detailbroadcastmember

import android.content.Context
import android.util.Log
import android.view.Gravity
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.ButterKnife
import com.gamatechno.chato.sdk.R
import com.gamatechno.chato.sdk.app.broadcastdirect.roombroadcast.RoomBroadcastActivity
import com.gamatechno.chato.sdk.app.kontakchat.KontakAdapter
import com.gamatechno.chato.sdk.app.kontakchat.KontakModel
import com.gamatechno.chato.sdk.app.kontakchat.ListKontakModel
import com.gamatechno.chato.sdk.data.constant.Preferences
import com.gamatechno.chato.sdk.data.model.UserModel
import com.gamatechno.ggfw.utils.DialogBuilder
import com.gamatechno.ggfw.utils.GGFWUtil
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_kontak_chat.*
import kotlinx.android.synthetic.main.layout_helper.*

class DialogDetailBroadcast(context: Context,val activity: RoomBroadcastActivity, internal var contacts: ListKontakModel?) : DialogBuilder(context, R.layout.activity_kontak_chat), View.OnClickListener {

    lateinit var list_contact : ListKontakModel
    lateinit var adapter: KontakAdapter

    lateinit var layoutManager: LinearLayoutManager

    init {
        ButterKnife.bind(this, dialog)
        this.list_contact = contacts!!
        initKomponen()
        show()
    }

    override fun onClick(p0: View?) {

    }

    public fun addContacts(datas : MutableList<KontakModel>){
        list_contact.contacts.addAll(datas)

    }

    public fun addContacts(datas : ListKontakModel){
        list_contact.contacts.addAll(datas!!.contacts!!)

    }

    private fun initKomponen() {
        with(dialog){
            setFullScreen(lay)
            setGravity(Gravity.BOTTOM)

            toggle_kontak.display(lay_toolbar)

            helper_loading_top.setInOutAnimation(R.anim.pull_in_bottom, R.anim.push_out_top)

            img_search.setImageResource(R.drawable.ic_add_black_24dp)
            img_search.setColorFilter(ContextCompat.getColor(context, R.color.grey_800), android.graphics.PorterDuff.Mode.SRC_IN);
            img_search.setOnClickListener {
                Log.d("dialogDetailBroadcast", "onAddClick")
//                activity.startActivityForResult(Intent(context, ContactBroadcastActivity::class.java).putExtra("data", list_contact), 100)
            }

            val model = Gson().fromJson(GGFWUtil.getStringFromSP(context, Preferences.USER_LOGIN), UserModel::class.java)
            adapter = KontakAdapter(getContext(), model, list_contact!!.contacts, true, object : KontakAdapter.OnKontakAdapter {
                override fun onKontakClick(kontakModel: KontakModel, position: Int) {

                }

                override fun onKontakLongClick(kontakModel: KontakModel?, position: Int) {

                }

                override fun onMakeGroup() {
                    dismiss()
                }
            })

            img_back.setOnClickListener {
                dismiss()
            }

            tv_title.setText("Member Broadcast")


            layoutManager = LinearLayoutManager(getContext())
            rv.layoutManager = layoutManager
            rv.adapter = adapter
            rv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

                }
            })
        }
    }

    fun updateList(contacts: ListKontakModel?){
        this.list_contact = contacts!!
        adapter.updateData(list_contact.contacts)
    }
}
