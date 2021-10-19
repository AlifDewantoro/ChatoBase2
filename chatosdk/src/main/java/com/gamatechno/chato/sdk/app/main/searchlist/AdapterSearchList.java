package com.gamatechno.chato.sdk.app.main.searchlist;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gamatechno.chato.sdk.R;
import com.gamatechno.chato.sdk.app.chatrooms.adapter.ChatroomViewHolder;
import com.gamatechno.chato.sdk.app.chatrooms.adapter.RoomAdapter;

import java.util.List;

public class AdapterSearchList extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {

    Context context;
    List<SearchChatroomModel> chatList;
    RoomAdapter.OnObrolanAdapter onObrolanAdapter;

    public AdapterSearchList(Context context, List<SearchChatroomModel> chatList, RoomAdapter.OnObrolanAdapter onObrolanAdapter) {
        this.context = context;
        this.chatList = chatList;
        this.onObrolanAdapter = onObrolanAdapter;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        switch (i){
            case SearchChatroomModel.chatroom_type:
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_chat_rooms, viewGroup, false);
                return new ChatroomViewHolder(view, context);
            case SearchChatroomModel.message_type:
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_chat_rooms, viewGroup, false);
                return new MessageSearchViewHolder(view);
            case SearchChatroomModel.header_type:
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_header_searchchatroom, viewGroup, false);
                return new HeaderViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        final int itemType = getItemViewType(i);
        switch (itemType){
            case SearchChatroomModel.chatroom_type:
                ((ChatroomViewHolder)viewHolder).bindDatas(chatList.get(i));
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onObrolanAdapter.onClickObrolan(chatList.get(i).getChatRoomUiModel());
                    }
                });
                break;
            case SearchChatroomModel.message_type:
                ((MessageSearchViewHolder)viewHolder).bindData(chatList.get(i));
                break;
            case SearchChatroomModel.header_type:
                ((HeaderViewHolder)viewHolder).bindData(chatList.get(i));
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return chatList.get(position).getType();
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

//    public class MyHolder extends RecyclerView.ViewHolder {
//        @BindView(R.id.avatarView)
//        AvatarView avatarView;
//        @BindView(R.id.title)
//        EmphasisTextView title;
//        @BindView(R.id.message)
//        TextView message;
//        @BindView(R.id.time)
//        TextView time;
//        @BindView(R.id.card_indicator)
//        CardView card_indicator;
//        @BindView(R.id.lay_chat_rooms)
//        ConstraintLayout lay_chat_rooms;
//
//        public MyHolder(@NonNull View itemView) {
//            super(itemView);
//            ButterKnife.bind(this, itemView);
//        }
//    }
}
