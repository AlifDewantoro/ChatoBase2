package com.gamatechno.chato.sdk.app.main.searchlist;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;


import com.gamatechno.chato.sdk.R;

public class HeaderViewHolder extends RecyclerView.ViewHolder{

    TextView tv_title;

    public HeaderViewHolder(@NonNull View itemView) {
        super(itemView);
        this.setIsRecyclable(false);
        tv_title = itemView.findViewById(R.id.tv_title);
    }

    public void bindData(SearchChatroomModel model) {
        tv_title.setText(model.getTitle());
    }
}
