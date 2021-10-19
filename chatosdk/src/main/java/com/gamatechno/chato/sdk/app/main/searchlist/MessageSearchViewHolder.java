package com.gamatechno.chato.sdk.app.main.searchlist;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;


import com.gamatechno.chato.sdk.R;
import com.gamatechno.chato.sdk.utils.ChatoText.EmphasisTextView.EmphasisTextView;
import com.gamatechno.ggfw_ui.avatarview.views.AvatarView;

public class MessageSearchViewHolder extends RecyclerView.ViewHolder{


    AvatarView avatarView;

    EmphasisTextView title;

    EmphasisTextView message;

    TextView time;

    CardView card_indicator;

    public MessageSearchViewHolder(@NonNull View itemView) {
        super(itemView);
        this.setIsRecyclable(false);
        initView( itemView);
    }

    public void bindData(SearchChatroomModel model) {
        card_indicator.setVisibility(View.GONE);
        avatarView.setVisibility(View.GONE);

//        title.setText(model.getChatRoomUiModel().getUser_name());
        title.setVisibility(View.GONE);
        time.setText(model.getChat().getLast_time());

        message.setText(model.getChat().getMessage_text());

        if(model.getTitle() != null){
            message.setTextToHighlight(model.getTitle());
            message.setTextHighlightColor("#BBDEFB");
            message.setCaseInsensitive(true);
            message.highlight();
        }
    }

    private void initView(View view){
        avatarView = view.findViewById(R.id.avatarView);
        title = view.findViewById(R.id.title);
        message = view.findViewById(R.id.message);
        time = view.findViewById(R.id.time);
        card_indicator = view.findViewById(R.id.card_indicator);

    }
}
