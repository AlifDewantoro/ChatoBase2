package com.gamatechno.chato.sdk.app.chatroom;

import android.Manifest;
import android.app.ActivityOptions;
import android.app.Dialog;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gamatechno.chato.sdk.R;
import com.gamatechno.chato.sdk.app.broadcastdirect.kontakbroadcast.ContactBroadcastActivity;
import com.gamatechno.chato.sdk.app.chatinfo.DialogChatInfo;
import com.gamatechno.chato.sdk.app.chatinfo.groupchatinfo.GroupChatInfoDialog;
import com.gamatechno.chato.sdk.app.chatroom.adapter.ChatRoomAdapter;
import com.gamatechno.chato.sdk.app.chatroom.adapter.MentionAdapter;
import com.gamatechno.chato.sdk.app.chatroom.helper.ChatRoomHelper;
import com.gamatechno.chato.sdk.app.chatroom.model.ChatListModel;
import com.gamatechno.chato.sdk.app.chatroom.model.ChatReactionModel;
import com.gamatechno.chato.sdk.app.chatroom.model.ChatReactionNotifModel;
import com.gamatechno.chato.sdk.app.chatroom.model.ChatReactionResponse;
import com.gamatechno.chato.sdk.app.chatroom.model.ChatRoomUiModel;
import com.gamatechno.chato.sdk.app.chatroom.model.FileModel;
import com.gamatechno.chato.sdk.app.chatroom.model.MentionModel;
import com.gamatechno.chato.sdk.app.chatroomdetail.UserRoomDetailNewActivity;
import com.gamatechno.chato.sdk.app.chatrooms.uimodel.ChatRoomsUiModel;
import com.gamatechno.chato.sdk.app.grouproomdetail.GroupInfoNewActivity;
import com.gamatechno.chato.sdk.app.grouproomdetail.GroupInfoPresenter;
import com.gamatechno.chato.sdk.app.grouproomdetail.GroupInfoView;
import com.gamatechno.chato.sdk.app.kontakchat.KontakChatDialog;
import com.gamatechno.chato.sdk.app.kontakchat.KontakModel;
import com.gamatechno.chato.sdk.app.photopreview.ImageViewActivity;
import com.gamatechno.chato.sdk.app.pinnedgroupmessage.PinnedGroupMessageDialog;
import com.gamatechno.chato.sdk.app.playvideo.PlayVideoActivity;
import com.gamatechno.chato.sdk.app.playvideo.sendvideopreview.VideoPreviewActivity;
import com.gamatechno.chato.sdk.app.starredmessage.StarredMessageActivity;
import com.gamatechno.chato.sdk.data.DAO.Chat.Chat;
import com.gamatechno.chato.sdk.data.DAO.Chat.NotifChat;
import com.gamatechno.chato.sdk.data.DAO.Chat.dbaccess.NotifChatDatabase;
import com.gamatechno.chato.sdk.data.DAO.Group.Group;
import com.gamatechno.chato.sdk.data.DAO.RoomChat.RoomChat;
import com.gamatechno.chato.sdk.data.constant.Preferences;
import com.gamatechno.chato.sdk.data.constant.StringConstant;
import com.gamatechno.chato.sdk.data.model.ListentoRoomModel;
import com.gamatechno.chato.sdk.data.model.PublishToRoom;
import com.gamatechno.chato.sdk.module.dialogs.EmojiBottomSheet.emoji.EmojInitListener;
import com.gamatechno.chato.sdk.module.dialogs.EmojiBottomSheet.emoji.EmojiCategoryTransformer;
import com.gamatechno.chato.sdk.module.dialogs.EmojiBottomSheet.emoji.EmojiCompatUtils;
import com.gamatechno.chato.sdk.module.dialogs.EmojiBottomSheet.emoji.categories.ActivitiesCategory;
import com.gamatechno.chato.sdk.module.dialogs.EmojiBottomSheet.emoji.categories.AnimalsNatureCategory;
import com.gamatechno.chato.sdk.module.dialogs.EmojiBottomSheet.emoji.categories.Category;
import com.gamatechno.chato.sdk.module.dialogs.EmojiBottomSheet.emoji.categories.FlagsCategory;
import com.gamatechno.chato.sdk.module.dialogs.EmojiBottomSheet.emoji.categories.FoodDrinkCategory;
import com.gamatechno.chato.sdk.module.dialogs.EmojiBottomSheet.emoji.categories.ObjectsCategory;
import com.gamatechno.chato.sdk.module.dialogs.EmojiBottomSheet.emoji.categories.SmileysPeopleCategory;
import com.gamatechno.chato.sdk.module.dialogs.EmojiBottomSheet.emoji.categories.SymbolsCategory;
import com.gamatechno.chato.sdk.module.dialogs.EmojiBottomSheet.emoji.categories.TravelPlacesCategory;
import com.gamatechno.chato.sdk.module.dialogs.EmojiBottomSheet.emoji.categoryUnicodes.ActivityCategoryUnicodes;
import com.gamatechno.chato.sdk.module.dialogs.EmojiBottomSheet.emoji.categoryUnicodes.AnimalsNatureCategoryUnicodes;
import com.gamatechno.chato.sdk.module.dialogs.EmojiBottomSheet.emoji.categoryUnicodes.FlagsCategoryUnicodes;
import com.gamatechno.chato.sdk.module.dialogs.EmojiBottomSheet.emoji.categoryUnicodes.FoodDrinkCategoryUnicodes;
import com.gamatechno.chato.sdk.module.dialogs.EmojiBottomSheet.emoji.categoryUnicodes.ObjectsCategoryUnicodes;
import com.gamatechno.chato.sdk.module.dialogs.EmojiBottomSheet.emoji.categoryUnicodes.SmileysPeopleCategoryUnicodes;
import com.gamatechno.chato.sdk.module.dialogs.EmojiBottomSheet.emoji.categoryUnicodes.SymbolsCategoryUnicodes;
import com.gamatechno.chato.sdk.module.dialogs.EmojiBottomSheet.emoji.categoryUnicodes.TravelPlacesCategoryUnicodes;
import com.gamatechno.chato.sdk.module.dialogs.EmojiBottomSheet.view.EmojiPickerDialog;
import com.gamatechno.chato.sdk.utils.AudioUploader;
import com.gamatechno.chato.sdk.utils.ChatUtils.EndlessRecyclerViewScrollListener;
import com.gamatechno.chato.sdk.utils.ChatUtils.SpeedyLinearLayoutManager;
import com.gamatechno.chato.sdk.utils.ChatoUtils;
import com.gamatechno.chato.sdk.utils.DateFormatUtilChat;
import com.gamatechno.chato.sdk.utils.DeleteMessageDialog;
import com.gamatechno.chato.sdk.utils.FilePath.FilePath;
import com.gamatechno.chato.sdk.utils.FilePath.IOUtils;
import com.gamatechno.chato.sdk.utils.MediaPlayerSingleton;
import com.gamatechno.ggfw.Activity.Interfaces.PermissionResultInterface;
import com.gamatechno.ggfw.easyphotopicker.Constants;
import com.gamatechno.ggfw.easyphotopicker.DefaultCallback;
import com.gamatechno.ggfw.easyphotopicker.EasyImage;
import com.gamatechno.ggfw.utils.AlertDialogBuilder;
import com.gamatechno.ggfw.utils.GGFWUtil;
import com.gamatechno.ggfw_ui.avatarview.AvatarPlaceholder;
import com.gamatechno.ggfw_ui.avatarview.loader.PicassoLoader;
import com.github.squti.androidwaverecorder.WaveRecorder;
import com.google.gson.Gson;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;
import java.util.Locale;

import static com.gamatechno.chato.sdk.data.constant.StringConstant.broadcast_listen_to_room;

public class ChatRoomActivity extends BaseChatRoomActivity implements ChatRoomView.View, GroupInfoView.View, View.OnClickListener {

    Uri uri_attachment = null;
    String name_attachment = "";
    FileModel fileModel = null;

    private final int REQUEST_CODE_ATTACHMENT = 212;
    private static final int REQUEST_ROOM_INFO = 100;
    private static final int REQUEST_STAR_MESSAGES = 101;
    private static final int PREPARE_SEND_VIDEO = 300;

    final String TAG = "ChatRoomActivity";
    ChatRoomAdapter adapter;
    MentionAdapter adapterMention;
    ChatRoomPresenter presenter;
    GroupInfoPresenter presenterGroupInfo;

    Bitmap bitmap_image;
    InputStream stream_attachment;

    SpeedyLinearLayoutManager layoutManager;
    EndlessRecyclerViewScrollListener endlessRecyclerViewScrollListener;

    String file_attachment = "";
    Bitmap thumb_file_attachment = null;
    String duration_file_attachment = "";
    IntentFilter filter;
    String lastSelectedChatType = "";

    int selectTextStart = 0;
    int selectTextEnd = 0;

    KontakChatDialog kontakChatDialog;

    ChatRoomViewModel viewModel;

    NotifChatDatabase chatNotifDatabase;

