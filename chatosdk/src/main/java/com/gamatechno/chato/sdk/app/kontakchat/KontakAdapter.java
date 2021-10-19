package com.gamatechno.chato.sdk.app.kontakchat;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.gamatechno.chato.sdk.R;
import com.gamatechno.chato.sdk.data.DAO.RoomChat.RoomChat;
import com.gamatechno.chato.sdk.data.model.UserModel;
import com.gamatechno.chato.sdk.utils.ChatoText.EmphasisTextView.EmphasisTextView;
import com.gamatechno.ggfw_ui.avatarview.AvatarPlaceholder;
import com.gamatechno.ggfw_ui.avatarview.loader.PicassoLoader;
import com.gamatechno.ggfw_ui.avatarview.views.AvatarView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class KontakAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {

    Context context;
    List<KontakModel> kontakModelList;
    OnKontakAdapter onKontakAdapter;
    UserModel userLogin;
    int showLimit;

    Boolean hideStatus = false;
    String today;
    String yesterday;

    public KontakAdapter(Context context, UserModel userLogin, List<KontakModel> kontakModelList, OnKontakAdapter onKontakAdapter) {
        this.context = context;
        this.kontakModelList = kontakModelList;
        this.onKontakAdapter = onKontakAdapter;
        this.userLogin = userLogin;
        this.showLimit = showLimit;
    }

    public KontakAdapter(Context context, UserModel userLogin, List<KontakModel> kontakModelList, boolean hideStatus, OnKontakAdapter onKontakAdapter) {
        this.context = context;
        this.kontakModelList = kontakModelList;
        this.onKontakAdapter = onKontakAdapter;
        this.hideStatus = hideStatus;
        this.userLogin = userLogin;
        this.showLimit = showLimit;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View layoutView;
        switch (i){
            case 0:
                layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_header_kontak, parent, false);
                return new HeaderViewHolder(layoutView);
            case 1:
                layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_kontak, parent, false);
                return new ViewHolder(layoutView);

        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        final int itemType = getItemViewType(i);
        KontakModel kontakModel = kontakModelList.get(i);
        if(itemType == 0){
            ((HeaderViewHolder)viewHolder).bindHeader(kontakModel);
        } else {
            ((ViewHolder)viewHolder).bindKontak(kontakModel, i);
        }
    }

    @Override
    public int getItemCount() {
        return kontakModelList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (kontakModelList.get(position).is_header ? 0 : 1);
    }

    public void updateData(List<KontakModel> kontakModels){
        kontakModelList = kontakModels;
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        AvatarView avatarView;

        EmphasisTextView tv_name;

        CardView card_indicator;

        TextView tv_position;

        TextView tv_last_seen;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            avatarView = itemView.findViewById(R.id.avatarView);
            tv_name = itemView.findViewById(R.id.tv_name);
            card_indicator = itemView.findViewById(R.id.card_indicator);
            tv_position = itemView.findViewById(R.id.tv_position);
            tv_last_seen = itemView.findViewById(R.id.tv_last_seen);

            Calendar calender = Calendar.getInstance();
            calender.setTime(new Date());
            today = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(calender.getTime());
            calender.add(Calendar.DAY_OF_YEAR, -1);
            yesterday = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(calender.getTime());
        }

        private String getRoomName(KontakModel kontakModel){
            return kontakModel.getRoom_type().equals(RoomChat.user_room_type) ? ""+kontakModel.getUser_name() : ""+kontakModel.getGroup_name();
        }

        private void bindKontak(KontakModel kontakModel, int i){
            tv_name.setText(getRoomName(kontakModel));
            if(hideStatus || kontakModel.getRoom_type().equals(RoomChat.group_room_type)){
                card_indicator.setVisibility(View.GONE);
            }

            if(kontakModel.is_group_add){
                bindAddGroup();
            } else {
                bindData(kontakModel, i);
            }
        }

        private void bindData(KontakModel kontakModel, int i){
            PicassoLoader imageLoader = new PicassoLoader();
            AvatarPlaceholder refreshableAvatarPlaceholder = new AvatarPlaceholder(getRoomName(kontakModel), Color.parseColor("#42B6B2"), Color.parseColor("#F9F9FC"));

            imageLoader.loadImage(avatarView, refreshableAvatarPlaceholder, kontakModel.getUser_photo());

            if (kontakModel.user_id != userLogin.getUser_id()) {
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onKontakAdapter.onKontakClick(kontakModel, i);
                    }
                });
            }else{
                itemView.setClickable(false);
            }

            if (kontakModel.user_id != userLogin.getUser_id()) {
                itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        onKontakAdapter.onKontakLongClick(kontakModel, i);
                        return true;
                    }
                });
            }else{
                itemView.setClickable(false);
            }

            if(kontakModel.is_online > 0 ){
                tv_last_seen.setVisibility(View.VISIBLE);
                tv_last_seen.setText("Online");
                tv_last_seen.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            }else {
                if (kontakModel.last_seen != null && !kontakModel.last_seen.isEmpty()) {
                    tv_last_seen.setVisibility(View.VISIBLE);
                    try {
                        Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse(kontakModel.getLast_seen());

                        String justDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(date);
                        String justTime = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(date);
                        if (justDate.equals(today))
                            tv_last_seen.setText(context.getResources().getString(R.string.last_seen_today)+" "+justTime);
                        else if (justDate.equals(yesterday))
                            tv_last_seen.setText(R.string.last_seen_yesterday);
                        else
                            tv_last_seen.setText(getDayPass(date.getTime()) + " " + context.getResources().getString(R.string.days_ago));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        tv_last_seen.setText("");
                    }
                }else {
                    tv_last_seen.setVisibility(View.GONE);
                }
            }

            if(kontakModel.getIs_admin() == 0){
                tv_position.setVisibility(View.GONE);
            } else {
                tv_position.setVisibility(View.VISIBLE);
            }

            if(kontakModel.getIs_online()==1){
                card_indicator.setCardBackgroundColor(context.getResources().getColor(R.color.green_600));
            } else {
                card_indicator.setCardBackgroundColor(context.getResources().getColor(R.color.grey_600));
            }
        }

        private int getDayPass(long dataDate){
            Calendar cal = Calendar.getInstance();
            long nowMilis = cal.getTimeInMillis();

            long diff = nowMilis - dataDate;
            return (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
        }

        private void bindAddGroup(){
//            Picasso.get()
//                    .load(R.drawable.ic_add_group)
//                    .into(avatarView);
            avatarView.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_add_group));

            card_indicator.setVisibility(View.GONE);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onKontakAdapter.onMakeGroup();
                }
            });
        }
    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView tv_name;

        public HeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_name);
        }

        private void bindHeader(KontakModel kontakModel){
            tv_name.setText(kontakModel.getUser_name());
        }
    }

    public interface OnKontakAdapter{
        public void onKontakClick(KontakModel kontakModel, int position);
        public void onKontakLongClick(KontakModel kontakModel, int position);
        public void onMakeGroup();
    }
}
