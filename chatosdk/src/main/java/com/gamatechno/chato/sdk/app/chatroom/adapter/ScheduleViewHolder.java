package com.gamatechno.chato.sdk.app.chatroom.adapter;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gamatechno.chato.sdk.R;
import com.gamatechno.chato.sdk.data.DAO.Chat.Chat;
import com.gamatechno.chato.sdk.utils.ChatoUtils;

public class ScheduleViewHolder extends RecyclerView.ViewHolder {
    Context context;
    LinearLayout lay_container_chat;
    TextView tvTitle;
    TextView tvPlace;
    TextView tvTime;
    RecyclerView rvParticipant;
    public ScheduleViewHolder(Context context, View view) {
        super(view);
        this.context = context;
        lay_container_chat = view.findViewById(R.id.lay_container_chat);
        tvTitle = view.findViewById(R.id.tv_title);
        tvPlace = view.findViewById(R.id.tv_place);
        tvTime = view.findViewById(R.id.tv_time);
        rvParticipant = view.findViewById(R.id.rv_participant);
    }

    public void bindData(Chat chat, int isMySchedule){
        ViewGroup.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        if(isMySchedule==1){
            ((LinearLayout.LayoutParams) params).setMargins(ChatoUtils.getDP(context, 100.0), 0, 0, 0);
            lay_container_chat.setGravity(Gravity.RIGHT);
//            img_receipt.setVisibility(View.VISIBLE);
//            p_time.gravity = Gravity.RIGHT;
        } else {
            ((LinearLayout.LayoutParams) params).setMargins(0, 0, ChatoUtils.getDP(context, 100.0), 0);
            lay_container_chat.setGravity(Gravity.LEFT);
        }
        lay_container_chat.setLayoutParams(params);
    }
}
