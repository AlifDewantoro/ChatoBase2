package com.gamatechno.chato.sdk.app.main;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.gamatechno.chato.sdk.R;
import com.gamatechno.chato.sdk.app.chatroom.ChatRoomActivity;
import com.gamatechno.chato.sdk.app.chatrooms.ChatRoomsFragment;
import com.gamatechno.chato.sdk.app.chatrooms.adapter.RoomAdapter;
import com.gamatechno.chato.sdk.app.chatrooms.uimodel.ChatRoomsUiModel;
import com.gamatechno.chato.sdk.app.chatrooms.viewmodel.ChatRoomsViewModel;
import com.gamatechno.chato.sdk.app.grouproomadd.addgroup.addmember.AddMemberDialog;
import com.gamatechno.chato.sdk.app.grouproomadd.addgroup.detailgroup.AddDetailGroupDialog;
import com.gamatechno.chato.sdk.app.kontakchat.KontakChatDialog;
import com.gamatechno.chato.sdk.app.kontakchat.KontakModel;
import com.gamatechno.chato.sdk.app.main.searchlist.AdapterSearchList;
import com.gamatechno.chato.sdk.app.main.searchlist.SearchChatroomModel;
import com.gamatechno.chato.sdk.data.constant.StringConstant;
import com.gamatechno.chato.sdk.module.dialogs.DialogImagePicker.DialogImagePicker;
import com.gamatechno.chato.sdk.utils.ChatoUtils;
import com.gamatechno.chato.sdk.utils.animation.AnimationToggle;
import com.gamatechno.ggfw.Activity.FragmentPermission;
import com.gamatechno.ggfw.Activity.Interfaces.PermissionResultInterface;
import com.gamatechno.ggfw.easyphotopicker.DefaultCallback;
import com.gamatechno.ggfw.easyphotopicker.EasyImage;
import com.gamatechno.ggfw.utils.AlertDialogBuilder;
import com.gamatechno.ggfw.utils.GGFWUtil;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static android.app.Activity.RESULT_OK;

public class ChatoFragment extends FragmentPermission implements ChatView.View, View.OnClickListener {
    final String TAG = "ChatFragment";


    TabLayout tablayout;

    ViewPager viewPager;

    Toolbar toolbar;

    FloatingActionButton fab_add;

    LinearLayout lay_top;

    LinearLayout lay_search;

    ImageView img_back_search;

    AnimationToggle wrapper_top;

    AnimationToggle toggle_fab;

    LinearLayout ll_search;

    EditText edt_search;

    ImageView img_clear_search;

    RelativeLayout lay_searchlist;

    RecyclerView rv_searchlist;

    RelativeLayout helper_loading;

    RelativeLayout helper_no_data;

    TextView tv_no_data;
    TextView toolbar_title;

    RelativeLayout lay_appbar;

    ImageView img_pin;

    ImageView img_label;

    ImageView img_delete_chat;

    TextView tv_action_title;

    KontakChatDialog kontakChatDialog;
    ChatPresenter presenter;
    MenuItem mSearch;
    SearchView mSearchView;
    boolean isObrolan = true;
    ChatRoomsViewModel chatRoomsViewModel;

    boolean isSearchh = false;

    boolean isPinnedRoom = true;

    List<SearchChatroomModel> list_searchlist = new ArrayList<>();
    AdapterSearchList adapterSearchList;

    private String[] RequiredPermissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    AddMemberDialog addMemberDialog;
    AddDetailGroupDialog addDetailGroupDialog;

    String imagepicker_code = "";

    private MainViewModel model = new MainViewModel();

    public static ChatoFragment newInstance() {
        ChatoFragment fragment = new ChatoFragment();
        return fragment;
    }

    private Timer timer = new Timer();
    private final long DELAY = 1000; // milliseconds

    private View rootview;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.activity_chato, container, false);
        return rootview;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
//        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("");

        initView(view);
        initSearchChatroom();

        presenter = new ChatPresenter(getContext(), this);
        wrapper_top.setInOutAnimation(R.anim.pull_in_bottom, R.anim.push_out_top);
        toggle_fab.setInOutAnimation(R.anim.pull_in_top, R.anim.push_out_bottom);
        chatRoomsViewModel = new ViewModelProvider(this).get(ChatRoomsViewModel.class);
