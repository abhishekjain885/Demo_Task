package com.example.apple.myapplication

import android.Manifest
import android.app.DownloadManager
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.net.Uri
import android.os.Build
import android.os.Environment
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.util.*
import android.content.Intent
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.widget.Toast


class MainActivity : AppCompatActivity() {

    private val MY_PERMISSIONS_REQUEST = 99
    private val downloadUrl = "https://ia802508.us.archive.org/5/items/testmp3testfile/mpthreetest.mp3"
    private val fileName: String = "test_audio.mp3"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            checkWritePermission()
        }

        initView()
    }


    private fun initView() {
        bt_download.setOnClickListener {
            downloadFile()
        }

        bt_play.setOnClickListener {
            playAudio()
            bt_play.isEnabled = false
            bt_stop.isEnabled = true
        }

        bt_stop.setOnClickListener {
            stopAudio()
            bt_play.isEnabled = true
            bt_stop.isEnabled = false
        }

        val file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileName)
        if (file.exists()) {
            bt_download.isEnabled = false
            bt_play.isEnabled = true
        } else {
            bt_download.isEnabled = true
            bt_play.isEnabled = false
        }

    }

    private fun downloadFile() {
        val request = DownloadManager.Request(Uri.parse(downloadUrl))
        request.setDescription("Music File")
        request.setTitle(fileName)
        request.setVisibleInDownloadsUi(true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            request.allowScanningByMediaScanner()
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN)
        }
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)

        val manager1 = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        Objects.requireNonNull(manager1).enqueue(request)
        if (DownloadManager.STATUS_SUCCESSFUL == 8) {
            bt_play.isEnabled = true
            bt_download.isEnabled = false
        }
    }

    private fun playAudio() {
        val objIntent = Intent(this, PlayAudioService::class.java)
        startService(objIntent)
    }

    private fun stopAudio() {
        val objIntent = Intent(this, PlayAudioService::class.java)
        stopService(objIntent)
    }

    private fun checkWritePermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {

                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    MY_PERMISSIONS_REQUEST
                )
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    MY_PERMISSIONS_REQUEST
                )
            }
        } else {
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            MY_PERMISSIONS_REQUEST -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.size > 0 && grantResults[0] === PackageManager.PERMISSION_GRANTED) {

                    if (ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                    }

                } else {

                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show()
                    finish()
                }
                return
            }
        }
    }
}
