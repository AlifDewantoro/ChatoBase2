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
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.gamatechno.chato.sdk.R
import com.gamatechno.chato.sdk.app.chatroom.ChatRoomActivity
import com.gamatechno.chato.sdk.app.chatrooms.adapter.RoomAdapter
import com.gamatechno.chato.sdk.app.chatrooms.adapter.RoomAdapter.OnObrolanAdapter
import com.gamatechno.chato.sdk.app.chatrooms.adapter.RoomFilterAdapter
import com.gamatechno.chato.sdk.app.chatrooms.helper.ChatRoomsHelper
import com.gamatechno.chato.sdk.app.chatrooms.uimodel.ChatRoomsUiModel
import com.gamatechno.chato.sdk.app.chatrooms.viewmodel.ChatRoomsViewModel
import com.gamatechno.chato.sdk.data.DAO.Label.LabelModel
import com.gamatechno.chato.sdk.data.DAO.RoomChat.RoomChat
import com.gamatechno.chato.sdk.data.constant.StringConstant
import com.gamatechno.chato.sdk.module.request.GGFWRest
import com.gamatechno.chato.sdk.utils.ChatoUtils
import com.gamatechno.chato.sdk.utils.ViewPagerAdapter
import com.gamatechno.ggfw.Activity.FragmentPermission
import com.gamatechno.ggfw.utils.GGFWUtil
import com.gamatechno.ggfw.utils.RecyclerScroll
import com.google.android.material.badge.BadgeDrawable
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.android.synthetic.main.activity_chat_rooms_fragment.rv
import kotlinx.android.synthetic.main.activity_chat_rooms_fragment.rv_filter
import kotlinx.android.synthetic.main.activity_chat_rooms_fragment.swipe
import kotlinx.android.synthetic.main.activity_chato_new.*
import kotlinx.android.synthetic.main.layout_helper.*

open class ChatRoomsFragmentNew : FragmentPermission(), ChatRoomsView.View {
    open var isGroup = false
    public open var roomAdapter: RoomAdapter? = null
    public open var obrolanPresenter: ChatRoomsPresenter? = null
    public open var viewModel: ChatRoomsViewModel? = null
    open var isLoadMore = true
    open var filter: IntentFilter? = null
    open var keyword = ""

    var isSearchh = false
    open var selectCount = 0
    open var needShowLoadingView = false
    lateinit var filter_adapter : RoomFilterAdapter
    open var linearLayoutManager: LinearLayoutManager? = null

    lateinit var fragmentOfficailChat: ChatRoomsFragment
    lateinit var fragmentGroupChat: ChatRoomsFragment
    lateinit var fragmentPersonalChat: ChatRoomsFragment

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
        Log.d("CreateView", "isviewcreated")
        val rootView = inflater.inflate(R.layout.activity_chato_new, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this)[ChatRoomsViewModel::class.java]
        fragmentOfficailChat = ChatRoomsFragment(RoomChat.official_room_type, viewModel!!)
        fragmentGroupChat = ChatRoomsFragment(RoomChat.group_room_type, viewModel!!)
        fragmentPersonalChat = ChatRoomsFragment(RoomChat.user_room_type, viewModel!!)

        linearLayoutManager = LinearLayoutManager(context)

        val pagerAdapter = ViewPagerAdapter(childFragmentManager,
            FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT)

        pagerAdapter.addFragments(fragmentOfficailChat, resources.getString(R.string.official))
        pagerAdapter.addFragments(fragmentGroupChat, resources.getString(R.string.group))
        pagerAdapter.addFragments(fragmentPersonalChat, resources.getString(R.string.personal))

        vpChat.adapter = pagerAdapter
        vpChat.offscreenPageLimit = pagerAdapter.count
        tlChat.setupWithViewPager(vpChat)

        for (i in 0 until tlChat.tabCount) {
            val tab = (tlChat.getChildAt(0) as ViewGroup).getChildAt(i)
            val m = tab.layoutParams as ViewGroup.MarginLayoutParams
            m.setMargins(0, 0, 0, 0)
            tab.requestLayout()
        }

        vpChat.setCurrentItem(0)
        rv.layoutManager = linearLayoutManager

