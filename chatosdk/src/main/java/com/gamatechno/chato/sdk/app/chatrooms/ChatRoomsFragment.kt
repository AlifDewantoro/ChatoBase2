package com.gamatechno.chato.sdk.app.chatrooms

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.gamatechno.chato.sdk.R
import com.gamatechno.chato.sdk.app.chatroom.ChatRoomActivity
import com.gamatechno.chato.sdk.app.chatrooms.adapter.RoomAdapter
import com.gamatechno.chato.sdk.app.chatrooms.adapter.RoomAdapter.OnObrolanAdapter
import com.gamatechno.chato.sdk.app.chatrooms.adapter.RoomFilterAdapter
import com.gamatechno.chato.sdk.app.chatrooms.helper.ChatRoomsHelper
import com.gamatechno.chato.sdk.app.chatrooms.uimodel.ChatRoomsUiModel
import com.gamatechno.chato.sdk.app.chatrooms.viewmodel.ChatRoomsViewModel
import com.gamatechno.chato.sdk.app.main.MainViewModel
import com.gamatechno.chato.sdk.data.DAO.Label.LabelModel
import com.gamatechno.chato.sdk.data.constant.StringConstant
import com.gamatechno.chato.sdk.module.request.GGFWRest
import com.gamatechno.ggfw.utils.GGFWUtil
import com.gamatechno.ggfw.utils.RecyclerScroll
import com.google.firebase.iid.FirebaseInstanceId
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_chat_rooms_fragment.*
import kotlinx.android.synthetic.main.layout_helper.*