    boolean isJoin = true;
    Thread timer1,timer2;
    boolean isAppBarShow = false;
    protected String[] RequiredPermissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            Log.d(TAG, "Receiving....");
            if (action.equals(StringConstant.broadcast_receive_status_chat)) {
                ChatListModel chatListModel = (ChatListModel) intent.getSerializableExtra("data");
                checkListMessageStatus(chatListModel.getChatList());
                adapter.notifiyListChanged();
            } else if(action.equals(StringConstant.broadcast_receive_chat)){
                Chat chat = (Chat) intent.getSerializableExtra("data");

                Log.e(TAG, "isi chat id new = "+ chat.getMessage_id());
                Log.e(TAG, "isi chat id last chat = "+ chatList.get(chatList.size()-1).getMessage_id());
                sendStatusReadMessage(chat);
                /*
                if (String.valueOf(chatList.get(chatList.size()-1).getMessage_id()).equals(chat.getMessage_id())) {

                }

                 */
                chatList.add(chat);
                adapter.notifyDataSetChanged();
                willShowUnreadNum(chat);
                setStatusOnList(StringConstant.chat_status_read);
            } else if(action.equals(StringConstant.broadcast_listen_to_room)){
                if(chatRoomUiModel.getType().equals(RoomChat.official_room_type)){
                    setStatusBarRoom("Official Group");
                } else {
                    ListentoRoomModel listentoRoomModel = (ListentoRoomModel) intent.getSerializableExtra("data");
                    Log.d("ChatRoomActivity", "ActGetType: " + listentoRoomModel.getType());
                    switch (listentoRoomModel.getType()) {
                        case ListentoRoomModel.STATE_LAST_SEEN:
                            if (grouproom != null) {
                                if (grouproom.getRoom_group_type().equals("BROADCAST")) {
                                    setStatusBarRoom("Broadcast Room");
                                } else {
                                    setStatusBarRoom("");
                                }
                            } else {
                                setStatusBarRoom(convertTime(listentoRoomModel.getDate_time()));
//                            setStatusBarRoom("");
                            }
                            break;
                        case ListentoRoomModel.STATE_OFFLINE:
//                        setStatusBarRoom("Terakhir dilihat : "+listentoRoomModel.getDate_time());
                            if (grouproom != null) {
                                if (grouproom.getRoom_group_type().equals("BROADCAST")) {
                                    setStatusBarRoom("Broadcast Room");
                                } else {
                                    setStatusBarRoom("");
                                }
                            } else {
                                setStatusBarRoom(convertTime(listentoRoomModel.getDate_time()));
//                            setStatusBarRoom("");
//                            TimeAgo.using(timeInMillis, messages);
                            }
                        /*if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                            Locale LocaleBylanguageTag = Locale.forLanguageTag("ID");
                            long timeInMillis = System.currentTimeMillis();
                            TimeAgoMessages messages = new TimeAgoMessages.Builder().withLocale(LocaleBylanguageTag).build();
                            String text = TimeAgo.using(timeInMillis, messages);
                            setStatusBarRoom("Terakhir dilihat : "+text);
                        } else {
                            setStatusBarRoom("Terakhir dilihat : "+listentoRoomModel.getDate_time());
                        }*/
                            break;
                        case ListentoRoomModel.STATE_ONLINE:
                            if (grouproom != null) {
                                if (grouproom.getRoom_group_type().equals("BROADCAST")) {
                                    setStatusBarRoom("Broadcast Room");
                                } else {
                                    setStatusBarRoom("");
                                }
                            } else {
                                setStatusBarRoom("Online");
                            }
                            break;
                        case ListentoRoomModel.STATE_TYPING:
                            Log.d(TAG, "onReceive: chatRoomId:" + chatRoomUiModel.getRoom_id() +
                                    " listenRoom: " + listentoRoomModel.getRoom_id());
                            if (chatRoomUiModel.getRoom_id().equals("" + listentoRoomModel.getRoom_id())) {
                                if (isRoomAGroup(chatRoomUiModel)) {
                                    setStatusBarRoom(ChatroomHelper.namaPanggilan(listentoRoomModel.getUsername()) + " sedang menulis...");
                                } else {
                                    setStatusBarRoom("Sedang menulis...");
                                }
                            }
                            break;
                        case ListentoRoomModel.STATE_DELETE:
                            deleteMessage(listentoRoomModel);
                            break;
                    }
                }
            } else if(action.equals(StringConstant.broadcast_get_update_group_info)){
                Log.d("ChatRoomActivity","getUpdateRoom: received broadcast success");
                if(presenter!=null){
                    presenter.getUpdatedGroupInfo(chatRoomUiModel);
                }
            } else if (action.equals(StringConstant.broadcast_receive_chat_reaction)){
                Log.d("ChatRoomActivity","getUpdateRoom: received broadcast reaction success");
                ChatReactionNotifModel data = (ChatReactionNotifModel) intent.getSerializableExtra("data");
                Log.d("ChatRoomActivity", "data: " + data.getReaction_data());

                if (ChatoUtils.getUserLogin(getContext()).getUser_id() != Integer.parseInt(data.getUser_id())) {
                    for (int x = 0; x < chatList.size(); x++) {
                        if (chatList.get(x).getMessage_id() == Integer.parseInt(data.getMessage_id())) {
                            List<ChatReactionModel> chat = new ArrayList<>();
                            if (chatList.get(x).getReactionList()!=null) {
                                Log.d("check size reaction", " " + chatList.get(x).getReactionList().size());
                                chat = chatList.get(x).getReactionList();
                            }

                            ChatReactionModel newModel = new Gson().fromJson(data.getReaction(), ChatReactionModel.class);

                            chat.add(newModel);
                            chatList.get(x).setReactionList(chat);

                            adapter.notifyDataSetChanged();
                        }
                    }
                }
            }
        }
    };

    private String convertTime(String dateTime){
        try {
            if(dateTime!=null && !dateTime.equals("")) {
                long curTime = System.currentTimeMillis();
                Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse(dateTime);
                Log.d("TimeCovert", "dateTime: " + dateTime + " lastTime:" + date.toString());
                long lastTime = date.getTime();
                long diffTime = curTime - lastTime;

                if (diffTime < 60000)
                    return "Terakhir dilihat " + diffTime / 1000 + " detik";
                else if (diffTime < 3600000)
                    return "Terakhir dilihat " + diffTime / 60000 + " menit";
                else if (diffTime < 24 * 3600000)
                    return "Terakhir dilihat " + diffTime / 3600000 + " jam";
                else
                    return "Terakhir dilihat pada " + new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(date);
            } else {
                return "";
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        initData();
        setToolbar(chatRoomUiModel);
        setupRecyclerView();
        registerReceiver();
        setupViewModel();
        initializeEmojis();

        chatNotifDatabase = new NotifChatDatabase(getContext());

        lay_action_attachment.setInOutAnimation(R.anim.pull_in_top, R.anim.push_out_bottom);
        container_reply.setInOutAnimation(R.anim.pull_in_top, R.anim.push_out_bottom);
        lay_action.display(lay_attachment);
        presenter = new ChatRoomPresenter(getContext(), this);
        presenterGroupInfo = new GroupInfoPresenter(getContext(), this);
        helper_loading_top.setInOutAnimation(R.anim.pull_in_bottom, R.anim.push_out_top);

        edt_message.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(lay_menu_attach.getVisibility()==View.VISIBLE){
                    lay_menu_attach.setVisibility(View.GONE);
//                    edt_message.requestFocus();
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                onTextChangeInput(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        edt_message.setAccessibilityDelegate(new View.AccessibilityDelegate(){
            @Override
            public void sendAccessibilityEvent(View host, int eventType) {
                super.sendAccessibilityEvent(host, eventType);
                if (eventType == AccessibilityEvent.TYPE_VIEW_TEXT_SELECTION_CHANGED &&
                        isRoomAGroup(chatRoomUiModel)){
                    int posCursor = edt_message.getSelectionStart();
                    Log.e(TAG, "cursor position = "+ posCursor);
                    if (posCursor-1 >= 0){
                        String prevChar = edt_message.getText().subSequence(posCursor-1, posCursor).toString();
                        if (prevChar.equals("@")){
                            selectTextStart = posCursor;
                            showMentionLayout();
                            selectTextEnd = edt_message.getSelectionStart();
                            searchUserToMention(selectTextStart, selectTextEnd, edt_message.getText().toString());
                        }else {
                            if (selectTextStart==0 || prevChar.equals(" ")) {
                                hideMentionLayout();
                                resetCursor();
                            }else if (selectTextStart>0){
                                showMentionLayout();
                                selectTextEnd = edt_message.getSelectionStart();
                                searchUserToMention(selectTextStart, selectTextEnd, edt_message.getText().toString());
                            }
                        }
                    }else{
                        hideMentionLayout();
                        resetCursor();
                    }
                }
            }
        });

        edt_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    ChatoUtils.hideSoftKeyboard(getContext(), edt_search);
                    presenter.searchChat(edt_search.getText().toString(), chatRoomUiModel.getRoom_id());
                    return true;
                }
                return false;
            }
        });
        requestHistory(chatRoomUiModel.getUser_id(), ""+ ChatoUtils.getUserLogin(getContext()).getUser_id(), true);
        requestGroupDetail(chatRoomUiModel.getRoom_id(), false);
        GGFWUtil.setStringToSP(getContext(), Preferences.OPENED_CHATROOM_ID, chatRoomUiModel.getRoom_id());

        img_attach.setOnClickListener(this);
        lay_document.setOnClickListener(this);
        lay_gallery.setOnClickListener(this);
        lay_record_video.setOnClickListener(this);
        img_camera.setOnClickListener(this);
        img_attachment_close.setOnClickListener(this);
        img_replied_close.setOnClickListener(this);
        img_send.setOnClickListener(this);
        lay_action_send.setOnClickListener(this);
        lay_action_mic.setOnClickListener(this);
        tv_cancel_record.setOnClickListener(this);
        img_action_back.setOnClickListener(this);
        container_pinned_message.setOnClickListener(this);
        img_reaction.setOnClickListener(this);
        img_copy.setOnClickListener(this);
        img_forward.setOnClickListener(this);
        img_reply.setOnClickListener(this);
        img_delete.setOnClickListener(this);
        img_star.setOnClickListener(this);
        img_info.setOnClickListener(this);
        img_pinmessage.setOnClickListener(this);
        lay_detail_room.setOnClickListener(this);
        edt_message.setOnClickListener(this);
        lay_menu.setOnClickListener(this);

        if(chatRoomUiModel.getType().equals(RoomChat.official_room_type)){
            lay_detail_room.setClickable(false);
        } else {
            lay_detail_room.setClickable(true);
        }
    }

    private void resetCursor(){
        selectTextStart = 0;
        selectTextEnd = 0;
    }

    public void searchUserToMention(int posStart,int posEnd, String fulltext){
        String selectedText = fulltext.substring(posStart, posEnd);
        Log.e(TAG, "onSearchMention "+ selectedText);
        adapterMention.getFilter().filter(selectedText);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if(intent.hasExtra(StringConstant.notification_message)){
            RoomChat roomChat = (RoomChat) intent.getSerializableExtra(StringConstant.notification_message);
            Chat chat = roomChat.getDetail_last_message();
            chatRoomUiModel = new ChatRoomUiModel(""+roomChat.getRoom_id(), roomChat.getRoom_name(), roomChat.getRoom_photo(), ""+chat.getRoom_id());
            chatRoomUiModel.setType(roomChat.getRoom_type());
            chatRoomUiModel.setRoom_code(roomChat.getRoom_code());
            requestHistory(chatRoomUiModel.getUser_id(), ""+ ChatoUtils.getUserLogin(getContext()).getUser_id(), true);
            setToolbar(chatRoomUiModel);
            GGFWUtil.setStringToSP(getContext(), Preferences.OPENED_CHATROOM_ID, chatRoomUiModel.getRoom_id());
        }
    }

    private void initializeEmojis(){
        EmojiCompatUtils.INSTANCE.initialize(getApplicationContext(), new EmojInitListener() {
            @Override
            public void onEmojisInitialized() {
                emojisInitializedActions();
            }

            @Override
            public void onEmojisInitializedError() {

            }
        });
    }

    private void emojisInitializedActions(){
        initializeEmojiCategoriesPreferred();
    }

    private void initializeEmojiCategoriesPreferred(){
        emojiItemViewList = new EmojiCategoryTransformer().transform(initializeEmojiCategoryList());
    }

    private List<Category> initializeEmojiCategoryList(){
        List<Category> list = new ArrayList<Category>();

        list.add(new SmileysPeopleCategory(getResources().getString(R.string.smileysAndPeopleTitle), new ArrayList<>(EnumSet.allOf(SmileysPeopleCategoryUnicodes.class))));
        list.add(new ActivitiesCategory(getResources().getString(R.string.activitiesCategoryTitle), new ArrayList<>(EnumSet.allOf(ActivityCategoryUnicodes.class))));
        list.add(new AnimalsNatureCategory(getResources().getString(R.string.animalsAndNatureTitle), new ArrayList<>(EnumSet.allOf(AnimalsNatureCategoryUnicodes.class))));
        list.add(new FoodDrinkCategory(getResources().getString(R.string.foodAndDrinkTitle), new ArrayList<>(EnumSet.allOf(FoodDrinkCategoryUnicodes.class))));
        list.add(new ObjectsCategory(getResources().getString(R.string.objectsTitle), new ArrayList<>(EnumSet.allOf(ObjectsCategoryUnicodes.class))));
        list.add(new SymbolsCategory(getResources().getString(R.string.symbolsTitle), new ArrayList<>(EnumSet.allOf(SymbolsCategoryUnicodes.class))));
        list.add(new TravelPlacesCategory(getResources().getString(R.string.travelAndPlacesTitle), new ArrayList<>(EnumSet.allOf(TravelPlacesCategoryUnicodes.class))));
        list.add(new FlagsCategory(getResources().getString(R.string.flagsTitle), new ArrayList<>(EnumSet.allOf(FlagsCategoryUnicodes.class))));
        return list;
    }

    private void showEmojiDialog(){
        Log.d("emojiItemViewList Size", " = "+emojiItemViewList.size());
        EmojiPickerDialog.Builder dialogBuilder = new EmojiPickerDialog.Builder(
                this,
                emojiItemViewList,
                getResources().getString(R.string.emojiDialogTitle),
                true,
                true,
                unicode -> {
                    presenter.reactionChat(ChatroomHelper.getSelectedOneChat(chatList), chatRoomUiModel.getRoom_id(), unicode.getValue());
                });
        dialogBuilder.build().show();
    }

    private void initData(){
        if(getIntent().hasExtra("data")){
            KontakModel kontakModel = (KontakModel) getIntent().getSerializableExtra("data");
            Log.d(TAG, "initData: name:"+ kontakModel.getUser_name() + " getnick" + kontakModel.getNick_name() + " getRoomType" + kontakModel.getRoom_type() + " getRoomphoto" + kontakModel.getUser_photo());
            if (kontakModel.getRoom_type().equals(RoomChat.user_room_type)){
                chatRoomUiModel = new ChatRoomUiModel(""+kontakModel.getUser_id(), (kontakModel.getNick_name() !=null && !kontakModel.getNick_name().isEmpty()) ? kontakModel.getNick_name() : kontakModel.getUser_name(), kontakModel.getUser_photo(), ""+kontakModel.getRoom_id(), kontakModel.getRoom_type());
            }else{
                chatRoomUiModel = new ChatRoomUiModel(""+kontakModel.getUser_id(), kontakModel.getGroup_name(), kontakModel.getUser_photo(), ""+kontakModel.getRoom_id(), kontakModel.getRoom_type());
            }
            chatRoomUiModel.setRoom_code(kontakModel.getRoom_code());
            Log.d(TAG, "initData: title:"+ chatRoomUiModel.getTitle() + " avatar" + chatRoomUiModel.getAvatar());

        } else if(getIntent().hasExtra("chatroom")){
            ChatRoomsUiModel chatrooms = (ChatRoomsUiModel) getIntent().getSerializableExtra("chatroom");
            if(chatrooms.getRoomChat().getRoom_type().equals(RoomChat.group_room_type)||chatrooms.getRoomChat().getRoom_type().equals(RoomChat.official_room_type)){
                chatRoomUiModel = new ChatRoomUiModel(""+chatrooms.getRoomChat().getRoom_id(), chatrooms.getRoomChat().getGroup_name(), chatrooms.getRoomChat().getRoom_photo() , ""+chatrooms.getRoomChat().getRoom_id(), ""+chatrooms.getRoomChat().getRoom_type(), chatrooms.getRoomChat().getIs_admin());
            } else {
                chatRoomUiModel = new ChatRoomUiModel(""+chatrooms.getRoomChat().getUser_id(), (chatrooms.getRoomChat().getRoom_nick_name() != null && !chatrooms.getRoomChat().getRoom_nick_name().isEmpty()) ? chatrooms.getRoomChat().getRoom_nick_name() : chatrooms.getRoomChat().getRoom_name(), chatrooms.getRoomChat().getUser_photo() , ""+chatrooms.getRoomChat().getRoom_id());
            }
            chatRoomUiModel.setRoom_code(chatrooms.getRoomChat().getRoom_code());

            if(getIntent().hasExtra("newgroup")){
                ChatoUtils.showKeyboard(getContext(), edt_message);
            }

            if(getIntent().hasExtra("chatBirthday")) {
                edt_message.setText(getIntent().getStringExtra("chatBirthday"));
                /*TYPE_ATTACHMENT = TYPE_MESSAGE;
                lay_menu_attach.setVisibility(View.GONE);
                img_send.setVisibility(View.VISIBLE);*/
                edt_message.requestFocus();
            }
        } else if(getIntent().hasExtra(StringConstant.notification_message)){
            isFinishNeedtoIn = true;
            RoomChat roomChat = (RoomChat) getIntent().getSerializableExtra(StringConstant.notification_message);
            Chat chat = roomChat.getDetail_last_message();
            chatRoomUiModel = new ChatRoomUiModel(""+roomChat.getRoom_id(), roomChat.getRoom_name(), roomChat.getRoom_photo(), ""+chat.getRoom_id());
            chatRoomUiModel.setType(roomChat.getRoom_type());
            chatRoomUiModel.setRoom_code(roomChat.getRoom_code());
            Log.d(TAG, "onCreate: notification_message"+chat.getFrom_username());
        } else if(getIntent().hasExtra("forward")){
            chatRoomUiModel = (ChatRoomUiModel) getIntent().getSerializableExtra("forward");
            chat_forward = (Chat) getIntent().getSerializableExtra("forward_data");
            chat_forward.setRoom_id(chatRoomUiModel.getRoom_id());
            chat_forward.setTo_user_id(Integer.parseInt(chatRoomUiModel.getUser_id()));
            switch (chat_forward.getMessage_type()){
                case Chat.chat_type_image:
                    try {
                        chat_forward.setMessage_attachment(GGFWUtil.convertToBase64(MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), chat_forward.convertUri_attachment())));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case Chat.chat_type_file:
                    chat_forward.setMessage_attachment(IOUtils.convertToBase64(IOUtils.getInputStream(getContext(), chat_forward.convertUri_attachment())));
                    chat_forward.setFileModel(new FileModel(chat_forward.getUri_attachment(), FilePath.getMimeType(getContext(), chat_forward.convertUri_attachment())));
                    break;
            }
            is_forward = true;
        } else {
            finish();
        }

        Log.d(TAG, "initData: "+chatRoomUiModel.getUser_id());
        if(!chatRoomUiModel.getRoom_id().equals(" ")){
            NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancel(Integer.valueOf(chatRoomUiModel.getRoom_id()));
        }
    }

    private void setupViewModel(){
        viewModel = ViewModelProviders.of(this).get(ChatRoomViewModel.class);
        viewModel.initAppbarAction().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                switch (s){
                    case StringConstant.appbar_reaction:
                        if(emojiItemViewList==null || emojiItemViewList.size()==0) {
                            EmojiCategoryTransformer transformer = new EmojiCategoryTransformer();
                            emojiItemViewList = transformer.transform(initializeEmojiCategoryList());
                        }
                        showEmojiDialog();
                        break;
                    case StringConstant.appbar_copy:
                        presenter.copyChat(ChatroomHelper.getSelectedChatList(chatList));
                        sterilizeChat();
                        break;
                    case StringConstant.appbar_delete:
                        if (mediaPlayer.isPlaying()){
                            mediaPlayer.stop();
                            mediaPlayer.reset();
                        }
                        List<Integer> clickeds = ChatroomHelper.getIndexListClickedChat(chatList);
                        if(clickeds.size()>10){
                            GGFWUtil.ToastShort(getContext(), "Anda hanya bisa menghapus maksimal 10 pesan saja");
                        } else {
                            if(ChatroomHelper.isAllSelectedChatisMine(getContext(), ChatroomHelper.getSelectedChatList(chatList))){
                                new DeleteMessageDialog(getContext(), new DeleteMessageDialog.OnAlertDialog() {
                                    @Override
                                    public void onDeleteForMeButton(DialogInterface var1) {
                                        presenter.requestDeleteMessage(ChatroomHelper.getSelectedChatList(chatList), "1");
                                        for(Chat c : ChatroomHelper.getSelectedChatList(chatList)){
                                            chatList.remove(c);
                                        }
                                        adapter.notifiyListChanged();
                                        sterilizeChat();
                                    }

                                    @Override
                                    public void onDeleteForAllButton(DialogInterface var1) {
                                        for (int i = 0; i < clickeds.size(); i++) {
                                            chatList.get(clickeds.get(i)).setIs_deleted(1);
                                            adapter.notifiyListChanged();
                                        }
                                        List<Chat> selected_chat = ChatroomHelper.getSelectedChatList(chatList);
                                        List<Integer> selected_id = new ArrayList<>();
                                        presenter.requestDeleteMessage(selected_chat, "0");
                                        for(Chat x : selected_chat){
                                            selected_id.add(x.getMessage_id());
                                        }
                                        sendBroadcast(new Intent(StringConstant.chatroom_state_publish_to_room).putExtra("data", new PublishToRoom(""+chatRoomUiModel.getRoom_id(), ListentoRoomModel.STATE_DELETE, ChatoUtils.getUserLogin(getContext()).getUser_name(), selected_id)));
                                        sterilizeChat();
                                    }
                                });
                            } else {
                                new AlertDialogBuilder(getContext(), "Hapus untuk saya?", "Ya", "Tidak", new AlertDialogBuilder.OnAlertDialog() {
                                    @Override
                                    public void onPositiveButton(DialogInterface dialog) {
                                        presenter.requestDeleteMessage(ChatroomHelper.getSelectedChatList(chatList), "1");
                                        for(Chat c : ChatroomHelper.getSelectedChatList(chatList)){
                                            chatList.remove(c);
                                        }
                                        adapter.notifiyListChanged();
                                        sterilizeChat();
                                    }

                                    @Override
                                    public void onNegativeButton(DialogInterface dialog) {
                                        sterilizeChat();
                                    }
                                });
                            }
                        }

                        break;
                    case StringConstant.appbar_forward:
                        prepareToForward();
                        break;
                    case StringConstant.appbar_reply:
                        setupReplyMessage(ChatroomHelper.getSelectedOneChat(chatList));
                        sterilizeChat();
                        break;
                    case StringConstant.appbar_pinmessage:
                        presenter.pinMessageGroup(ChatroomHelper.getSelectedOneChat(chatList), grouproom.getPinned_message(), chatRoomUiModel.getRoom_id());
                        sterilizeChat();
                        break;
                    case StringConstant.appbar_star:
                        presenter.starChat(ChatroomHelper.getSelectedOneChat(chatList), chatRoomUiModel.getRoom_id());
                        sterilizeChat();
                        break;
                    case StringConstant.appbar_info:
                        if(isRoomAGroup(chatRoomUiModel)){
                            presenter.getGroupMessageInfo(ChatroomHelper.getSelectedOneChat(chatList), chatRoomUiModel.getRoom_id());
                        } else {
                            presenter.getMessageInfo(ChatroomHelper.getSelectedOneChat(chatList));
                        }
                        sterilizeChat();
                        break;

                }
            }
        });
    }

    private void checkChatType(String typeChat){
        switch (typeChat){
            case Chat.chat_type_message:

                break;
            case Chat.chat_type_label:
                appbar_action.hide(img_reaction);
                break;
            case Chat.chat_type_image:

                break;
            case Chat.chat_type_file:
                appbar_action.hide(img_reaction);
                break;
            case Chat.chat_type_audio:
                appbar_action.hide(img_reaction);
                appbar_action.hide(img_reply);
                appbar_action.hide(img_copy);
                break;
            case Chat.chat_type_video:
                appbar_action.hide(img_reaction);
                break;
        }
    }

    Chat prevChat;
    int prevPos = 0;

    private void setupRecyclerView(){
        layoutManager = new SpeedyLinearLayoutManager(this, SpeedyLinearLayoutManager.VERTICAL, false);
        layoutManager.setStackFromEnd(true);

        rv.setLayoutManager(layoutManager);
        rv_mention.setLayoutManager(new LinearLayoutManager(getContext()));
        chatList = new ArrayList<>();
        chatList_temp = new ArrayList<>();
        mentionList = new ArrayList<>();

        adapterMention = new MentionAdapter(getContext(), mentionList, new MentionAdapter.MentionAdapterView() {
            @Override
            public void onClickItemMention(MentionModel item) {
                Log.e("ChatRoom", "ini item data "+ item.getName());
                Editable editable = edt_message.getText();
                editable.delete(selectTextStart,selectTextEnd);
                editable.insert(selectTextStart, item.getName().replace(" ", "_") + " ");
                resetCursor();
            }
        });

        adapter = new ChatRoomAdapter(getContext(), chatList, new ChatRoomAdapter.OnChatRoomClick() {
            @Override
            public void onLongPress(View view, int position) {
                chatList.get(position).setClicked(true);
                adapter.notifyDataSetChanged();
                lastSelectedChatType = chatList.get(position).getMessage_type();
                presenter.checkSelectedChat(chatList);
            }

            @Override
            public void onClickItemView(View view, int position) {
                if(chatList.get(position).isClicked()){
                    chatList.get(position).setClicked(false);
                    adapter.notifyDataSetChanged();
                    presenter.checkSelectedChat(chatList);
                } else {
                    if(adapter.checkIsListClicked()){
                        chatList.get(position).setClicked(true);
                        adapter.notifyDataSetChanged();
                        presenter.checkSelectedChat(chatList);
                    }
                }
            }

            @Override
            public void onClickRepliedMessage(Chat chat) {
                if(!chat.getMessage_replay_id().equals("")){
                    if(ChatRoomHelper.isMessageAvailable(Integer.valueOf(chat.getMessage_replay_id()), adapter.getItems(), adapter.getItemCount())){
                        rv.scrollToPosition(ChatRoomHelper.indexRedirect(Integer.valueOf(chat.getMessage_replay_id()), adapter.getItems()));
                        initTimer1(ChatRoomHelper.indexRedirect(Integer.valueOf(chat.getMessage_replay_id()), adapter.getItems()));
                        timer1.start();
                    }
                }
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
            public void onDownloadingAttachment(boolean isdownload, int position) {
                askCompactPermissions(RequiredPermissions, new PermissionResultInterface() {
                    @Override
                    public void permissionGranted() {
                        chatList.get(position).setVideoDownloding(isdownload);
                        adapter.notifiyListChanged();
                    }

                    @Override
                    public void permissionDenied() {
                        GGFWUtil.ToastShort(getContext(), "Anda perlu memberikan akses terlebih dahulu");
                    }
                });
            }

            @Override
            public void onOpenVideo(View view, Chat chat, Uri uri) {
                Bundle data = new Bundle();
                data.putSerializable("chat", chat);
                data.putString("uri", uri.toString());

                if (!ChatoUtils.isPreLolipop()){
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(ChatRoomActivity.this, view, ViewCompat.getTransitionName(view));
                    startActivity(new Intent(getContext(), PlayVideoActivity.class).putExtras(data), options.toBundle());
                }else{
                    startActivity(new Intent(getContext(), PlayVideoActivity.class).putExtras(data));
                }

            }

            @Override
            public void onPlayAudio(Chat chat, int position) {
                if (prevChat!=null && prevPos!=position){
                    adapter.notifyItemChanged(prevPos);
                }
                prevChat = chat;
                prevPos = position;
            }

            @Override
            public void onImageClick(View view, Chat chat) {
                Bundle data = new Bundle();
                try {
                    data.putString("title", chat.getFrom_username());
                    String dateTime = chat.getMessage_date() + " " + chat.getMessage_time();
                    Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).parse(dateTime);

                    data.putString("subtitle",new SimpleDateFormat("dd-MMMM-yyyy, HH:mm", Locale.getDefault()).format(date));
                    data.putString("image", chat.getMessage_attachment());

                    if (!ChatoUtils.isPreLolipop()){
                        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(ChatRoomActivity.this, view, ViewCompat.getTransitionName(view));
                        startActivity(new Intent(getContext(), ImageViewActivity.class).putExtras(data), options.toBundle());
                    }else{
                        startActivity(new Intent(getContext(), ImageViewActivity.class).putExtras(data));
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onReadMessage(Chat chat) {
                chat.setRoom_id(chatRoomUiModel.getRoom_id());
                List<Chat> lChat = new ArrayList<>();
                lChat.add(chat);
                presenter.sendStatusMessage(lChat);
            }
        }, isRoomAGroup(chatRoomUiModel));

        endlessRecyclerViewScrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int var1, int var2, RecyclerView var3) {
                Log.d(TAG, "onLoadMore: here im loading");
                requestHistory(chatRoomUiModel.getUser_id(), String.valueOf(chatList.get(0).getMessage_id()), false);
            }
        };

        RecyclerView.OnScrollListener fabScrollListener = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if(!rv.canScrollVertically(1)){
                    fab_down.hide();
                    initNumUnread(false);
                } else {
                    if (dy < 0 && fab_down.getVisibility() == View.GONE) {
                        fab_down.show();
                    }
                }
            }
        };

        rv.addOnScrollListener(endlessRecyclerViewScrollListener);
        rv.addOnScrollListener(fabScrollListener);

        rv.setItemAnimator(new DefaultItemAnimator());
        rv.setAdapter(adapter);

        rv_mention.setItemAnimator(new DefaultItemAnimator());
        rv_mention.setAdapter(adapterMention);
        adapterMention.notifyDataSetChanged();

        fab_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initNumUnread(false);
                rv.scrollToPosition(chatList.size()-1);
                fab_down.hide();
            }
        });
    }

    MediaPlayer mediaPlayer = MediaPlayerSingleton.INSTANCE.getMediaPlayer();

    private void initTimer1(int position){
        timer1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(300);
                } catch (Exception e) {

                } finally {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            View view = layoutManager.findViewByPosition(position);
                            if(view!=null)
                                view.findViewById(R.id.lay_message).setBackgroundColor(Color.parseColor("#963A3C3D"));
                            initTimer2(position);
                            timer2.start();
                        }
                    });
                }
            }
        });
    }

    private void initTimer2(Integer position){
        timer2 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(300);
                } catch (Exception e) {

                } finally {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            layoutManager.findViewByPosition(position).findViewById(R.id.lay_message).setBackgroundColor(Color.TRANSPARENT);
                        }
                    });
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_chatroom, menu);
        /*
        getMenuInflater().inflate(R.menu.menu_new_chatroom, menu);

        menu_list.setLayoutManager(new GridLayoutManager(getContext(),3));
        ArrayList<MenuModel> menuModels = new ArrayList<>();
        menuModels.add(new MenuModel(R.drawable.ic_info_black_24dp, "Info", () -> {
            if(!chatRoomUiModel.getType().equals(RoomChat.official_room_type))
                openRoomInfo();
        }));
        menuModels.add(new MenuModel(R.drawable.ic_star_full, "Pesan Berbintang", () -> {
            startActivityForResult(new Intent(getContext(), StarredMessageActivity.class).putExtra("room", chatRoomUiModel), REQUEST_STAR_MESSAGES);
        }));
        menuModels.add(new MenuModel(R.drawable.ic_chato_search, "Cari", () -> {
            appbar_action.show();
            isAppBarShow = true;
            appbar_action.displaying(lay_searchbar);
            lay_action_chat.setVisibility(View.GONE);
            lay_action_mic.setVisibility(View.GONE);
            toolbar.setVisibility(View.GONE);
            ChatoUtils.showKeyboard(getContext(), edt_search);
            fab_down.hide();
        }));
        menu_list.setAdapter(new MenuAdapter(menuModels, lay_menu));

         */
        if(chatRoomUiModel.getType().equals(RoomChat.official_room_type)){
            menu.findItem(R.id.action_room_info).setTitle("Info Grup");
            menu.findItem(R.id.action_room_info).setVisible(false);
            menu.findItem(R.id.action_phonecall).setVisible(false);
            menu.findItem(R.id.action_videocall).setVisible(false);
            menu.findItem(R.id.action_starredmessage).setVisible(false);
        } else {
            if(isRoomAGroup(chatRoomUiModel) && isJoin){
                menu.findItem(R.id.action_room_info).setTitle("Info Grup");
                menu.findItem(R.id.action_phonecall).setVisible(false);
                menu.findItem(R.id.action_videocall).setVisible(false);
                //menu.findItem(R.id.action_starredmessage).setVisible(false);
            }else {
                menu.findItem(R.id.action_room_info).setTitle("Info Grup");
                menu.findItem(R.id.action_room_info).setVisible(false);
                menu.findItem(R.id.action_phonecall).setVisible(false);
                menu.findItem(R.id.action_videocall).setVisible(false);
                menu.findItem(R.id.action_starredmessage).setVisible(false);
            }
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        /*if (itemId == R.id.action_phonecall) {
            startActivity(new Intent(getContext(), CallActivity.class).putExtra("data", chatRoomUiModel).putExtra(StringConstant.call_outcoming_audio, true));
        } else if (itemId == R.id.action_videocall) {
            startActivity(new Intent(getContext(), CallActivity.class).putExtra("data", chatRoomUiModel).putExtra(StringConstant.call_outcoming_video, true));
        }*/ if (itemId == R.id.action_starredmessage) {
            startActivityForResult(new Intent(getContext(), StarredMessageActivity.class).putExtra("room", chatRoomUiModel), REQUEST_STAR_MESSAGES);
        } else if (itemId == R.id.action_broadcast) {
            startActivity(new Intent(getContext(), ContactBroadcastActivity.class) );
        } else if (itemId == R.id.action_searchmessage) {
            appbar_action.show();
            isAppBarShow = true;
            appbar_action.displaying(lay_searchbar);
            lay_action_chat.setVisibility(View.GONE);
            lay_action_mic_or_send.setVisibility(View.GONE);
            toolbar.setVisibility(View.GONE);
            ChatoUtils.showKeyboard(getContext(), edt_search);
            fab_down.hide();
        } else if (itemId == R.id.action_room_info){
            if(!chatRoomUiModel.getType().equals(RoomChat.official_room_type))
                openRoomInfo();
        } else if (itemId == R.id.action_menu){
            if(lay_menu.getVisibility()==View.GONE){
                lay_menu.setVisibility(View.VISIBLE);
            } else {
                lay_menu.setVisibility(View.GONE);
            }
        }
        return super.onOptionsItemSelected(item);
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
        if(!message.equals(""))
            GGFWUtil.ToastShort(getContext(), message);
        else
            GGFWUtil.ToastShort(getContext(), getString(R.string.helper_noconnection));
    }

    @Override
    public void onAuthFailed(String error) {

    }

    private void onShowAttachment(Object o, int type, String name){
        TYPE_ATTACHMENT = type;
        if(o == null){
            emptyAttachment();
            if(type != TYPE_MESSAGE){
                lay_action_attachment.hide();
                TYPE_ATTACHMENT = TYPE_MESSAGE;
            }
            onTextChangeInput(uri_attachment);
            file_attachment = "";
        } else {
            tv_attachment.setText(name);
            switch (type){
                case TYPE_IMAGE:
                    img_attachment.setImageBitmap((Bitmap) o);
                    file_attachment = GGFWUtil.convertToBase64((Bitmap) o);
                    break;
                case TYPE_FILE:
                    img_attachment.setImageResource(R.drawable.ic_arsip);
                    file_attachment = IOUtils.convertToBase64((InputStream) o);
            }
            Log.d(TAG, "onShowAttachment: "+file_attachment);
            lay_action_attachment.show();
            onTextChangeInput(uri_attachment);
        }
    }

    public void setViewOnClickEvent(View view) {
        Log.d(TAG, "setViewOnClickEvent: "+view.getId());
        int id = view.getId();
        if (id == R.id.card_bar) {
            finish();
        } else if (id == R.id.img_attach) {
            if (lay_menu_attach.getVisibility() == View.VISIBLE) {
                lay_menu_attach.setVisibility(View.GONE);
                edt_message.requestFocus();
            } else {
                lay_menu_attach.setVisibility(View.VISIBLE);
                ChatoUtils.hideSoftKeyboard(this, edt_message);
            }
        } else if (id == R.id.lay_document) {
            askCompactPermissions(RequiredPermissions, new PermissionResultInterface() {
                @Override
                public void permissionGranted() {
                    /*new FileShareDialog(getContext(), new FileShareDialog.OnAction() {
                        @Override
                        public void onMyDocumentSelected() {
                            startActivity(new Intent(getContext(), MyDocumentActivity.class)
                                    .putExtra(MyDocumentActivity.OPEN_TYPE, MyDocumentActivity.OPEN_DOCUMENT));
                        }

                        @Override
                        public void onInternalStorageSelected() {

                        }
                    });*/
                    Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    intent.putExtra(Intent.EXTRA_MIME_TYPES, StringConstant.mimeTypesFile);
                    intent.setType("*/*");
                    intent.addCategory(Intent.CATEGORY_OPENABLE);

                    startActivityForResult(intent, REQUEST_CODE_ATTACHMENT);
                }

                @Override
                public void permissionDenied() {
                    GGFWUtil.ToastShort(getContext(), "Anda perlu memberikan akses terlebih dahulu");
                }
            });
        } else if (id == R.id.lay_gallery) {
            askCompactPermissions(RequiredPermissions, new PermissionResultInterface() {
                @Override
                public void permissionGranted() {
                    /*new FileShareDialog(getContext(), new FileShareDialog.OnAction() {
                        @Override
                        public void onMyDocumentSelected() {
                            startActivity(new Intent(getContext(), MyDocumentActivity.class)
                                    .putExtra(MyDocumentActivity.OPEN_TYPE, MyDocumentActivity.OPEN_GALLERY));
                        }

                        @Override
                        public void onInternalStorageSelected() {

                        }
                    });*/
                    EasyImage.openGalleryVideo(ChatRoomActivity.this, 0);
                }

                @Override
                public void permissionDenied() {
                    GGFWUtil.ToastShort(getContext(), "Anda perlu memberikan akses terlebih dahulu");
                }
            });
        } else if (id == R.id.lay_record_video) {
            askCompactPermissions(RequiredPermissions, new PermissionResultInterface() {
                @Override
                public void permissionGranted() {
                    EasyImage.openVideo(ChatRoomActivity.this, 0);
                }

                @Override
                public void permissionDenied() {
                    GGFWUtil.ToastShort(getContext(), "Anda perlu memberikan akses terlebih dahulu");
                }
            });
        } else if (id == R.id.img_camera) {
            askCompactPermissions(RequiredPermissions, new PermissionResultInterface() {
                @Override
                public void permissionGranted() {
                    EasyImage.openCamera(ChatRoomActivity.this, 0);
//                        new DialogImagePicker(getContext(), true, new DialogImagePicker.OnDialogImagePicker() {
//                            @Override
//                            public void onCameraClick() {
////                                EasyImage.openCamera(ChatRoomActivity.this, 0);
//                                EasyImage.openCamera(ChatRoomActivity.this, 0);
//                            }
//
//                            @Override
//                            public void onFileManagerClick() {
//                                EasyImage.openGalleryVideo(ChatRoomActivity.this, 0);
//                            }
//
//                            @Override
//                            public void onVideoCameraClick() {
//                                EasyImage.openVideo(ChatRoomActivity.this, 0);
//                            }
//                        });
                }

                @Override
                public void permissionDenied() {
                    GGFWUtil.ToastShort(getContext(), "Anda perlu memberikan akses terlebih dahulu");
                }
            });
        } else if (id == R.id.img_attachment_close) {
            onShowAttachment(null, TYPE_IMAGE, "");
        } else if (id == R.id.img_replied_close) {
            setupReplyMessage(false);
        } else if (id == R.id.img_send) {
            prepareToSendMessage();
        } else if (id == R.id.lay_action_send) {
            if (!stop){
                stop = true;
                if (waveRecorder!=null){
                    waveRecorder.stopRecording();
                    Log.e("LOG", "ini path audio = "+nowAudioRecordPath);
                }
                onFinishRecordAudio(nowAudioRecordPath);
            }else {
                prepareToSendMessage();
            }
        } else if (id == R.id.lay_action_mic) {
            prepareRecordAudio();
        } else if (id == R.id.tv_cancel_record) {
            changeUiToChat();
        } else if (id == R.id.img_action_back) {
            onActionBarBack();
        } else if (id == R.id.container_pinned_message) {
            new PinnedGroupMessageDialog(getContext(), grouproom.getPinned_message(), (grouproom.getIs_admin() == 0 ? false : true), new PinnedGroupMessageDialog.OnPinnedGroupMessage() {
                @Override
                public void onUnpinMessage(Chat chat, Dialog dialog) {
                    presenter.pinMessageGroup(chat, chat, chatRoomUiModel.getRoom_id());
                    dialog.dismiss();
                }
            });
        } else if (id == R.id.img_reaction) {
            viewModel.updateAppbarAction(StringConstant.appbar_reaction);
        } else if (id == R.id.img_copy) {
            viewModel.updateAppbarAction(StringConstant.appbar_copy);
        } else if (id == R.id.img_forward) {
            viewModel.updateAppbarAction(StringConstant.appbar_forward);
        } else if (id == R.id.img_reply) {
            viewModel.updateAppbarAction(StringConstant.appbar_reply);
        } else if (id == R.id.img_delete) {
            viewModel.updateAppbarAction(StringConstant.appbar_delete);
        } else if (id == R.id.img_star) {
            viewModel.updateAppbarAction(StringConstant.appbar_star);
        } else if (id == R.id.img_info) {
            viewModel.updateAppbarAction(StringConstant.appbar_info);
        } else if (id == R.id.img_pinmessage) {
            viewModel.updateAppbarAction(StringConstant.appbar_pinmessage);
        } else if (id == R.id.lay_detail_room) {
            if(!chatRoomUiModel.getType().equals(RoomChat.official_room_type) || isJoin)
                openRoomInfo();
        } else if (id == R.id.edt_message) {
            emojIcon.closeEmojIcon();
            if (lay_menu_attach.getVisibility() == View.VISIBLE) {
                lay_menu_attach.setVisibility(View.GONE);
                edt_message.requestFocus();
            }
        } else if (id == R.id.ll_menu) {
            if(lay_menu.getVisibility()==View.GONE){
                lay_menu.setVisibility(View.VISIBLE);
            } else {
                lay_menu.setVisibility(View.GONE);
            }
        }
    }
    boolean stop = true;
    int second = 0;
    int minute = 0;
    //long milisecond = 0;

    private void doCountLenghtRecord(){
        second = 0;
        minute = 0;
        stop = false;
        Thread thread = new Thread(){
            @Override
            public void run() {
                super.run();
                try{
                    while(!stop){
                        sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //milisecond +=1000;
                                second ++;
                                if (second == 60){
                                    minute++;
                                    second = 0;
                                }
                                tv_count_record.setText(String.format("%s:%s", minute, secondToString(second)));
                            }
                        });
                    }

                }catch (InterruptedException e){

                }
            }
        };
        thread.start();
    }

    private String secondToString(int second){
        String result = "";
        String sSecond = String.valueOf(second);
        if (sSecond.length()==1){
            result = "0"+sSecond;
        }else {
            result = sSecond;
        }
        return result;
    }

    private void changeUiToChat(){
        lay_on_chat_or_record.hide(lay_onrecord);
        lay_on_chat_or_record.display(lay_onchat);
        lay_action_mic_or_send.hide(lay_action_send);
        lay_action_mic_or_send.display(lay_action_mic);

        TYPE_ATTACHMENT = TYPE_MESSAGE;
        iv_onrecord.clearAnimation();
        stop = true;
    }

    private void changeUiToRecord(){
        lay_on_chat_or_record.display(lay_onrecord);
        lay_on_chat_or_record.hide(lay_onchat);
        lay_action_mic_or_send.hide(lay_action_mic);
        lay_action_mic_or_send.display(lay_action_send);

        TYPE_ATTACHMENT = AUDIO_FILE;
        Animation animation = new AlphaAnimation(1, 0);
        animation.setDuration(1000);
        animation.setInterpolator(new LinearInterpolator());
        animation.setRepeatCount(Animation.INFINITE);
        animation.setRepeatMode(Animation.REVERSE);
        iv_onrecord.startAnimation(animation);
        /*

        stop = false;
        tv_count_record.setText("0:00");
        doCountLenghtRecord();

         */
    }

    String nowAudioRecordPath = "";
    String nowAudioRecordName = "";
    String nowAudioRecordUrl = "";
    WaveRecorder waveRecorder;

    private void onFinishRecordAudio(String absolutePath){
        new AudioUploader(this, loading, Uri.fromFile(new File(absolutePath)), false, new AudioUploader.OnUploadAudio() {
            @Override
            public void onSuccessUploadAudio(String url) {
                nowAudioRecordUrl = url;
                prepareToSendMessage();
            }

            @Override
            public void onFailedUploadAudio(String message) {

            }

        });
    }

    private void recordAudio(){
        Calendar cal = Calendar.getInstance();
        long mils = cal.getTimeInMillis();
        String dateNow = DateFormatUtilChat.INSTANCE.longToString(mils,"hhmmss_ddMMyyyy");
        nowAudioRecordName = String.format("audio_record_%s", dateNow);
        nowAudioRecordPath = getExternalCacheDir().getAbsolutePath() + "/"+ nowAudioRecordName +".wav";

        waveRecorder = new WaveRecorder(nowAudioRecordPath);
        waveRecorder.startRecording();

        stop = false;
        tv_count_record.setText("0:00");
        doCountLenghtRecord();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                handleCropResult(resultUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                String error = result.getError().toString();
                GGFWUtil.ToastShort(getContext(), ""+error);
            }
        } else if(requestCode == REQUEST_CODE_ATTACHMENT) {
            try {
                Uri resultUri = data.getData();
                handleFileResult(resultUri);
            } catch (NullPointerException e){

            }
        } else if(requestCode == Constants.RequestCodes.TAKE_VIDEO){
            Log.d(TAG, "herevideo: hehehe");
            if(data != null) {
//                new DialogVideoPreview(getContext(), FilePath.uriFileOrOther(getContext(), data.getData()), new DialogVideoPreview.OnDialogVideoPreview() {
//                    @Override
//                    public void onPrepareVideoToSend(VideoPreview videoPreview) {
//                        TYPE_ATTACHMENT = VIDEO_FILE;
//                        edt_message.setText(videoPreview.getMessageVideo());
//                        file_attachment = videoPreview.getBase64Video();
//                        fileModel = new FileModel(videoPreview.getUri(), videoPreview.getMimeType(getContext()), videoPreview.getVideo_name());
//                        Log.d(TAG, "onPrepareVideoToSend: " + videoPreview.getUri() + " - " + videoPreview.getRealUri().getPath());
//                        thumb_file_attachment = ThumbnailUtils.createVideoThumbnail(videoPreview.getRealUri().getPath(), MediaStore.Video.Thumbnails.MINI_KIND);
//                        Log.d(TAG, "onPrepareVideoToSend: " + thumb_file_attachment);
//                        prepareToSendMessage();
//                    }
//                });

                startActivityForResult(new Intent(this, VideoPreviewActivity.class).putExtra("uri",FilePath.uriFileOrOther(getContext(), data.getData())), PREPARE_SEND_VIDEO);
            }
            lay_menu_attach.setVisibility(View.GONE);
        } else if(requestCode == PREPARE_SEND_VIDEO){
            if(resultCode == 100 && data!=null) {
                TYPE_ATTACHMENT = VIDEO_FILE;
                edt_message.setText(data.getStringExtra("text"));
                file_attachment = IOUtils.convertToBase64(IOUtils.getInputStream(getContext(), data.getData()));
                fileModel = new FileModel(data.getData().toString(), FilePath.getMimeType(getContext(), data.getData()), data.getStringExtra("videoName"));
//                    Log.d(TAG, "onPrepareVideoToSend: " + videoPreview.getUri() + " - " + videoPreview.getRealUri().getPath());
                thumb_file_attachment = ThumbnailUtils.createVideoThumbnail(data.getData().getPath(), MediaStore.Video.Thumbnails.MINI_KIND);
                duration_file_attachment = ""+ChatoUtils.getVideoDuration(getContext(), Uri.parse(data.getData().getPath()));
                Log.d(TAG, "onPrepareVideoToSend: " + thumb_file_attachment);
                Log.d(TAG, "onPrepareVideoToSend duration: " + duration_file_attachment);
                prepareToSendMessage();
            }
            lay_menu_attach.setVisibility(View.GONE);
        } else if(requestCode == REQUEST_ROOM_INFO){
            if(resultCode == StringConstant.REFRESH_CHAT_HISTORY){
                requestHistory(chatRoomUiModel.getUser_id(), ""+ ChatoUtils.getUserLogin(getContext()).getUser_id(), true);
            } else if(resultCode == StringConstant.FINNISH_CHAT_ACTIVITY){
                finish();
            }
        } else if(requestCode == REQUEST_STAR_MESSAGES){
            if(resultCode == StringConstant.REFRESH_CHAT_HISTORY) {
                requestHistory(chatRoomUiModel.getUser_id(), "" + ChatoUtils.getUserLogin(getContext()).getUser_id(), true);
            }
        } else {
            EasyImage.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {
                @Override
                public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
                    e.printStackTrace();

                }

                @Override
                public void onImagesPicked(List<File> imageFiles, EasyImage.ImageSource source, int type) {
                    String mimetype = FilePath.getMimeType(getContext(), Uri.fromFile(imageFiles.get(0)));
                    Log.d(TAG, "onImagesPicked: "+mimetype);
                    if(mimetype.equals("image/jpeg") || mimetype.equals("image/png")){
                        name_attachment = FilePath.getFileName(getContext(), Uri.fromFile(imageFiles.get(0)));
                        startCropActivity(Uri.fromFile(imageFiles.get(0)));
                    } else {
                        startActivityForResult(new Intent(getContext(), VideoPreviewActivity.class).putExtra("uri",FilePath.uriFileOrOther(getContext(), data.getData())), PREPARE_SEND_VIDEO);
//                        new DialogVideoPreview(getContext(), FilePath.uriFileOrOther(getContext(), data.getData()), new DialogVideoPreview.OnDialogVideoPreview() {
//                            @Override
//                            public void onPrepareVideoToSend(VideoPreview videoPreview) {
//                                TYPE_ATTACHMENT = VIDEO_FILE;
//                                edt_message.setText(videoPreview.getMessageVideo());
//                                file_attachment = videoPreview.getBase64Video();
//                                fileModel = new FileModel(videoPreview.getUri(), videoPreview.getMimeType(getContext()));
//                                Log.d(TAG, "onPrepareVideoToSend: "+videoPreview.getUri()+" - "+videoPreview.getRealUri().getPath());
//                                thumb_file_attachment = ThumbnailUtils.createVideoThumbnail(videoPreview.getRealUri().getPath(), MediaStore.Video.Thumbnails.MINI_KIND);
//                                Log.d(TAG, "onPrepareVideoToSend: "+thumb_file_attachment);
//                                prepareToSendMessage();
//                            }
//                        });
                    }
                }

                @Override
                public void onCanceled(EasyImage.ImageSource source, int type) {
                    //Cancel handling, you might wanna remove taken photo if it was canceled
                    if (source == EasyImage.ImageSource.CAMERA) {
                        File photoFile = EasyImage.lastlyTakenButCanceledPhoto(getContext());
                        if (photoFile != null) photoFile.delete();
                    }
                }
            });
        }
    }

    private void openRoomInfo(){
        if (!isRoomAGroup(chatRoomUiModel)) {
            /*
            if (!ChatoUtils.isPreLolipop()) {
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(ChatRoomActivity.this, img_profile, ViewCompat.getTransitionName(img_profile));
                startActivityForResult(new Intent(getContext(), UserRoomDetailActivity.class).putExtra("data", chatRoomUiModel), REQUEST_ROOM_INFO, options.toBundle());
            } else {
                startActivityForResult(new Intent(getContext(), UserRoomDetailActivity.class).putExtra("data", chatRoomUiModel), REQUEST_ROOM_INFO);
            }

             */
            startActivityForResult(new Intent(getContext(), UserRoomDetailNewActivity.class).putExtra("data", chatRoomUiModel), REQUEST_ROOM_INFO);
        } else {
            /*
            if (!ChatoUtils.isPreLolipop()) {
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(ChatRoomActivity.this, img_profile, ViewCompat.getTransitionName(img_profile));
                startActivityForResult(new Intent(getContext(), GroupInfoNewActivity.class).putExtra("data", chatRoomUiModel), REQUEST_ROOM_INFO, options.toBundle());
            } else {
                startActivityForResult(new Intent(getContext(), GroupInfoNewActivity.class).putExtra("data", chatRoomUiModel), REQUEST_ROOM_INFO);
            }

             */
            startActivityForResult(new Intent(getContext(), GroupInfoNewActivity.class).putExtra("data", chatRoomUiModel), REQUEST_ROOM_INFO);
        }
    }

    private void deleteMessage(ListentoRoomModel listentoRoomModel){
        for (int x: listentoRoomModel.getMessage_id()){
            int indexredirect = ChatRoomHelper.indexRedirect(x, chatList);
            if(indexredirect != 0){
                chatList.get(indexredirect).setIs_deleted(1);
            }
        }
        adapter.notifiyListChanged();
    }

    private void onActionBarBack(){
        if (lay_searchbar.getVisibility() == View.VISIBLE) {
            edt_search.setText("");
            appbar_action.hide(lay_searchbar);
            appbar_action.hide();
            lay_action_chat.setVisibility(View.VISIBLE);
            lay_action_mic_or_send.setVisibility(View.VISIBLE);
            toolbar.setVisibility(View.VISIBLE);
            ChatoUtils.hideSoftKeyboard(getContext(), edt_search);
            requestHistory(chatRoomUiModel.getUser_id(), ""+ ChatoUtils.getUserLogin(getContext()).getUser_id(), true);
        } else {
            sterilizeChat();
        }
    }

    private void handleCropResult(Uri uri){
        if(uri != null){
            try {
                Log.d(TAG, "handleCropResult: "+uri);
                bitmap_image = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                uri_attachment = uri;
                fileModel = new FileModel(uri.toString(), FilePath.getMimeType(getContext(), uri));
                onShowAttachment(bitmap_image, TYPE_IMAGE, name_attachment);
                if(lay_menu_attach.getVisibility()==View.VISIBLE){
                    lay_menu_attach.setVisibility(View.GONE);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            GGFWUtil.ToastShort(getContext(), "Cannot Retrieve Cropped Image");
        }
    }

    private void handleFileResult(Uri uri){
        if(uri != null){
            uri_attachment = uri;
            Log.d(TAG, "handleFileResult: "+FilePath.getMimeType(getContext(), uri));
            name_attachment = FilePath.getFileName(getContext(), uri);
            stream_attachment = IOUtils.getInputStream(getContext(), uri);
            fileModel = new FileModel(uri.toString(), FilePath.getMimeType(getContext(), uri));
            onShowAttachment(stream_attachment, TYPE_FILE, name_attachment);
            if(lay_menu_attach.getVisibility()==View.VISIBLE){
                lay_menu_attach.setVisibility(View.GONE);
//                    edt_message.requestFocus();
            }
        }
    }

    private void emptyAttachment(){
        name_attachment = "";
        uri_attachment = null;
    }

    @Override
    public void onRequestHistoryChat(List<Chat> chats, boolean isRefresh) {
        endlessRecyclerViewScrollListener.resetState();
        adapter.setKeyword("");
        if(isRefresh){
            chatList.clear();
            for (Chat c: chats){
//                Log.d(TAG, "onRequestHistoryChat: "+c.getMessage_status());
                chatList.add(c);
            }
            adapter.notifyDataSetChanged();

            if(is_forward){
                is_forward = false;
                sendMessage(chat_forward);
            }

            int unreadstate = ChatroomHelper.getIndexUnread(getContext(), chats);
//            Log.d(TAG, "onRequestHistoryChat: "+unreadstate);
            if(unreadstate > 0){
                rv.scrollToPosition(unreadstate);
                fab_down.show();
            } else {
                rv.scrollToPosition(chatList.size()-1);
            }
        } else {
            for (Chat c: chats){
                chatList.add(0, c);
            }
            adapter.notifiyRangeChanged(chats.size());
        }

        presenter.sendStatusMessage(getListWithNoStatus(chats, StringConstant.chat_status_read));
    }

    @Override
    public void onSendMessage(Chat chat) {
        for (int i = 0; i < chatList.size(); i++) {
            if(chat.getPayload().equals(chatList.get(i).getPayload())){
                chatList.set(i, chat);
                adapter.notifiyListChanged();
            }
        }
    }

    @Override
    public void onFailedRequestHistoryChat(String message) {
        if(chatList.size()==0){
            if(is_forward){
                is_forward = false;
                sendMessage(chat_forward);
            }
        }
    }

    @Override
    public void onFailedSendMessage(Chat chat) {
        for (int i = 0; i < chatList.size(); i++) {
            if(chat.getPayload().equals(chatList.get(i).getPayload())){
                chatList.set(i, chat);
                adapter.notifiyListChanged();
            }
        }
    }

    @Override
    public void onFailedSendMessage(Chat chat, String message) {
        tv_loading_top.setText(message);
        helper_loading_top.showForAWhile(this);

        for (int i = 0; i < chatList.size(); i++) {
            if(chat.getPayload().equals(chatList.get(i).getPayload())){
                chatList.set(i, chat);
                adapter.notifiyListChanged();
            }
        }
    }

    @Override
    public void onAppBarAction(int status_messageselected) {
        switch (status_messageselected){
            case StringConstant.status_messageselected_none:
                if(isAppBarShow) {
                    appbar_action.hide();
                    isAppBarShow = false;
                }
                toolbar.setVisibility(View.VISIBLE);
                break;
            case StringConstant.status_messageselected_one:
                updateActionAppBar(false, (ChatroomHelper.getSelectedOneChat(chatList).getFrom_user_id() == ChatoUtils.getUserLogin(getContext()).getUser_id() ? true : false));

                appbar_action.show();
                isAppBarShow = true;
                toolbar.setVisibility(View.GONE);
                checkChatType(lastSelectedChatType);
                appbar_action.hide(lay_searchbar);

                tv_action_title.setText(""+ChatroomHelper.totalSelected(chatList));
                setupStarIcon(ChatroomHelper.getSelectedOneChat(chatList).getMessage_star() == 0 ? false : true);
                break;
            case StringConstant.status_messageselected_multiple:
                updateActionAppBar(true, false);

                appbar_action.show();
                isAppBarShow = true;
                toolbar.setVisibility(View.GONE);
                appbar_action.hide(lay_searchbar);

                tv_action_title.setText(""+ChatroomHelper.totalSelected(chatList));
                break;

        }
    }

    @Override
    public void setupStarIcon(boolean isStarred) {
        if(isStarred){
            img_star.setImageResource(R.drawable.ic_action_message_star_24dp);
        } else {
            img_star.setImageResource(R.drawable.ic_star_outline);
        }
    }

    @Override
    public void onStarChat(Chat chat, boolean isStarred) {
        chatList.get(ChatroomHelper.getIndexClickedChat(chatList)).setMessage_star(isStarred == true ? 1 : 0);
        adapter.notifiyListChanged();
    }

    @Override
    public void checkListMessageStatus(List<Chat> list_message_with_status) {
        Log.d(TAG, "checkListMessageStatus: "+list_message_with_status.get(0).getMessage_id() + ","
                + list_message_with_status.get(0).getMessage_status());
        if(list_message_with_status.size()>0){
//            if(list_message_with_status.get(0).getFrom_user_id() == Integer.valueOf(chatRoomUiModel.getUser_id())){
            if(list_message_with_status.get(0).getRoom_id().equals(chatRoomUiModel.getRoom_id())){
                for (int i = 0; i < chatList.size(); i++) {
                    for (Chat chat : list_message_with_status){
                        if(chatList.get(i).getMessage_id() == chat.getMessage_id()){
                            chatList.get(i).setMessage_status(chat.getMessage_status());
                        }
                    }
                }
            }
        }
    }

    @Override
    public void setStatusOnList(String state) {
        for (int i = 0; i < chatList.size(); i++) {
            if(chatList.get(i).getFrom_user_id() != ChatoUtils.getUserLogin(getContext()).getUser_id()){
                chatList.get(i).setMessage_status(state);
            }
        }
    }

    @Override
    public void hideContainerReply() {
        if(container_reply.isShown()){
            container_reply.hide();
        }
    }

    @Override
    public void onSendStatusMessage() {
        setStatusOnList(StringConstant.chat_status_read);
    }

    //    Fungsi untuk mengambil list chat yang memiliki status belum terkirim
    private List<Chat> getListWithNoStatus(List<Chat> list, String status){
        List<Chat> val = new ArrayList<>();
        for (Chat chat : list){
            if(chat.getFrom_user_id() != ChatoUtils.getUserLogin(getContext()).getUser_id()){
                if(!chat.getMessage_status().equals(status)){
                    val.add(chat);
                }
            }
        }
        return val;
    }

    private void sendStatusReadMessage(Chat chat){
        Log.d(TAG, "readMessage: " + chat.getMessage_text());
        List<Chat> chats = new ArrayList<>();
        chat.setMessage_status(StringConstant.chat_status_read);
        chats.add(chat);
        presenter.sendStatusMessage(chats);
    }

    private void prepareToSendMessage(){
        Chat ch = null;
        switch (TYPE_ATTACHMENT){
            case TYPE_MESSAGE:
                ch = new Chat(ChatoUtils.getUserLogin(getContext()).getUser_id(), Integer.valueOf(chatRoomUiModel.getUser_id()), edt_message.getText().toString(), Chat.chat_type_message, "", StringConstant.chat_status_sending, ""+chatList.size(), chatRoomUiModel.getRoom_id(), chatRoomUiModel.getRoom_code());
                onShowAttachment(null, TYPE_MESSAGE, "");
                break;
            case TYPE_IMAGE:
                ch = new Chat(ChatoUtils.getUserLogin(getContext()).getUser_id(), Integer.valueOf(chatRoomUiModel.getUser_id()), edt_message.getText().toString(), Chat.chat_type_image, file_attachment,  StringConstant.chat_status_sending, ""+chatList.size(), fileModel, bitmap_image, chatRoomUiModel.getRoom_id(), chatRoomUiModel.getRoom_code());
                onShowAttachment(null, TYPE_IMAGE, "");
                break;
            case TYPE_FILE:
                ch = new Chat(ChatoUtils.getUserLogin(getContext()).getUser_id(), Integer.valueOf(chatRoomUiModel.getUser_id()), edt_message.getText().toString(), Chat.chat_type_file, file_attachment,  StringConstant.chat_status_sending, ""+chatList.size(), fileModel, chatRoomUiModel.getRoom_id(), chatRoomUiModel.getRoom_code());
                onShowAttachment(null, TYPE_IMAGE, "");
                ch.setUri_attachment(fileModel.getUri().toString());
//                IOUtils.savefile(fileModel.getUri(), Chat.chat_type_file);
                break;
            case VIDEO_FILE:
                ch = new Chat(ChatoUtils.getUserLogin(getContext()).getUser_id(), Integer.valueOf(chatRoomUiModel.getUser_id()), edt_message.getText().toString(), Chat.chat_type_video, file_attachment,  StringConstant.chat_status_sending, ""+chatList.size(), fileModel, chatRoomUiModel.getRoom_id(), GGFWUtil.convertToBase64(thumb_file_attachment), duration_file_attachment, chatRoomUiModel.getRoom_code());
                ch.setVideoDownloding(true);
                ch.setUri_attachment(fileModel.getUri().toString());
                ch.setMessage_attachment_name(fileModel.getNamefile());
                onShowAttachment(null, TYPE_IMAGE, "");
//                IOUtils.savefile(fileModel.getUri(), Chat.chat_type_video);
                break;
            case AUDIO_FILE:
                fileModel = audioFileToFileModel(nowAudioRecordPath);
                ch = new Chat(ChatoUtils.getUserLogin(getContext()).getUser_id(), Integer.valueOf(chatRoomUiModel.getUser_id()), "", Chat.chat_type_audio, "", StringConstant.chat_status_sending, ""+chatList.size(), chatRoomUiModel.getRoom_id(), chatRoomUiModel.getRoom_code());
                ch.setFileModel(fileModel);
                ch.setMessage_attachment(nowAudioRecordUrl);
                ch.setUri_attachment(fileModel.getUri().toString());
                ch.setMessage_file_duration(getAudioDuration(nowAudioRecordUrl));

                resetAudioData();
                break;
        }
        if(ch!=null && is_reply){
            ch.setReplyComponent(chat_reply);
        }

        if (edt_message.getText().toString().contains("@")){
            assert ch != null;
            ch.setMentionId(getIdMentioned(edt_message.getText().toString()));
        }else {
            assert ch != null;
            ch.setMentionId(new ArrayList<>());
        }
        is_reply = false;
        sendMessage(ch);
        if(lay_menu_attach.getVisibility()==View.VISIBLE){
            lay_menu_attach.setVisibility(View.GONE);
        }
    }

    private int getAudioDuration(String url){
        MediaPlayer mediaPlayer = new MediaPlayer();
        int result = 0;

        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        try {
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepare();

            result = mediaPlayer.getDuration();

            mediaPlayer.reset();
            mediaPlayer.release();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    private void resetAudioData(){
        nowAudioRecordPath = "";
        nowAudioRecordName = "";
        nowAudioRecordUrl = "";

        changeUiToChat();
    }

    private FileModel audioFileToFileModel(String absolutePath){
        return new FileModel(
                Uri.fromFile(new File(absolutePath)).toString(),
                FilePath.getMimeType(this, Uri.fromFile(new File(absolutePath))),
                FilePath.getFileName(getContext(), Uri.fromFile(new File(absolutePath)))
        );
    }

    private List<Integer> getIdMentioned(String msg){
        String[] bufferMsg = msg.split(" ");
        List<String> bufferName = new ArrayList<>();
        List<Integer> bufferMention = new ArrayList<>();
        String bufferString;

        //untuk cleaning mention agar dapat list nama user
        for (String s : bufferMsg) {
            if (s.contains("@")) {
                bufferString = s;
                bufferString = bufferString.replace("@", "");

                if (bufferString.length() != 0) {
                    Log.e(TAG, "cleaning... "+ bufferString.replace("_", " "));
                    bufferName.add(bufferString.replace("_", " "));
                }
            }
        }

        //untuk mencari id user yang mentioned
        if (bufferName.size()>0) {
            for (int x = 0; x < bufferName.size(); x++) {
                for (int y = 0; y < mentionList.size(); y++) {
                    if (mentionList.get(y).getName().equals(bufferName.get(x))) {
                        Log.e(TAG, "adding... "+ mentionList.get(y).getId());
                        bufferMention.add(mentionList.get(y).getId());
                    }
                }

            }
        }

        return bufferMention;  //return list id mentioned user
    }

    private void sendMessage(Chat ch){

        presenter.sendMessage(ch);

        hideContainerReply();
        updateDataChat(ch);
    }

    private void updateDataChat(Chat ch){
        chatList.add(ch);
        adapter.notifyDataSetChanged();
        rv.scrollToPosition(chatList.size()-1);
        sterilizeChat();
        edt_message.setText("");
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(receiver, filter);
        GGFWUtil.setStringToSP(getContext(), Preferences.CHATROOM_STATE, StringConstant.chatroom_state_open);

        if(chatList.size() > 0){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Log.d(TAG, "run: "+chatRoomUiModel.getRoom_id());
                    List<NotifChat> unreadNotif = chatNotifDatabase.getNotifbyRoomid(chatRoomUiModel.getRoom_id());
                    Log.d(TAG, "run: "+unreadNotif.size());
                    if(unreadNotif.size() > 0){
                        sendBroadcast(new Intent(StringConstant.chatroom_state_close_notif).putExtra("data", chatRoomUiModel.getUser_id()));
                        List<Chat> unreadChat = new ArrayList<>();
                        for(int i = 0; i < unreadNotif.size(); i++){
                            NotifChat notifChat = unreadNotif.get(i);
                            notifChat.setMessage_status(StringConstant.chat_status_read);
                            unreadChat.add(notifChat);
                            unreadNotif.get(i).setMessage_status(StringConstant.chat_status_delivered);
                            chatList.add(unreadNotif.get(i));
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                willShowUnreadNum(unreadChat);
                                adapter.notifyDataSetChanged();
                                presenter.sendStatusMessage(unreadChat);
//                                setStatusOnList(StringConstant.chat_status_read);
                            }
                        });
                        chatNotifDatabase.deleteNotifbyRoomId(chatRoomUiModel.getRoom_id());
                    }
                }
            }).start();
        } else {
            Log.d(TAG, "room id: " + chatRoomUiModel.getRoom_id());
            chatNotifDatabase.deleteNotifbyRoomId(chatRoomUiModel.getRoom_id());
        }

        if(isRoomAGroup(chatRoomUiModel)){
            presenter.getUpdatedGroupInfo(chatRoomUiModel);
        }

        if(GGFWUtil.getStringFromSP(getContext(), Preferences.CHATROOM_ID_FROM_NOTIF).equals(chatRoomUiModel.getUser_id())){
            requestHistory(chatRoomUiModel.getUser_id(), ""+ ChatoUtils.getUserLogin(getContext()).getUser_id(), true);
            sendBroadcast(new Intent(StringConstant.chatroom_state_close_notif).putExtra("data", chatRoomUiModel.getUser_id()));
        }

        sendBroadcast(new Intent(StringConstant.chatroom_state_open_to_room).putExtra("data", new PublishToRoom(chatRoomUiModel.getRoom_id(), chatRoomUiModel.getRoom_code())));
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: here i'm");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: here i'm");

        /*if(GGFWUtil.getStringFromSP(getContext(), Preferences.OPENED_CHATROOM_ID).equals(chatRoomUiModel.getRoom_id())){
            GGFWUtil.setStringToSP(getContext(), Preferences.CHATROOM_STATE, StringConstant.chatroom_state_close);
        }*/
        GGFWUtil.setStringToSP(getContext(), Preferences.CHATROOM_STATE, StringConstant.chatroom_state_close);
        try {
            unregisterReceiver(receiver);
        } catch(IllegalArgumentException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: here i'm");
        /*if(GGFWUtil.getStringFromSP(getContext(), Preferences.OPENED_CHATROOM_ID).equals(chatRoomUiModel.getRoom_id())){
            GGFWUtil.setStringToSP(getContext(), Preferences.CHATROOM_STATE, StringConstant.chatroom_state_close);
        }*/
        if (mediaPlayer.isPlaying()){
            mediaPlayer.stop();
        }

        GGFWUtil.setStringToSP(getContext(), Preferences.CHATROOM_STATE, StringConstant.chatroom_state_close);
    }

    @Override
    public void onSuccessDownloadFileToForward(Chat chat, KontakModel kontakModel) {
        forwardChat(kontakModel, chat);
    }

    @Override
    public void onSearchChat(List<Chat> chats) {
        endlessRecyclerViewScrollListener.resetState();
        adapter.setKeyword(edt_search.getText().toString());
        chatList.clear();
        for (Chat c: chats){
            chatList.add(c);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onSuccessReaction(ChatReactionResponse response) {
        Log.d("check id user", "login user = "+ChatoUtils.getUserLogin(getContext()).getUser_id()
                + ", react user ="+ response.getUser_id());

        if (ChatoUtils.getUserLogin(getContext()).getUser_id() == Integer.parseInt(response.getUser_id())) {
            for (int x = 0; x < chatList.size(); x++) {
                Log.d("check id chat", "item id = "+chatList.get(x).getMessage_id()
                        + ", react message ="+ response.getMessage_id());

                if (chatList.get(x).getMessage_id() == Integer.parseInt(response.getMessage_id())) {
                    List<ChatReactionModel> chat = new ArrayList<>();
                    if (chatList.get(x).getReactionList()!=null) {
                        Log.d("check size reaction", " " + chatList.get(x).getReactionList().size());
                        chat = chatList.get(x).getReactionList();
                    }

                    ChatReactionModel newModel = new Gson().fromJson(response.getReaction(), ChatReactionModel.class);

                    chat.add(newModel);
                    chatList.get(x).setReactionList(chat);

                    adapter.notifyDataSetChanged();
                    Log.d("check size reaction", " "+chatList.get(x).getReactionList().size());
                }
            }
        }

        sterilizeChat();
    }

    @Override
    public void openFile(String uri) {
        try {
            IOUtils.openFile(getContext(), new File(uri.replace("file:/", "")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onGetMessageInfo(Chat chat, String time_delivered, String time_read) {
        new DialogChatInfo(getContext(), chat, time_delivered, time_read);
    }

    @Override
    public void onGetGroupMessageInfo(Chat chat, List<KontakModel> terkirim, List<KontakModel> terbaca) {
        new GroupChatInfoDialog(getContext(), chat, terkirim, terbaca);
    }

    @Override
    public void onCheckStatusRoom(String message) {
        if(chatRoomUiModel.getType().equals(RoomChat.official_room_type)){
            setStatusBarRoom("Official Group");
        } else {
            if (message.equals("Online"))
                setStatusBarRoom(message);
            else {
                setStatusBarRoom(convertTime(message));
            }
        }
    }

    @Override
    public void onUpdateGroupInfo(Group group) {
        super.grouproom = group;
        chatRoomUiModel.setAvatar(grouproom.getRoom_photo_url());
        chatRoomUiModel.setTitle(grouproom.getRoom_name());
        txt_title.setText(group.getRoom_name());

        if(!group.getRoom_photo_url().equals("")){
            PicassoLoader imageLoader = new PicassoLoader();
            AvatarPlaceholder refreshableAvatarPlaceholder = new AvatarPlaceholder(chatRoomUiModel.getTitle());
            imageLoader.loadImage(img_profile, refreshableAvatarPlaceholder, (group.getRoom_photo_url().equals("") ? "google.com" : group.getRoom_photo_url()));
        }

        if(chatRoomUiModel.getType().equals(RoomChat.official_room_type)){
            setStatusBarRoom("Official Group");
            lay_no_action_chat.setVisibility(View.VISIBLE);
            lay_action_chat.setVisibility(View.GONE);
            lay_action_mic_or_send.setVisibility(View.GONE);
//            tv_no_action_chat.setText("Hanya admin yang dapat mengirimkan pesan");
            if (group.getIs_admin() == 0) {
                lay_no_action_chat.setVisibility(View.VISIBLE);
                lay_action_chat.setVisibility(View.GONE);
                lay_action_mic_or_send.setVisibility(View.GONE);
                tv_no_action_chat.setText("Hanya admin yang dapat mengirimkan pesan");
            } else {
                lay_action_chat.setVisibility(View.VISIBLE);
                lay_action_mic_or_send.setVisibility(View.VISIBLE);
            }
        }  else {
            if (group.getIs_exit() == 1) {
                lay_no_action_chat.setVisibility(View.VISIBLE);
                lay_action_chat.setVisibility(View.GONE);
                lay_detail_room.setClickable(false);
                isJoin = false;
                invalidateOptionsMenu();
                lay_action_mic_or_send.setVisibility(View.GONE);
                tv_no_action_chat.setText("Anda tidak tergabung dalam grup " + chatRoomUiModel.getTitle());
            } else {
                if (group.getRoom_group_type().equals("OPEN")) {
                    setStatusBarRoom("");
                    lay_action_chat.setVisibility(View.VISIBLE);
                    lay_action_mic_or_send.setVisibility(View.VISIBLE);
                } else {
                    setStatusBarRoom("Broadcast Room");
                    if (group.getIs_admin() == 0) {
                        lay_no_action_chat.setVisibility(View.VISIBLE);
                        lay_action_chat.setVisibility(View.GONE);
                        lay_action_mic_or_send.setVisibility(View.GONE);
                        tv_no_action_chat.setText("Hanya admin yang dapat mengirimkan pesan");
                    } else {
                        lay_action_chat.setVisibility(View.VISIBLE);
                        lay_action_mic_or_send.setVisibility(View.VISIBLE);
                    }
                }
            }
        }

        initPinnedMessageGroup(grouproom);

    }

    @Override
    public void onPinMessageGroup(int is_pinned, Chat chat) {
        grouproom.setIs_pinned_message(is_pinned);
        grouproom.setPinned_message(chat);
        initPinnedMessageGroup(grouproom);
    }

    @Override
    public void onLoadingChat() {
        if(chatList.size() == 0){
            pb.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onHideLoadingChat() {
        if(chatList.size() == 0){
            pb.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {

        if(appbar_action.getVisibility() == View.VISIBLE){
            onActionBarBack();
        } else {
            if (lay_menu_attach.getVisibility() == View.VISIBLE) {
                lay_menu_attach.setVisibility(View.GONE);
            } else {
                super.onBackPressed();
            }
        }
    }

    private void registerReceiver(){
        filter = new IntentFilter();
        filter.addAction(StringConstant.broadcast_receive_chat);
        filter.addAction(StringConstant.broadcast_receive_status_chat);
        filter.addAction(StringConstant.broadcast_get_update_group_info);
        filter.addAction(StringConstant.broadcast_receive_chat_reaction);
        filter.addAction(broadcast_listen_to_room);
    }

    private void sterilizeChat(){
        for (int i = 0; i < chatList.size(); i++) {
            chatList.get(i).setClicked(false);
        }
        adapter.notifiyListChanged();
        presenter.checkSelectedChat(chatList);
        if(!edt_search.getText().toString().equals("")){
            appbar_action.show();
            isAppBarShow = true;
            toolbar.setVisibility(View.GONE);
            appbar_action.displaying(lay_searchbar);
        }
    }

    private void prepareRecordAudio(){
        askCompactPermissions(AudioRequiredPermissions, new PermissionResultInterface() {
            @Override
            public void permissionGranted() {
                changeUiToRecord();
                recordAudio();
            }

            @Override
            public void permissionDenied() {

            }
        });
    }

    private void prepareToForward(){
        askCompactPermissions(FileRequiredPermission, new PermissionResultInterface() {
            @Override
            public void permissionGranted() {
                if(kontakChatDialog == null){
                    kontakChatDialog = new KontakChatDialog(getContext(), false, true, new KontakChatDialog.OnKontakChatDialog() {
                        @Override
                        public void onClickKontak(KontakModel model) {
                            Chat chat = ChatroomHelper.getSelectedOneChat(chatList);
                            chat.setForwardComponent(model.getUser_id(), ChatoUtils.getUserLogin(getContext()).getUser_id());
                            if(GGFWUtil.isValidURL(chat.getMessage_attachment())){
                                if(!chat.getMessage_type().equals(Chat.chat_type_message)){
                                    presenter.downloadFileFromChatToForward(chat, model);
                                } else {
                                    forwardChat(model, chat);
                                }
                            } else {
                                forwardChat(model, chat);
                            }
                        }

                        @Override
                        public void onAddGroup() {

                        }
                    });
                } else {
                    kontakChatDialog.show();
                }
            }

            @Override
            public void permissionDenied() {

            }
        });
    }

    private void forwardChat(KontakModel model, Chat chat){
        Intent intent = new Intent(getContext(), ChatRoomActivity.class);
        if(model.getRoom_type().equals(RoomChat.user_room_type)) {
            chatRoomUiModel.setId("" + model.getUser_id());
            chatRoomUiModel.setRoom_id("" + model.getRoom_id());
            chatRoomUiModel.setTitle("" + model.getUser_name());
        } else {
            chatRoomUiModel.setId("" + model.getRoom_id());
            chatRoomUiModel.setRoom_id("" + model.getRoom_id());
            chatRoomUiModel.setTitle("" + model.getGroup_name());

        }
        chatRoomUiModel.setAvatar("" + model.getUser_photo());
        chatRoomUiModel.setType(model.getRoom_type());
        intent.putExtra("forward", chatRoomUiModel);
        intent.putExtra("forward_data", chat);
        sterilizeChat();
        finish();
        startActivity(intent);
    }

    private void requestHistory(String id, String from_idchat, boolean isRefresh){
        GGFWUtil.setStringToSP(getContext(), Preferences.CHATROOM_ID_FROM_NOTIF, "");
        presenter.requestHistoryChat(id, from_idchat, ""+chatRoomUiModel.getRoom_code(), isRefresh);
    }

    private void requestGroupDetail(String roomId, Boolean isLoadingShow){
        presenterGroupInfo.requestInfoGroup(roomId, isLoadingShow);
    }

    @Override
    public void onClick(View view) {
        setViewOnClickEvent(view);
    }

    @Override
    public void onRequestInfoGroup(Group group, boolean isRefresh) {
        List<KontakModel> kontak = group.getList_user();
        for (int x= 0; x < kontak.size(); x++){
            MentionModel itemKontak = new MentionModel();
            itemKontak.setId(kontak.get(x).getUser_id());
            itemKontak.setName(kontak.get(x).getUser_name());
            itemKontak.setPictureUrl(kontak.get(x).getUser_photo());
            mentionList.add(itemKontak);
        }
        adapterMention.notifyDataSetChanged();
    }

    @Override
    public void onFailedRequestData(String message) {
        Log.e(TAG, message);
    }
}
