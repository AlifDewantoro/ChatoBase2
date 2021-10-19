package com.gamatechno.chato.sdk.app.chatroom.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gamatechno.chato.sdk.R;
import com.gamatechno.chato.sdk.data.DAO.Chat.Chat;
import com.gamatechno.ggfw.utils.StringUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LabelViewHolder extends RecyclerView.ViewHolder {

    Context context;
    DateFormat dateFormat;
    Chat chat;
    Date dateNow;


    TextView tv_message;

    TextView tv_date;

    TextView tv_unread;

    RelativeLayout lay_not_read_yet;

//    @BindView(R.id.tv_name)
//    TextView tv_name;

    public LabelViewHolder(@NonNull Context context, @NonNull View itemView) {
        super(itemView);
        this.context = context;
        dateNow = new Date();
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        initView(itemView);
    }

    public void bindData(Chat chat, boolean isDateVisible){
        this.chat = chat;

        tv_message.setText(""+chat.getMessage_text());
        if(chat.getMessage_date()!=null){
            if(!chat.getMessage_date().equals("")){
                if(dateFormat.format(dateNow).equalsIgnoreCase(chat.getMessage_date())){
                    tv_date.setText("HARI INI");
                } else {
                    try {
                        tv_date.setText(chat.getMessage_date().split("-")[2]+" "+ StringUtils.getCompleteMonthName(Integer.valueOf(chat.getMessage_date().split("-")[1])-1)+" "+chat.getMessage_date().split("-")[0]);
                    } catch (IndexOutOfBoundsException e){
                        e.printStackTrace();
                    }
                }
            }
        }

        if(isDateVisible){
            tv_date.setVisibility(View.VISIBLE);
        } else {
            tv_date.setVisibility(View.GONE);
        }
    }

    public void bindUnreadIndicator(boolean isUnread, int data){
        if(isUnread){
            lay_not_read_yet.setVisibility(View.VISIBLE);
            tv_unread.setText(data+" pesan belum terbaca");
        } else {
            lay_not_read_yet.setVisibility(View.GONE);
        }
    }

    private void initView(View view){
        tv_message = view.findViewById(R.id.tv_message);
        tv_date = view.findViewById(R.id.tv_date);
        tv_unread = view.findViewById(R.id.tv_unread);
        lay_not_read_yet = view.findViewById(R.id.lay_not_read_yet);
    }
}
