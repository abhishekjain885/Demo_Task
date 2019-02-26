package com.example.apple.myapplication

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.media.MediaPlayer
import android.net.Uri
import android.os.Environment
import android.util.Log


class PlayAudioService : Service() {
    private lateinit var objPlayer: MediaPlayer
    private val fileName: String = "test_audio.mp3"


    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        objPlayer = MediaPlayer.create(
            this,
            Uri.parse("""${Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).path}/$fileName""")
        )
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        objPlayer.isLooping = true
        objPlayer.start()
        Log.d("PlayAudioService", "Media Player started!")
        return START_STICKY
    }


    override fun onDestroy() {
        super.onDestroy()
        if (objPlayer != null) {
            objPlayer.stop()
            objPlayer.release()
        }
    }

}
