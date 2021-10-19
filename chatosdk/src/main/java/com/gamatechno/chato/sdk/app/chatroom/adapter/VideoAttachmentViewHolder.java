package com.gamatechno.chato.sdk.app.chatroom.adapter;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.gamatechno.chato.sdk.R;
import com.gamatechno.chato.sdk.data.DAO.Chat.Chat;
import com.gamatechno.chato.sdk.utils.FilePath.IOUtils;
import com.gamatechno.chato.sdk.utils.downloader.Exception.GTDownloadException;
import com.gamatechno.chato.sdk.utils.downloader.GTDownloadCallback;
import com.gamatechno.chato.sdk.utils.downloader.GTDownloadManager;
import com.gamatechno.chato.sdk.utils.downloader.GTDownloadRequest;
import com.gamatechno.ggfw.utils.GGFWUtil;
import com.squareup.picasso.Picasso;

public class VideoAttachmentViewHolder extends BaseChatViewHolder {

    ImageView img_receipt;

    LinearLayout lay_message;

    ProgressBar pb_video;

    CardView card_download;

    ImageView image_attachment;

    ImageView image_download;

    boolean isGroup;

    Context context;
    ChatRoomAdapter.OnChatRoomClick onChatRoomClick;

    public VideoAttachmentViewHolder(@NonNull View itemView, Context context, ChatRoomAdapter.OnChatRoomClick onChatRoomClick) {
        super(context, itemView);
        this.setIsRecyclable(false);
        this.context = context;
        this.onChatRoomClick = onChatRoomClick;
        initView(itemView);
    }

    public VideoAttachmentViewHolder(@NonNull View itemView, Context context, ChatRoomAdapter.OnChatRoomClick onChatRoomClick, boolean isGroup) {
        super(context, itemView);
        this.setIsRecyclable(false);
        this.context = context;
        this.isGroup = isGroup;
        this.onChatRoomClick = onChatRoomClick;
        initView(itemView);
    }

    public void bindData(Chat chat, int isMyChat, int isBeforeMyChat, int position, boolean isDateShow, boolean isRect, boolean isGroupNeedName) {
        super.bindData(chat, isDateShow,  isRect,  isGroup, isGroupNeedName, position, isMyChat, isBeforeMyChat, onChatRoomClick);

        View.OnClickListener buttonClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!chat.isClicked()){
                    if(GGFWUtil.isValidURL(chat.getMessage_attachment())){
                        String name_file = chat.getMessage_attachment().split("/")[chat.getMessage_attachment().split("/").length - 1];
                        Uri destination = Uri.withAppendedPath(Uri.fromFile(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)), "Chato/Video");
                        Uri file_uri = Uri.withAppendedPath(Uri.fromFile(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)), "Chato/Video/"+chat.getMessage_attachment_name());
                        if(IOUtils.isFileExist(file_uri.toString().replace("file:/", ""))){
                            onChatRoomClick.onOpenVideo(image_attachment, chat, file_uri);
                        } else {
                            try {
                                onChatRoomClick.onDownloadingAttachment(true, position);
                                new GTDownloadManager(context, new GTDownloadCallback(){
                                    @Override
                                    public void onProcess(GTDownloadRequest request) {

                                    }

                                    @Override
                                    public void onCancel(GTDownloadRequest request) {

                                    }

                                    @Override
                                    public void onSuccess(GTDownloadRequest request) {
                                        onChatRoomClick.onDownloadingAttachment(false, position);
                                        onChatRoomClick.onOpenVideo(image_attachment, chat, request.getDestinationUri());
                                    }
                                }).startRequest(new GTDownloadRequest(Uri.parse(chat.getMessage_attachment()), chat.getMessage_attachment_name()).setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN).setDestinationUri(destination));
                            } catch (GTDownloadException e) {
                                e.printStackTrace();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                } else {
                    onChatRoomClick.onClickItemView(v, position);
                }
            }
        };

        if(chat.isVideoDownloding()){
            pb_video.setVisibility(View.VISIBLE);
            card_download.setVisibility(View.GONE);
            image_attachment.setOnClickListener(null);
            image_attachment.setOnLongClickListener(null);
            card_download.setOnClickListener(null);
        } else {
            image_attachment.setOnClickListener(buttonClickListener);
            image_attachment.setOnLongClickListener(onLongClickListener(position, onChatRoomClick));
            card_download.setOnClickListener(buttonClickListener);
            pb_video.setVisibility(View.GONE);
            card_download.setVisibility(View.VISIBLE);
        }



        if(GGFWUtil.isValidURL(chat.getMessage_attachment())){
            image_download.setVisibility(View.VISIBLE);
//            String name_file = chat.getMessage_attachment().split("/")[chat.getMessage_attachment().split("/").length-1];
            Uri file_uri = Uri.withAppendedPath(Uri.fromFile(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)), "Chato/Video/"+chat.getMessage_attachment_name());
            if(IOUtils.isFileExist(file_uri.toString().replace("file:/", ""))){
                image_download.setImageResource(R.drawable.ic_playing);
                Glide.with(context)
                        .asBitmap()
                        .load(file_uri)
                        .into(image_attachment);
            } else {
                image_download.setImageResource(R.drawable.ic_file_download_black_24dp);
                if(chat.getMessage_attachment_thumbnail() != null){
                    Picasso.get()
                            .load((chat.getMessage_attachment_thumbnail().equals("") ? "google.com" : chat.getMessage_attachment_thumbnail()))
                            .into(image_attachment);
                }
            }
        } else {
            image_download.setVisibility(View.GONE);
        }
    }

    private void initView(View view){
        img_receipt = view.findViewById(R.id.img_receipt);
        lay_message = view.findViewById(R.id.lay_message);
        card_download = view.findViewById(R.id.card_download);
        image_attachment = view.findViewById(R.id.image_attachment);
        image_download = view.findViewById(R.id.image_download);
        pb_video = view.findViewById(R.id.pb_video);
    }
}
