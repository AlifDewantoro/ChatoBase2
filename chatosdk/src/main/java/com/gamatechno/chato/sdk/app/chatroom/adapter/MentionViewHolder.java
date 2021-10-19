package com.gamatechno.chato.sdk.app.chatroom.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gamatechno.chato.sdk.R;
import com.gamatechno.chato.sdk.app.chatroom.model.MentionModel;
import com.gamatechno.chato.sdk.data.model.UserModel;
import com.gamatechno.chato.sdk.utils.ChatoUtils;
import com.gamatechno.ggfw_ui.avatarview.AvatarPlaceholder;
import com.gamatechno.ggfw_ui.avatarview.loader.PicassoLoader;
import com.gamatechno.ggfw_ui.avatarview.views.AvatarView;

public class MentionViewHolder extends RecyclerView.ViewHolder{
    AvatarView ava_mention;
    TextView tv_mention_name;

    Context context;
    UserModel userModel;

    public MentionViewHolder(@NonNull View itemView, Context context) {
        super(itemView);
        initView(itemView);
        this.context = context;
        userModel = ChatoUtils.getUserLogin(context);
    }

    public void bindData(MentionModel data){
        PicassoLoader imageLoader = new PicassoLoader();
        AvatarPlaceholder refreshableAvatarPlaceholder = new AvatarPlaceholder(data.getName());
        imageLoader.loadImage(ava_mention, refreshableAvatarPlaceholder, (data.getPictureUrl().equals("") ? data.getName() : data.getPictureUrl()));
        tv_mention_name.setText(data.getName());
    }

    private void initView(View view){
        ava_mention = view.findViewById(R.id.ava_mention);
        tv_mention_name = view.findViewById(R.id.tv_mention_name);
    }
}