//        toggle_fab.setInOutAnimation(R.anim.pull_in_top, R.anim.slide_out_bottom);

        chatRoomsViewModel.initBackPressed().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean isBackPressed) {
                if(isBackPressed != null && isBackPressed){
                    if(isSearchh){
                        chatRoomsViewModel.updateKeyword("");
                        showOrHideTopView(true, false);
                    } else {
                        chatRoomsViewModel.updateBackPressedUpdate(isSearchh);
                    }
                } else if(isBackPressed != null && !isBackPressed){
                    showOrHideTopView(true, false);
                }
            }
        });

        chatRoomsViewModel.initKeyword().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                if(s.equals("")){
                    edt_search.setText("");
//                    showOrHideTopView(true, true);
                    lay_searchlist.setVisibility(View.GONE);
                    list_searchlist.clear();
                    adapterSearchList.notifyDataSetChanged();
                } else {
                    lay_searchlist.setVisibility(View.VISIBLE);
                    presenter.searchChat(s);
                }
            }
        });

        chatRoomsViewModel.initChatRoomsLongPress().observe(getViewLifecycleOwner(), new Observer<ChatRoomsUiModel>() {
            @Override
            public void onChanged(@Nullable ChatRoomsUiModel chatRoomUiModel) {
                if(chatRoomUiModel != null){
                    if(chatRoomUiModel.getIs_checked()){
                        showOrHideTopView(false, false);
                        tv_action_title.setText(chatRoomUiModel.getRoomChat().getRoom_name());
                        ll_search.setVisibility(View.GONE);
                        lay_appbar.setVisibility(View.VISIBLE);
                        isPinnedRoom = true;
                        if(chatRoomUiModel.getRoomChat().getIs_pined() == 0){
                            img_pin.setImageResource(R.drawable.ic_pin_chat);
                        } else {
                            img_pin.setImageResource(R.drawable.ic_unpin_chat);
                        }
                    } else {
                        showOrHideTopView(true, false);
                    }
                }
            }
        });


        setTabAdapter();

        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initKontakDialog();
            }
        });

        edt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.toString().equals("")){
                    img_clear_search.setVisibility(View.VISIBLE);
                } else {
                    img_clear_search.setVisibility(View.GONE);
                }
                if(isSearchh){
                    timer.cancel();
                    timer = new Timer();
                    timer.schedule(
                            new TimerTask() {
                                @Override
                                public void run() {
                                    chatRoomsViewModel.updateKeyword(edt_search.getText().toString());
                                }
                            },
                            DELAY
                    );
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        img_back_search.setOnClickListener(this);
        img_pin.setOnClickListener(this);
        img_label.setOnClickListener(this);
        img_delete_chat.setOnClickListener(this);
        img_clear_search.setOnClickListener(this);
    }

    private void setTabAdapter() {
        viewPager.setAdapter(new Tab(getActivity().getSupportFragmentManager(), chatRoomsViewModel, model));
        viewPager.setOffscreenPageLimit(0);
        tablayout.setupWithViewPager(viewPager);
        tablayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tablayout.getTabAt(0).setText(getString(R.string.title_chat_obrolan));
//        tablayout.getTabAt(1).setText(getString(R.string.title_chat_grup));

        tablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                chatRoomsViewModel.updateScrollStatus(true);
                switch (tab.getPosition()){
                    case 0:
                        isObrolan = true;
                        break;
                    case 1:
                        isObrolan = false;
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    public void setViewOnClickEvent(View view) {
        Log.d(TAG, "setViewOnClickEvent: "+view.getId());
        int id = view.getId();
        if (id == R.id.img_back_search) {
            chatRoomsViewModel.updateKeyword("");
            showOrHideTopView(true, false);
        } else if (id == R.id.img_pin) {
            chatRoomsViewModel.updateRequestPin(isPinnedRoom);
            showOrHideTopView(true, false);
        } else if (id == R.id.img_delete_chat) {
            new AlertDialogBuilder(getContext(), "Apakah Anda yakin ingin menghapus obrolan ini?", "Ya", "Tidak", new AlertDialogBuilder.OnAlertDialog() {
                @Override
                public void onPositiveButton(DialogInterface dialog) {
                    chatRoomsViewModel.updateRequestDelete(isPinnedRoom);
                    showOrHideTopView(true, false);
                }

                @Override
                public void onNegativeButton(DialogInterface dialog) {

                }
            });
        } else if (id == R.id.img_label){
            chatRoomsViewModel.updateLabel(true);
        } else if (id == R.id.img_clear_search){
            if(edt_search.getText().toString().equals("")){
                showOrHideTopView(true, false);
            } else {
                edt_search.setText("");
            }
        }
    }

    private void initSearchChatroom(){
        adapterSearchList = new AdapterSearchList(getContext(), list_searchlist, new RoomAdapter.OnObrolanAdapter() {
            @Override
            public void onClickObrolan(ChatRoomsUiModel model) {
                chatRoomsViewModel.updateChatRoomsClickFromSearch(model);
            }

            @Override
            public void onLongClick(ChatRoomsUiModel model) {
                chatRoomsViewModel.updateChatRoomsLongPressFromSearch(model);
            }
        });
        rv_searchlist.setLayoutManager(new LinearLayoutManager(getContext()));
        rv_searchlist.setAdapter(adapterSearchList);

    }

    private void showOrHideTopView(boolean isSHow, boolean isSearch){
        isSearchh = isSearch;
        if(isSHow){
            wrapper_top.displaying(lay_top);
            wrapper_top.hide(lay_search);
            ChatoUtils.hideSoftKeyboard(getContext(), edt_search);
        } else {
            if(isSearch){
                ll_search.setVisibility(View.VISIBLE);
                lay_appbar.setVisibility(View.GONE);
                ChatoUtils.showKeyboard(getContext(), edt_search);
            } else {
                ll_search.setVisibility(View.GONE);
                lay_appbar.setVisibility(View.VISIBLE);
            }
            if(!wrapper_top.isDisplaying(lay_search)){
                wrapper_top.displaying(lay_search);
                wrapper_top.hide(lay_top);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.action_search) {
            showOrHideTopView(false, true);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onLoading() {
        helper_no_data.setVisibility(View.GONE);
        helper_loading.setVisibility(View.VISIBLE);
    }

    @Override
    public void onHideLoading() {
        helper_loading.setVisibility(View.GONE);
    }

    @Override
    public void onErrorConnection(String message) {

    }

    @Override
    public void onAuthFailed(String error) {

    }

    @Override
    public void onSearchChat(List<SearchChatroomModel> list) {
        list_searchlist.clear();
        if(list.size()>0) {
            helper_no_data.setVisibility(View.GONE);
            list_searchlist.addAll(list);
        } else {
            helper_no_data.setVisibility(View.VISIBLE);
            tv_no_data.setText("Ruang obrolan tidak ditemukan");
        }
        adapterSearchList.notifyDataSetChanged();
    }

    @Override
    public void onFailedRequestChat() {
        list_searchlist.clear();
        adapterSearchList.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        setViewOnClickEvent(view);
    }

    private class Tab extends FragmentPagerAdapter {
        ChatRoomsViewModel viewModel;
        MainViewModel model;

        public Tab(FragmentManager fm, ChatRoomsViewModel viewModel, MainViewModel model) {
            super(fm);
            this.viewModel = viewModel;
            this.model = model;
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            switch (position){
                case 0 :
                    fragment = ChatRoomsFragment.newInstance(false, viewModel, model);
                    break;
                case 1 :
                    fragment = ChatRoomsFragment.newInstance(true, viewModel, model);
                    break;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return 1;
        }
    }

    private void initSearch(){
        mSearch.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                return false;
            }
        });
        mSearchView = (SearchView) mSearch.getActionView();
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void initKontakDialog(){
        if(kontakChatDialog == null){
            kontakChatDialog = new KontakChatDialog(getContext(), true, false, new KontakChatDialog.OnKontakChatDialog() {
                @Override
                public void onClickKontak(KontakModel model) {
                    Intent intent = new Intent(getContext(), ChatRoomActivity.class);
                    intent.putExtra("data", model);
                    startActivity(intent);
                }

                @Override
                public void onAddGroup() {
                    addGroup();
                }
            });
        } else {
            kontakChatDialog.show();
        }
    }

    private void addGroup(){
        addMemberDialog = new AddMemberDialog(getContext(), new AddMemberDialog.OnAddMember() {
            @Override
            public void onAfterAddingMember(Dialog dialog, List<KontakModel> list) {
                addDetailGroupDialog = new AddDetailGroupDialog(getContext(), list, new AddDetailGroupDialog.OnAddDetailGroup() {
                    @Override
                    public void onImageClick() {
                        imagepicker_code = StringConstant.imagepicker_addgroup;
                        askCompactPermissions(RequiredPermissions, new PermissionResultInterface() {
                            @Override
                            public void permissionGranted() {
                                new DialogImagePicker(getContext(), new DialogImagePicker.OnDialogImagePicker() {
                                    @Override
                                    public void onCameraClick() {
                                        EasyImage.openCamera(ChatoFragment.this, 0);
                                    }

                                    @Override
                                    public void onFileManagerClick() {
                                        EasyImage.openGallery(ChatoFragment.this, 0);
                                    }

                                    @Override
                                    public void onVideoCameraClick() {

                                    }
                                });
                            }

                            @Override
                            public void permissionDenied() {
                                GGFWUtil.ToastShort(getContext(), "Anda perlu memberikan akses terlebih dahulu");
                            }
                        });
                    }

                    @Override
                    public void onSuccessAddGroup() {
                        addMemberDialog.dismiss();
                        chatRoomsViewModel.updateRefreshRoom(true);
                    }

                    @Override
                    public void onCancelAddDetailGroup() {
                        addMemberDialog.show();
                    }
                });
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_search, menu);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                if(imagepicker_code.equals(StringConstant.imagepicker_addgroup)){
                    addDetailGroupDialog.setImageGroup(GGFWUtil.getBitmapFromUri(getContext(), resultUri), resultUri);
                }

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                String error = result.getError().toString();
                GGFWUtil.ToastShort(getContext(), ""+error);
            }
        } else {
            EasyImage.handleActivityResult(requestCode, resultCode, data, getActivity(), new DefaultCallback() {
                @Override
                public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
                    e.printStackTrace();

                }

                @Override
                public void onImagesPicked(List<File> imageFiles, EasyImage.ImageSource source, int type) {
                    Log.d(TAG, "onImagesPicked: "+type);
                    startCropActivity(Uri.fromFile(imageFiles.get(0)));
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

    private void startCropActivity(Uri uri){
        CropImage.activity(uri)
                .setCropShape(CropImageView.CropShape.RECTANGLE)
                .start(getActivity());
    }

    private void initView(View view){
        toolbar = view.findViewById(R.id.toolbar);
        toolbar_title = view.findViewById(R.id.toolbar_title);
        tablayout = view.findViewById(R.id.tablayout);
        viewPager = view.findViewById(R.id.viewPager);
        fab_add = view.findViewById(R.id.fab_add);
        lay_top = view.findViewById(R.id.lay_top);
        lay_search = view.findViewById(R.id.lay_search);
        img_back_search = view.findViewById(R.id.img_back_search);
        wrapper_top = view.findViewById(R.id.wrapper_top);
        toggle_fab = view.findViewById(R.id.toggle_fab);
        ll_search = view.findViewById(R.id.ll_search);
        edt_search = view.findViewById(R.id.edt_search);
        lay_searchlist = view.findViewById(R.id.lay_searchlist);
        rv_searchlist = view.findViewById(R.id.rv_searchlist);
        helper_loading = view.findViewById(R.id.helper_loading);
        helper_no_data = view.findViewById(R.id.helper_nodata);
        tv_no_data = view.findViewById(R.id.tv_nodata);
        lay_appbar = view.findViewById(R.id.lay_appbar);
        img_pin = view.findViewById(R.id.img_pin);
        img_label = view.findViewById(R.id.img_label);
        img_delete_chat = view.findViewById(R.id.img_delete_chat);
        img_clear_search = view.findViewById(R.id.img_clear_search);
        tv_action_title = view.findViewById(R.id.tv_action_title);
    }
}
