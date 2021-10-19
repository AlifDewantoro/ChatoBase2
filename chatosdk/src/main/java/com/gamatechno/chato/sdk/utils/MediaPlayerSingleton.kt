package com.gamatechno.chato.sdk.utils

import android.media.MediaPlayer
import android.os.Handler

object MediaPlayerSingleton {
    var mediaPlayer: MediaPlayer?=null
    var runnable: Runnable? = null
    var handler: Handler? = null

    init {
        mediaPlayer = MediaPlayer()
        runnable = Runnable {  }
        handler = Handler()
    }


    /*
    companion object{
        private var mInstance: MediaPlayerSingleton?=null
        private var mediaPlayer: MediaPlayer?=null
        private var handler: Handler?=null
        private var runnable: Runnable?=null

        fun getInstance(): MediaPlayerSingleton{
            if (mInstance==null)
                mInstance = MediaPlayerSingleton()

            return mInstance as MediaPlayerSingleton
        }

        fun getMediaPlayer(): MediaPlayer{
            if (mediaPlayer==null)
                mediaPlayer = MediaPlayer()

            return mediaPlayer as MediaPlayer
        }

        fun getHandler(): Handler{
            if (handler==null)
                handler = Handler()

            return handler as Handler
        }

        fun getRunnable(view: SeekBar): Runnable{
            if (runnable==null) {
                runnable = Runnable {
                    view.setProgress(mediaPlayer!!.currentPosition)
                    handler?.postDelayed(this.runnable, 250)
                }
            }else{
                view.setProgress(mediaPlayer!!.currentPosition)
                handler?.postDelayed(this.runnable, 250)
            }

            return runnable as Runnable
        }


    }

     */
}