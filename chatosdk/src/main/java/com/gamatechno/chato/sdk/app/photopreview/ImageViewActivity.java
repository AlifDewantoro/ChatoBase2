package com.gamatechno.chato.sdk.app.photopreview;

import android.Manifest;
import android.app.DownloadManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ProgressBar;

import com.gamatechno.chato.sdk.R;
import com.gamatechno.chato.sdk.module.activity.ChatoPermissionActivity;
import com.gamatechno.chato.sdk.module.core.ChatoBaseApplication;
import com.gamatechno.chato.sdk.utils.FilePath.IOUtils;
import com.gamatechno.chato.sdk.utils.ChatoToolbar;
import com.gamatechno.chato.sdk.utils.animation.AnimationToggle;
import com.gamatechno.chato.sdk.utils.downloader.Exception.GTDownloadException;
import com.gamatechno.chato.sdk.utils.downloader.GTDownloadCallback;
import com.gamatechno.chato.sdk.utils.downloader.GTDownloadManager;
import com.gamatechno.chato.sdk.utils.downloader.GTDownloadRequest;
import com.gamatechno.ggfw.Activity.Interfaces.PermissionResultInterface;
import com.gamatechno.ggfw.utils.GGFWUtil;
import com.gamatechno.ggfw_ui.ui.TouchImageView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class ImageViewActivity extends ChatoPermissionActivity {


    TouchImageView image;
    AnimationToggle lay_flat;
    ChatoToolbar toolbar;
    ProgressBar progress_bar;

    protected String[] RequiredPermissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};

    Bundle data = new Bundle();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);
        initView();

        data = getIntent().getExtras();

        if(data == null)
            finish();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setTitle(data.getString("title"));
        getSupportActionBar().setTitle(data.getString("title", "Detail"));
        getSupportActionBar().setSubtitle(data.getString("subtitle",null));
//        getSupportActionBar().setTitle("");

        String img = data.getString("image");

        if(!img.equals("")){
            if(data.getBoolean("isDownload",true)) {
                Uri file_uri = Uri.withAppendedPath(Uri.fromFile(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)), "Chato/Images/" + img.split("/")[img.split("/").length - 1]);
                Uri destination = Uri.withAppendedPath(Uri.fromFile(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)), "Chato/Images");

                if (!IOUtils.isFileExist(file_uri.toString().replace("file:/", ""))) {
                    askCompactPermissions(RequiredPermissions, new PermissionResultInterface() {
                        @Override
                        public void permissionGranted() {
                            try {
                                new GTDownloadManager(getContext(), new GTDownloadCallback() {
                                    @Override
                                    public void onProcess(GTDownloadRequest request) {

                                    }

                                    @Override
                                    public void onCancel(GTDownloadRequest request) {

                                    }

                                    @Override
                                    public void onSuccess(GTDownloadRequest request) {

                                    }
                                }).startRequest(new GTDownloadRequest(Uri.parse(img)).setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN).setDestinationUri(destination));
                            } catch (GTDownloadException e) {
                                e.printStackTrace();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void permissionDenied() {
                            GGFWUtil.ToastShort(getContext(), "Anda perlu memberikan akses terlebih dahulu");
                        }
                    });

                    Picasso.get()
                            .load(img)
                            .placeholder(ChatoBaseApplication.Companion.getInstance().getChatoPlaceholder())
                            .into(image, new Callback() {
                                @Override
                                public void onSuccess() {
                                    progress_bar.setVisibility(View.GONE);
                                }

                                @Override
                                public void onError(Exception e) {
                                    progress_bar.setVisibility(View.GONE);

                                }
                            });
                } else {
                    Picasso.get()
                            .load(file_uri)
                            .placeholder(ChatoBaseApplication.Companion.getInstance().getChatoPlaceholder())
                            .into(image, new Callback() {
                                @Override
                                public void onSuccess() {
                                    progress_bar.setVisibility(View.GONE);
                                }

                                @Override
                                public void onError(Exception e) {
                                    progress_bar.setVisibility(View.GONE);

                                }
                            });
                }
            } else {
                Picasso.get()
                        .load(img)
                        .placeholder(ChatoBaseApplication.Companion.getInstance().getChatoPlaceholder())
                        .into(image, new Callback() {
                            @Override
                            public void onSuccess() {
                                progress_bar.setVisibility(View.GONE);
                            }

                            @Override
                            public void onError(Exception e) {
                                progress_bar.setVisibility(View.GONE);

                            }
                        });
            }
        }


//        draggable_frame.addListener(new ElasticDragDismissListener() {
//            @Override
//            public void onDrag(float elasticOffset, float elasticOffsetPixels, float rawOffset, float rawOffsetPixels) {}
//
//            @Override
//            public void onDragDismissed() {
//                //if you are targeting 21+ you might want to finish after transition
//                finish();
//            }
//        });
    }

    private void initView(){
        image = findViewById(R.id.image);
        lay_flat = findViewById(R.id.lay_flat);
        toolbar = findViewById(R.id.toolbar);
        progress_bar = findViewById(R.id.progress_bar);
    }
}
