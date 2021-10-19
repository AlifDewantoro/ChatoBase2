package com.gamatechno.chato.sdk.app.chatroom.adapter;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.gamatechno.chato.sdk.R;
import com.gamatechno.chato.sdk.data.DAO.Chat.Chat;
import com.gamatechno.chato.sdk.module.core.ChatoBaseApplication;
import com.gamatechno.chato.sdk.utils.BitmapTransform;
import com.gamatechno.chato.sdk.utils.FilePath.IOUtils;
import com.gamatechno.ggfw.utils.GGFWUtil;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

//import butterknife.BindView;
//import butterknife.ButterKnife;

public class ImageAttachmentViewHolder extends BaseChatViewHolder {

    TextView tv_name;

    ImageView image_attachment;

    ProgressBar pb_image;

    TextView tv_time;

    ImageView img_receipt;

    LinearLayout lay_message;


    boolean isGroup;

    Context context;
    ChatRoomAdapter.OnChatRoomClick onChatRoomClick;

    public ImageAttachmentViewHolder(@NonNull View itemView, Context context, ChatRoomAdapter.OnChatRoomClick onChatRoomClick) {
        super(context, itemView);
        this.setIsRecyclable(false);
        this.context = context;
        this.onChatRoomClick = onChatRoomClick;
        initView( itemView);
    }

    public ImageAttachmentViewHolder(@NonNull View itemView, Context context, ChatRoomAdapter.OnChatRoomClick onChatRoomClick, boolean isGroup) {
        super(context, itemView);
        this.setIsRecyclable(false);
        this.context = context;
        this.isGroup = isGroup;
        this.onChatRoomClick = onChatRoomClick;
        initView( itemView);
    }

    public void bindData(Chat chat, int isMyChat, int isBeforeMyChat, int position, boolean isDateShow, boolean isRect, boolean isGroupNeedName) {
        super.bindData(chat, isDateShow, isRect,  isGroup, isGroupNeedName, position, isMyChat, isBeforeMyChat, onChatRoomClick);

        image_attachment.setOnLongClickListener(onLongClickListener(position, onChatRoomClick));

        image_attachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!chat.isClicked()){
                    onChatRoomClick.onImageClick(image_attachment, chat);
                } else {
                    onChatRoomClick.onClickItemView(view, position);
                }
            }
        });

        if(chat.getBitmap_image() != null){
            Log.d("imageattachment", "bindData: oke");
            image_attachment.setImageBitmap(chat.getBitmap_image());
        } else {
            if(GGFWUtil.isValidURL(chat.getMessage_attachment())){
                String name_file = chat.getMessage_attachment().split("/")[chat.getMessage_attachment().split("/").length - 1];
                Uri file_uri = Uri.withAppendedPath(Uri.fromFile(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)), "Chato/Images/" + chat.getMessage_attachment_name());

                pb_image.setVisibility(View.VISIBLE);
                if(!IOUtils.isFileExist(file_uri.toString().replace("file:/", ""))){
                    Picasso.get()
                            .load(chat.getMessage_attachment())
                            .placeholder(ChatoBaseApplication.Companion.getInstance().getChatoPlaceholder())
                            .transform(new BitmapTransform())
                            .into(image_attachment, new Callback() {
                                @Override
                                public void onSuccess() {
                                    pb_image.setVisibility(View.GONE);
                                }

                                @Override
                                public void onError(Exception e) {
                                    pb_image.setVisibility(View.GONE);

                                }
                            });
                } else {
                    Picasso.get()
                            .load(file_uri)
                            .placeholder(ChatoBaseApplication.Companion.getInstance().getChatoPlaceholder())
                            .transform(new BitmapTransform())
                            .into(image_attachment, new Callback() {
                                @Override
                                public void onSuccess() {
                                    pb_image.setVisibility(View.GONE);
                                }

                                @Override
                                public void onError(Exception e) {
                                    pb_image.setVisibility(View.GONE);

                                }
                            });
                }
            }
        }
    }

    private void initView(View view){
        tv_name = view.findViewById(R.id.tv_name);
        image_attachment = view.findViewById(R.id.image_attachment);
        pb_image = view.findViewById(R.id.pb_image);
        tv_time = view.findViewById(R.id.tv_time);
        img_receipt = view.findViewById(R.id.img_receipt);
        lay_message = view.findViewById(R.id.lay_message);
    }
}
