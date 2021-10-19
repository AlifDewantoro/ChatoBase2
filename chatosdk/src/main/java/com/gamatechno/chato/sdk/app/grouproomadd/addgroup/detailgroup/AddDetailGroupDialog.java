package com.gamatechno.chato.sdk.app.grouproomadd.addgroup.detailgroup;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gamatechno.chato.sdk.R;
import com.gamatechno.chato.sdk.app.chatroom.ChatRoomActivity;
import com.gamatechno.chato.sdk.app.chatrooms.uimodel.ChatRoomsUiModel;
import com.gamatechno.chato.sdk.app.kontakchat.KontakAdapter;
import com.gamatechno.chato.sdk.app.kontakchat.KontakModel;
import com.gamatechno.chato.sdk.data.DAO.RoomChat.RoomChat;
import com.gamatechno.chato.sdk.data.constant.Preferences;
import com.gamatechno.chato.sdk.data.model.UserModel;
import com.gamatechno.chato.sdk.utils.ImageUploader;
import com.gamatechno.chato.sdk.utils.Loading;
import com.gamatechno.ggfw.utils.AlertDialogBuilder;
import com.gamatechno.ggfw.utils.DialogBuilder;
import com.gamatechno.ggfw.utils.GGFWUtil;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.gson.Gson;

import java.util.List;

public class AddDetailGroupDialog extends DialogBuilder implements AddDetailGroupView.View {


    RelativeLayout lay_dialog;

    ImageView img_back;

    FloatingActionButton fab_ok;

    TextView tv_title;

    RecyclerView rv_member;

    CardView card_image;

    ImageView img_preview;

    EditText edt_name;

    TextView tv_total_member;

    SwitchMaterial switch_broadcast;

    List<KontakModel> kontakModels;
    KontakAdapter kontakAdapter;
    Boolean isBitmapFilled = true;
    Boolean isTextFilled = false;

    OnAddDetailGroup onAddDetailGroup;

    AddDetailGroupPresenter presenter;

    AddDetailGroupUiModel addDetailGroupUiModel;

    Loading loading;

    public AddDetailGroupDialog(Context context, List<KontakModel> kontakModels, OnAddDetailGroup onAddDetailGroup) {
        super(context, R.layout.dialog_add_detail_group);
        initView(getDialog());
        this.kontakModels = kontakModels;
        this.onAddDetailGroup = onAddDetailGroup;
        addDetailGroupUiModel = new AddDetailGroupUiModel();
        addDetailGroupUiModel.setKontakModelList(kontakModels);

        presenter = new AddDetailGroupPresenter(getContext(), this);
        loading = new Loading(getContext());

        setFullWidth(lay_dialog);
        setAnimation(R.style.DialogAnimationRight);
        setGravity(Gravity.BOTTOM);

        initKontakAdapter();
        initActions();

        tv_total_member.setText("Jumlah Member : "+kontakModels.size());
        isDataComplete();
        show();
    }

    private void initKontakAdapter(){
        UserModel model = new Gson().fromJson(GGFWUtil.getStringFromSP(context, Preferences.USER_LOGIN), UserModel.class);
        kontakAdapter = new KontakAdapter(getContext(), model, kontakModels, true, new KontakAdapter.OnKontakAdapter() {
            @Override
            public void onKontakClick(KontakModel kontakModel, int position) {

            }

            @Override
            public void onKontakLongClick(KontakModel kontakModel, int position) {

            }

            @Override
            public void onMakeGroup() {

            }
        });

        rv_member.setLayoutManager(new LinearLayoutManager(getContext()));
        rv_member.setAdapter(kontakAdapter);
    }

