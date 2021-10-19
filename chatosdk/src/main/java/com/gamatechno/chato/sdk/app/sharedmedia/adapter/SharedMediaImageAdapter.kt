package com.gamatechno.chato.sdk.app.sharedmedia.adapter

import android.app.Activity
import android.app.ActivityOptions
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.gamatechno.chato.sdk.R
import com.gamatechno.chato.sdk.app.photopreview.ImageViewActivity
import com.gamatechno.chato.sdk.app.playvideo.PlayVideoActivity
import com.gamatechno.chato.sdk.app.sharedmedia.SharedMediaActivity
import com.gamatechno.chato.sdk.data.DAO.Chat.Chat
import com.gamatechno.chato.sdk.module.core.ChatoBaseApplication
import com.gamatechno.chato.sdk.module.core.ChatoBaseApplication.Companion.instance
import com.gamatechno.chato.sdk.utils.BitmapTransform
import com.gamatechno.chato.sdk.utils.ChatoUtils
import com.gamatechno.chato.sdk.utils.FilePath.IOUtils
import com.gamatechno.chato.sdk.utils.downloader.GTDownloadCallback
import com.gamatechno.chato.sdk.utils.downloader.GTDownloadManager
import com.gamatechno.chato.sdk.utils.downloader.GTDownloadRequest
import com.gamatechno.ggfw.utils.GGFWUtil
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_shared_image.view.*

