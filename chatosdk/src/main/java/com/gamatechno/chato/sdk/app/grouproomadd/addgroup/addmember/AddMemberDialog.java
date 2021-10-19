package com.gamatechno.chato.sdk.app.grouproomadd.addgroup.addmember;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.gamatechno.chato.sdk.R;
import com.gamatechno.chato.sdk.app.chatrooms.uimodel.ChatRoomsUiModel;
import com.gamatechno.chato.sdk.app.grouproomadd.addgroup.adapter.MemberCheckedAdapter;
import com.gamatechno.chato.sdk.app.kontakchat.KontakAdapter;
import com.gamatechno.chato.sdk.app.kontakchat.KontakModel;
import com.gamatechno.chato.sdk.data.constant.Preferences;
import com.gamatechno.chato.sdk.data.model.UserModel;
import com.gamatechno.chato.sdk.utils.ChatoUtils;
import com.gamatechno.chato.sdk.utils.animation.AnimationToggle;
import com.gamatechno.ggfw.utils.AlertDialogBuilder;
import com.gamatechno.ggfw.utils.DialogBuilder;
import com.gamatechno.ggfw.utils.GGFWUtil;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class AddMemberDialog extends DialogBuilder implements AddMemberView.View {

    private AddMemberPresenter presenter;

    private ImageView img_back;

    private AnimationToggle toggle_kontak;

    private EditText edt_search;

    private RelativeLayout lay_toolbar;

    private ImageView img_search;

    private TextView tv_title;

    private TextView tv_subtitle;

    private LinearLayout lay_result;

    private RecyclerView rv_result;

    private RecyclerView rv;

    private RelativeLayout helper_nodata;

    private RelativeLayout helper_loading_more;

    private RelativeLayout helper_loading;

    private RelativeLayout lay_dialog;

    private SwipeRefreshLayout lay_refresh;

    private FloatingActionButton fab;

    private KontakAdapter kontakAdapter;
    private MemberCheckedAdapter memberCheckedAdapter;

    private List<KontakModel> kontakModels;
    private List<KontakModel> addedKontakModels;
    private List<ChatRoomsUiModel> addedContactException;

    private LinearLayoutManager layoutManager;
    boolean isSearch = false;
    boolean isLoadMore = true;

    private Timer timer = new Timer();
    private final long DELAY = 1000; // milliseconds

    OnAddMember onAddMember;
    Boolean isRequired = true;


    public AddMemberDialog(Context context, OnAddMember onAddMember) {
        super(context, R.layout.dialog_add_member);
        initView( getDialog());
        this.onAddMember = onAddMember;


        setFullWidth(lay_dialog);
        setAnimation(R.style.DialogAnimationRight);
        setGravity(Gravity.BOTTOM);

        toggle_kontak.display(lay_toolbar);
        presenter = new AddMemberPresenter(getContext(), this);

        initKontakAdapter();
        checkEmptinessResult();
        initActions();

        tv_title.setText("Tambah Anggota Grup");

        presenter.requestKontak(true, false);
        show();
    }

    public AddMemberDialog(Context context, String title, Boolean isRequired, OnAddMember onAddMember){
        super(context, R.layout.dialog_add_member);
        initView( getDialog());
        this.onAddMember = onAddMember;
        this.isRequired = isRequired;

        setFullWidth(lay_dialog);
        setAnimation(R.style.DialogAnimationRight);
        setGravity(Gravity.BOTTOM);

        toggle_kontak.display(lay_toolbar);
        presenter = new AddMemberPresenter(getContext(), this);

        initKontakAdapter();
        checkEmptinessResult();
        initActions();

        tv_title.setText(title);

        presenter.requestKontak(true, false);
        show();
    }

    public void setAddedList(List<ChatRoomsUiModel> roomChats){
        Log.d("AddMemberDialog", "setAddedList: "+roomChats.size());
        addedContactException.addAll(roomChats);
    }

    public void setTitle(String title){
        tv_title.setText(title);
    }

    public void setParticipant(List<KontakModel> list){
        addedKontakModels.addAll(list);
        memberCheckedAdapter.notifyDataSetChanged();
        lay_result.setVisibility(View.VISIBLE);
    }

    public void initActions(){
        lay_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                lay_refresh.setRefreshing(false);
                disableSearch(true);
//                presenter.requestKontak(true);
            }
        });


        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isSearch){
                    disableSearch(true);
                } else {
                    dismiss();
                }
            }
        });

        tv_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enableSearch();
            }
        });

        img_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enableSearch();
            }
        });

        edt_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    ChatoUtils.hideSoftKeyboard(getContext(), edt_search);
                    return true;
                }
                return false;
            }
        });

        edt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(isSearch){
                    timer.cancel();
                    timer = new Timer();
                    timer.schedule(
                            new TimerTask() {
                                @Override
                                public void run() {
                                    if(charSequence.toString().equals("")){
                                        presenter.requestKontak(true, false);
                                    } else {
                                        presenter.searchUser(charSequence.toString(), true);
                                    }
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

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isRequired){
                    if(addedKontakModels.size() > 0){
                        new AlertDialogBuilder(getContext(), "Apakah Anda yakin ingin melanjutkan?", new AlertDialogBuilder.OnAlertDialog() {
                            @Override
                            public void onPositiveButton(DialogInterface dialog) {
                                onAddMember.onAfterAddingMember(getDialog(), addedKontakModels);
                                dismiss();
                            }

                            @Override
                            public void onNegativeButton(DialogInterface dialog) {

                            }
                        });
                    } else {
                        GGFWUtil.ToastShort(getContext(), "Tambahkan minimal satu member terlebih dahulu");
                    }
                } else {
                    new AlertDialogBuilder(getContext(), "Apakah Anda yakin ingin melanjutkan?", new AlertDialogBuilder.OnAlertDialog() {
                        @Override
                        public void onPositiveButton(DialogInterface dialog) {
                            onAddMember.onAfterAddingMember(getDialog(), addedKontakModels);
                            dismiss();
                        }

                        @Override
                        public void onNegativeButton(DialogInterface dialog) {

                        }
                    });
                }
            }
        });
    }

    private void initKontakAdapter(){
        kontakModels = new ArrayList<>();
        addedKontakModels = new ArrayList<>();
        addedContactException = new ArrayList<>();
        UserModel model = new Gson().fromJson(GGFWUtil.getStringFromSP(context, Preferences.USER_LOGIN), UserModel.class);
        kontakAdapter = new KontakAdapter(getContext(), model, kontakModels, true, new KontakAdapter.OnKontakAdapter() {
            @Override
            public void onKontakClick(KontakModel kontakModel, int position) {
                addedKontakModels.add(kontakModel);
                kontakModels.remove(position);
                kontakAdapter.notifyDataSetChanged();
                memberCheckedAdapter.notifyDataSetChanged();
                rv_result.smoothScrollToPosition(addedKontakModels.size()-1);

//                presenter.requestKontak(true, false);
                edt_search.setText("");

                checkEmptinessResult();
            }

            @Override
            public void onKontakLongClick(KontakModel kontakModel, int position) {

            }

            @Override
            public void onMakeGroup() {

            }
        });

        memberCheckedAdapter = new MemberCheckedAdapter(getContext(), addedKontakModels, new MemberCheckedAdapter.OnKontakMiniAdapter() {
            @Override
            public void onKontakClick(KontakModel kontakModel, int position) {
                kontakModels.add(0, kontakModel);
                addedKontakModels.remove(position);
                kontakAdapter.notifyDataSetChanged();
                memberCheckedAdapter.notifyDataSetChanged();

                checkEmptinessResult();
            }
        });

        layoutManager = new LinearLayoutManager(getContext());
        rv.setLayoutManager(layoutManager);
        rv.setAdapter(kontakAdapter);
        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, final int dy) {
                if (dy > 0) //check for scroll down
                {
                    int visibleItemCount = layoutManager.getChildCount();
                    int totalItemCount = layoutManager.getItemCount();
                    int pastVisiblesItems = layoutManager.findFirstVisibleItemPosition();

                    if (isLoadMore) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            isLoadMore = false;
                            System.out.println("Last Item Bro " + totalItemCount);
                            if(edt_search.getText().toString().equals("")){
                                presenter.requestKontak(false, false);
                            } else {
                                presenter.searchUser(edt_search.getText().toString(), false);
                            }
                        }
                    }
                }
            }
        });

        rv_result.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rv_result.setAdapter(memberCheckedAdapter);
    }

    private void checkEmptinessResult(){
        if(addedKontakModels.size() == 0){
            lay_result.setVisibility(View.GONE);
            tv_subtitle.setText("Belum ada member dipilih");
        } else {
            lay_result.setVisibility(View.VISIBLE);
            tv_subtitle.setText(""+addedKontakModels.size()+" Member telah ditambahkan");
        }
    }

    private void addAllData(List<KontakModel> models){
        for (KontakModel model : models){
            boolean isOkToAdd = true;
            for (KontakModel m : addedKontakModels){
                if(model.getUser_id() == m.getUser_id()){
                    isOkToAdd = false;
                }
            }
            for (ChatRoomsUiModel m : addedContactException){
                if(model.getRoom_id() == m.getRoomChat().getRoom_id()){
                    isOkToAdd = false;
                }
            }
            if(isOkToAdd){
                kontakModels.add(model);
            }
        }
    }

    public interface OnAddMember{
        void onAfterAddingMember(Dialog dialog, List<KontakModel> list);
    }



    @Override
    public void onRequestKontak(List<KontakModel> models, List<KontakModel> groupmodels, boolean isRefresh) {
        isLoadMore = true;
        if(isRefresh){
            kontakModels.clear();
        }

        addAllData(models);
        kontakAdapter.notifyDataSetChanged();

        if(kontakModels.size() == 0){
            helper_nodata.setVisibility(View.VISIBLE);
        } else{
            helper_nodata.setVisibility(View.GONE);
        }
    }

    @Override
    public void onCreateRoomId(KontakModel model) {

    }

    @Override
    public void onFailedRequestKontak(boolean isRefresh) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(isRefresh){
                    if(isSearch){
                        kontakModels.clear();
                        helper_nodata.setVisibility(View.VISIBLE);
                    } else {
                        if(kontakModels.size() == 0){
                            helper_nodata.setVisibility(View.VISIBLE);
                        }
                    }
                }

            }
        });
    }

    @Override
    public void onLoadMore() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                helper_loading_more.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onLoading() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                helper_nodata.setVisibility(View.GONE);
                helper_loading.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onHideLoading() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                helper_loading.setVisibility(View.GONE);
                helper_loading_more.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onErrorConnection(String message) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(kontakModels.size()==0)
                    helper_nodata.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onAuthFailed(String error) {

    }

    private void enableSearch(){
        isSearch = true;
        toggle_kontak.display(edt_search);
        ChatoUtils.showKeyboard(getContext(), edt_search);
    }

    private void disableSearch(boolean isNeedToRefresh){
        isSearch = false;
        toggle_kontak.display(lay_toolbar);

        if(!edt_search.getText().toString().equals("")){
            edt_search.setText("");
        }
        ChatoUtils.hideSoftKeyboard(getContext(), edt_search);

        if(isNeedToRefresh)
            presenter.requestKontak(true, false);
    }

    private void initView(Dialog view){
        img_back = view.findViewById(R.id.img_back);
        toggle_kontak = view.findViewById(R.id.toggle_kontak);
        edt_search = view.findViewById(R.id.edt_search);
        lay_toolbar = view.findViewById(R.id.lay_toolbar);
        img_search = view.findViewById(R.id.img_search);
        tv_title = view.findViewById(R.id.tv_title);
        tv_subtitle = view.findViewById(R.id.tv_subtitle);
        lay_refresh = view.findViewById(R.id.lay_refresh);
        lay_result = view.findViewById(R.id.lay_result);
        rv_result = view.findViewById(R.id.rv_result);
        rv = view.findViewById(R.id.rv);
        helper_nodata = view.findViewById(R.id.helper_nodata);
        helper_loading = view.findViewById(R.id.helper_loading);
        helper_loading_more = view.findViewById(R.id.helper_loading_more);
        lay_dialog = view.findViewById(R.id.lay_dialog);
        fab = view.findViewById(R.id.fab);

    }
}
