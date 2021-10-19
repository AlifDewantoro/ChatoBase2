package com.gamatechno.chato.sdk.app.kontakchat


import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.ButterKnife
import com.gamatechno.chato.sdk.R
import com.gamatechno.chato.sdk.data.constant.Preferences
import com.gamatechno.chato.sdk.data.model.UserModel
import com.gamatechno.chato.sdk.module.request.GGFWRest.*
import com.gamatechno.chato.sdk.utils.ChatoUtils
import com.gamatechno.ggfw.utils.DialogBuilder
import com.gamatechno.ggfw.utils.GGFWUtil
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_kontak_chat.*
import kotlinx.android.synthetic.main.layout_helper.*
import java.util.*

class KontakChatDialog(context: Context,
                       internal var isNeedAddGroupIcon: Boolean?,
                       internal var isForwarded: Boolean?,
                       internal var onKontakChatDialog: OnKontakChatDialog)
    : DialogBuilder(context, R.layout.activity_kontak_chat), KontakView.View, View.OnClickListener {

    internal var presenter: KontakPresenter

    lateinit var adapter: KontakAdapter
    var kontakModels: MutableList<KontakModel>
    lateinit var layoutManager: LinearLayoutManager

    internal var isSearch = false
    internal var isLoadMore = true

    private var timer = Timer()
    private val DELAY: Long = 1000 // milliseconds

    init {
        ButterKnife.bind(this, dialog)
        kontakModels = ArrayList()
        presenter = KontakPresenter(getContext(), this)
        initKomponen()

        show()
    }

    private fun initKomponen() {
        with(dialog){
            setFullScreen(lay)
            setGravity(Gravity.BOTTOM)

            toggle_kontak.display(lay_toolbar)

            helper_loading_top.setInOutAnimation(R.anim.pull_in_bottom, R.anim.push_out_top)

            val model = Gson().fromJson(GGFWUtil.getStringFromSP(context, Preferences.USER_LOGIN), UserModel::class.java)
            adapter = KontakAdapter(getContext(), model, kontakModels, true, object : KontakAdapter.OnKontakAdapter {
                override fun onKontakClick(kontakModel: KontakModel, position: Int) {
                    if(kontakModel.room_id==0){
                        presenter.createRoomId(kontakModel)
                    } else {
                        onKontakChatDialog.onClickKontak(kontakModel)
                        dismiss()
                    }
                }

                override fun onKontakLongClick(kontakModel: KontakModel?, position: Int) {

                }

                override fun onMakeGroup() {
                    dismiss()
                    onKontakChatDialog.onAddGroup()
                }
            })

            img_back.setOnClickListener {
                if (isSearch) {
                    disableSearch(true)
                } else {
                    dismiss()
                }
            }

            tv_title.setOnClickListener { enableSearch() }

            img_search.setOnClickListener { enableSearch() }


            edt_search.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    ChatoUtils.hideSoftKeyboard(getContext(), edt_search)
                    return@OnEditorActionListener true
                }
                false
            })

            edt_search.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

                }

                override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                    if (isSearch) {
                        timer.cancel()
                        timer = Timer()
                        timer.schedule(
                                object : TimerTask() {
                                    override fun run() {
                                        if (charSequence.toString() == "") {
                                            presenter.requestKontak(true, isForwarded)
                                        } else {
                                            presenter.searchUser(charSequence.toString(), true)
                                        }
                                    }
                                },
                                DELAY
                        )
                    }
                }

                override fun afterTextChanged(editable: Editable) {

                }
            })

            lay_refresh.setOnRefreshListener {
                lay_refresh.isRefreshing = false
                disableSearch(true)
                //                presenter.requestKontak(true);
            }

            layoutManager = LinearLayoutManager(getContext())
            rv.layoutManager = layoutManager
            rv.adapter = adapter
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
                                    presenter.requestKontak(false, isForwarded)
                                } else {
                                    presenter.searchUser(edt_search.text.toString(), false)
                                }
                            }
                        }
                    }
                }
            })
            btn_tryservererror.setOnClickListener(this@KontakChatDialog)
            btn_tryconnection.setOnClickListener(this@KontakChatDialog)
        }
    }

    override fun onCreateRoomId(model: KontakModel?) {
        onKontakChatDialog.onClickKontak(model!!)
        dismiss()
    }

    override fun onRequestKontak(models: List<KontakModel>, groupmodels: List<KontakModel>, isRefresh: Boolean) {
        isLoadMore = true
        if (isRefresh) {
            kontakModels.clear()
            if(models.size > 0 || groupmodels.size > 0){
                if (isNeedAddGroupIcon!!) {
                    kontakModels.add(KontakModel("Buat Grup", true))
                }

                if(groupmodels.size > 0 && isForwarded!!){
                    kontakModels.add(KontakModel("Daftar Grup", true, 0))
                    for (model in groupmodels) {
                        kontakModels.add(model)
                    }
                }
                if(models.size > 0 ){
                    kontakModels.add(KontakModel("Daftar Kontak", true, 0))
                }
            } else {
                dialog.helper_nodata_text.visibility = View.VISIBLE
                dialog.tv_nodata_text.setText(dialog.edt_search.text.toString()+" tidak ditemukan")
            }
        }

        for (model in models) {
            kontakModels.add(model)
        }
        adapter.notifyDataSetChanged()

        with(dialog){
            if (kontakModels.size == 0) {
                helper_nodata.visibility = View.VISIBLE
            } else {
                helper_nodata.visibility = View.GONE
            }
        }
    }

    override fun onFailedRequestKontak(isRefresh: Boolean) {
        activity.runOnUiThread {
            with(dialog){
                if (isRefresh) {
                    if (isSearch) {
                        kontakModels.clear()
                        helper_nodata_text.visibility = View.VISIBLE
                    } else {
                        if (kontakModels.size == 0) {
                            helper_nodata_text.visibility = View.VISIBLE
                        }
                    }
                    adapter.notifyDataSetChanged()
                }
            }
        }
    }

    override fun onLoadMore() {
        activity.runOnUiThread {
            with(dialog){
                helper_loading_more.visibility = View.VISIBLE
            }
        }
    }

    override fun onLoading() {
        activity.runOnUiThread {
            with(dialog){
                helper_nodata_text.visibility = View.GONE
                helper_nodata.visibility = View.GONE
                helper_loading_top.show()
                helper_noconnection.visibility = View.GONE
                helper_servererror.visibility = View.GONE
            }
        }
    }

    override fun onHideLoading() {
        activity.runOnUiThread {
            with(dialog){
                helper_loading_top.hide()
                helper_loading_more.visibility = View.GONE
            }
        }
    }

    override fun onErrorConnection(message: String) {
        activity.runOnUiThread {
            with(dialog){
                if(kontakModels.size == 0) {
                    when(message){
                        CODE_SERVERERROR -> {
                            helper_servererror.visibility = View.VISIBLE
                        }
                        CODE_NETWORKERROR -> {
                            helper_noconnection.visibility = View.VISIBLE
                        }
                        CODE_NOCONNECTIONERROR -> {
                            helper_noconnection.visibility = View.VISIBLE
                        }
                        else -> {
                            GGFWUtil.ToastShort(context, message)
                        }
                    }
                } else {
                    GGFWUtil.ToastLong(context, message)
                }

            }
        }
    }

    override fun onAuthFailed(error: String) {

    }

    interface OnKontakChatDialog {
        fun onClickKontak(model: KontakModel)
        fun onAddGroup()
    }

    override fun show() {
        super.show()
        kontakModels.clear()
        adapter.notifyDataSetChanged()
        disableSearch(true)
        //presenter.requestKontak(true, isForwarded)
    }

    override fun dismiss() {
        super.dismiss()
        disableSearch(false)
    }

    private fun enableSearch() {
        isSearch = true
        with(dialog){
            toggle_kontak.display(edt_search)
            ChatoUtils.showKeyboard(getContext(), edt_search)
        }
    }

    private fun disableSearch(isNeedToRefresh: Boolean) {
        isSearch = false
        with(dialog){
            toggle_kontak.display(lay_toolbar)

            if (edt_search.text.toString() != "") {
                edt_search.setText("")
            }
            ChatoUtils.hideSoftKeyboard(getContext(), edt_search)

            if (isNeedToRefresh)
                presenter.requestKontak(true, isForwarded)
        }
    }

    fun setViewOnClickEvent(view: View) {
        if (view.id == R.id.btn_tryservererror || view.id == R.id.btn_tryconnection) {
            presenter.searchUser(dialog.edt_search.text.toString(), false)
        }
    }

    override fun onClick(p0: View?) {
        setViewOnClickEvent(p0!!)
    }
}