package com.gamatechno.chato.sdk.app.sharedmedia.fragment.sharedimage

import android.content.Context
import android.graphics.Canvas
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gamatechno.chato.sdk.R
import com.gamatechno.chato.sdk.app.chatroom.model.ChatRoomUiModel
import com.gamatechno.chato.sdk.app.sharedmedia.adapter.SharedMediaImageAdapter
import com.gamatechno.chato.sdk.data.DAO.Chat.Chat
import com.gamatechno.chato.sdk.data.constant.Api
import kotlinx.android.synthetic.main.fragment_shared_media.*

class SharedMediaFragment: Fragment(), SharedMediaView.View {

    lateinit var adapter: SharedMediaImageAdapter
    lateinit var presenter: SharedMediaPresenter
//    lateinit var roomId : String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_shared_media,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter = SharedMediaPresenter(context, this)

        initList()
    }

    private fun initList() {
        rv_list.layoutManager = GridLayoutManager(context,3)
        adapter = SharedMediaImageAdapter(context!!)
        rv_list.adapter = adapter
        rv_list.addItemDecoration(Divider(context!!,Divider.ALL))
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if(activity!!.intent.hasExtra("data")) {
            val chatRoomUiModel = activity!!.intent.getSerializableExtra("data") as ChatRoomUiModel
            //if (activity!!.intent.hasExtra("sharedMedia")) {
                presenter.getList(Api.list_room_media(chatRoomUiModel.room_id, chatRoomUiModel.user_id, "media"))
            //}
        }
    }

    override fun setListView(list: ArrayList<Chat>) {
        adapter.addAll(list)
        rl_no_data.visibility = View.GONE
    }

    override fun setEmptyView() {
        rl_no_data.visibility = View.VISIBLE
    }

    override fun onLoading() {

    }

    override fun onHideLoading() {

    }

    override fun onErrorConnection(message: String?) {

    }

    override fun onAuthFailed(error: String?) {

    }

    class Divider(context: Context, orientation: Int) : RecyclerView.ItemDecoration() {

        private var mDivider: Drawable? = null

        /**
         * Current orientation. Either [.HORIZONTAL] or [.VERTICAL].
         */
        private var mOrientation: Int = 0

        private val mBounds = Rect()

        init {
            val a = context.obtainStyledAttributes(ATTRS)
            mDivider = a.getDrawable(0)
            if (mDivider == null) {
            }
            a.recycle()
            mDivider!!.colorFilter = PorterDuffColorFilter(ContextCompat.getColor(context,R.color.white),
                    PorterDuff.Mode.SRC_ATOP)
            setOrientation(orientation)
        }
        override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
            if (parent.layoutManager == null || mDivider == null) {
                return
            }

            when (mOrientation) {
                ALL -> {
                    drawVertical(c, parent)
                    drawHorizontal(c, parent)
                }
                VERTICAL -> drawVertical(c, parent)
                else -> drawHorizontal(c, parent)
            }
        }

        private fun setOrientation(orientation: Int) {
            if (orientation != HORIZONTAL && orientation != VERTICAL && orientation != ALL) {
                throw IllegalArgumentException(
                        "Invalid orientation. It should be either HORIZONTAL or VERTICAL"
                )
            }
            mOrientation = orientation
        }

        private fun drawVertical(canvas: Canvas, parent: RecyclerView) {
            canvas.save()
            val left: Int
            val right: Int

            if (parent.clipToPadding) {
                left = parent.paddingLeft
                right = parent.width - parent.paddingRight
                canvas.clipRect(
                        left, parent.paddingTop, right,
                        parent.height - parent.paddingBottom
                )
            } else {
                left = 0
                right = parent.width
            }

            var childCount = parent.childCount
            if (parent.layoutManager is GridLayoutManager) {
                var leftItems = childCount % (parent.layoutManager as GridLayoutManager).spanCount
                if (leftItems == 0) {
                    leftItems = (parent.layoutManager as GridLayoutManager).spanCount
                }
                //Identify last row, and don't draw border for these items
                childCount -= leftItems
            }

            for (i in 0 until childCount - 1) {
                val child = parent.getChildAt(i) ?: return
                parent.getDecoratedBoundsWithMargins(child, mBounds)
                val bottom = mBounds.bottom + Math.round(child.translationY)
                val top = bottom - mDivider!!.intrinsicHeight
                mDivider!!.setBounds(left, top, right, bottom)
                mDivider!!.draw(canvas)
            }
            canvas.restore()
        }

        private fun drawHorizontal(canvas: Canvas, parent: RecyclerView) {
            canvas.save()
            val top: Int
            val bottom: Int

            if (parent.clipToPadding) {
                top = parent.paddingTop
                bottom = parent.height - parent.paddingBottom
                canvas.clipRect(
                        parent.paddingLeft, top,
                        parent.width - parent.paddingRight, bottom
                )
            } else {
                top = 0
                bottom = parent.height
            }

            var childCount = parent.childCount
            if (parent.layoutManager is GridLayoutManager) {
                childCount = (parent.layoutManager as GridLayoutManager).spanCount
            }

            for (i in 0 until childCount - 1) {
                val child = parent.getChildAt(i) ?: return
                parent.layoutManager!!.getDecoratedBoundsWithMargins(child, mBounds)
                val right = mBounds.right + Math.round(child.translationX)
                val left = right - mDivider!!.intrinsicWidth
                mDivider!!.setBounds(left, top, right, bottom)
                mDivider!!.draw(canvas)
            }
            canvas.restore()
        }

        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            if (mDivider == null) {
                outRect.set(0, 0, 0, 0)
                return
            }
            if (mOrientation == VERTICAL) {
                outRect.set(0, 0, 0, mDivider!!.intrinsicHeight)
            } else {
                outRect.set(0, 0, mDivider!!.intrinsicWidth, 0)
            }
        }

        companion object {
            const val HORIZONTAL = LinearLayout.HORIZONTAL
            const val VERTICAL = LinearLayout.VERTICAL
            //mainly used for GridLayoutManager
            const val ALL = 2

            private val ATTRS = intArrayOf(android.R.attr.listDivider)
        }
    }
}