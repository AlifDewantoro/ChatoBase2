package com.gamatechno.chato.sdk.app.chatroom.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PorterDuff;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import com.gamatechno.chato.sdk.R;
import com.gamatechno.chato.sdk.data.DAO.Chat.Chat;
import com.gamatechno.chato.sdk.utils.MediaPlayerSingleton;
import com.gamatechno.chato.sdk.utils.animation.AnimationToggle;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class VoiceNoteViewHolder extends BaseChatViewHolder implements BaseChatViewHolder.OnChatViewHolder {

    TextView tvTimer;
    TextView tvTimerSeek;
    AnimationToggle lay_play_pause;
    CardView lay_action_pause;
    CardView lay_action_play;
    //CardView lay_action_loading;

    SeekBar sbTrack;

    boolean isGroup;


    Context context;
    ChatRoomAdapter.OnChatRoomClick onChatRoomClick;

    public VoiceNoteViewHolder(@NonNull View itemView, Context context, ChatRoomAdapter.OnChatRoomClick onChatRoomClick) {
        super(context, itemView);
        this.setIsRecyclable(false);
        this.context = context;
        this.onChatRoomClick = onChatRoomClick;
        initView( itemView);
    }

    public VoiceNoteViewHolder(@NonNull View itemView, Context context, ChatRoomAdapter.OnChatRoomClick onChatRoomClick, boolean isGroup) {
        super(context, itemView);
        this.setIsRecyclable(false);
        this.context = context;
        this.isGroup = isGroup;
        this.onChatRoomClick = onChatRoomClick;
        initView( itemView);
    }

    @SuppressLint("DefaultLocale")
    private String convertFormatTime(int duration){
        return String.format("%2d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(duration),
                TimeUnit.MILLISECONDS.toSeconds(duration)-
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration)));

    }

    boolean isPause = false;
    Handler handler;
    Runnable runnable;

    public void bindData(Chat chat, int isMyChat, int isBeforeMyChat, int position, boolean isDateShow, boolean isRect, boolean isGroupNeedName) {
        super.bindData(chat, isDateShow, isRect, isGroup, isGroupNeedName, position, isMyChat, isBeforeMyChat, onChatRoomClick);

        if(chat.getFileModel() == null){
            if(isMyChat==1) {
                tvTimer.setTextColor(context.getResources().getColor(R.color.white));
                tvTimerSeek.setTextColor(context.getResources().getColor(R.color.white));
                sbTrack.getProgressDrawable().setColorFilter(
                        context.getResources().getColor(R.color.white),
                        PorterDuff.Mode.SRC_ATOP
                );
            }else {
                tvTimer.setTextColor(context.getResources().getColor(R.color.colorMonocrome100));
                tvTimerSeek.setTextColor(context.getResources().getColor(R.color.colorMonocrome100));
                sbTrack.getProgressDrawable().setColorFilter(
                        context.getResources().getColor(R.color.colorMonocrome100),
                        PorterDuff.Mode.SRC_ATOP
                );
            }

            MediaPlayer mediaPlayer = MediaPlayerSingleton.INSTANCE.getMediaPlayer();

            int duration = mediaPlayer.getDuration();
            Log.e("LOG", "ini log durasi "+mediaPlayer.getDuration());
            String sDuration = convertFormatTime(duration);
            tvTimerSeek.setText(String.format("%s ", sDuration));
            tvTimer.setText(String.format("/%s", convertFormatTime(chat.getMessage_file_duration())));


            lay_action_play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    onChatRoomClick.onPlayAudio(chat, position);

                    if (mediaPlayer.isPlaying()){
                        mediaPlayer.reset();
                        mediaPlayer.stop();
                        if (MediaPlayerSingleton.INSTANCE.getRunnable()!=null) {
                            Objects.requireNonNull(MediaPlayerSingleton.INSTANCE.getHandler()).
                                    removeCallbacks(MediaPlayerSingleton.INSTANCE.getRunnable());
                        }
                    }
                    //lay_play_pause.display(lay_action_loading);
                    lay_play_pause.hide(lay_action_play);
                    lay_play_pause.display(lay_action_pause);

                    /*
                    lay_play_pause.hide(lay_action_play);
                    lay_play_pause.display(lay_action_pause);

                     */

                    //handler = new Handler();
                    //runnable = MediaPlayerSingleton.Companion.getRunnable(sbTrack);

                    MediaPlayerSingleton.INSTANCE.setRunnable(new Runnable() {
                        @Override
                        public void run() {
                            sbTrack.setProgress(mediaPlayer.getCurrentPosition());
                            handler.postDelayed(this, 250);
                        }
                    });

                    MediaPlayerSingleton.INSTANCE.setHandler(new Handler());

                    handler = MediaPlayerSingleton.INSTANCE.getHandler();

                    runnable = MediaPlayerSingleton.INSTANCE.getRunnable();
                    /*
                    runnable = new Runnable() {
                        @Override
                        public void run() {
                            sbTrack.setProgress(mediaPlayer.getCurrentPosition());
                            handler.postDelayed(this, 250);
                        }
                    };

                     */

                    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            lay_play_pause.display(lay_action_play);
                            lay_play_pause.hide(lay_action_pause);
                            mediaPlayer.seekTo(0);
                            sbTrack.setProgress(mediaPlayer.getCurrentPosition());
                            mediaPlayer.reset();
                            handler.removeCallbacks(runnable);
                        }
                    });

                    try {
                        if (!isPause) {
                            isPause = false;
                            mediaPlayer.reset();
                            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                            mediaPlayer.setDataSource(chat.getMessage_attachment());
                            mediaPlayer.prepare();
                            mediaPlayer.start();
                        }else{
                            mediaPlayer.start();
                        }

                        sbTrack.setMax(mediaPlayer.getDuration());
                        handler.postDelayed(runnable, 0);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            });

            lay_action_pause.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //lay_play_pause.hide(lay_action_loading);
                    lay_play_pause.display(lay_action_play);
                    lay_play_pause.hide(lay_action_pause);

                    if (mediaPlayer.isPlaying()){
                        mediaPlayer.pause();
                        isPause = true;
                    }
                    handler.removeCallbacks(runnable);
                }
            });

            sbTrack.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if (fromUser) {
                        mediaPlayer.seekTo(progress);
                    }

                    tvTimerSeek.setText(String.format("%s ", convertFormatTime(mediaPlayer.getCurrentPosition())));
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });

            /*
            voicePlayer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (GGFWUtil.isValidURL(chat.getMessage_attachment())) {
                        String name_file = chat.getMessage_attachment().split("/")[chat.getMessage_attachment().split("/").length - 1];
                        Uri destination = Uri.withAppendedPath(Uri.fromFile(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)), "Chato/Document");
                        Uri file_uri = Uri.withAppendedPath(Uri.fromFile(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)), "Chato/Document/" +chat.getMessage_attachment_name());
                        if (IOUtils.isFileExist(file_uri.toString().replace("file:/", ""))) {
                            onChatRoomClick.onClickAttachment(chat, file_uri);
                        } else {
                            try {
                                new GTDownloadManager(context, new GTDownloadCallback(){
                                    @Override
                                    public void onProcess(GTDownloadRequest request) {
                                        onChatRoomClick.onDownloadingAttachment(true, position);
                                    }

                                    @Override
                                    public void onCancel(GTDownloadRequest request) {

                                    }

                                    @Override
                                    public void onSuccess(GTDownloadRequest request) {
                                        onChatRoomClick.onDownloadingAttachment(false, position);
                                        onChatRoomClick.onClickAttachment(chat, request.getDestinationUri());
                                    }
                                }).startRequest(new GTDownloadRequest(Uri.parse(chat.getMessage_attachment().replace(" ", "%20")), chat.getMessage_attachment_name()).setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN).setDestinationUri(destination));
                            } catch (GTDownloadException e) {
                                e.printStackTrace();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                });

             */
        }

    }

    private void initView(View view){
        tvTimer = view.findViewById(R.id.tvTimer);
        tvTimerSeek = view.findViewById(R.id.tvTimerSeek);
        sbTrack = view.findViewById(R.id.sbTrack);
        lay_action_pause = view.findViewById(R.id.lay_action_pause);
        lay_action_play = view.findViewById(R.id.lay_action_play);
        lay_play_pause = view.findViewById(R.id.lay_play_pause);
        //lay_action_loading = view.findViewById(R.id.lay_action_loading);
        /*
        container_attachment = view.findViewById(R.id.container_attachment);
        tv_attachment = view.findViewById(R.id.tv_attachment);
        img_attachment = view.findViewById(R.id.img_attachment);
        pb_download = view.findViewById(R.id.pb_download);
        
         */
    }
}
