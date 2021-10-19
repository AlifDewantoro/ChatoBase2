package com.gamatechno.chato.sdk.app.chatrooms.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.gamatechno.chato.sdk.R;
import com.gamatechno.chato.sdk.app.chatrooms.uimodel.ChatRoomsUiModel;
import com.gamatechno.chato.sdk.app.main.searchlist.SearchChatroomModel;
import com.gamatechno.chato.sdk.data.DAO.RoomChat.RoomChat;
import com.gamatechno.chato.sdk.data.constant.StringConstant;
import com.gamatechno.chato.sdk.data.model.UserModel;
import com.gamatechno.chato.sdk.utils.ChatoText.EmphasisTextView.EmphasisTextView;
import com.gamatechno.chato.sdk.utils.ChatoUtils;
import com.gamatechno.ggfw_ui.avatarview.AvatarPlaceholder;
import com.gamatechno.ggfw_ui.avatarview.loader.PicassoLoader;
import com.gamatechno.ggfw_ui.avatarview.views.AvatarView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ChatroomViewHolder extends RecyclerView.ViewHolder{


    AvatarView avatarView;

    EmphasisTextView title;

    EmphasisTextView message;
    EmphasisTextView tv_label;

    TextView time;

    CardView card_indicator;

    //ImageView iv_group;

    CardView card_check;

    CardView lay_chat_rooms;

    ImageView img_pinned;

    CardView card_tint;

    LinearLayout card_mention;

    TextView tint;

    ShimmerFrameLayout loading;

    RelativeLayout adapter;

    ImageView img_indicator_attachment;

    ImageView img_check;

    Context context;
    UserModel userModel;

    String today;
    String labels = "";
    String yesterday;
//        CardView card_color;

    public ChatroomViewHolder(@NonNull View itemView, Context context) {
        super(itemView);
        this.setIsRecyclable(false);
        initView(itemView);
        this.context = context;
        userModel = ChatoUtils.getUserLogin(context);

        Calendar calender = Calendar.getInstance();
        calender.setTime(new Date());
        today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calender.getTime());
        calender.add(Calendar.DAY_OF_YEAR, -1);
        yesterday = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calender.getTime());
    }

    public void bindLoading(){
        adapter.setVisibility(View.GONE);
        loading.setVisibility(View.VISIBLE);
    }

    public void bindDatas(SearchChatroomModel data) {
        ChatRoomsUiModel model = data.getChatRoomUiModel();
        adapter.setVisibility(View.VISIBLE);
        loading.setVisibility(View.GONE);
        tv_label.setText(model.getRoomChat().getRoom_category());

        PicassoLoader imageLoader = new PicassoLoader();
        AvatarPlaceholder refreshableAvatarPlaceholder;
//        AvatarPlaceholder refreshableAvatarPlaceholder = new AvatarPlaceholder(model.getRoomChat().getRoom_name());
//        imageLoader.loadImage(avatarView, refreshableAvatarPlaceholder, (model.getRoomChat().getRoom_photo().equals("") ? model.getRoomChat().getRoom_name() : model.getRoomChat().getRoom_photo()));
//        title.setText(model.getRoomChat().getRoom_name());

        switch (model.getRoomChat().getRoom_type()){
            case RoomChat.group_room_type :
                refreshableAvatarPlaceholder = new AvatarPlaceholder(model.getRoomChat().getRoom_name());
                imageLoader.loadImage(avatarView, refreshableAvatarPlaceholder, (model.getRoomChat().getRoom_photo().equals("") ? model.getRoomChat().getRoom_name() : model.getRoomChat().getRoom_photo()));
                title.setText(model.getRoomChat().getRoom_name());
                bindGroupObrolanData(model);
                break;
            case RoomChat.official_room_type :
                refreshableAvatarPlaceholder = new AvatarPlaceholder(model.getRoomChat().getRoom_name());
                imageLoader.loadImage(avatarView, refreshableAvatarPlaceholder, (model.getRoomChat().getRoom_photo().equals("") ? model.getRoomChat().getRoom_name() : model.getRoomChat().getRoom_photo()));
                title.setText(model.getRoomChat().getRoom_name());
                bindOfficialObrolanData(model);
                break;
            case RoomChat.user_room_type :
            default:
                refreshableAvatarPlaceholder = new AvatarPlaceholder(model.getRoomChat().getUser_name());
                imageLoader.loadImage(avatarView, refreshableAvatarPlaceholder, (model.getRoomChat().getUser_photo().equals("") ? model.getRoomChat().getUser_name() : model.getRoomChat().getUser_photo()));
                title.setText(model.getRoomChat().getUser_name());
                bindObrolanData(model);
                break;
        }
//        if(model.getRoomChat().getRoom_type().equalsIgnoreCase(RoomChat.group_room_type)){
//            AvatarPlaceholder refreshableAvatarPlaceholder = new AvatarPlaceholder(model.getRoomChat().getRoom_name());
//            imageLoader.loadImage(avatarView, refreshableAvatarPlaceholder, (model.getRoomChat().getRoom_photo().equals("") ? model.getRoomChat().getRoom_name() : model.getRoomChat().getRoom_photo()));
//            title.setText(model.getRoomChat().getRoom_name());
//            bindGroupObrolanData(model);
//        } else {
//            AvatarPlaceholder refreshableAvatarPlaceholder = new AvatarPlaceholder(model.getRoomChat().getRoom_name());
//            imageLoader.loadImage(avatarView, refreshableAvatarPlaceholder, (model.getRoomChat().getUser_photo().equals("") ? model.getRoomChat().getUser_name() : model.getRoomChat().getUser_photo()));
//            title.setText(model.getRoomChat().getUser_name());
//            bindObrolanData(model);
//        }

        if(data.getTitle() != null){
            title.setTextToHighlight(data.getTitle());
            title.setTextHighlightColor("#BBDEFB");
            title.setCaseInsensitive(true);
            title.highlight();
        }
    }

    public void bindDatas(ChatRoomsUiModel model) {
        adapter.setVisibility(View.VISIBLE);
        loading.setVisibility(View.GONE);
        tv_label.setText(model.getRoomChat().getRoom_category());
        PicassoLoader imageLoader = new PicassoLoader();
        AvatarPlaceholder refreshableAvatarPlaceholder = new AvatarPlaceholder(model.getRoomChat().getRoom_name(), Color.parseColor("#42B6B2"), Color.parseColor("#F9F9FC"));
        imageLoader.loadImage(avatarView, refreshableAvatarPlaceholder, (model.getRoomChat().getRoom_photo().equals("") ? model.getRoomChat().getRoom_name() : model.getRoomChat().getRoom_photo()));
        title.setText((model.getRoomChat().getRoom_nick_name().isEmpty()) ? model.getRoomChat().getRoom_name() : model.getRoomChat().getRoom_nick_name());

        /*for (int i = 0; i < model.getRoomChat().getLabel_title().size(); i++) {
            labels += model.getRoomChat().getLabel_title().get(i);
            if(i < model.getRoomChat().getLabel_title().size()-1)
                labels+=", ";
        }
        tv_label.setText(labels);*/


        switch (model.getRoomChat().getRoom_type()){
            case RoomChat.group_room_type :
                bindGroupObrolanData(model);
                break;
            case RoomChat.official_room_type :
                bindOfficialObrolanData(model);
                break;
            case RoomChat.user_room_type :
            default:
                bindObrolanData(model);
                break;
        }
//        if(model.getRoomChat().getRoom_type().equalsIgnoreCase(RoomChat.group_room_type)){
//            bindGroupObrolanData(model);
//        } else {
//            bindObrolanData(model);
//        }
    }

    public void bindObrolanData(ChatRoomsUiModel model){
        card_indicator.setVisibility(View.VISIBLE);
        //iv_group.setVisibility(View.GONE);
        if(model.getRoomChat().getIs_online()==1){
            card_indicator.setCardBackgroundColor(context.getResources().getColor(R.color.green_600));
            card_indicator.setVisibility(View.VISIBLE);
        } else {
            card_indicator.setCardBackgroundColor(context.getResources().getColor(R.color.grey_600));
            card_indicator.setVisibility(View.GONE);
        }

        bindData(model);
    }

    public void bindGroupObrolanData(ChatRoomsUiModel model){
        card_indicator.setVisibility(View.GONE);
        //iv_group.setVisibility(View.VISIBLE);
        //iv_group.setImageResource(R.drawable.ic_group_black_24dp);

        bindData(model);
    }

    public void bindOfficialObrolanData(ChatRoomsUiModel model){
        card_indicator.setVisibility(View.GONE);
        //iv_group.setVisibility(View.VISIBLE);
        //iv_group.setImageResource(R.drawable.ic_star_full);

        bindData(model);
    }

    private void bindData(ChatRoomsUiModel model){
        message.setText(getHeaderMessage(model, model.getRoomChat().getLast_message()));
        tv_label.setText(model.getRoomChat().getRoom_category());
        String lastTime = model.getRoomChat().getLast_date();
        card_indicator.setVisibility(View.GONE);
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(lastTime);

            if(model.getRoomChat().getLast_date().equals(today))
                time.setText(model.getRoomChat().getLast_time());
            else if(model.getRoomChat().getLast_date().equals(yesterday))
                time.setText("Kemarin");
            else
                time.setText(new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (Exception e){
            time.setText("");
        }
//
        if(model.getRoomChat().getIs_pined() == 0){
            img_pinned.setVisibility(View.GONE);
        } else {
            img_pinned.setVisibility(View.VISIBLE);
        }

        if(model.getIs_checked()){
            card_check.setVisibility(View.VISIBLE);
            lay_chat_rooms.setBackground(context.getResources().getDrawable(R.drawable.bg_chat_item_selected));
        } else {
            card_check.setVisibility(View.GONE);
            lay_chat_rooms.setBackground(context.getResources().getDrawable(R.drawable.bg_chat_item));
        }

        if(model.getRoomChat().getUnread_message() != 0){
            card_tint.setVisibility(View.VISIBLE);
            tint.setText(""+(model.getRoomChat().getUnread_message()));
        } else {
            card_tint.setVisibility(View.GONE);
        }

        if (model.getRoomChat().getIs_mention()==1){
            card_mention.setVisibility(View.VISIBLE);
        } else {
            card_mention.setVisibility(View.GONE);
        }

        if(model.getRoomChat().getDetail_last_message().getFrom_user_id() == userModel.getUser_id()){
            img_check.setVisibility(View.VISIBLE);
            switch (model.getRoomChat().getDetail_last_message().getMessage_status()){
                case StringConstant.chat_status_delivered:
                    img_check.setImageResource(R.drawable.ic_double_tick_indicator);
                    break;
                case StringConstant.chat_status_pending:
                    img_check.setImageResource(R.drawable.ic_message_terkirim);
                    break;
                case StringConstant.chat_status_read:
                    img_check.setImageResource(R.drawable.ic_double_tick_blue);
                    break;
                case StringConstant.chat_status_failed:
                    img_check.setImageResource(R.drawable.ic_message_failed);
                    break;
                case StringConstant.chat_status_sending:
                    img_check.setImageResource(R.drawable.ic_message_loading);
                    break;
            }
        } else {
            img_check.setVisibility(View.GONE);
        }

        if(model.getRoomChat().getDetail_last_message().getMessage_type()!=null && model.getRoomChat().getDetail_last_message().getIs_deleted() == 0){
            message.setTextColor(context.getResources().getColor(R.color.grey_700));
            message.setTypeface(message.getTypeface(), Typeface.NORMAL);

            switch (model.getRoomChat().getDetail_last_message().getMessage_type()){
                case RoomChat.image_type:
                    img_indicator_attachment.setVisibility(View.VISIBLE);
                    img_indicator_attachment.setImageResource(R.drawable.ic_galeri);
                    message.setText(getHeaderMessage(model, (model.getRoomChat().getLast_message().equals(" ") ? "(Photo)" : ""+model.getRoomChat().getLast_message())));
                    break;
                case RoomChat.video_type:
                    img_indicator_attachment.setVisibility(View.VISIBLE);
                    img_indicator_attachment.setImageResource(R.drawable.ic_camera_alt_black_24dp);
                    message.setText(getHeaderMessage(model, (model.getRoomChat().getLast_message().equals(" ") ? "(Video)" : ""+model.getRoomChat().getLast_message())));
                    break;
                case RoomChat.file_type:
                    img_indicator_attachment.setVisibility(View.VISIBLE);
                    img_indicator_attachment.setImageResource(R.drawable.ic_dokumen);
                    message.setText(getHeaderMessage(model, (model.getRoomChat().getLast_message().equals(" ") ? "(Berkas)" : ""+model.getRoomChat().getLast_message())));
                    break;
                case RoomChat.voice_type:
                    img_indicator_attachment.setVisibility(View.VISIBLE);
                    img_indicator_attachment.setImageResource(R.drawable.ic_mic);
                    message.setText(getHeaderMessage(model, (model.getRoomChat().getLast_message().equals(" ") ? "(Voice)" : ""+model.getRoomChat().getLast_message())));
                    break;
                default:
                    img_indicator_attachment.setVisibility(View.GONE);
                    break;
            }
        } else {
            img_indicator_attachment.setVisibility(View.GONE);
            message.setTypeface(message.getTypeface(), Typeface.ITALIC);
            message.setText("Pesan telah dihapus");
            message.setTextColor(context.getResources().getColor(R.color.grey_500));
        }
    }

    private String getHeaderMessage(ChatRoomsUiModel model, String message){
        if(model.getRoomChat().getDetail_last_message().getFrom_user_id() != userModel.getUser_id()
                && model.getRoomChat().getRoom_type().equalsIgnoreCase(RoomChat.group_room_type)
                && model.getRoomChat().getDetail_last_message().getMessage_type_num() != StringConstant.type_item_label){
            return model.getRoomChat().getDetail_last_message().getFrom_username()+" : "+ message;
        }
        return message;
    }

    private void initView(View view){
        adapter = view.findViewById(R.id.adapter);
        loading = view.findViewById(R.id.loading);
        avatarView = view.findViewById(R.id.avatarView);
        title = view.findViewById(R.id.title);
        message = view.findViewById(R.id.message);
        tv_label = view.findViewById(R.id.tv_label);
        time = view.findViewById(R.id.time);
        card_indicator = view.findViewById(R.id.card_indicator);
        //iv_group = view.findViewById(R.id.iv_group);
        card_check = view.findViewById(R.id.card_check);
        lay_chat_rooms = view.findViewById(R.id.lay_chat_rooms);
        img_pinned = view.findViewById(R.id.img_pinned);
        card_tint = view.findViewById(R.id.card_tint);
        card_mention = view.findViewById(R.id.card_mention);
        tint = view.findViewById(R.id.tint);
        img_indicator_attachment = view.findViewById(R.id.img_indicator_attachment);
        img_check = view.findViewById(R.id.img_check);
//        card_color = view.findViewById(R.id.card_color);
    }


}
