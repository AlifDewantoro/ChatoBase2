package com.gamatechno.chato.sdk.app.chatroom.adapter;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gamatechno.chato.sdk.R;
import com.gamatechno.chato.sdk.data.DAO.Chat.Chat;
import com.gamatechno.chato.sdk.data.constant.StringConstant;
import com.gamatechno.chato.sdk.utils.ChatoUtils;
import com.google.gson.Gson;

import java.util.List;

public class ChatRoomAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {

    Context context;
    List<Chat> chatList;
    Gson gson = new Gson();
    OnChatRoomClick onChatRoomClick;
    boolean isGroup = false;

    boolean isLoading = true;
    String keyword = "";

    public ChatRoomAdapter(Context context, List<Chat> chatList, OnChatRoomClick onChatRoomClick, boolean isGroup) {
        this.context = context;
        this.isGroup = isGroup;
        this.onChatRoomClick = onChatRoomClick;
        this.chatList = chatList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        Log.d("sample", "onCreateViewHolder: "+i);
        switch (i){
            case StringConstant.type_item_message:
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_message, viewGroup, false);
                return new MessageViewHolder(view, context, onChatRoomClick, isGroup);
            case StringConstant.type_video_attachment:
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_video_attachment, viewGroup, false);
                return new VideoAttachmentViewHolder(view, context, onChatRoomClick, isGroup);
            case StringConstant.type_audio_attachment:
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_audio_attachment, viewGroup, false);
                return new VoiceNoteViewHolder(view, context, onChatRoomClick, isGroup);
            case StringConstant.type_file_attachment:
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_file_attachment, viewGroup, false);
                return new FileAttachmentViewHolder(view, context, onChatRoomClick, isGroup);
            case StringConstant.type_image_attachment:
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_image_attachment, viewGroup, false);
                return new ImageAttachmentViewHolder(view, context, onChatRoomClick, isGroup);
            case StringConstant.type_item_label:
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_label, viewGroup, false);
                return new LabelViewHolder(context, view);
            case StringConstant.type_item_schedule:
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_chat_schedule, viewGroup, false);
                return new ScheduleViewHolder(context, view);
        }
        return null;
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int i) {
        final int itemType = getItemViewType(i);
        int isMine;
        int isBeforeMine;
        Log.d("ChatroomAdapter", "onBindViewHolder: "+ChatoUtils.getUserLogin(context).getUser_id()+" - "+chatList.get(i).getFrom_user_id());
        if(chatList.get(i).getFrom_user_id() == ChatoUtils.getUserLogin(context).getUser_id()){
            isMine = 1;
        } else {
            isMine = 0;
        }

        if(i>0 && chatList.get(i-1).getFrom_user_id() == chatList.get(i).getFrom_user_id()){
            isBeforeMine = 1;
        } else {
            isBeforeMine = 0;
        }

        boolean isDateShow;
        if(i==0){
            isDateShow = true;
        } else {
            if(!chatList.get(i).getMessage_date().equals("")){
                if(!chatList.get(i).getMessage_date().equalsIgnoreCase(chatList.get(i-1).getMessage_date())){
                    isDateShow = true;
                } else {
                    isDateShow = false;
                }
            } else
                isDateShow = false;
        }

        boolean isBubbleReact = true;
        if(i == chatList.size()-1){
            if(i > 0 && chatList.get(i).getFrom_user_id() == chatList.get(i-1).getFrom_user_id()){
                if(!chatList.get(i).getMessage_date().equalsIgnoreCase(chatList.get(i-1).getMessage_date())){
                    isBubbleReact = true;
                } else {
                    isBubbleReact = false;
                }
            } else {
                isBubbleReact = true;
            }
        } else {
            int h = i-1;
            if(h >= 0){
                if(chatList.get(i).getFrom_user_id() == chatList.get(h).getFrom_user_id()){
                    if(!chatList.get(i).getMessage_date().equalsIgnoreCase(chatList.get(h).getMessage_date())){
                        isBubbleReact = true;
                    } else {
                        isBubbleReact = false;
                    }
                    /*if(i-1 >= 0){
                        if(!chatList.get(i).getMessage_date().equalsIgnoreCase(chatList.get(i-1).getMessage_date())){
                            isBubbleReact = false;
                        } else {
                            isBubbleReact = true;
                        }
                    } else {
                        isBubbleReact = true;
                    }*/
                } else {
                    isBubbleReact = true;
                }
            }else{
                isBubbleReact = true;
            }
        }

        boolean isGroupNeedName = false;
        if(i == 0){
            if(getItemCount() == 1){
                isGroupNeedName = true;
            } else {
                int h = i+1;
                if(chatList.get(i).getFrom_user_id() != chatList.get(h).getFrom_user_id()){
                    isGroupNeedName = true;
                }
            }
        } else {
            int h = i-1;
            if(chatList.get(i).getFrom_user_id() != chatList.get(h).getFrom_user_id()){
                isGroupNeedName = true;
            }
        }


        switch (itemType){
            case StringConstant.type_item_message:
                ((MessageViewHolder)holder).bindData(chatList.get(i), isMine, isBeforeMine, i, isDateShow, isBubbleReact, isGroupNeedName);
                ((MessageViewHolder) holder).bindUnreadIndicator((unreadCounter(i, (isMine == 1 ? true:false)) == 0 ? false : true),  unreadCounter(i, (isMine == 1 ? true:false)), onChatRoomClick);
                break;
            case StringConstant.type_video_attachment:
                ((VideoAttachmentViewHolder)holder).bindData(chatList.get(i), isMine, isBeforeMine, i, isDateShow, isBubbleReact, isGroupNeedName);
                ((VideoAttachmentViewHolder) holder).bindUnreadIndicator((unreadCounter(i, (isMine == 1 ? true:false)) == 0 ? false : true),  unreadCounter(i, (isMine == 1 ? true:false)), onChatRoomClick);
                break;
            case StringConstant.type_audio_attachment:
                ((VoiceNoteViewHolder)holder).bindData(chatList.get(i), isMine, isBeforeMine, i, isDateShow, isBubbleReact, isGroupNeedName);
                ((VoiceNoteViewHolder) holder).bindUnreadIndicator((unreadCounter(i, (isMine == 1 ? true:false)) == 0 ? false : true),  unreadCounter(i, (isMine == 1 ? true:false)), onChatRoomClick);
                break;
            case StringConstant.type_image_attachment:
                ((ImageAttachmentViewHolder)holder).bindData(chatList.get(i), isMine, isBeforeMine, i, isDateShow, isBubbleReact, isGroupNeedName);
                ((ImageAttachmentViewHolder) holder).bindUnreadIndicator((unreadCounter(i, (isMine == 1 ? true:false)) == 0 ? false : true),  unreadCounter(i, (isMine == 1 ? true:false)), onChatRoomClick);
                break;
            case StringConstant.type_file_attachment:
                ((FileAttachmentViewHolder)holder).bindData(chatList.get(i), isMine, isBeforeMine, i, isDateShow, isBubbleReact, isGroupNeedName);
                ((FileAttachmentViewHolder) holder).bindUnreadIndicator((unreadCounter(i, (isMine == 1 ? true:false)) == 0 ? false : true),  unreadCounter(i, (isMine == 1 ? true:false)), onChatRoomClick);
                break;
            case StringConstant.type_item_label:
                ((LabelViewHolder)holder).bindData(chatList.get(i), isDateShow);
                ((LabelViewHolder) holder).bindUnreadIndicator((unreadCounter(i, (isMine == 1 ? true:false)) == 0 ? false : true),  unreadCounter(i, (isMine == 1 ? true:false)));
                break;
            case StringConstant.type_item_schedule:
                ((ScheduleViewHolder)holder).bindData(chatList.get(i), isMine);
//                ((ScheduleViewHolder)holder).bindUnreadIndicator((unreadCounter(i, (isMine == 1 ? true:false)) == 0 ? false : true),  unreadCounter(i, (isMine == 1 ? true:false)));
                break;
        }

        if(!keyword.equals("")){
            ((BaseChatViewHolder) holder).bindSearchKeyword(keyword);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return chatList.get(position).getMessage_type_num();
    }

    public List<Chat> getItems(){
        return chatList;
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    public interface OnChatRoomClick{
        void onImageClick(View view, Chat chat);
        void onLongPress(View view, int position);
        void onClickItemView(View view, int position);
        void onClickRepliedMessage(Chat chat);
        void onClickAttachment(Chat chat, Uri uri);
        void onDownloadingAttachment(boolean isDownload, int position);
        void onOpenVideo(View view, Chat chat, Uri uri);
        void onPlayAudio(Chat chat, int position);
        void onReadMessage(Chat chat);
    }

    private int unreadCounter(int position, boolean isMine){
        if(position == 0){
            return 0;
        } else {
            if(!isMine){
//                Log.d("ChatRoomAdapter", "unreadCounter: "+chatList.get(position).getMessage_status() +" "+ (position));
                if(chatList.get(position).getMessage_status().equals(StringConstant.chat_status_pending) || chatList.get(position).getMessage_status().equals(StringConstant.chat_status_delivered)){
//                    Log.d("ChatRoomAdapter2", "unreadCounter: "+chatList.get(position-1).getMessage_status()+" "+ (position-1));
                    if(chatList.get(position-1).getMessage_status().equals(StringConstant.chat_status_read)){
                        return (getItemCount() - position);
                    } else {
                        return 0;
                    }
                } else {
                    return 0;
                }
            } else {
                return 0;
            }
        }
    }

    public void notifiyRangeChanged(int size) {
        notifyItemRangeInserted(0, size);
    }

    public void setIsLoading(boolean isl) {
        isLoading = isl;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public boolean isLoading() {
        return isLoading;
    }

    public void notifiyListChanged() {
        notifyDataSetChanged();
    }

    public boolean checkIsListClicked(){
        for(Chat chat: chatList){
            if(chat.isClicked())
                return true;
        }
        return false;
    }
}
