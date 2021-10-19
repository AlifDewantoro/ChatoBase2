package com.gamatechno.chato.sdk.app.starredmessage;

import android.app.ActivityOptions;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gamatechno.chato.sdk.R;
import com.gamatechno.chato.sdk.app.chatroom.ChatroomHelper;
import com.gamatechno.chato.sdk.app.chatroom.adapter.ChatRoomAdapter;
import com.gamatechno.chato.sdk.app.chatroom.model.ChatRoomUiModel;
import com.gamatechno.chato.sdk.app.photopreview.ImageViewActivity;
import com.gamatechno.chato.sdk.app.playvideo.PlayVideoActivity;
import com.gamatechno.chato.sdk.data.DAO.Chat.Chat;
import com.gamatechno.chato.sdk.data.constant.Api;
import com.gamatechno.chato.sdk.data.constant.StringConstant;
import com.gamatechno.chato.sdk.module.activity.ChatoCoreActivity;
import com.gamatechno.chato.sdk.utils.ChatoUtils;
import com.gamatechno.chato.sdk.utils.FilePath.IOUtils;
import com.gamatechno.chato.sdk.utils.animation.AnimationToggle;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class StarredMessageActivity extends ChatoCoreActivity implements StarredMessageView.View, View.OnClickListener {


    protected Toolbar toolbar;

    protected RecyclerView rv;

    protected RelativeLayout helper_nodata;

    protected TextView tv_nodata;

    protected RelativeLayout helper_loading;

    protected AnimationToggle appbar_action;

    protected ImageView img_action_back;

    protected ImageView img_unstar;

    ChatRoomAdapter adapter;
    List<Chat> chatList;
    Chat selectedChat;

    ChatRoomUiModel chatRoomUiModel;
    String room_id = "";
    String api = "";
    boolean isActionBarActive = false;

    StarredMessagePresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_global_starred_message);
        initView();

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        presenter = new StarredMessagePresenter(getContext(), this);

        api = Api.list_starred_message(room_id);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Pesan Berbintang");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(getIntent().hasExtra("room")){
