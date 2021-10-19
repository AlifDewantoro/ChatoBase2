package com.gamatechno.chato.sdk.app.sharedmedia.adapter

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gamatechno.chato.sdk.R
import com.gamatechno.chato.sdk.app.sharedmedia.SharedMediaActivity
import com.gamatechno.chato.sdk.data.DAO.Chat.Chat
import com.gamatechno.chato.sdk.utils.FilePath.FilePath
import com.gamatechno.chato.sdk.utils.FilePath.IOUtils
import com.gamatechno.chato.sdk.utils.downloader.GTDownloadCallback
import com.gamatechno.chato.sdk.utils.downloader.GTDownloadManager
import com.gamatechno.chato.sdk.utils.downloader.GTDownloadRequest
import com.gamatechno.ggfw.utils.GGFWUtil
import kotlinx.android.synthetic.main.item_shared_file.view.*
import java.io.File
import java.io.IOException
import java.text.DateFormat
import java.util.*
import java.text.SimpleDateFormat


class SharedMediaFileAdapter(var context: Context) : RecyclerView.Adapter<SharedMediaFileAdapter.Holder>() {
    class Holder (val view: View, val viewType: Int) : RecyclerView.ViewHolder(view)

    internal var dateFormat: DateFormat? = null
    internal var chat: Chat? = null
    internal var dateNow: Date? = null

    var list : ArrayList<Chat>
    init {
        list = ArrayList()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(LayoutInflater
                .from(parent.context)
                .inflate(R.layout.item_shared_file, parent, false), viewType)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(vh: Holder, pos: Int) {
        val chat = list[pos]
        val nameFile = chat.message_attachment.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[chat.message_attachment.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray().size - 1]
        val dateFile = chat.message_date
        val file_uri = Uri.withAppendedPath(Uri.fromFile(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)), "Chato/Images/$nameFile")

        vh.view.tv_file_name.text = chat.message_attachment_name
        vh.view.tv_note.text = convertDate(dateFile)
        if(pos==0){
            vh.view.divider.visibility = View.GONE
        }
        when(nameFile.substringAfterLast(".")){
            "doc","docx"        -> vh.view.iv_icon.setImageResource(R.drawable.ic_file_doc)
            "xls","xlsx","csv"  -> vh.view.iv_icon.setImageResource(R.drawable.ic_file_excel)
            "ppt","pptx"        -> vh.view.iv_icon.setImageResource(R.drawable.ic_file_ppt)
            "pdf"               -> vh.view.iv_icon.setImageResource(R.drawable.ic_file_pdf)
            else                -> vh.view.iv_icon.setImageResource(R.drawable.ic_file_general)
        }
        vh.view.setOnClickListener {
            if (GGFWUtil.isValidURL(chat.message_attachment)) {
                val name_file = chat.message_attachment.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[chat.message_attachment.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray().size - 1]
                val destination = Uri.withAppendedPath(Uri.fromFile(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)), "Chato/Document")
                val fileUri = Uri.withAppendedPath(Uri.fromFile(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)), "Chato/Document/" + chat.message_attachment_name)
                Log.d("MediaShared", "name_file: " + name_file)
                if (IOUtils.isFileExist(fileUri.toString().replace("file:/", ""))) {
                    openFile(chat, fileUri)
                } else {
                    (context as SharedMediaActivity).permission(object : SharedMediaActivity.AskPermission{
                        override fun permission() {
                            chat.isVideoDownloding = true
                            notifyDataSetChanged()
                        }
                    })
                    GTDownloadManager(context, object : GTDownloadCallback {
                        override fun onProcess(request : GTDownloadRequest) {

                        }

                        override fun onCancel(request : GTDownloadRequest ) {
                            (context as SharedMediaActivity).permission(object : SharedMediaActivity.AskPermission{
                                override fun permission() {
                                    chat.isVideoDownloding = false
                                    notifyDataSetChanged()
                                }
                            })
                        }

                        override fun onSuccess(request : GTDownloadRequest) {
                            (context as SharedMediaActivity).permission(object : SharedMediaActivity.AskPermission{
                                override fun permission() {
                                    chat.isVideoDownloding = false
                                    notifyDataSetChanged()
                                }
                            })
                            openFile(chat, fileUri)
//                                        onChatRoomClick.onClickAttachment(chat, request.getDestinationUri());
                        }
                    }).startRequest(GTDownloadRequest (Uri.parse(chat.message_attachment.replace(" ", "%20")), chat.message_attachment_name).setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN).setDestinationUri(destination))
                }
            } else {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.setDataAndType(chat.fileModel.uri, FilePath.getMimeType(context, chat.fileModel.uri))
                context.startActivity(intent)
            }
        }
    }

    fun addAll(list: ArrayList<Chat>) {
        this.list.addAll(list)
        notifyDataSetChanged()
    }

    private fun openFile(chat : Chat, uri : Uri) {
        try {
            IOUtils.openFile(context, File(uri.toString().replace("file:/", "")))
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    private fun convertDate(inpDate : String) : String {
        val input = SimpleDateFormat("yyyy-MM-dd")
        val output = SimpleDateFormat("dd/MM/yy")

        val dateInput = input.parse(inpDate)
        return output.format(dateInput)
    }
}
