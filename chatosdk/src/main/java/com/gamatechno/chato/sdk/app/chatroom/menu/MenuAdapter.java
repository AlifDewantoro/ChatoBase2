package com.gamatechno.chato.sdk.app.chatroom.menu;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gamatechno.chato.sdk.R;

import java.util.ArrayList;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuHolder> {

    ArrayList<MenuModel> menu;
    RelativeLayout layMenu;

    public MenuAdapter(ArrayList<MenuModel> menu, RelativeLayout layMenu) {
        this.menu = menu;
        this.layMenu = layMenu;
    }

    @NonNull
    @Override
    public MenuHolder onCreateViewHolder(@NonNull ViewGroup parent, int type) {
        return new MenuHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_menu_chatroom, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MenuHolder holder, int pos) {
        MenuModel menuItem = menu.get(pos);
        holder.ivIcon.setImageResource(menuItem.icon);
        holder.tvTitle.setText(menuItem.title);
        holder.view.setOnClickListener(v -> {
                menuItem.action.onClick();
                layMenu.setVisibility(View.GONE);
        });
    }

    @Override
    public int getItemCount() {
        return menu.size();
    }

    class MenuHolder extends RecyclerView.ViewHolder {
        View view;
        TextView tvTitle;
        ImageView ivIcon;

        public MenuHolder(@NonNull View v) {
            super(v);
            view = v;
            tvTitle = v.findViewById(R.id.tv_title);
            ivIcon = v.findViewById(R.id.iv_icon);
        }
    }
}