class ChatRoomsFragment(var type: String, var viewModel: ChatRoomsViewModel?) : Fragment(), ChatRoomsView.View {
    var isGroup = false
    var roomAdapter: RoomAdapter? = null
    var obrolanPresenter: ChatRoomsPresenter? = null
    //var viewModel: ChatRoomsViewModel? = ChatRoomsViewModel()
    var model: MainViewModel? = null
    var isLoadMore = true
    var filter: IntentFilter? = null
    var keyword = ""
    var needShowLoadingView = false
    lateinit var filter_adapter : RoomFilterAdapter
    var linearLayoutManager: LinearLayoutManager? = null
    private val receiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            Log.d(TAG, "Receiving....")
            if (action == StringConstant.broadcast_refresh_chat) {
                obrolanPresenter!!.requestObrolan(true, keyword, filter_adapter.getSortBy())
            }
        }
    }

    private fun registerReceiver() {
        filter = IntentFilter()
        filter!!.addAction(StringConstant.broadcast_refresh_chat)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.activity_chat_rooms_fragment, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        linearLayoutManager = LinearLayoutManager(context)
        rv.layoutManager = linearLayoutManager

        filter_adapter = RoomFilterAdapter(requireContext(), object : RoomFilterAdapter.OnRoomFilter{
            override fun onSetRoomFilter(list: MutableList<LabelModel>) {
                needShowLoadingView = true
                obrolanPresenter!!.requestObrolan(true, keyword, filter_adapter.getSortBy())
            }
        })
        filter_adapter.setDatas(ChatRoomsHelper.filtered_labels())
        rv_filter.adapter = filter_adapter

        helper_loading_top!!.setInOutAnimation(R.anim.pull_in_bottom, R.anim.push_out_top)
        registerReceiver()
        initObrolan()
        rv!!.addOnScrollListener(object : RecyclerScroll(linearLayoutManager) {
            override fun show() {
//                Log.d("ChatRoomsFragment", "show: ");
                viewModel!!.updateScrollStatus(true)
            }

            override fun hide() {
                viewModel!!.updateScrollStatus(false)
            }

            override fun loadMore() {
                if (obrolanPresenter!!.isSuccess && !obrolanPresenter!!.isLoading) {
                    obrolanPresenter!!.requestObrolan(false, "", filter_adapter.getSortBy())
                }
            }
        })
        tv_startnewconversation!!.setOnClickListener { viewModel!!.updateStartChat(true) }
        swipe!!.setOnRefreshListener {
            keyword = ""
            obrolanPresenter!!.requestObrolan(true, "", filter_adapter.getSortBy())
        }
        observeViewModel()
        btn_tryservererror!!.setOnClickListener(onClickListener())
        btn_tryconnection!!.setOnClickListener(onClickListener())
    }

    fun checkStatus(): Boolean{
        for (item in roomAdapter!!.data){
            if (item.is_checked && item.roomChat.is_pined == 1){
                return true
            }
        }
        return false
    }

    private fun observeViewModel() {
        viewModel!!.initRequestDelete().observe(viewLifecycleOwner, Observer { aBoolean ->
            if (aBoolean!!) {
                if (roomAdapter!!.data != null) {
                    /*
                    val model = ChatRoomsHelper.getChatRoomClicked(roomAdapter!!.data)
                    obrolanPresenter!!.deleteRoom(model)

                     */
                    uncheckTheChatRoom(false)
                }
            }
        })
        viewModel!!.initKeyword().observe(viewLifecycleOwner, Observer { s ->
            if (s == "") {
                keyword = s
                uncheckTheChatRoom(false)
            }
        })
        viewModel!!.initRequestPin().observe(viewLifecycleOwner, Observer { isRoom ->
            if (isRoom != null) {
                if (roomAdapter!!.data != null) {
                    /*
                    Log.e("child", "ini log fragment child")
                    obrolanPresenter!!.pinMultipleChatRoom(ChatRoomsHelper.getChatListRoomClicked(roomAdapter!!.data),
                        ChatRoomsHelper.totalPinnedChatRoom(roomAdapter!!.data),
                        if (checkStatus()) "PIN" else "UNPIN")
                     */
                    uncheckTheChatRoom(false)

                }
            }
        })
        viewModel!!.initChatRoomClickFromSearch().observe(viewLifecycleOwner, Observer { model ->
            if (ChatRoomsHelper.getChatRoomClicked(roomAdapter!!.data) != null) {
                if (ChatRoomsHelper.getIndexChatRoom(roomAdapter!!.data, model) != -1) {
                    if (ChatRoomsHelper.getChatRoomClicked(roomAdapter!!.data)?.roomChat?.room_id == model!!.roomChat.room_id) {
                        roomAdapter!!.data[ChatRoomsHelper.getIndexChatRoom(roomAdapter!!.data, model)].is_checked = false
                    } else {
                        checkTheChatRoom(ChatRoomsHelper.getIndexChatRoom(roomAdapter!!.data, model), true)
                    }
                    viewModel!!.updateChatRoomsLongPress(model)
                }
            } else {
                val intent = Intent(context, ChatRoomActivity::class.java)
                intent.putExtra("chatroom", model)
                startActivity(intent)
            }
            roomAdapter!!.notifyDataSetChanged()
        })
        viewModel!!.initChatRoomLongPressFromSearch().observe(viewLifecycleOwner, Observer { chatRoomUiModel ->
            if (ChatRoomsHelper.getChatRoomClicked(roomAdapter!!.data) != null) {
                if (ChatRoomsHelper.getIndexChatRoom(roomAdapter!!.data, chatRoomUiModel) != -1) {
                    if (ChatRoomsHelper.getChatRoomClicked(roomAdapter!!.data)?.roomChat?.room_id == chatRoomUiModel!!.roomChat.room_id) {
                        roomAdapter!!.data[ChatRoomsHelper.getIndexChatRoom(roomAdapter!!.data, chatRoomUiModel)].is_checked = false
                    } else {
                        checkTheChatRoom(ChatRoomsHelper.getIndexChatRoom(roomAdapter!!.data, chatRoomUiModel), true)
                    }
                    viewModel!!.updateChatRoomsLongPress(chatRoomUiModel)
                }
            } else {
                if (ChatRoomsHelper.getIndexChatRoom(roomAdapter!!.data, chatRoomUiModel) != -1) {
                    checkTheChatRoom(ChatRoomsHelper.getIndexChatRoom(roomAdapter!!.data, chatRoomUiModel), true)
                    viewModel!!.updateChatRoomsLongPress(chatRoomUiModel)
                }
            }
            roomAdapter!!.notifyDataSetChanged()
        })
        viewModel!!.initRefreshRoom().observe(viewLifecycleOwner, Observer { obrolanPresenter!!.requestObrolan(true, "", filter_adapter.getSortBy()) })
    }

    fun onClickListener(): View.OnClickListener {
        return View.OnClickListener { view ->
            if (view.id == R.id.btn_tryservererror || view.id == R.id.btn_tryconnection) {
                swipe!!.isRefreshing = false
                keyword = ""
                obrolanPresenter!!.requestObrolan(true, "", filter_adapter.getSortBy())
            }
        }
    }

    fun getChatUnread(): Int{
        return roomAdapter!!.sumUnreadMessage
    }

    private fun initObrolan() {
        obrolanPresenter = ChatRoomsPresenter(context, this)
        roomAdapter = RoomAdapter(context, type, object : OnObrolanAdapter {
            override fun onClickObrolan(model: ChatRoomsUiModel) {
                if (ChatRoomsHelper.getChatRoomClicked(roomAdapter!!.data) != null) {
                    if (ChatRoomsHelper.getIndexChatRoom(roomAdapter!!.data, model) != -1) {
                        if (ChatRoomsHelper.getChatRoomClicked(roomAdapter!!.data)?.roomChat?.room_id == model.roomChat.room_id) {
                            roomAdapter!!.data[ChatRoomsHelper.getIndexChatRoom(roomAdapter!!.data, model)].is_checked = false
                        } else {
                            if (model.is_checked){
                                checkTheChatRoom(ChatRoomsHelper.getIndexChatRoom(roomAdapter!!.data, model), false)
                            }else {
                                checkTheChatRoom(ChatRoomsHelper.getIndexChatRoom(roomAdapter!!.data, model), true)
                            }
                            //checkTheChatRoom(ChatRoomsHelper.getIndexChatRoom(roomAdapter!!.data, model), true)
                        }
                        viewModel!!.updateChatRoomsLongPress(model)
                    }
                } else {
                    val intent = Intent(context, ChatRoomActivity::class.java)
                    intent.putExtra("chatroom", model)
                    startActivity(intent)
                }
                roomAdapter!!.notifyDataSetChanged()
            }

            override fun onLongClick(chatRoomUiModel: ChatRoomsUiModel) {
                if (ChatRoomsHelper.getChatRoomClicked(roomAdapter!!.data) != null) {
                    if (ChatRoomsHelper.getIndexChatRoom(roomAdapter!!.data, chatRoomUiModel) != -1) {
                        if (ChatRoomsHelper.getChatRoomClicked(roomAdapter!!.data)?.roomChat?.room_id == chatRoomUiModel.roomChat.room_id) {
                            roomAdapter!!.data[ChatRoomsHelper.getIndexChatRoom(roomAdapter!!.data, chatRoomUiModel)].is_checked = false
                        } else {
                            if (chatRoomUiModel.is_checked){
                                checkTheChatRoom(ChatRoomsHelper.getIndexChatRoom(roomAdapter!!.data, chatRoomUiModel), false)
                            }else {
                                checkTheChatRoom(ChatRoomsHelper.getIndexChatRoom(roomAdapter!!.data, chatRoomUiModel), true)
                            }
                            //checkTheChatRoom(ChatRoomsHelper.getIndexChatRoom(roomAdapter!!.data, chatRoomUiModel), true)
                        }
                        viewModel!!.updateChatRoomsLongPress(chatRoomUiModel)
                    }
                } else {
                    if (ChatRoomsHelper.getIndexChatRoom(roomAdapter!!.data, chatRoomUiModel) != -1) {
                        checkTheChatRoom(ChatRoomsHelper.getIndexChatRoom(roomAdapter!!.data, chatRoomUiModel), true)
                        viewModel!!.updateChatRoomsLongPress(chatRoomUiModel)
                    }
                }
                roomAdapter!!.notifyDataSetChanged()
            }
        })
        rv!!.adapter = roomAdapter
    }

    private fun checkTheChatRoom(i: Int, isOK: Boolean) {
        for (j in roomAdapter!!.data.indices) {
            if (j == i) {
                roomAdapter!!.data[j].is_checked = isOK
            }
            /*
            else {
                roomAdapter!!.data[j].is_checked = !isOK
            }

             */
        }
    }

    private fun uncheckTheChatRoom(isOk: Boolean) {
        for (j in roomAdapter!!.data.indices) {
            roomAdapter!!.data[j].is_checked = isOk
        }
        roomAdapter!!.notifyDataSetChanged()
    }

    override fun onLoading() {
        helper_noconversation!!.visibility = View.GONE
        helper_noconnection!!.visibility = View.GONE
        helper_servererror!!.visibility = View.GONE
        if (roomAdapter!!.data.size == 0) {
            roomAdapter!!.initLoading(true)
        } else {
            if(needShowLoadingView){
                swipe!!.isRefreshing = false
                roomAdapter!!.initLoading(true)
            }
        }
    }

    override fun onHideLoading() {
        if (helper_loading_top!=null) {
            helper_loading_top!!.hide()
        }
        if (roomAdapter!!.data.size == 0 || needShowLoadingView) {
            needShowLoadingView = false
            roomAdapter!!.initLoading(false)
        }

        if (swipe!!.isRefreshing) swipe!!.isRefreshing = false
    }

    override fun onErrorConnection(message: String) {
        if (roomAdapter!!.data.size == 0) {
            when (message) {
                GGFWRest.CODE_SERVERERROR -> helper_servererror!!.visibility = View.VISIBLE
                GGFWRest.CODE_NETWORKERROR -> helper_noconnection!!.visibility = View.VISIBLE
                GGFWRest.CODE_NOCONNECTIONERROR -> helper_noconnection!!.visibility = View.VISIBLE
                else -> GGFWUtil.ToastShort(context, message)
            }
        } else {
            GGFWUtil.ToastShort(context, message)
        }
    }

    override fun onAuthFailed(error: String) {
        GGFWUtil.ToastShort(context, error)
    }


    override fun onRequestObrolan(list: List<ChatRoomsUiModel?>?, isRefresh: Boolean) {
        isLoadMore = true
        helper_noconversation!!.visibility = View.GONE
        Log.d("KENALOG", "list chat1 ${Gson().toJson(list)}")
        Log.d("KENALOG", "list chat2 ${list.toString()}")
        roomAdapter!!.addData(isRefresh, list)
        if (roomAdapter!!.data.size == 0) helper_noconversation!!.visibility = View.VISIBLE else helper_noconversation!!.visibility = View.GONE

        (parentFragment as ChatRoomsFragmentNew).updateBadge(type)
    }

    override fun successPinnedChatRoom(message: String?) {
        GGFWUtil.ToastShort(context, message)
        obrolanPresenter!!.requestObrolan(true, keyword, filter_adapter.getSortBy())
    }

    override fun successPinnedMultipleChatRoom(message: String?) {
        GGFWUtil.ToastShort(context, message)
        obrolanPresenter!!.requestObrolan(true, keyword, filter_adapter.getSortBy())
    }

    override fun onDeleteRoom(isSuccess: Boolean, message: String?) {
        GGFWUtil.ToastShort(context, message)
        if (isSuccess) {
            obrolanPresenter!!.requestObrolan(true, keyword, filter_adapter.getSortBy())
        }
    }

    override fun onFailedRequestObrolan() {
        if (roomAdapter!!.data.size == 0 || needShowLoadingView) {
            helper_noconversation!!.visibility = View.VISIBLE
            needShowLoadingView = false
        }
    }

    override fun onFailedPin(message: String?) {
        // sudah di eksekusi parent fragment
    }

    override fun onFailedDelete(message: String?) {
        // sudah di eksekusi parent fragment
    }


    override fun onStop() {
        try {
            if (context != null) context!!.unregisterReceiver(receiver)
        } catch (e: Exception) {
            e.message
        }
        super.onStop()
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume: " + FirebaseInstanceId.getInstance().token)
        context!!.registerReceiver(receiver, filter)
        obrolanPresenter!!.requestObrolan(true, keyword, filter_adapter.getSortBy())

        /*
        model!!.initClick().observe(this, Observer {
            roomAdapter!!.filterChat(it)
        })

         */
    }

    companion object {
        private val TAG = ChatRoomsFragment::class.java.simpleName
        @JvmStatic
        fun newInstance(isGroup: Boolean, viewModel: ChatRoomsViewModel, model: MainViewModel): ChatRoomsFragment {
            val type = if (isGroup) "group" else "personal"
            val fragment = ChatRoomsFragment(type, ChatRoomsViewModel())
            fragment.isGroup = isGroup
            fragment.viewModel = viewModel
            fragment.model = model
            return fragment
        }
    }
}