//            room_id = chatRoomUiModel.getRoom_id();
            chatRoomUiModel = (ChatRoomUiModel) getIntent().getSerializableExtra("room");
            api = Api.list_starred_message(""+chatRoomUiModel.getRoom_id(), ""+chatRoomUiModel.getUser_id());
            getSupportActionBar().setSubtitle(chatRoomUiModel.getTitle());
        }

        chatList = new ArrayList<>();
        initComponent();

        appbar_action.setInOutAnimation(R.anim.pull_in_bottom, R.anim.push_out_top);
        img_action_back.setOnClickListener(this);
        img_unstar.setOnClickListener(this);
        presenter.requestStarredMessage(api);
    }

    @Override
    public void onRequestStarredMessage(List<Chat> cl) {
        if(cl.size()>0) {
            chatList.clear();
            chatList.addAll(cl);
            adapter.notifiyListChanged();
        } else {
            helper_nodata.setVisibility(View.VISIBLE);
            helper_loading.setVisibility(View.GONE);
            tv_nodata.setText("Tidak ada Pesan Berbintang");
        }
    }

    @Override
    public void onFailedRequest(String message) {
        helper_nodata.setVisibility(View.VISIBLE);
        helper_loading.setVisibility(View.GONE);
        tv_nodata.setText(message);
    }

    @Override
    public void onLoading() {
        helper_loading.setVisibility(View.VISIBLE);
        helper_nodata.setVisibility(View.GONE);
    }

    @Override
    public void onHideLoading() {
        helper_loading.setVisibility(View.GONE);
    }

    @Override
    public void onErrorConnection(String message) {
        helper_nodata.setVisibility(View.VISIBLE);
        tv_nodata.setText(message);
    }

    @Override
    public void onAuthFailed(String error) {

    }

    Chat prevChat;
    int prevPos = 0;

    private void initComponent(){
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ChatRoomAdapter(getContext(), chatList, new ChatRoomAdapter.OnChatRoomClick() {
            @Override
            public void onLongPress(View view, int position) {
                if(chatRoomUiModel!=null) {
                    chatList.get(position).setClicked(true);
                    selectedChat = chatList.get(position);
                    adapter.notifyDataSetChanged();
                    setActionBar(true);
                }
//                presenter.checkSelectedChat(chatList);
            }

            @Override
            public void onClickItemView(View view, int position) {
                if(isActionBarActive){
                    if(chatList.get(position).isClicked()){
                        chatList.get(position).setClicked(false);
                        selectedChat = null;
                        adapter.notifiyListChanged();
                        setActionBar(false);
                    } else {
                        for(Chat chat:chatList){
                            chat.setClicked(false);
                        }
                        chatList.get(position).setClicked(true);
                        selectedChat = chatList.get(position);
                        adapter.notifiyListChanged();
                    }
                }
            }

            @Override
            public void onClickRepliedMessage(Chat chat) {

            }

            @Override
            public void onClickAttachment(Chat chat, Uri uri) {
                try {
                    IOUtils.openFile(getContext(), new File(uri.toString().replace("file:/", "")));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onDownloadingAttachment(boolean isDownload, int position) {
                chatList.get(position).setVideoDownloding(isDownload);
                adapter.notifiyListChanged();
            }

            @Override
            public void onOpenVideo(View view, Chat chat, Uri uri) {
                Bundle data = new Bundle();
                data.putSerializable("chat", chat);
                data.putString("uri", uri.toString());

                if (!ChatoUtils.isPreLolipop()){
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(StarredMessageActivity.this, view, ViewCompat.getTransitionName(view));
                    startActivity(new Intent(getContext(), PlayVideoActivity.class).putExtras(data), options.toBundle());
                }else{
                    startActivity(new Intent(getContext(), ImageViewActivity.class).putExtras(data));
                }
            }

            @Override
            public void onPlayAudio(Chat chat, int position) {
                if (prevChat!=null){
                    adapter.notifyItemChanged(prevPos);
                }
                prevChat = chat;
                prevPos = position;
            }

            @Override
            public void onImageClick(View view, Chat chat) {
                Bundle data = new Bundle();

//                data.putString("title", "Detail");
                try {
                    data.putString("title", chat.getFrom_username());
                    String dateTime = chat.getMessage_date() + " " + chat.getMessage_time();
                    Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).parse(dateTime);
                    data.putString("subtitle", new SimpleDateFormat("dd MMMM yyyy, HH:mm", Locale.getDefault()).format(date));
                    data.putString("subtitle", chat.getMessage_date());
                    data.putString("image", chat.getMessage_attachment());

                    startActivity(new Intent(getContext(), ImageViewActivity.class).putExtras(data));
                } catch (ParseException ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            public void onReadMessage(Chat chat) {

            }
        }, true);
        rv.setAdapter(adapter);
    }

    private void setActionBar(boolean isActive){
        this.isActionBarActive = isActive;
        if(isActive){
            appbar_action.show();
        } else {
            appbar_action.hide();
        }
    }

    @Override
    public void onUnStarSuccess() {
        chatList.remove(ChatroomHelper.getSelectedOneChat(chatList));
        adapter.notifiyListChanged();
        setActionBar(false);
        if(chatList.size()==0){
            helper_nodata.setVisibility(View.VISIBLE);
        }
        onHideLoading();
        setResult(StringConstant.REFRESH_CHAT_HISTORY);
    }

    @Override
    public void onUnStarFailed(String text) {
        onHideLoading();
    }

    private void initView(){
        toolbar = findViewById(R.id.toolbar);
        rv = findViewById(R.id.rv);
        helper_nodata = findViewById(R.id.helper_nodata);
        helper_loading = findViewById(R.id.helper_loading);
        tv_nodata = findViewById(R.id.tv_nodata);
        appbar_action = findViewById(R.id.appbar_action);
        img_action_back = findViewById(R.id.img_action_back);
        img_unstar = findViewById(R.id.img_star);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.img_action_back){
            for(Chat chat:chatList){
                chat.setClicked(false);
            }
            selectedChat = null;
            adapter.notifiyListChanged();
            setActionBar(false);
        } else if(v.getId()==R.id.img_star){
            if(selectedChat!=null && chatRoomUiModel!=null) {
                onLoading();
                presenter.requestUnStarMessage(selectedChat, chatRoomUiModel.getRoom_id());
            }
        }
    }
}
