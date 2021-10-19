package com.gamatechno.chato.sdk.app.chatrooms.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gamatechno.chato.sdk.R;
import com.gamatechno.chato.sdk.app.chatrooms.uimodel.ChatRoomsUiModel;
import com.gamatechno.chato.sdk.data.DAO.RoomChat.RoomChat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RoomAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    List<ChatRoomsUiModel> chatList;
    List<ChatRoomsUiModel> bufferList;
    List<ChatRoomsUiModel> bufferPinedList;
    OnObrolanAdapter onObrolanAdapter;
    String typeChat;

    boolean isLoading = false;

    public RoomAdapter(Context context, List<ChatRoomsUiModel> chatList, OnObrolanAdapter onObrolanAdapter) {
        this.context = context;
        this.chatList = chatList;
        this.onObrolanAdapter = onObrolanAdapter;
    }

    public RoomAdapter(Context context, String type, OnObrolanAdapter onObrolanAdapter) {
        this.context = context;
        this.chatList = new ArrayList();
        this.onObrolanAdapter = onObrolanAdapter;
        this.bufferList = new ArrayList();
        this.bufferPinedList = new ArrayList();
        this.typeChat = type;
    }

    public void initLoading(boolean istrue) {
        isLoading = istrue;
        notifyDataSetChanged();
    }

    public List<ChatRoomsUiModel> getData() {
        return chatList;
    }

    public void addData(boolean isRefresh, List<ChatRoomsUiModel> list) {
        if (isRefresh)
            chatList.clear();

        switch(typeChat){
            case "all":{
                if (list.size()>1) {
                    Collections.sort(list, (lhs, rhs) ->
                            rhs.getRoomChat().getRoom_type().compareTo(lhs.getRoomChat().getRoom_type()));
                }
                chatList.addAll(list);
                break;
            }
            case RoomChat.official_room_type:{
                for (int x=0; x < list.size(); x++){
                    if (list.get(x).getRoomChat().getRoom_type().equals(RoomChat.official_room_type)){
                        if (list.get(x).getRoomChat().getIs_pined() == 1){
                            bufferPinedList.add(list.get(x));
                        }else {
                            bufferList.add(list.get(x));
                        }
                    }
                }
                /*
                if (bufferPinedList.size()>1) {
                    Collections.sort(bufferPinedList, (lhs, rhs) ->
                            rhs.getRoomChat().getRoom_name().compareTo(lhs.getRoomChat().getRoom_name()));
                }
                if (bufferList.size()>1) {
                    Collections.sort(bufferList, (lhs, rhs) ->
                            rhs.getRoomChat().getRoom_name().compareTo(lhs.getRoomChat().getRoom_name()));
                }

                 */
                chatList.addAll(bufferPinedList);
                chatList.addAll(bufferList);
                bufferPinedList.clear();
                bufferList.clear();
                break;
            }
            case RoomChat.group_room_type:{
                for (int x=0; x < list.size(); x++){
                    if (list.get(x).getRoomChat().getRoom_type().equals(RoomChat.group_room_type)){
                        if (list.get(x).getRoomChat().getIs_pined() == 1){
                            bufferPinedList.add(list.get(x));
                        }else {
                            bufferList.add(list.get(x));
                        }
                    }
                }
                /*
                if (bufferPinedList.size()>1) {
                    Collections.sort(bufferPinedList, (lhs, rhs) ->
                            rhs.getRoomChat().getRoom_name().compareTo(lhs.getRoomChat().getRoom_name()));
                }
                if (bufferList.size()>1) {
                    Collections.sort(bufferList, (lhs, rhs) ->
                            rhs.getRoomChat().getRoom_name().compareTo(lhs.getRoomChat().getRoom_name()));
                }

                 */
                chatList.addAll(bufferPinedList);
                chatList.addAll(bufferList);
                bufferPinedList.clear();
                bufferList.clear();
                break;
            }
            case RoomChat.user_room_type:{
                for (int x=0; x < list.size(); x++){
                    if (list.get(x).getRoomChat().getRoom_type().equals(RoomChat.user_room_type)){
                        if (list.get(x).getRoomChat().getIs_pined() == 1){
                            bufferPinedList.add(list.get(x));
                        }else {
                            bufferList.add(list.get(x));
                        }
                    }
                }
                /*
                if (bufferPinedList.size()>1) {
                    Collections.sort(bufferPinedList, (lhs, rhs) ->
                            rhs.getRoomChat().getRoom_name().compareTo(lhs.getRoomChat().getRoom_name()));
                }
                if (bufferList.size()>1) {
                    Collections.sort(bufferList, (lhs, rhs) ->
                            rhs.getRoomChat().getRoom_name().compareTo(lhs.getRoomChat().getRoom_name()));
                }

                 */
                chatList.addAll(bufferPinedList);
                chatList.addAll(bufferList);
                bufferPinedList.clear();
                bufferList.clear();
                break;
            }
            default: break;
        }

        notifyDataSetChanged();
    }

    public void sortChat(int type) {
        if (type == 1) {
            Collections.sort(chatList, (lhs, rhs) ->
                    lhs.getRoomChat().getRoom_name().compareTo(rhs.getRoomChat().getRoom_name()));
            notifyDataSetChanged();
        } else if (type == 2) {
            Collections.sort(chatList, (lhs, rhs) ->
                    rhs.getRoomChat().getRoom_name().compareTo(lhs.getRoomChat().getRoom_name()));
            notifyDataSetChanged();
        } else if (type == 3) {
            for (int x=0; x < chatList.size(); x++){
                if (chatList.get(x).getRoomChat().getIs_pined() == 1){
                    bufferPinedList.add(chatList.get(x));
                }else {
                    bufferList.add(chatList.get(x));
                }
            }

            if (bufferPinedList.size()>1) {
                Collections.sort(bufferPinedList, (lhs, rhs) ->
                        rhs.getRoomChat().getRoom_name().compareTo(lhs.getRoomChat().getRoom_name()));
            }
            if (bufferList.size()>1) {
                Collections.sort(bufferList, (lhs, rhs) ->
                        rhs.getRoomChat().getRoom_name().compareTo(lhs.getRoomChat().getRoom_name()));
            }
            chatList.clear();
            chatList.addAll(bufferPinedList);
            chatList.addAll(bufferList);
            bufferPinedList.clear();
            bufferList.clear();

            notifyDataSetChanged();
        }
    }

    public int getSumUnreadMessage(){
        int unread = 0;
        for (int x= 0; x < chatList.size(); x++){
            unread += chatList.get(x).getRoomChat().getUnread_message();
        }
        return unread;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_chat_rooms, viewGroup, false);
        return new ChatroomViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (isLoading) {
            ((ChatroomViewHolder) viewHolder).bindLoading();
        } else {
            ChatRoomsUiModel model = chatList.get(i);
            ((ChatroomViewHolder) viewHolder).bindDatas(model);

            viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    onObrolanAdapter.onLongClick(model);
                    return true;
                }
            });

            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onObrolanAdapter.onClickObrolan(model);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (isLoading) {
            return 4;
        } else {
            return chatList.size();
        }

    }

    public interface OnObrolanAdapter {
        void onClickObrolan(ChatRoomsUiModel model);

        void onLongClick(ChatRoomsUiModel model);
    }
}
