package com.gamatechno.chato.sdk.app.chatroom.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.text.TextUtils;
import android.text.util.Linkify;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.gamatechno.chato.sdk.R;
import com.gamatechno.chato.sdk.data.DAO.Chat.Chat;
import com.gamatechno.chato.sdk.data.DAO.RoomChat.RoomChat;
import com.gamatechno.chato.sdk.data.constant.StringConstant;
import com.gamatechno.chato.sdk.utils.ChatoText.ExpandableTextView;
import com.gamatechno.chato.sdk.utils.ChatoUtils;
import com.gamatechno.chato.sdk.utils.DetectHtml;
import com.gamatechno.chato.sdk.utils.animation.AnimationToggle;
import com.gamatechno.ggfw.utils.StringUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BaseChatViewHolder extends RecyclerView.ViewHolder{

    DateFormat dateFormat;
    Chat chat;
    Date dateNow;

    TextView tv_date;

    ExpandableTextView tv_message;

    TextView tv_name;

    TextView tv_time;

    LinearLayout lay_message;

    ImageView img_receipt;

    LinearLayout lay_chat;

    LinearLayout lay_container_chat;

    LinearLayout lay_status;

    ImageView img_star;

    ImageView img_terusan;

    ImageView img_replied_message;

    LinearLayout lay_forwarded;

    ImageView img_broadcast;

    AnimationToggle container_reply;

    TextView tv_replied_title;

    TextView tv_terusan;

    TextView tv_replied_message;

    RelativeLayout lay_reply;

    RelativeLayout lay_text;

    RelativeLayout lay_not_read_yet;

    TextView tv_unread;

    TextView tv_reactions;

    Context context;

    public BaseChatViewHolder(@NonNull Context context, @NonNull View itemView) {
        super(itemView);
        this.context = context;
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateNow = new Date();
        initView(itemView);
    }

    public void bindSearchKeyword(String keyword){
        tv_message.setTextToHighlight(DetectHtml.convertHtmlToPlain(keyword));
        tv_message.setTextHighlightColor("#BBDEFB");
        tv_message.setCaseInsensitive(true);
        tv_message.highlight();
    }

    public void bindData(Chat chat, boolean isDateVisible, boolean isRect, boolean isGroup, boolean isGroupNeedName, int position, int isMyChat, int isBeforeMyChat, ChatRoomAdapter.OnChatRoomClick onChatRoomClick){
        this.chat = chat;

        tv_message.setText(DetectHtml.convertHtmlToPlain(chat.getMessage_text()));
        tv_message.setVisibility((chat.getMessage_text().equals(" ") ? View.GONE : View.VISIBLE));

        tv_message.setTypeface(tv_message.getTypeface(), Typeface.NORMAL);
        tv_time.setText(chat.getMessage_time());

        Linkify.addLinks(tv_message, Linkify.PHONE_NUMBERS | Linkify.WEB_URLS);

        ViewGroup.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        Log.e("from view holder", "ini awal reaction");//+ chat.getReactionList().size());

        if (tv_reactions != null && chat.getReactionList() != null && chat.getReactionList().size() > 0) {
            Log.e("from view holder", "ini besar reaction " + chat.getReactionList().size());
            tv_reactions.setVisibility(View.VISIBLE);
            String buffer = "";
            for (int x = 0; x < chat.getReactionList().size(); x++) {
                buffer += chat.getReactionList().get(x).getEmoji() + " | " +
                        chat.getReactionList().get(x).getName();

                if (x <= chat.getReactionList().size() - 1) {
                    buffer += "\n";
                }
            }
            tv_reactions.setText(buffer);

        }

        LinearLayout.LayoutParams p_time = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//        p_time.weight = 1.0f;
//        p_time.setMargins(Utillity.getDP(context, 8.0), 0, Utillity.getDP(context, 8.0), 0);
        p_time.setMargins(0, 0, 0, 0);
        if (isMyChat == 1) {
            if (isBeforeMyChat == 1) {
            } else {
            }
            if (isRect) {
                lay_chat.setBackgroundResource(R.drawable.bg_space_chat_r_z);
                ((LinearLayout.LayoutParams) params).setMargins(ChatoUtils.getDP(context, 33.0), ChatoUtils.getDP(context, 12.0), ChatoUtils.getDP(context, 8.2), 0);
            } else {
                lay_chat.setBackgroundResource(R.drawable.bg_space_chat_r);
                ((LinearLayout.LayoutParams) params).setMargins(ChatoUtils.getDP(context, 33.0), ChatoUtils.getDP(context, 2.0), ChatoUtils.getDP(context, 16.0), 0);
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                lay_chat.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.colorBubbleActive));
            }
            tv_message.setTextColor(context.getResources().getColor(R.color.white));
            tv_time.setTextColor(context.getResources().getColor(R.color.white));

            lay_container_chat.setGravity(Gravity.RIGHT);
            img_receipt.setVisibility(View.VISIBLE);
            p_time.gravity = Gravity.RIGHT;
        } else {
            if (isBeforeMyChat == 1) {
            } else {
            }
            if (isRect) {
                lay_chat.setBackgroundResource(R.drawable.bg_space_chat_l_z);
                ((LinearLayout.LayoutParams) params).setMargins(ChatoUtils.getDP(context, 8.2), ChatoUtils.getDP(context, 12.0), ChatoUtils.getDP(context, 33.0), 0);
            } else {
                lay_chat.setBackgroundResource(R.drawable.bg_space_chat_l);
                ((LinearLayout.LayoutParams) params).setMargins(ChatoUtils.getDP(context, 16.0), ChatoUtils.getDP(context, 2.0), ChatoUtils.getDP(context, 33.0), 0);
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                lay_chat.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.colorOppositeChatBubble));
            }
            tv_message.setTextColor(context.getResources().getColor(R.color.colorMonocrome100));
            tv_time.setTextColor(context.getResources().getColor(R.color.colorMonocrome100));

            lay_container_chat.setGravity(Gravity.LEFT);
            img_receipt.setVisibility(View.GONE);
            p_time.gravity = Gravity.RIGHT;
        }

        lay_container_chat.setLayoutParams(params);
        lay_status.setLayoutParams(p_time);

        if (chat.getMessage_date() != null) {
            if (!chat.getMessage_date().equals("")) {
                if (dateFormat.format(dateNow).equalsIgnoreCase(chat.getMessage_date())) {
                    tv_date.setText("HARI INI");
                } else {
                    try {
                        tv_date.setText(chat.getMessage_date().split("-")[2] + " " + StringUtils.getCompleteMonthName(Integer.valueOf(chat.getMessage_date().split("-")[1]) - 1) + " " + chat.getMessage_date().split("-")[0]);
                    } catch (IndexOutOfBoundsException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        if (isGroup && isMyChat != 1) {
            tv_name.setText(chat.getFrom_username());
            if (isGroupNeedName) {
                tv_name.setVisibility(View.VISIBLE);
                tv_name.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
            } else {
                tv_name.setVisibility(View.GONE);
            }
        } else {
            tv_name.setVisibility(View.GONE);
        }

        if (isDateVisible) {
            tv_date.setVisibility(View.VISIBLE);
        } else {
            tv_date.setVisibility(View.GONE);
        }

        switch (chat.getMessage_status()) {
            case StringConstant.chat_status_delivered:
                img_receipt.setImageResource(R.drawable.ic_double_tick_indicator);
                break;
            case StringConstant.chat_status_pending:
                img_receipt.setImageResource(R.drawable.ic_message_terkirim);
                break;
            case StringConstant.chat_status_read:
                img_receipt.setImageResource(R.drawable.ic_double_tick_blue);
                break;
            case StringConstant.chat_status_failed:
                img_receipt.setImageResource(R.drawable.ic_error_black_24dp);
                break;
            case StringConstant.chat_status_sending:
                img_receipt.setImageResource(R.drawable.ic_message_loading);
                break;
        }

        if (chat.getMessage_is_forward() == 1) {
            lay_forwarded.setVisibility(View.VISIBLE);
        } else {
            lay_forwarded.setVisibility(View.GONE);
        }

        if (chat.getMessage_is_broadcast() == 1) {
            img_broadcast.setVisibility(View.VISIBLE);
        } else {
            img_broadcast.setVisibility(View.GONE);
        }


        lay_message.setOnLongClickListener(onLongClickListener(position, onChatRoomClick));

        lay_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onChatRoomClick.onClickItemView(view, position);
            }
        });

        if (chat.isClicked()) {
            lay_message.setBackgroundColor(Color.parseColor("#965956FF"));
        } else {
            lay_message.setBackgroundColor(Color.TRANSPARENT);
        }

        if (chat.getMessage_star() == 1) {
            img_star.setVisibility(View.VISIBLE);
        } else {
            img_star.setVisibility(View.GONE);
        }

        if (chat.getMessage_is_replay() == 1 ||
                (chat.getMessage_replay_username() != null && !TextUtils.isEmpty(chat.getMessage_replay_username()))) {
            container_reply.setVisibility(View.VISIBLE);
            bindReplyMessage(chat, onChatRoomClick);
        } else {
            container_reply.setVisibility(View.GONE);
        }

    }

    protected View.OnLongClickListener onLongClickListener(int position, ChatRoomAdapter.OnChatRoomClick onChatRoomClick){
        return new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                onChatRoomClick.onLongPress(view, position);
                return true;
            }
        };
    }

    public void bindDeletedData(Chat chat, boolean isDateVisible, boolean isRect, boolean isGroup, int position, int isMyChat, int isBeforeMyChat, ChatRoomAdapter.OnChatRoomClick onChatRoomClick){
        this.chat = chat;

        tv_message.setText("Pesan telah dihapus");
        tv_time.setText(chat.getMessage_time());
        tv_message.setTextColor(context.getResources().getColor(R.color.grey_400));
        tv_message.setTypeface(tv_message.getTypeface(), Typeface.ITALIC);


        ViewGroup.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        LinearLayout.LayoutParams p_time = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        p_time.setMargins(0, 0, 0, 0);
        if(isMyChat == 1){
            if (isBeforeMyChat==1) {
            }else {
            }
            if(isRect){
                lay_chat.setBackgroundResource(R.drawable.bg_space_chat_r_z);
                ((LinearLayout.LayoutParams) params).setMargins(ChatoUtils.getDP(context, 33.0), ChatoUtils.getDP(context, 12.0), ChatoUtils.getDP(context, 8.2), 0);
            } else {
                lay_chat.setBackgroundResource(R.drawable.bg_space_chat_r);
                ((LinearLayout.LayoutParams) params).setMargins(ChatoUtils.getDP(context, 33.0), ChatoUtils.getDP(context, 2.0), ChatoUtils.getDP(context, 16.0), 0);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                lay_chat.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.colorBubble));
                lay_chat.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.colorBubbleActive));
            }
            tv_message.setTextColor(context.getResources().getColor(R.color.white));
            tv_time.setTextColor(context.getResources().getColor(R.color.white));

            lay_container_chat.setGravity(Gravity.RIGHT);
            img_receipt.setVisibility(View.VISIBLE);
            p_time.gravity = Gravity.RIGHT;
        } else {
            if (isBeforeMyChat ==1) {
            }else {
            }
            if(isRect){
                lay_chat.setBackgroundResource(R.drawable.bg_space_chat_l_z);
                ((LinearLayout.LayoutParams) params).setMargins(ChatoUtils.getDP(context, 8.2), ChatoUtils.getDP(context, 12.0), ChatoUtils.getDP(context, 33.0), 0);
            } else {
                lay_chat.setBackgroundResource(R.drawable.bg_space_chat_l);
                ((LinearLayout.LayoutParams) params).setMargins(ChatoUtils.getDP(context, 16.0), ChatoUtils.getDP(context, 2.0), ChatoUtils.getDP(context, 33.0), 0);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                lay_chat.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.colorOppositeChatBubble));
            }

            tv_time.setTextColor(context.getResources().getColor(R.color.colorMonocrome100));
            lay_container_chat.setGravity(Gravity.LEFT);
            img_receipt.setVisibility(View.GONE);
            p_time.gravity = Gravity.LEFT;
        }

        lay_container_chat.setLayoutParams(params);
        lay_status.setLayoutParams(p_time);

        if(chat.getMessage_date()!=null){
            if(!chat.getMessage_date().equals("")){
                if(dateFormat.format(dateNow).equalsIgnoreCase(chat.getMessage_date())){
                    tv_date.setText("HARI INI");
                } else {
                    try {
                        tv_date.setText(chat.getMessage_date().split("-")[2]+" "+StringUtils.getCompleteMonthName(Integer.valueOf(chat.getMessage_date().split("-")[1])-1)+" "+chat.getMessage_date().split("-")[0]);
                    } catch (IndexOutOfBoundsException e){
                        e.printStackTrace();
                    }
                }
            }
        }

        if(isGroup && isMyChat !=1){
            tv_name.setVisibility(View.VISIBLE);
            tv_name.setText(chat.getFrom_username());
        } else {
            tv_name.setVisibility(View.GONE);
        }

        if(isDateVisible){
            tv_date.setVisibility(View.VISIBLE);
        } else {
            tv_date.setVisibility(View.GONE);
        }

        img_receipt.setVisibility(View.GONE);
        lay_forwarded.setVisibility(View.GONE);
        img_star.setVisibility(View.GONE);
        container_reply.setVisibility(View.GONE);
        lay_message.setBackgroundColor(Color.TRANSPARENT);
        lay_message.setOnLongClickListener(null);
        lay_message.setOnClickListener(null);
    }

    private void bindReplyMessage(Chat chat, ChatRoomAdapter.OnChatRoomClick onChatRoomClick){


        /*lay_reply.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int height = lay_reply.getMeasuredWidth();
                lay_text.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener(){
                    @Override
                    public void onGlobalLayout() {
                        Log.d("reply", "bindReplyMessage: "+height +" - "+lay_text.getMeasuredWidth());

                        if(height >= lay_text.getMeasuredWidth()){
                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.WRAP_CONTENT, height);
                            lay_text.setLayoutParams(params);

                        } else {
                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.WRAP_CONTENT, lay_text.getMeasuredWidth());
                            lay_reply.setLayoutParams(params);
                        }

                        lay_text.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                });

                // Ensure you call it only once :
                lay_reply.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });*/

        img_replied_message.setVisibility(View.GONE);
        switch (chat.getMessage_replay_type()){
            case RoomChat.image_type:
//                img_replied_message.setVisibility(View.VISIBLE);
//                Picasso.get()
//                        .load(chat.getReplay_message().getUri_attachment())
//                        .placeholder(R.drawable.chato_logo)
//                        .into(img_replied_message);
                tv_replied_message.setText(String.format("(Foto) %s", chat.getMessage_replay_text()));
                break;
            case RoomChat.file_type:
                tv_replied_message.setText(String.format("(Berkas) %s", chat.getMessage_replay_text()));
                break;
            case RoomChat.video_type:
                tv_replied_message.setText(String.format("(Video) %s", chat.getMessage_replay_text()));
                break;
            default:
                tv_replied_message.setText(chat.getMessage_replay_text());
                break;
        }

        tv_replied_title.setText(chat.getMessage_replay_username());
        container_reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onChatRoomClick.onClickRepliedMessage(chat);
            }
        });
    }

    public interface OnChatViewHolder{

    }

    public void bindUnreadIndicator(boolean isUnread, int data, ChatRoomAdapter.OnChatRoomClick onChatRoomClick){
        if(isUnread){
            lay_not_read_yet.setVisibility(View.VISIBLE);
            tv_unread.setText(data+" pesan belum terbaca");
            onChatRoomClick.onReadMessage(chat);
        } else {
            lay_not_read_yet.setVisibility(View.GONE);
        }
    }

    private void initView(View view){
        tv_date = view.findViewById(R.id.tv_date);
        tv_message = view.findViewById(R.id.tv_message);
        tv_name = view.findViewById(R.id.tv_name);
        tv_time = view.findViewById(R.id.tv_time);
        img_terusan = view.findViewById(R.id.img_terusan);
        lay_message = view.findViewById(R.id.lay_message);
        img_receipt = view.findViewById(R.id.img_receipt);
        lay_chat = view.findViewById(R.id.lay_chat);
        lay_container_chat = view.findViewById(R.id.lay_container_chat);
        lay_status = view.findViewById(R.id.lay_status);
        img_star = view.findViewById(R.id.img_star);
        img_replied_message = view.findViewById(R.id.img_replied_message);
        lay_forwarded = view.findViewById(R.id.lay_forwarded);
        img_broadcast = view.findViewById(R.id.img_broadcast);
        tv_terusan = view.findViewById(R.id.tv_terusan);
        container_reply = view.findViewById(R.id.container_reply);
        tv_replied_title = view.findViewById(R.id.tv_replied_title);
        tv_replied_message = view.findViewById(R.id.tv_replied_message);
        lay_reply = view.findViewById(R.id.lay_reply);
        lay_text = view.findViewById(R.id.lay_text);
        lay_not_read_yet = view.findViewById(R.id.lay_not_read_yet);
        tv_unread = view.findViewById(R.id.tv_unread);
        tv_reactions = view.findViewById(R.id.tv_reactions);
    }

}
