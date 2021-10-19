package com.gamatechno.chato.sdk.app.chatroom.adapter;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.gamatechno.chato.sdk.R;
import com.gamatechno.chato.sdk.data.DAO.Chat.Chat;
import com.gamatechno.chato.sdk.utils.FilePath.FilePath;
import com.gamatechno.chato.sdk.utils.FilePath.IOUtils;
import com.gamatechno.chato.sdk.utils.animation.AnimationToggle;
import com.gamatechno.chato.sdk.utils.downloader.Exception.GTDownloadException;
import com.gamatechno.chato.sdk.utils.downloader.GTDownloadCallback;
import com.gamatechno.chato.sdk.utils.downloader.GTDownloadManager;
import com.gamatechno.chato.sdk.utils.downloader.GTDownloadRequest;
import com.gamatechno.ggfw.utils.GGFWUtil;

public class FileAttachmentViewHolder extends BaseChatViewHolder implements BaseChatViewHolder.OnChatViewHolder {

    LinearLayout lay_attachment;

    AnimationToggle container_attachment;

    TextView tv_attachment;

    ImageView img_attachment;

    ProgressBar pb_download;

    boolean isGroup;

    Context context;
    ChatRoomAdapter.OnChatRoomClick onChatRoomClick;

    public FileAttachmentViewHolder(@NonNull View itemView, Context context, ChatRoomAdapter.OnChatRoomClick onChatRoomClick) {
        super(context, itemView);
        this.setIsRecyclable(false);
        this.context = context;
        this.onChatRoomClick = onChatRoomClick;
        initView( itemView);
    }

    public FileAttachmentViewHolder(@NonNull View itemView, Context context, ChatRoomAdapter.OnChatRoomClick onChatRoomClick, boolean isGroup) {
        super(context, itemView);
        this.setIsRecyclable(false);
        this.context = context;
        this.isGroup = isGroup;
        this.onChatRoomClick = onChatRoomClick;
        initView( itemView);
    }


    public void bindData(Chat chat, int isMyChat, int isBeforeMyChat, int position, boolean isDateShow, boolean isRect, boolean isGroupNeedName) {
        super.bindData(chat, isDateShow, isRect, isGroup, isGroupNeedName, position, isMyChat, isBeforeMyChat, onChatRoomClick);

        if(chat.getFileModel() == null){
            try {
                tv_attachment.setText(chat.getMessage_attachment_name());
            } catch (IndexOutOfBoundsException e){
                tv_attachment.setText(chat.getMessage_attachment());
            } catch (NullPointerException e){
                tv_attachment.setText("Error");
            }
            container_attachment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (GGFWUtil.isValidURL(chat.getMessage_attachment())) {
                        String name_file = chat.getMessage_attachment().split("/")[chat.getMessage_attachment().split("/").length - 1];
                        Uri destination = Uri.withAppendedPath(Uri.fromFile(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)), "Chato/Document");
                        Uri file_uri = Uri.withAppendedPath(Uri.fromFile(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)), "Chato/Document/" +chat.getMessage_attachment_name());
                        if (IOUtils.isFileExist(file_uri.toString().replace("file:/", ""))) {
                            onChatRoomClick.onClickAttachment(chat, file_uri);
                        } else {
                            try {
                                new GTDownloadManager(context, new GTDownloadCallback(){
                                    @Override
                                    public void onProcess(GTDownloadRequest request) {
                                        onChatRoomClick.onDownloadingAttachment(true, position);
                                    }

                                    @Override
                                    public void onCancel(GTDownloadRequest request) {

                                    }

                                    @Override
                                    public void onSuccess(GTDownloadRequest request) {
                                        onChatRoomClick.onDownloadingAttachment(false, position);
                                        onChatRoomClick.onClickAttachment(chat, request.getDestinationUri());
                                    }
                                }).startRequest(new GTDownloadRequest(Uri.parse(chat.getMessage_attachment().replace(" ", "%20")), chat.getMessage_attachment_name()).setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN).setDestinationUri(destination));
                            } catch (GTDownloadException e) {
                                e.printStackTrace();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                });
        }else {
            tv_attachment.setText(FilePath.getFileName(context, chat.getFileModel().getUri()));
//            tv_attachment.setText(chat.getMessage_attachment_name());
            container_attachment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(chat.getFileModel().getUri(), FilePath.getMimeType(context, chat.getFileModel().getUri()));
                    context.startActivity(intent);
                }
            });
        }

        if(GGFWUtil.isValidURL(chat.getMessage_attachment())){
//            String name_file = chat.getMessage_attachment().split("/")[chat.getMessage_attachment().split("/").length - 1];
            Uri file_uri = Uri.withAppendedPath(Uri.fromFile(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)), "Chato/Document/" +chat.getMessage_attachment_name());
            if(IOUtils.isFileExist(file_uri.toString().replace("file:/", ""))){
                img_attachment.setImageResource(R.drawable.ic_attachment_black_24dp);
            } else {
                img_attachment.setImageResource(R.drawable.ic_file_download_black_24dp);
            }

        } else {
            img_attachment.setImageResource(R.drawable.ic_attachment_black_24dp);
        }

        if(chat.isVideoDownloding()){
            pb_download.setVisibility(View.VISIBLE);
            img_attachment.setVisibility(View.GONE);
        } else {
            pb_download.setVisibility(View.GONE);
            img_attachment.setVisibility(View.VISIBLE);
        }
    }

    private void initView(View view){
        lay_attachment = view.findViewById(R.id.lay_attachment);
        container_attachment = view.findViewById(R.id.container_attachment);
        tv_attachment = view.findViewById(R.id.tv_attachment);
        img_attachment = view.findViewById(R.id.img_attachment);
        pb_download = view.findViewById(R.id.pb_download);
    }
}
