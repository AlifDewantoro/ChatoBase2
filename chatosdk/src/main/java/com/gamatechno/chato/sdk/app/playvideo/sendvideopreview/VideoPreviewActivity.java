package com.gamatechno.chato.sdk.app.playvideo.sendvideopreview;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.gamatechno.chato.sdk.R;
import com.gamatechno.chato.sdk.module.activity.ChatoCoreActivity;
import com.gamatechno.chato.sdk.utils.ChatoEditText.ChatEditText;
import com.gamatechno.chato.sdk.utils.ChatoToolbar;
import com.gamatechno.ggfw.videocompressv2.VideoCompress;

import java.io.File;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.chato.chato_emoticon.Actions.EmojIconActions;
import com.chato.chato_emoticon.Helper.EmojiconsPopup;

import static android.view.WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN;

public class VideoPreviewActivity extends AppCompatActivity {

    ChatoToolbar toolbar;

    ChatEditText edt_message;

    VideoView videoview;

    ImageView img_ok;

    ProgressBar pb_ok;

    ImageView img_emoticon;

    Uri afterUriVideo;
    EmojIconActions  emojIcon;
    String video_name = "";
    String compressedPath = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_video);

        initView();
        setupEmoji();

        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null) {
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");
        }

        if(getIntent().hasExtra("uri")){
            compressVideo(getIntent().getStringExtra("uri"));
        }
        img_ok.setOnClickListener( v -> {
            try {
                Log.d("VideoPreview","imgOk onClick");
//                File videoFile = EasyImageFiles.pickedExistingPicture(getContext(), Uri.fromFile(new File(String.valueOf(afterUriVideo))));
                Intent intent = new Intent();
                intent.putExtra("text",edt_message.getText().toString());
                intent.putExtra("videoName", video_name);
                intent.setData(Uri.fromFile(new File(String.valueOf(afterUriVideo))));
                VideoPreviewActivity.this.setResult(100,intent);
                VideoPreviewActivity.this.finish();
            } catch (Exception ex){

            }
        });
    }

    private void compressVideo(String uri){
        File f = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "Chato/Video/");
        f.mkdirs();
        video_name = "VIDEO_" + new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date()) + ".mp4";
        compressedPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                + "/Chato/Video/"+video_name;

        VideoCompress.compressVideoLow(uri, compressedPath, new VideoCompress.CompressListener() {
            @Override
            public void onStart() {
                //Start Compress
            }

            @Override
            public void onSuccess() {
                Uri fileUriAfterCompress = Uri.parse(compressedPath);
                pb_ok.setVisibility(View.GONE);
                img_ok.setVisibility(View.VISIBLE);
                afterUriVideo = fileUriAfterCompress;
//                videoview.setMediaController(new MediaController(getContext()));
//                MediaController mc = new MediaController(VideoPreviewActivity.this);
//                mc.setAnchorView(edt_message);
//                mc.setMediaPlayer(videoview);
//                videoview.setMediaController(mc);
                videoview.setVideoURI(fileUriAfterCompress);
                videoview.start();
            }

            @Override
            public void onFail() {
                //Failed
            }

            @Override
            public void onProgress(float percent) {
                //Progress
            }
        });
    }

    public void setupEmoji() {
        emojIcon = new EmojIconActions(this, this.getWindow().getDecorView().getRootView(), edt_message, img_emoticon,"#495C66","#DCE1E2","#E6EBEF");
        emojIcon.setIconsIds(R.drawable.ic_keyboard_black_24dp, R.drawable.ic_tag_faces_black_24dp);
        emojIcon.setUseSystemEmoji(true);
        edt_message.setUseSystemDefault(true);
        emojIcon.ShowEmojIcon();
        disableAutoOpenEmoji(emojIcon);
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

    private void initView(){
        toolbar = findViewById(R.id.toolbar);
        edt_message = findViewById(R.id.edt_message);
        videoview = findViewById(R.id.videoview);
        img_ok = findViewById(R.id.img_ok);
        pb_ok = findViewById(R.id.pb_ok);
        img_emoticon = findViewById(R.id.img_emoticon);
    }
}
