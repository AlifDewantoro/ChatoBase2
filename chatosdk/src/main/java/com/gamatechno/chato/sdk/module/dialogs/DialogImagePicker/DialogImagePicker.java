package com.gamatechno.chato.sdk.module.dialogs.DialogImagePicker;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.gamatechno.chato.sdk.R;
import com.gamatechno.ggfw.utils.DialogBuilder;

public class DialogImagePicker extends DialogBuilder {


    RelativeLayout lay_dialogimagepicker;

    LinearLayout lay_camera;

    LinearLayout lay_folder;

    LinearLayout lay_video;

    OnDialogImagePicker onDialogImagePicker;

    public DialogImagePicker(Context context, OnDialogImagePicker dialogImagePicker) {
        super(context, R.layout.dialog_image_picker);
        initView( getDialog());
        initComponent(dialogImagePicker);

    }

    public DialogImagePicker(Context context, boolean isVideo, OnDialogImagePicker dialogImagePicker) {
        super(context, R.layout.dialog_image_picker);
        initView( getDialog());
        initComponent(dialogImagePicker);

        lay_video.setVisibility(View.VISIBLE);
        lay_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                onDialogImagePicker.onVideoCameraClick();
            }
        });
        if(!isVideo){
            lay_video.setVisibility(View.GONE);
        }
    }

    private void initComponent(OnDialogImagePicker dialogImagePicker){
        setFullWidth(lay_dialogimagepicker);
        setGravity(Gravity.BOTTOM);
        setAnimation(R.style.DialogBottomAnimation);

        onDialogImagePicker = dialogImagePicker;

        lay_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                onDialogImagePicker.onCameraClick();
            }
        });


        lay_folder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                onDialogImagePicker.onFileManagerClick();
            }
        });

        show();
    }

    public interface OnDialogImagePicker{
        void onCameraClick();
        void onFileManagerClick();
        void onVideoCameraClick();
    }

    private void initView(Dialog view){
        lay_dialogimagepicker = view.findViewById(R.id.lay_dialogimagepicker);
        lay_camera = view.findViewById(R.id.lay_camera);
        lay_folder = view.findViewById(R.id.lay_folder);
        lay_video = view.findViewById(R.id.lay_video);
    }
}