class SharedMediaImageAdapter(var context: Context) : RecyclerView.Adapter<SharedMediaImageAdapter.ViewHolder>() {
    var list : ArrayList<Chat> = ArrayList()

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val view = LayoutInflater.from(p0.context).inflate(R.layout.item_shared_image,p0,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(vh: ViewHolder, pos: Int) {
        val chat = list[pos]
        val buttonOnClickListener = View.OnClickListener {
            if(GGFWUtil.isValidURL(chat.message_attachment)) {
                val nameFile = chat.message_attachment.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[chat.message_attachment.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray().size - 1]
                val destination = Uri.withAppendedPath(Uri.fromFile(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)), "Chato/Video")
                val fileUri = Uri.withAppendedPath(Uri.fromFile(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)), "Chato/Video/" + chat.message_attachment_name)

                vh.view.pb_image.visibility = View.VISIBLE
                if (IOUtils.isFileExist(fileUri.toString().replace("file:/", ""))) run {
                    onOpenVideo(vh.view.image, chat, fileUri)
                    vh.view.pb_image.visibility = View.GONE
                } else {
                    (context as SharedMediaActivity).permission(object : SharedMediaActivity.AskPermission{
                        override fun permission() {
                            chat.isVideoDownloding = true
                            notifyDataSetChanged()
                        }
                    })
                    GTDownloadManager(context, object : GTDownloadCallback {
                        override fun onProcess(request: GTDownloadRequest) {

                        }

                        override fun onCancel(request: GTDownloadRequest) {
                            (context as SharedMediaActivity).permission(object : SharedMediaActivity.AskPermission{
                                override fun permission() {
                                    chat.isVideoDownloding = false
                                    notifyDataSetChanged()
                                }
                            })
                            vh.view.pb_image.visibility = View.GONE
                        }

                        override fun onSuccess(request: GTDownloadRequest) {
                            (context as SharedMediaActivity).permission(object : SharedMediaActivity.AskPermission{
                                override fun permission() {
                                    chat.isVideoDownloding = false
                                    notifyDataSetChanged()
                                }
                            })
                            vh.view.pb_image.visibility = View.GONE
                            onOpenVideo(vh.view.image, chat, request.destinationUri)
                        }
                    }).startRequest(GTDownloadRequest(Uri.parse(chat.message_attachment), chat.message_attachment_name).setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN).setDestinationUri(destination))
                }
            }
        }
        if(chat.message_type == "image") {
            vh.view.ll_video.visibility = View.GONE
            vh.view.pb_image.visibility = View.VISIBLE
            vh.run {
                view.setOnClickListener {
                    val data = Bundle()

                    data.putString("title", "Detail")
                    data.putString("image", chat.message_attachment)

                    context.startActivity(Intent(context, ImageViewActivity::class.java).putExtras(data))
                }
            }
            if(chat.bitmap_image!=null) {
                vh.view.image.setImageBitmap(chat.bitmap_image)
                vh.view.pb_image.visibility = View.GONE
            } else {
                if(GGFWUtil.isValidURL(chat.message_attachment)) run {
                    val nameFile = chat.message_attachment.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[chat.message_attachment.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray().size - 1]
                    val fileUri = Uri.withAppendedPath(Uri.fromFile(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)), "Chato/Images/$nameFile")

                    if (!IOUtils.isFileExist(fileUri.toString().replace("file:/", ""))) {
                        Picasso.get()
                                .load(chat.message_attachment)
                                .placeholder(instance.getChatoPlaceholder())
                                .transform(BitmapTransform())
                                .into(vh.view.image, object : Callback {
                                    override fun onSuccess() {
                                        vh.view.pb_image.visibility = View.GONE
                                    }

                                    override fun onError(e: Exception) {
                                        vh.view.pb_image.visibility = View.GONE
                                    }
                                })
                    } else {
                        Picasso.get()
                                .load(fileUri)
                                .placeholder(instance.getChatoPlaceholder())
                                .transform(BitmapTransform())
                                .into(vh.view.image, object : Callback {
                                    override fun onSuccess() {
                                        vh.view.pb_image.visibility = View.GONE
                                    }

                                    override fun onError(e: Exception) {
                                        vh.view.pb_image.visibility = View.GONE
                                    }
                                })
                    }
                }
            }
        } else {
            vh.view.ll_video.visibility = View.VISIBLE
            vh.view.pb_image.visibility = View.VISIBLE
            vh.view.tv_time.text = convertTime(chat.message_attachment_duration)
            vh.view.setOnClickListener(buttonOnClickListener)
            if(chat.bitmap_image!=null) {
                vh.view.image.setImageBitmap(chat.bitmap_image)
                vh.view.pb_image.visibility = View.GONE
            } else {
                if(GGFWUtil.isValidURL(chat.message_attachment)) run {
                    val nameFile = chat.message_attachment.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[chat.message_attachment.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray().size - 1]
                    val fileUri = Uri.withAppendedPath(Uri.fromFile(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)), "Chato/Images/$nameFile")

                    if (!IOUtils.isFileExist(fileUri.toString().replace("file:/", ""))) {
                        Picasso.get()
                                .load(chat.message_attachment_thumbnail)
                                .placeholder(ChatoBaseApplication.instance.getChatoPlaceholder())
                                .transform(BitmapTransform())
                                .into(vh.view.image, object : Callback {
                                    override fun onSuccess() {
                                        vh.view.pb_image.visibility = View.GONE
                                    }

                                    override fun onError(e: Exception) {
                                        vh.view.pb_image.visibility = View.GONE
                                    }
                                })
                    } else {
                        Picasso.get()
                                .load(fileUri)
                                .placeholder(instance.getChatoPlaceholder())
                                .transform(BitmapTransform())
                                .into(vh.view.image, object : Callback {
                                    override fun onSuccess() {
                                        vh.view.pb_image.visibility = View.GONE
                                    }

                                    override fun onError(e: Exception) {
                                        vh.view.pb_image.visibility = View.GONE
                                    }
                                })
                    }
                }
            }
        }
    }

    private fun onOpenVideo(view: View, chat: Chat, uri: Uri) {
        val data = Bundle()
        data.putSerializable("chat", chat)
        data.putString("uri", uri.toString())

        if (!ChatoUtils.isPreLolipop()) {
            view.transitionName = "video"
            val options = ActivityOptions.makeSceneTransitionAnimation((context as Activity), view, ViewCompat.getTransitionName(view))
            context.startActivity(Intent(context, PlayVideoActivity::class.java).putExtras(data), options.toBundle())
        } else {
            context.startActivity(Intent(context, PlayVideoActivity::class.java).putExtras(data))
        }

    }

    fun addAll(list: ArrayList<Chat>) {
        this.list.addAll(list)
        notifyDataSetChanged()
    }

    private fun convertTime(time: Int): String {
        val minute = time/60
        val second = time%60
        val minuteString = if(minute<10)
            "0$minute"
        else
            "$minute"
        val secondString = if(second<10)
            "0$second"
        else
            "$second"
        return "$minuteString:$secondString"
    }

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)
}
