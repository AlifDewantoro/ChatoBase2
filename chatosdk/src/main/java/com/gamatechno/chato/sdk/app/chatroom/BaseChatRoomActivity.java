package com.gamatechno.chato.sdk.app.chatroom;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.chato.chato_emoticon.Actions.EmojIconActions;
import com.chato.chato_emoticon.Helper.EmojiconsPopup;
import com.gamatechno.chato.sdk.R;
import com.gamatechno.chato.sdk.app.chatroom.model.ChatRoomUiModel;
import com.gamatechno.chato.sdk.app.chatroom.model.MentionModel;
import com.gamatechno.chato.sdk.data.DAO.Chat.Chat;
import com.gamatechno.chato.sdk.data.DAO.Group.Group;
import com.gamatechno.chato.sdk.data.DAO.RoomChat.RoomChat;
import com.gamatechno.chato.sdk.data.constant.StringConstant;
import com.gamatechno.chato.sdk.data.model.ListentoRoomModel;
import com.gamatechno.chato.sdk.data.model.PublishToRoom;
import com.gamatechno.chato.sdk.module.activity.ChatoPermissionActivity;
import com.gamatechno.chato.sdk.module.core.ChatoBaseApplication;
import com.gamatechno.chato.sdk.module.dialogs.EmojiBottomSheet.view.recyclerview.EmojiItemView;
import com.gamatechno.chato.sdk.utils.ChatoEditText.ChatEditText;
import com.gamatechno.chato.sdk.utils.ChatoUtils;
import com.gamatechno.chato.sdk.utils.DetectHtml;
import com.gamatechno.chato.sdk.utils.Loading;
import com.gamatechno.chato.sdk.utils.animation.AnimationToggle;
import com.gamatechno.ggfw_ui.avatarview.AvatarPlaceholder;
import com.gamatechno.ggfw_ui.avatarview.loader.PicassoLoader;
import com.gamatechno.ggfw_ui.avatarview.views.AvatarView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

public class BaseChatRoomActivity extends ChatoPermissionActivity {
    //    Replied Content

