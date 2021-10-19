package com.gamatechno.chato.sdk.app.grouproomadd.addgroup.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gamatechno.chato.sdk.R;
import com.gamatechno.chato.sdk.app.kontakchat.KontakModel;
import com.gamatechno.ggfw_ui.avatarview.AvatarPlaceholder;
import com.gamatechno.ggfw_ui.avatarview.loader.PicassoLoader;
import com.gamatechno.ggfw_ui.avatarview.views.AvatarView;

import java.util.List;

public class MemberCheckedAdapter extends RecyclerView.Adapter<MemberCheckedAdapter.ViewHolder>  {

    Context context;
    List<KontakModel> kontakModelList;
    OnKontakMiniAdapter onKontakAdapter;

    public MemberCheckedAdapter(Context context, List<KontakModel> kontakModelList, OnKontakMiniAdapter onKontakAdapter) {
        this.context = context;
        this.kontakModelList = kontakModelList;
        this.onKontakAdapter = onKontakAdapter;
    }

    @NonNull
    @Override
    public MemberCheckedAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_kontak_mini, parent, false);
        ViewHolder rcv = new ViewHolder(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(@NonNull MemberCheckedAdapter.ViewHolder viewHolder, int i) {
        KontakModel kontakModel = kontakModelList.get(i);

        PicassoLoader imageLoader = new PicassoLoader();
        AvatarPlaceholder refreshableAvatarPlaceholder = new AvatarPlaceholder(kontakModel.getUser_name(), Color.parseColor("#42B6B2"), Color.parseColor("#F9F9FC"));

        imageLoader.loadImage(viewHolder.avatar, refreshableAvatarPlaceholder, kontakModel.getUser_photo());
        viewHolder.tv_name.setText(kontakModel.getUser_name());

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onKontakAdapter.onKontakClick(kontakModel, i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return kontakModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        AvatarView avatar;

        TextView tv_name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            avatar = itemView.findViewById(R.id.avatar);
            tv_name = itemView.findViewById(R.id.tv_name);
        }
    }

    public interface OnKontakMiniAdapter{
        public void onKontakClick(KontakModel kontakModel, int position);
    }
}
