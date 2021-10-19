package com.gamatechno.chato.sdk.app.playvideo;

import android.net.Uri;
import android.os.Bundle;
import android.widget.VideoView;

import com.gamatechno.chato.sdk.R;

import com.gamatechno.chato.sdk.module.activity.ChatoCoreActivity;
import com.gamatechno.chato.sdk.utils.ChatoToolbar;

public class PlayVideoActivity extends ChatoCoreActivity {

    ChatoToolbar toolbar;
    VideoView videoview;

    Bundle data = new Bundle();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_video);
        initView();

        data = getIntent().getExtras();

        if(data == null)
            finish();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        Uri uriVideo = Uri.parse(data.getString("uri"));
        videoview.setMediaController(new android.widget.MediaController(getContext()));
        videoview.setVideoURI(uriVideo);
        videoview.start();
    }

    private void initView(){
        toolbar = findViewById(R.id.toolbar);
        videoview = findViewById(R.id.videoview);
    }
}
