package com.gamatechno.chato.sdk.app.playvideo.sendvideopreview;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.VideoView;

import com.gamatechno.chato.sdk.R;
import com.gamatechno.chato.sdk.app.playvideo.sendvideopreview.model.VideoPreview;
import com.gamatechno.chato.sdk.utils.FilePath.FilePath;
import com.gamatechno.chato.sdk.utils.ChatoEditText.ChatEditText;
import com.gamatechno.ggfw.easyphotopicker.EasyImageFiles;
import com.gamatechno.ggfw.utils.DialogBuilder;
import com.gamatechno.ggfw.videocompressv2.VideoCompress;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.chato.chato_emoticon.Actions.EmojIconActions;
import com.chato.chato_emoticon.Helper.EmojiconsPopup;

public class DialogVideoPreview extends DialogBuilder {

    ChatEditText edt_message;

    VideoView videoview;

    ImageView img_ok;

    ProgressBar pb_ok;

    ImageView img_emoticon;

    VideoPreview videoPreview;
    OnDialogVideoPreview onDialogVideoPreview;
    Uri afterUriVideo;
    EmojIconActions  emojIcon;
    String video_name = "";
    String compressedPath = "";

    public DialogVideoPreview(Context context, String uri, OnDialogVideoPreview onDialogVideoPreview) {
        super(context, R.layout.dialog_video_preview);
        initView(getDialog());

        this.onDialogVideoPreview = onDialogVideoPreview;

        img_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    File videoFile = EasyImageFiles.pickedExistingPicture(getContext(), Uri.fromFile(new File(String.valueOf(afterUriVideo))));
                    onDialogVideoPreview.onPrepareVideoToSend(new VideoPreview(getContext(), videoFile, edt_message.getText().toString(), video_name));
                    dismiss();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        setupEmoji();
        compressVideo(uri);
        show();
    }

    private void compressVideo(String uri){
        File f = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "Chato/Video/");
        f.mkdirs();
        video_name = "VIDEO_" + new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date()) + ".mp4";
        compressedPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                + "/Chato/Video/"+video_name;

        VideoCompress.VideoCompressTask task = VideoCompress.compressVideoLow(uri, compressedPath, new VideoCompress.CompressListener() {
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
                videoview.setMediaController(new MediaController(getContext()));
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

        /*new VideoCompressor(compressedPath, new onCompressVideo(){
            @Override
            public void onCompressSuccess(Uri uriVideo) {
                pb_ok.setVisibility(View.GONE);
                img_ok.setVisibility(View.VISIBLE);
                afterUriVideo = uriVideo;
                videoview.setMediaController(new MediaController(getContext()));
                videoview.setVideoURI(uriVideo);
                videoview.start();
            }
        }).execute(uri, compressedPath);*/
    }

    public void setupEmoji() {
        emojIcon = new EmojIconActions(getContext(), getActivity().getWindow().getDecorView().getRootView(), edt_message, img_emoticon,"#495C66","#DCE1E2","#E6EBEF");
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

    public interface OnDialogVideoPreview{
        void onPrepareVideoToSend(VideoPreview videoPreview);
    }

    interface onCompressVideo{
        void onCompressSuccess(Uri uriVideo);
    }

    class VideoCompressor extends AsyncTask<String, Void, Boolean> {

        String compressedPath;
        onCompressVideo onCompressVideo;

        public VideoCompressor(String compressedPath, DialogVideoPreview.onCompressVideo onCompressVideo) {
            this.compressedPath = compressedPath;
            this.onCompressVideo = onCompressVideo;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            android.util.Log.d(TAG, "Start video compression");
        }

        @Override
        protected Boolean doInBackground(String... params) {
            return com.gamatechno.ggfw.videocompress.MediaController.getInstance().convertVideo(params[0], params[1]);
        }

        @Override
        protected void onPostExecute(Boolean compressed) {
            super.onPostExecute(compressed);
            if (compressed) {
                Log.d("cool", "onPostExecute: yes compressed "+compressedPath);
                Uri fileUriAfterCompress = Uri.parse(compressedPath);
                onCompressVideo.onCompressSuccess(fileUriAfterCompress);
                Log.d("cool", "compress video : " + FilePath.getMimeType(getContext(), fileUriAfterCompress));
            } else {
                Log.d("not cool", "onPostExecute: yes compressed");
            }
        }
    }

    private void initView(Dialog view){
        edt_message = view.findViewById(R.id.edt_message);
        videoview = view.findViewById(R.id.videoview);
        img_ok = view.findViewById(R.id.img_ok);
        pb_ok = view.findViewById(R.id.pb_ok);
        img_emoticon = view.findViewById(R.id.img_emoticon);
    }
}