        filter_adapter = RoomFilterAdapter(requireContext(), object : RoomFilterAdapter.OnRoomFilter{
            override fun onSetRoomFilter(list: MutableList<LabelModel>) {
                needShowLoadingView = true
                obrolanPresenter!!.requestObrolan(true, keyword, filter_adapter.getSortBy())
            }
        })
        filter_adapter.setDatas(ChatRoomsHelper.filtered_labels())
        rv_filter.adapter = filter_adapter

        helper_loading_top.setInOutAnimation(R.anim.pull_in_bottom, R.anim.push_out_top)
        registerReceiver()
        initObrolan()
        rv!!.addOnScrollListener(object : RecyclerScroll(linearLayoutManager) {
            override fun show() {
//                Log.d("ChatRoomsFragment", "show: ");
                viewModel!!.updateScrollStatus(true)
                fab_add!!.show()
            }

            override fun hide() {
                viewModel!!.updateScrollStatus(false)
                fab_add!!.hide()
            }

            override fun loadMore() {
                if (obrolanPresenter!!.isSuccess && !obrolanPresenter!!.isLoading) {
                    obrolanPresenter!!.requestObrolan(false, "", filter_adapter.getSortBy())
                }
            }
        })
        tv_startnewconversation.setOnClickListener { viewModel!!.updateStartChat(true) }
        swipe.setOnRefreshListener {
            keyword = ""
            obrolanPresenter!!.requestObrolan(true, "", filter_adapter.getSortBy())
        }
        observeViewModel()
        btn_tryservererror.setOnClickListener(onClickListener())
        btn_tryconnection.setOnClickListener(onClickListener())
    }

    fun isSearchShow(): Boolean{
        return wrapper_top.isDisplaying(lay_search)
    }

    fun showOrHideTopView(
        isSHow: Boolean,
        isSearch: Boolean
    ) {
        isSearchh = isSearch
        if (isSHow) {
            if (isSearchShow()) {
                if (selectCount < 1) {
                    wrapper_top.displaying(lay_top)
                    wrapper_top.hide(lay_search)
                    keyword = ""
                    edt_search.setText("")
                    viewModel!!.updateKeyword(keyword)
                    ChatoUtils.hideSoftKeyboard(context, edt_search)
                    bgMain.setBackgroundColor(resources.getColor(R.color.colorPrimaryDark))
                }
            }
        } else {
            if (isSearch) {
                ll_search.setVisibility(View.VISIBLE)
                lay_appbar.setVisibility(View.GONE)
                ChatoUtils.showKeyboard(context, edt_search)
            } else {
                ll_search.setVisibility(View.GONE)
                lay_appbar.setVisibility(View.VISIBLE)
            }
            if (!wrapper_top.isDisplaying(lay_search)) {
                wrapper_top.displaying(lay_search)
                wrapper_top.hide(lay_top)
            }
            bgMain.setBackgroundColor(resources.getColor(R.color.colorPurple10))
        }
    }

    fun updateBadge(type: String){
        when (type){
            RoomChat.official_room_type -> {
                val badgeDrawable: BadgeDrawable = tlChat.getTabAt(0)!!.orCreateBadge
                if (checkNewMessageByType("O")>0) {
                    badgeDrawable.setVisible(true)
                    badgeDrawable.backgroundColor = resources.getColor(R.color.colorRedNotif)
                    badgeDrawable.number = checkNewMessageByType("O")
                    badgeDrawable.maxCharacterCount = 2
                    //badgeDrawable.number = fragmentOfficailChat.getChatUnread()
                }else{
                    badgeDrawable.setVisible(false)
                }
            }
            RoomChat.group_room_type -> {
                val badgeDrawable: BadgeDrawable = tlChat.getTabAt(1)!!.orCreateBadge
                if (checkNewMessageByType("G")>0) {
                    badgeDrawable.setVisible(true)
                    badgeDrawable.backgroundColor = resources.getColor(R.color.colorRedNotif)
                    //badgeDrawable.number = fragmentGroupChat.getChatUnread()
                    badgeDrawable.number = checkNewMessageByType("G")
                    badgeDrawable.maxCharacterCount = 2
                }else{
                    badgeDrawable.setVisible(false)
                }
            }
            RoomChat.user_room_type -> {
                val badgeDrawable: BadgeDrawable = tlChat.getTabAt(2)!!.orCreateBadge
                if (checkNewMessageByType("D")>0) {
                    badgeDrawable.backgroundColor = resources.getColor(R.color.colorRedNotif)
                    //badgeDrawable.number = fragmentPersonalChat.getChatUnread()
                    badgeDrawable.number = checkNewMessageByType("D")
                    badgeDrawable.maxCharacterCount = 2
                }else{
                    badgeDrawable.setVisible(false)
                }
            }
        }
    }

    fun isPinOrUnpin(): Boolean{
        Log.e("position", "pos pin unpin"+tlChat.selectedTabPosition)
        when(tlChat.selectedTabPosition){
            0 -> {
                return fragmentOfficailChat.checkStatus()
            }
            1 -> {
                return fragmentGroupChat.checkStatus()
            }
            2 -> {
                return fragmentPersonalChat.checkStatus()
            }
            else -> return false
        }
    }

    private fun observeViewModel() {
        viewModel!!.initRequestDelete().observe(viewLifecycleOwner, Observer { aBoolean ->
            if (aBoolean!!) {
                if (roomAdapter!!.data != null) {
                    var model = ChatRoomsHelper.getChatListRoomClicked(roomAdapter!!.data)

                    // todo bikin multiple delete dari endpoint
                    Log.e("position", "pos "+tlChat.selectedTabPosition)
                    when(tlChat.selectedTabPosition){
                        0 -> {
                            Log.e("child", "ini log fragment parent 1")
                            /*
                            obrolanPresenter!!.pinMultipleChatRoom(ChatRoomsHelper.getChatListRoomClicked(fragmentOfficailChat.roomAdapter!!.data),
                                ChatRoomsHelper.totalPinnedChatRoom(fragmentOfficailChat.roomAdapter!!.data),
                                if (fragmentOfficailChat.checkStatus()) "UNPIN" else "PIN")

                             */
                            model = ChatRoomsHelper.getChatListRoomClicked(fragmentOfficailChat.roomAdapter!!.data)
                        }
                        1 -> {
                            Log.e("child", "ini log fragment parent 2")
                            /*
                            obrolanPresenter!!.pinMultipleChatRoom(ChatRoomsHelper.getChatListRoomClicked(fragmentGroupChat.roomAdapter!!.data),
                                ChatRoomsHelper.totalPinnedChatRoom(fragmentGroupChat.roomAdapter!!.data),
                                if (fragmentGroupChat.checkStatus()) "UNPIN" else "PIN")

                             */
                            model = ChatRoomsHelper.getChatListRoomClicked(fragmentGroupChat.roomAdapter!!.data)
                        }
                        2 -> {
                            Log.e("child", "ini log fragment parent 3")
                            /*
                            obrolanPresenter!!.pinMultipleChatRoom(ChatRoomsHelper.getChatListRoomClicked(fragmentPersonalChat.roomAdapter!!.data),
                                ChatRoomsHelper.totalPinnedChatRoom(fragmentPersonalChat.roomAdapter!!.data),
                                if (fragmentPersonalChat.checkStatus()) "UNPIN" else "PIN")

                             */
                            model = ChatRoomsHelper.getChatListRoomClicked(fragmentPersonalChat.roomAdapter!!.data)
                        }
                    }

                    obrolanPresenter!!.deleteRoom(model.get(0))
                    uncheckTheChatRoom(false)
                }
            }
        })
        /*viewModel!!.initKeyword().observe(viewLifecycleOwner, Observer { s ->

        })*/
        viewModel!!.initRequestPin().observe(viewLifecycleOwner, Observer { isRoom ->
            if (isRoom != null) {
                if (roomAdapter!!.data != null) {
                    //obrolanPresenter!!.pinChatRoom(ChatRoomsHelper.getChatRoomClicked(roomAdapter!!.data), ChatRoomsHelper.totalPinnedChatRoom(roomAdapter!!.data))

                    Log.e("position", "pos "+tlChat.selectedTabPosition)
                    when(tlChat.selectedTabPosition){
                        0 -> {
                            Log.e("child", "ini log fragment parent 1")
                            obrolanPresenter!!.pinMultipleChatRoom(ChatRoomsHelper.getChatListRoomClicked(fragmentOfficailChat.roomAdapter!!.data),
                                ChatRoomsHelper.totalPinnedChatRoom(fragmentOfficailChat.roomAdapter!!.data),
                                if (fragmentOfficailChat.checkStatus()) "UNPIN" else "PIN")
                        }
                        1 -> {
                            Log.e("child", "ini log fragment parent 2")
                            obrolanPresenter!!.pinMultipleChatRoom(ChatRoomsHelper.getChatListRoomClicked(fragmentGroupChat.roomAdapter!!.data),
                                ChatRoomsHelper.totalPinnedChatRoom(fragmentGroupChat.roomAdapter!!.data),
                                if (fragmentGroupChat.checkStatus()) "UNPIN" else "PIN")
                        }
                        2 -> {
                            Log.e("child", "ini log fragment parent 3")
                            obrolanPresenter!!.pinMultipleChatRoom(ChatRoomsHelper.getChatListRoomClicked(fragmentPersonalChat.roomAdapter!!.data),
                                ChatRoomsHelper.totalPinnedChatRoom(fragmentPersonalChat.roomAdapter!!.data),
                                if (fragmentPersonalChat.checkStatus()) "UNPIN" else "PIN")
                        }
                    }
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
                swipe.isRefreshing = false
                keyword = ""
                obrolanPresenter!!.requestObrolan(true, "", filter_adapter.getSortBy())
            }
        }
    }

    private fun initObrolan() {
        obrolanPresenter = ChatRoomsPresenter(context, this)
        roomAdapter = RoomAdapter(context, "all", object : OnObrolanAdapter {
            override fun onClickObrolan(model: ChatRoomsUiModel) {
                if (ChatRoomsHelper.getChatRoomClicked(roomAdapter!!.data) != null) {
                    if (ChatRoomsHelper.getIndexChatRoom(roomAdapter!!.data, model) != -1) {
                        if (ChatRoomsHelper.getChatRoomClicked(roomAdapter!!.data)?.roomChat?.room_id == model.roomChat.room_id) {
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
            }

            override fun onLongClick(chatRoomUiModel: ChatRoomsUiModel) {
                if (ChatRoomsHelper.getChatRoomClicked(roomAdapter!!.data) != null) {
                    if (ChatRoomsHelper.getIndexChatRoom(roomAdapter!!.data, chatRoomUiModel) != -1) {
                        if (ChatRoomsHelper.getChatRoomClicked(roomAdapter!!.data)?.roomChat?.room_id == chatRoomUiModel.roomChat.room_id) {
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
            }
        })
        rv!!.adapter = roomAdapter
    }

    fun checkNewMessage(): Boolean{
        var result = false
        for (item in roomAdapter!!.data){
            if (item.roomChat.unread_message > 0){
                result = true
            }
        }
        return result
    }

    private fun checkNewMessageByType(type: String): Int{
        var result = 0
        for (item in roomAdapter!!.data){
            if (item.roomChat.room_type.equals(type)){
                result += item.roomChat.unread_message
            }
        }
        return result
    }

    private fun checkTheChatRoom(i: Int, isOK: Boolean) {
        for (j in roomAdapter!!.data.indices) {
            if (j == i) {
                roomAdapter!!.data[j].is_checked = isOK
            } else {
                roomAdapter!!.data[j].is_checked = !isOK
            }
        }
    }

    fun uncheckTheChatRoom(isOk: Boolean) {
        for (j in roomAdapter!!.data.indices) {
            roomAdapter!!.data[j].is_checked = isOk
        }
        roomAdapter!!.notifyDataSetChanged()
    }

    override fun onLoading() {
        helper_noconversation.visibility = View.GONE
        helper_noconnection.visibility = View.GONE
        helper_servererror.visibility = View.GONE
        if (roomAdapter!!.data.size == 0) {
            roomAdapter!!.initLoading(true)
            swipe.isRefreshing = false
        } else {
            if(needShowLoadingView){
                swipe.isRefreshing = false
                roomAdapter!!.initLoading(true)
            }
        }
    }

    override fun onHideLoading() {
        try {
            helper_loading_top.hide()
            if (roomAdapter!!.data.size == 0 || needShowLoadingView) {
                needShowLoadingView = false
                roomAdapter!!.initLoading(false)
                if (swipe.isRefreshing) swipe.isRefreshing = false
            } else {
                if (swipe.isRefreshing) swipe.isRefreshing = false
            }
        } catch (e : Exception){

        }
    }

    override fun onErrorConnection(message: String) {
        if (roomAdapter!!.data.size == 0) {
            when (message) {
                GGFWRest.CODE_SERVERERROR -> helper_servererror.visibility = View.VISIBLE
                GGFWRest.CODE_NETWORKERROR -> helper_noconnection.visibility = View.VISIBLE
                GGFWRest.CODE_NOCONNECTIONERROR -> helper_noconnection.visibility = View.VISIBLE
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
        try {
            isLoadMore = true
            helper_noconversation.visibility = View.GONE
            roomAdapter!!.addData(isRefresh, list)
            if (roomAdapter!!.data.size == 0) {
                helper_noconversation.visibility = View.VISIBLE
            } else {
                helper_noconversation.visibility = View.GONE
                updateBadge(RoomChat.official_room_type)
                updateBadge(RoomChat.group_room_type)
                updateBadge(RoomChat.user_room_type)
            }

        } catch (e : Exception){
            Log.e(TAG, "error : " + e.localizedMessage)
        }

    }

    override fun successPinnedChatRoom(message: String?) {
        GGFWUtil.ToastShort(context, message)
        obrolanPresenter!!.requestObrolan(true, keyword, filter_adapter.getSortBy())
    }

    override fun successPinnedMultipleChatRoom(message: String?) {
        GGFWUtil.ToastShort(context, message)
        Log.e("position", "pos "+tlChat.selectedTabPosition)
        when(tlChat.selectedTabPosition){
            0 -> {
                fragmentOfficailChat.successPinnedMultipleChatRoom(message)
            }
            1 -> {
                fragmentGroupChat.successPinnedMultipleChatRoom(message)
            }
            2 -> {
                fragmentPersonalChat.successPinnedMultipleChatRoom(message)
            }
        }
        selectCount = 0
        showOrHideTopView(true, false)
        //obrolanPresenter!!.requestObrolan(true, keyword, filter_adapter.getSortBy())
    }

    override fun onDeleteRoom(isSuccess: Boolean, message: String?) {
        GGFWUtil.ToastShort(context, message)
        if (isSuccess) {
            Log.e("position", "pos "+tlChat.selectedTabPosition)
            when(tlChat.selectedTabPosition){
                0 -> {
                    fragmentOfficailChat.onDeleteRoom(isSuccess, message)
                }
                1 -> {
                    fragmentGroupChat.onDeleteRoom(isSuccess, message)
                }
                2 -> {
                    fragmentPersonalChat.onDeleteRoom(isSuccess, message)
                }
            }
            //obrolanPresenter!!.requestObrolan(true, keyword, filter_adapter.getSortBy())
        }
        selectCount = 0
        showOrHideTopView(true, false)
    }

    override fun onFailedRequestObrolan() {
        try {
            if (roomAdapter!!.data.size == 0 || needShowLoadingView) {
                helper_noconversation.visibility = View.VISIBLE
                needShowLoadingView = false
            }
        } catch (e : Exception){

        }
    }

    override fun onFailedPin(message: String?) {
        selectCount = 0
        showOrHideTopView(true, false)
    }

    override fun onFailedDelete(message: String?) {
        selectCount = 0
        showOrHideTopView(true, false)
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
    }

    companion object {
        val TAG = ChatRoomsFragmentNew::class.java.simpleName
        /*@JvmStatic
        fun newInstance(isGroup: Boolean, viewModel: ChatRoomsViewModel): ChatRoomsFragmentNew {
            val fragment = ChatRoomsFragmentNew()
            fragment.isGroup = isGroup
            fragment.viewModel = viewModel
            return fragment
        }*/
    }
}