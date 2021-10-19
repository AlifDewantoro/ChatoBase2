package com.gamatechno.chato.sdk.app.chatroomdetail.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gamatechno.chato.sdk.R;
import com.gamatechno.chato.sdk.data.DAO.RoomChat.RoomChat;
import com.gamatechno.ggfw_ui.avatarview.AvatarPlaceholder;
import com.gamatechno.ggfw_ui.avatarview.loader.PicassoLoader;
import com.gamatechno.ggfw_ui.avatarview.views.AvatarView;

import java.util.List;

public class GroupCommonAdapter extends RecyclerView.Adapter<GroupCommonAdapter.ViewHolder>  {

    List<RoomChat> roomChatList;
    OnActionItem listener;

    public GroupCommonAdapter(List<RoomChat> roomChatList, OnActionItem listener) {
        this.roomChatList = roomChatList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public GroupCommonAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_group_common, parent, false);
        ViewHolder rcv = new ViewHolder(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(@NonNull GroupCommonAdapter.ViewHolder viewHolder, int i) {
        viewHolder.bind(roomChatList.get(i));
    }

    @Override
    public int getItemCount() {
        return roomChatList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_chooser;
        AvatarView avatarView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_chooser = itemView.findViewById(R.id.tv_chooser);
            avatarView = itemView.findViewById(R.id.avatarView);
        }
        void bind(RoomChat roomChat){
            tv_chooser.setText(roomChat.getRoom_name());
            PicassoLoader imageLoader = new PicassoLoader();
            AvatarPlaceholder refreshableAvatarPlaceholder = new AvatarPlaceholder(roomChat.getRoom_name());
            imageLoader.loadImage(avatarView, refreshableAvatarPlaceholder, (roomChat.getRoom_photo_url().equals("") ? "google.com" : roomChat.getRoom_photo_url()));

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClickItem(roomChat);
                }
            });
        }
    }

    public interface OnActionItem{
        void onClickItem(RoomChat roomChat);
    }
}