    protected String[] AudioRequiredPermissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO};
    protected String[] RequiredPermissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO};
    protected String[] FileRequiredPermission = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
    protected int TYPE_ATTACHMENT = 0;
    protected final int TYPE_IMAGE = 1;
    protected final int TYPE_FILE = 2;
    protected final int VIDEO_FILE = 3;
    protected final int AUDIO_FILE = 4;
    protected final int TYPE_MESSAGE = 0;


    ImageView img_replied_message;
    AnimationToggle container_reply;
    AnimationToggle container_pinned_message;
    TextView tv_replied_title;
    TextView tv_replied_message;
    TextView tv_cancel_record;
    ImageView img_replied_close;
    RelativeLayout lay_actionbar, fab_num;
    RelativeLayout lay_searchbar;
    CardView lay_action_chat;
    AnimationToggle lay_action_mic_or_send;
    AnimationToggle lay_on_chat_or_record;
    CardView lay_action_mic;
    CardView lay_action_send;
    LinearLayout lay_onrecord;
    ImageView iv_onrecord;
    TextView tv_count_record;
    ConstraintLayout lay_onchat;
    RelativeLayout lay_no_action_chat;
    TextView tv_no_action_chat, tv_num;
    AnimationToggle lay_action_attachment;
    AnimationToggle lay_action;
    ImageView img_attachment_close;
    ImageView img_close_pinnedchat;
    ImageView img_attachment;
    RelativeLayout lay_attachment;
    ChatEditText edt_message;
    EditText edt_search;
    ImageView img_emoticon;
    ImageView img_camera;
    ImageView img_send;
    ImageView img_attach;
    TextView tv_attachment;
    RecyclerView rv;
    RecyclerView rv_mention;
    LinearLayout lay_detail_room;
    LinearLayout lay_menu_attach;
    LinearLayout lay_document;
    LinearLayout lay_gallery;
    LinearLayout lay_record_video;
    AnimationToggle lay_mention_list;
    RelativeLayout lay_menu;
    RecyclerView menu_list;
    Toolbar toolbar;
    AvatarView img_profile;
    ImageView img_back;
    LinearLayout lay_back;
    CardView card_bar;
    TextView txt_title;
    RelativeLayout lay_toolbar;
    AnimationToggle helper_loading_top;
    TextView tv_loading_top;
    AnimationToggle appbar_action;
    ImageView img_action_back;
    ImageView img_forward;
    ImageView img_reaction;
    ImageView img_info;
    ImageView img_pinmessage;
    ImageView img_copy;
    ImageView img_reply;
    ImageView img_star;
    ImageView img_delete;
    TextView tv_action_title;
    TextView tv_statusbar;
    ProgressBar pb;
    TextView tvgroup_pinned_message;
    FloatingActionButton fab_down;
    Chat chat_reply, chat_forward;
    EmojIconActions  emojIcon;
    Loading loading;
    List<ImageView> list_actionAppbar;

    String TAG = BaseChatRoomActivity.class.getName();

    Boolean is_forward = false;
    Boolean is_reply = false;

    ChatRoomUiModel chatRoomUiModel;
    List<Chat> chatList = new ArrayList<>();
    List<Chat> chatList_temp = new ArrayList<>();
    List<MentionModel> mentionList = new ArrayList<>();
    List<EmojiItemView> emojiItemViewList= new ArrayList<>();

    boolean isFinishNeedtoIn = false;
    Group grouproom;

    private PublishSubject<String> subject_edittext;

    int selectTextStart = 0;
    int selectTextEnd = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
        initView();
        loading = new Loading(getContext());
        setupEmoji();
        setList_actionAppbar();
        onEditTextWatch();

        lay_action.setOnClickListener(null);
        img_close_pinnedchat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                container_pinned_message.hide();
            }
        });
    }

    private void onEditTextWatch(){
        subject_edittext = PublishSubject.create();
        subject_edittext.debounce(200, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                // Be notified on the main thread
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String s) {
//                        Log.d(TAG, "onNextText: "+s);
                        sendBroadcast(new Intent(StringConstant.chatroom_state_publish_to_room).putExtra("data", new PublishToRoom(chatRoomUiModel.getRoom_id(), ListentoRoomModel.STATE_TYPING, ChatoUtils.getUserLogin(getContext()).getUser_name())));
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
        edt_message.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                subject_edittext.onNext(s.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public void setupReplyMessage(Chat chat) {
//        Log.d(TAG,"username: " + chat.getUser_name() + " fromUsername" + chat.getFrom_username());
        this.chat_reply = chat;
        is_reply = true;
        container_reply.show();
        switch (chat.getMessage_type()){
            case Chat.chat_type_message:
                tv_replied_message.setText(chat.getMessage_text());
                tv_replied_title.setText(chat.getFrom_username());
                img_replied_message.setVisibility(View.GONE);
                break;
            case Chat.chat_type_image:
                tv_replied_message.setText(String.format("(Foto) %s", chat.getMessage_text()));
                tv_replied_title.setText(chat.getFrom_username());
                img_replied_message.setVisibility(View.VISIBLE);
                Picasso.get()
                        .load(chat.getMessage_attachment())
                        .placeholder(R.drawable.ic_logo_space)
                        .into(img_replied_message);
                break;
            case Chat.chat_type_file:
                tv_replied_message.setText(String.format("(Berkas) %s", chat.getMessage_text()));
                tv_replied_title.setText(chat.getFrom_username());
                img_replied_message.setVisibility(View.GONE);
                if(chat.getMessage_attachment().endsWith(".doc") ||
                        chat.getMessage_attachment().endsWith(".docx"))
                    img_replied_message.setImageResource(R.drawable.ic_file_doc);
                else if(chat.getMessage_attachment().endsWith(".xls") ||
                        chat.getMessage_attachment().endsWith(".xlsx") ||
                        chat.getMessage_attachment().endsWith(".csv"))
                    img_replied_message.setImageResource(R.drawable.ic_file_excel);
                else if(chat.getMessage_attachment().endsWith(".ppt") ||
                        chat.getMessage_attachment().endsWith(".pptx"))
                    img_replied_message.setImageResource(R.drawable.ic_file_ppt);
                else if(chat.getMessage_attachment().endsWith(".pdf"))
                    img_replied_message.setImageResource(R.drawable.ic_file_pdf);
                else
                    img_replied_message.setImageResource(R.drawable.ic_file_general);
                img_replied_message.setVisibility(View.VISIBLE);
                break;
            case Chat.chat_type_video:
                tv_replied_message.setText(String.format("(Video) %s", chat.getMessage_text()));
                tv_replied_title.setText(chat.getFrom_username());
                img_replied_message.setVisibility(View.VISIBLE);
                Picasso.get()
                        .load(chat.getMessage_attachment_thumbnail())
                        .placeholder(R.drawable.ic_logo_space)
                        .into(img_replied_message);
        }
        ChatoUtils.showKeyboard(getContext(), edt_message);
    }

    public void setupReplyMessage(Boolean closed){
        if(!closed){
            container_reply.hide();
            is_reply = false;
        }
    }

    protected void initNumUnread(Boolean willShow){
        if(!willShow){
            chatList_temp.clear();
            fab_num.setVisibility(View.GONE);
        } else {
            tv_num.setText(""+chatList_temp.size());
            fab_num.setVisibility(View.VISIBLE);
        }
    }

    protected void willShowUnreadNum(Chat chat){
        if(!fab_down.isShown()){
            rv.scrollToPosition(chatList.size()-1);
        } else {
            chatList_temp.add(chat);
            initNumUnread(true);
        }
    }

    protected void willShowUnreadNum(List<Chat> chats){
        if(!fab_down.isShown()){
            rv.scrollToPosition(chatList.size()-1);
        } else {
            chatList_temp.addAll(chats);
            initNumUnread(true);
        }
    }

    protected void disableAutoOpenEmoji(EmojIconActions emojActions) {
        try {
            Field field = emojActions.getClass().getDeclaredField("popup");
            field.setAccessible(true);
            EmojiconsPopup emojiconsPopup = (EmojiconsPopup) field.get(emojActions);
            field = emojiconsPopup.getClass().getDeclaredField("pendingOpen");
            field.setAccessible(true);
            field.set(emojiconsPopup, false);
        } catch (Exception exception) {

        }
    }

    protected void onTextChangeInput(String input){
        Log.d(TAG, "onTextChangeInput: "+input);
        if(input.equals("")){
            if(is_forward){
                showSendMessage();
            } else {
                hideSendMessage();
            }
        } else {
            showSendMessage();
        }
    }


    protected void onTextChangeInput(Uri uri_attachment){
        Log.d(TAG, "onTextChangeInput: "+uri_attachment);
        if(uri_attachment != null){
            showSendMessage();
        } else {
            hideSendMessage();
        }
    }

    public void setupEmoji() {
        emojIcon = new EmojIconActions(this,getWindow().getDecorView().getRootView(), edt_message, img_emoticon,"#495C66","#DCE1E2","#E6EBEF");
        emojIcon.setIconsIds(R.drawable.ic_keyboard_black_24dp, R.drawable.ic_tag_faces_black_24dp);
        emojIcon.setUseSystemEmoji(true);
        edt_message.setUseSystemDefault(true);
        emojIcon.ShowEmojIcon();
        disableAutoOpenEmoji(emojIcon);
    }

    protected void setToolbar(ChatRoomUiModel chatRoomUiModel){
        toolbar.setOverflowIcon(getResources().getDrawable(R.drawable.ic_more_vert));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        card_bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*if(isFinishNeedtoIn){
                    startActivity(new Intent(getContext(), ChatoActivity.class));
                }*/
                finish();
                ChatoBaseApplication.Companion.getInstance().cancelPendingChatoRequest();
            }
        });

        PicassoLoader imageLoader = new PicassoLoader();
        AvatarPlaceholder refreshableAvatarPlaceholder = new AvatarPlaceholder(chatRoomUiModel.getTitle(), Color.parseColor("#42B6B2"), Color.parseColor("#F9F9FC"));
        imageLoader.loadImage(img_profile, refreshableAvatarPlaceholder, (chatRoomUiModel.getAvatar().equals("") ? "google.com" : chatRoomUiModel.getAvatar()));

        txt_title.setText(chatRoomUiModel.getTitle());
        appbar_action.setInOutAnimation(R.anim.pull_in_bottom, R.anim.push_out_top);
        container_pinned_message.setInOutAnimation(R.anim.pull_in_bottom, R.anim.push_out_top);
    }

    protected void startCropActivity(Uri uri){
        CropImage.activity(uri)
                .setCropShape(CropImageView.CropShape.RECTANGLE)
                .start(this);
    }

    protected void hideSendMessage(){
        //lay_action_mic_or_send.hide(lay_action_send);
        lay_action_mic_or_send.display(lay_action_mic);
        if (lay_action.getVisibility()==View.GONE) {
            lay_action.show();
        }
    }

    protected void showSendMessage(){
        lay_action_mic_or_send.display(lay_action_send);
        //lay_action_mic_or_send.hide(lay_action_mic);
        if (lay_action.getVisibility()==View.VISIBLE) {
            lay_action.hide();
        }
    }

    protected void hideMentionLayout(){
        if (lay_mention_list.getVisibility()==View.VISIBLE) {
            lay_mention_list.hide();
        }
    }

    protected void showMentionLayout(){
        if (lay_mention_list.getVisibility()==View.GONE) {
            lay_mention_list.show();
        }
    }

    protected void updateActionAppBar(boolean isMultiple, boolean isMine) {
        showAllActionAppbar();
        if(isMultiple){
            appbar_action.hide(img_reply);
            appbar_action.hide(img_forward);
            appbar_action.hide(img_star);
            appbar_action.hide(img_info);
            appbar_action.hide(img_pinmessage);
            appbar_action.hide(img_reaction);
        } else {
            appbar_action.hide(img_forward);
            if(!isMine){
                appbar_action.hide(img_info);
            }

            if(!isRoomAGroup(chatRoomUiModel)){
                appbar_action.hide(img_pinmessage);
                appbar_action.hide(img_reaction);
            } else {
                if(grouproom.getRoom_group_type().equals("BROADCAST")){
                    appbar_action.hide(img_pinmessage);
                    appbar_action.hide(img_reply);
                }
                if(grouproom.getPinned_message().getMessage_id() == ChatroomHelper.getSelectedOneChat(chatList).getMessage_id()){
                    img_pinmessage.setImageResource(R.drawable.ic_unpin_chat);
                } else {
                    img_pinmessage.setImageResource(R.drawable.ic_pin_chat);
                }
            }
        }
    }

    protected void setList_actionAppbar(){
        list_actionAppbar = new ArrayList<>();
//        list_actionAppbar.add(img_forward);
        list_actionAppbar.add(img_reaction);
        list_actionAppbar.add(img_copy);
        list_actionAppbar.add(img_pinmessage);
        list_actionAppbar.add(img_reply);
        list_actionAppbar.add(img_star);
        list_actionAppbar.add(img_delete);
        list_actionAppbar.add(img_info);
    }

    protected void showAllActionAppbar(){
        for (int i = 0; i < list_actionAppbar.size(); i++) {
            appbar_action.displaying(list_actionAppbar.get(i));
        }
    }

    protected void setStatusBarRoom(String status){
        Log.d(TAG, "setStatusBarRoom: "+status);
        if(status.equals("")){
            tv_statusbar.setVisibility(View.GONE);
        } else {
            tv_statusbar.setVisibility(View.VISIBLE);
            tv_statusbar.setText(status);
        }
    }

    protected boolean isRoomAGroup(ChatRoomUiModel chatRoomUiModel){
        return (chatRoomUiModel.getType().equals(RoomChat.user_room_type) ? false : true);
    }

    protected void initPinnedMessageGroup(Group grouproom){
        if(grouproom.getIs_pinned_message() == 1){
            if(!container_pinned_message.isDisplaying(container_pinned_message)){
                container_pinned_message.show();
            }

            String type = "";
            if(grouproom.getPinned_message().getMessage_type().equals(Chat.chat_type_file))
                type = "(Berkas) ";
            else if (grouproom.getPinned_message().getMessage_type().equals(Chat.chat_type_image))
                type = "(Foto) ";
            else if (grouproom.getPinned_message().getMessage_type().equals(Chat.chat_type_video))
                type = "(Video) ";
            else if (grouproom.getPinned_message().getMessage_type().equals(Chat.chat_type_audio))
                type = "(Voice) ";
            tvgroup_pinned_message.setText(DetectHtml.convertHtmlToPlain(String.format("%s%s", type, grouproom.getPinned_message().getMessage_text())));
        } else {
            if(container_pinned_message.isDisplaying(container_pinned_message)){
                container_pinned_message.hide();
            }
        }
    }

    private void initView(){
        toolbar = findViewById(R.id.toolbar);
        img_replied_message = findViewById(R.id.img_replied_message);
        container_reply = findViewById(R.id.container_reply);
        container_pinned_message = findViewById(R.id.container_pinned_message);
        tv_replied_message = findViewById(R.id.tv_replied_message);
        tv_replied_title = findViewById(R.id.tv_replied_title);
        img_replied_close = findViewById(R.id.img_replied_close);
        tv_cancel_record = findViewById(R.id.tv_cancel_record);
        lay_actionbar = findViewById(R.id.lay_actionbar);
        lay_searchbar = findViewById(R.id.lay_searchbar);
        lay_action_chat = findViewById(R.id.lay_action_chat);
        lay_action_mic_or_send = findViewById(R.id.lay_action_mic_or_send);
        lay_on_chat_or_record = findViewById(R.id.lay_on_chat_or_record);
        lay_action_mic = findViewById(R.id.lay_action_mic);
        lay_action_send = findViewById(R.id.lay_action_send);
        lay_onrecord = findViewById(R.id.lay_onrecord);
        iv_onrecord = findViewById(R.id.iv_onrecord);
        tv_count_record = findViewById(R.id.tv_count_record);
        lay_onchat = findViewById(R.id.lay_onchat);
        lay_no_action_chat = findViewById(R.id.lay_no_action_chat);
        lay_action_attachment = findViewById(R.id.lay_action_attachment);
        lay_action = findViewById(R.id.lay_action);
        img_attachment_close = findViewById(R.id.img_attachment_close);
        img_attach = findViewById(R.id.img_attach);
        img_close_pinnedchat = findViewById(R.id.img_close_pinnedchat);
        img_attachment = findViewById(R.id.img_attachment);
        lay_attachment = findViewById(R.id.lay_attachment);
        edt_message = findViewById(R.id.edt_message);
        edt_search = findViewById(R.id.edt_search);
        img_emoticon = findViewById(R.id.img_emoticon);
        img_camera = findViewById(R.id.img_camera);
        img_send = findViewById(R.id.img_send);
        img_attachment = findViewById(R.id.img_attachment);
        tv_attachment = findViewById(R.id.tv_attachment);
        rv = findViewById(R.id.rv);
        rv_mention = findViewById(R.id.rv_mention);
        lay_detail_room = findViewById(R.id.lay_detail_room);
        lay_menu_attach = findViewById(R.id.lay_menu_attach);
        lay_mention_list = findViewById(R.id.lay_mention_list);
        img_profile = findViewById(R.id.img_profile);
        img_back = findViewById(R.id.img_back);
        lay_back = findViewById(R.id.lay_back);
        card_bar = findViewById(R.id.card_bar);
        txt_title = findViewById(R.id.txt_title);
        lay_toolbar = findViewById(R.id.lay_toolbar);
        helper_loading_top = findViewById(R.id.helper_loading_top);
        tv_loading_top = findViewById(R.id.tv_loading_top);
        appbar_action = findViewById(R.id.appbar_action);
        img_action_back = findViewById(R.id.img_action_back);
        img_forward = findViewById(R.id.img_forward);
        img_reaction = findViewById(R.id.img_reaction);
        img_info = findViewById(R.id.img_info);
        img_pinmessage = findViewById(R.id.img_pinmessage);
        img_copy = findViewById(R.id.img_copy);
        img_reply = findViewById(R.id.img_reply);
        img_star = findViewById(R.id.img_star);
        img_delete = findViewById(R.id.img_delete);
        tv_action_title = findViewById(R.id.tv_action_title);
        tv_statusbar = findViewById(R.id.tv_statusbar);
        pb = findViewById(R.id.pb);
        tvgroup_pinned_message = findViewById(R.id.tvgroup_pinned_message);
        fab_down = findViewById(R.id.fab_down);
        lay_document = findViewById(R.id.lay_document);
        lay_gallery = findViewById(R.id.lay_gallery);
        lay_record_video = findViewById(R.id.lay_record_video);
        tv_no_action_chat = findViewById(R.id.tv_no_action_chat);
        lay_menu = findViewById(R.id.ll_menu);
        menu_list = findViewById(R.id.menu_list);
        tv_num = findViewById(R.id.tv_num);
        fab_num = findViewById(R.id.fab_num);
    }
}
