package com.gamatechno.chato.sdk.app.chatroomdetail;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gamatechno.chato.sdk.R;
import com.gamatechno.chato.sdk.app.chatroom.model.ChatRoomUiModel;
import com.gamatechno.chato.sdk.app.chatroomdetail.adapter.GroupCommonAdapter;
import com.gamatechno.chato.sdk.app.chatroomdetail.model.RoomDetailUiModel;
import com.gamatechno.chato.sdk.app.photopreview.ImageViewActivity;
import com.gamatechno.chato.sdk.app.sharedmedia.SharedMediaActivity;
import com.gamatechno.chato.sdk.app.starredmessage.StarredMessageActivity;
import com.gamatechno.chato.sdk.data.DAO.RoomChat.RoomChat;
import com.gamatechno.chato.sdk.module.activity.ChatoCoreActivity;
import com.gamatechno.chato.sdk.module.core.ChatoBaseApplication;
import com.gamatechno.chato.sdk.utils.ChatoUtils;
import com.gamatechno.chato.sdk.utils.Loading;
import com.gamatechno.ggfw.utils.GGFWUtil;
import com.gamatechno.ggfw_ui.avatarview.views.AvatarView;
import com.google.android.material.appbar.AppBarLayout;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class UserRoomDetailActivity extends ChatoCoreActivity implements UserRoomDetailView.View{

    private static final int REQUEST_STAR_MESSAGES = 200;

    protected Toolbar toolbar;
    protected ImageView avatar_header;
    protected TextView tv_posisi;
    protected TextView tv_shared_content;
    protected TextView tv_count_group;
    protected TextView tv_starred_message;
    protected LinearLayout lay_starredmessage;
    protected LinearLayout lay_sharedcontent;
    protected RecyclerView rv;
    protected AvatarView avatar;
    protected TextView tv_name;
    AppBarLayout appBarLayout;
    LinearLayout lay_name;
    RelativeLayout lay_avatar;

    ChatRoomUiModel chatRoomUiModel;
    GroupCommonAdapter adapter;
    List<RoomChat> group_in_common = new ArrayList<>();

    Loading loading;
    UserRoomDetailPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_room_detail);
        initView();


        loading = new Loading(getContext());
        adapter = new GroupCommonAdapter(group_in_common, new GroupCommonAdapter.OnActionItem() {
            @Override
            public void onClickItem(RoomChat roomChat) {

            }
        });
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setAdapter(adapter);

        presenter = new UserRoomDetailPresenter(getContext(), this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initData();
        initComponent();

        presenter.requestRoomDetail(chatRoomUiModel.getRoom_id());
        lay_sharedcontent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                startActivity(new Intent(getContext(), StarredMessageActivity.class).putExtra("room", chatRoomUiModel).putExtra("sharedmedia", true));
                startActivity(new Intent(getContext(), SharedMediaActivity.class).putExtra("room", chatRoomUiModel).putExtra("sharedMedia", true));
            }
        });

        lay_starredmessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                startActivity(new Intent(getContext(), StarredMessageActivity.class).putExtra("room", chatRoomUiModel));
                startActivityForResult(new Intent(getContext(), StarredMessageActivity.class).putExtra("room", chatRoomUiModel), REQUEST_STAR_MESSAGES);
            }
        });

        avatar_header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle data = new Bundle();
                data.putString("title", "Detail");
                data.putBoolean("isDownload", false);
                data.putString("image", (chatRoomUiModel.getAvatar().equals("") ? chatRoomUiModel.getTitle(): chatRoomUiModel.getAvatar()));

                if (!ChatoUtils.isPreLolipop()){
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(UserRoomDetailActivity.this, view, ViewCompat.getTransitionName(view));
                    startActivity(new Intent(getContext(), ImageViewActivity.class).putExtras(data), options.toBundle());
                }else{
                    startActivity(new Intent(getContext(), ImageViewActivity.class).putExtras(data));
                }
            }
        });

    }

    private void initData(){
        chatRoomUiModel = (ChatRoomUiModel) getIntent().getSerializableExtra("data");
        Log.e("Traceing data", "user_id = "+ chatRoomUiModel.getUser_id());
        Log.e("Traceing data", "room_id = "+ chatRoomUiModel.getRoom_id());
    }

    private void initComponent(){
        Picasso.get()
                .load((chatRoomUiModel.getAvatar().equals("") ? "google.com": chatRoomUiModel.getAvatar()))
//                .load(ChatoBaseApplication.getInstance().getChatoPlaceholder())
                .placeholder(ChatoBaseApplication.Companion.getInstance().getChatoPlaceholder())
                .into(avatar_header);
        getSupportActionBar().setTitle(chatRoomUiModel.getTitle());

//        PicassoLoader imageLoader = new PicassoLoader();
//        AvatarPlaceholder refreshableAvatarPlaceholder = new AvatarPlaceholder(chatRoomUiModel.getTitle());
//        imageLoader.loadImage(avatar_header, refreshableAvatarPlaceholder, (chatRoomUiModel.getAvatar().equals("") ? chatRoomUiModel.getTitle(): chatRoomUiModel.getAvatar()));
//        tv_name.setText(chatRoomUiModel.getTitle());


//        appBarLayout.addOnOffsetChangedListener(new AppBarStateChangeListener() {
//            @Override
//            public void onStateChanged(AppBarLayout appBarLayout, State state) {
//                Log.d("STATE", state.name());
//                switch (state.name()){
//                    case "EXPANDED":
//                        lay_name.setVisibility(View.GONE);
//                        lay_avatar.setVisibility(View.GONE);
//
//                        break;
//                    case "COLLAPSED":
//                        lay_name.setVisibility(View.GONE);
//                        lay_avatar.setVisibility(View.GONE);
//
//                        break;
//                }
//            }
//        });
    }

    @Override
    public void onRequestRoomDetail(RoomDetailUiModel model) {
        tv_posisi.setText(model.getPosition());
        tv_starred_message.setText(""+model.getCount_star_message());
        tv_shared_content.setText(""+model.getCount_shared());
        group_in_common.clear();
        group_in_common.addAll(model.getCommon_group());
        tv_count_group.setText(""+model.getCommon_group().size());
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onLoading() {
        loading.show();
    }

    @Override
    public void onHideLoading() {
        loading.dismiss();
    }

    @Override
    public void onErrorConnection(String message) {
        GGFWUtil.ToastShort(getContext(), message);
    }

    @Override
    public void onAuthFailed(String error) {

    }

    private void initView(){
        toolbar = findViewById(R.id.toolbar);
        avatar_header = findViewById(R.id.avatar_header);
        tv_posisi = findViewById(R.id.tv_posisi);
        tv_shared_content = findViewById(R.id.tv_shared_content);
        tv_count_group = findViewById(R.id.tv_count_group);
        tv_starred_message = findViewById(R.id.tv_starred_message);
        lay_starredmessage = findViewById(R.id.lay_starredmessage);
        lay_sharedcontent = findViewById(R.id.lay_sharedcontent);
        rv = findViewById(R.id.rv);
        avatar = findViewById(R.id.avatar);
        tv_name = findViewById(R.id.tv_name);
        appBarLayout = findViewById(R.id.appBarLayout);
        lay_name = findViewById(R.id.lay_name);
        lay_avatar = findViewById(R.id.lay_avatar);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==REQUEST_STAR_MESSAGES){
            setResult(resultCode);
            presenter.requestRoomDetail(chatRoomUiModel.getRoom_id());
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