    private void initActions(){
        edt_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                isTextFilled = s.length()>0;
                isDataComplete();
            }
        });
        card_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onAddDetailGroup.onImageClick();
            }
        });

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialogBuilder(getContext(), "Apakah Anda yakin ingin kembali?", new AlertDialogBuilder.OnAlertDialog() {
                    @Override
                    public void onPositiveButton(DialogInterface dialog) {
                        dismiss();
                        onAddDetailGroup.onCancelAddDetailGroup();
                    }

                    @Override
                    public void onNegativeButton(DialogInterface dialog) {

                    }
                });
            }
        });

        fab_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isValidate()){
                    addDetailGroupUiModel.setTitle(edt_name.getText().toString());
                    addDetailGroupUiModel.setDeskripsi("");
                    addDetailGroupUiModel.setGroup_type(switch_broadcast.isChecked() ? "BROADCAST" : "OPEN");

                    if((""+addDetailGroupUiModel.getUri()).equals("null")){
                        presenter.requestAddGroup(addDetailGroupUiModel, "");
                    } else {
                        new ImageUploader(context, loading, addDetailGroupUiModel.getUri(), false, new ImageUploader.OnUploadImage() {
                            @Override
                            public void onSuccessUploadImage(String url) {
                                new AlertDialogBuilder(getContext(), "Apakah Anda yakin membuat grup "+addDetailGroupUiModel.getTitle()+"?", new AlertDialogBuilder.OnAlertDialog() {
                                    @Override
                                    public void onPositiveButton(DialogInterface dialog) {
                                        presenter.requestAddGroup(addDetailGroupUiModel, url);
                                    }

                                    @Override
                                    public void onNegativeButton(DialogInterface dialog) {

                                    }
                                });

                            }

                            @Override
                            public void onFailedUploadImage(String message) {
                                GGFWUtil.ToastShort(context, message);
                            }
                        });
                    }


                }
            }
        });
    }

    private Boolean isValidate(){
        /*if(edt_name.getText().toString().equalsIgnoreCase("") && !isBitmapFilled){
            GGFWUtil.ToastShort(getContext(), "Tambahkan nama dan foto grup terlebih dahulu");
            return false;
        }*/

        if(edt_name.getText().toString().equalsIgnoreCase("")){
            GGFWUtil.ToastShort(getContext(), "Tambahkan nama grup terlebih dahulu");
            return false;
        }

        /*if(!isBitmapFilled){
            GGFWUtil.ToastShort(getContext(), "Tambahkan foto grup terlebih dahulu");
            return false;
        }*/

        return true;
    }

    public void setImageGroup(Bitmap bitmap){
        isBitmapFilled = true;
        img_preview.setImageBitmap(bitmap);
        addDetailGroupUiModel.setBase64_image(GGFWUtil.convertToBase64(bitmap));
    }

    public void setImageGroup(Bitmap bitmap, Uri uri){
        isBitmapFilled = true;
        img_preview.setImageBitmap(bitmap);
        addDetailGroupUiModel.setBase64_image(GGFWUtil.convertToBase64(bitmap));
        addDetailGroupUiModel.setUri(uri);
        isDataComplete();
    }

    @Override
    public void onSuccessAddingGroup(RoomChat roomChat, String message) {
        ChatRoomsUiModel model = new ChatRoomsUiModel(roomChat);
        dismiss();
        Intent intent = new Intent(getContext(), ChatRoomActivity.class);
        intent.putExtra("chatroom", model);
        intent.putExtra("newgroup", true);
        getContext().startActivity(intent);
//        onAddDetailGroup.onSuccessAddGroup();
        GGFWUtil.ToastShort(getContext(), message);
    }

    private void isDataComplete(){
        if(isBitmapFilled&&isTextFilled){
            fab_ok.setColorFilter(ContextCompat.getColor(getContext(),R.color.white));
            fab_ok.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(),R.color.colorPrimaryDark)));
            fab_ok.setClickable(true);
        } else {
            fab_ok.setColorFilter(ContextCompat.getColor(getContext(),R.color.grey_700));
            fab_ok.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(),R.color.grey_500)));
            fab_ok.setClickable(true);
        }
    }

    @Override
    public void onFailedAddingGroup(String message) {
        GGFWUtil.ToastShort(getContext(), message);
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

    }

    @Override
    public void onAuthFailed(String error) {

    }

    public interface OnAddDetailGroup{
        void onImageClick();
        void onSuccessAddGroup();
        void onCancelAddDetailGroup();
    }

    private void initView(Dialog view){
        lay_dialog = view.findViewById(R.id.lay_dialog);
        img_back = view.findViewById(R.id.img_back);
        fab_ok = view.findViewById(R.id.fab_ok);
        tv_title = view.findViewById(R.id.tv_title);
        rv_member = view.findViewById(R.id.rv_member);
        card_image = view.findViewById(R.id.card_image);
        img_preview = view.findViewById(R.id.img_preview);
        edt_name = view.findViewById(R.id.edt_name);
        tv_total_member = view.findViewById(R.id.tv_total_member);
        switch_broadcast = view.findViewById(R.id.switch_broadcast);
    }
}
