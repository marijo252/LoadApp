package com.udacity

import android.app.DownloadManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity() {

    private var downloadID: Long = 0
    private var buttonUrl: String = ""
    private var fileName: String = ""

    private lateinit var notificationManager: NotificationManager
    private lateinit var pendingIntent: PendingIntent
    private lateinit var action: NotificationCompat.Action

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        createChannel(
            CHANNEL_ID,
            getString(R.string.channelId)
        )

        custom_button.setOnClickListener {
            download(buttonUrl)
            custom_button.buttonState = ButtonState.Loading
        }
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            if(id == downloadID){
                val notificationManager = ContextCompat.getSystemService(
                    context,
                    NotificationManager::class.java
                ) as NotificationManager
                custom_button.buttonState = ButtonState.Completed
                notificationManager.cancelNotifications()
                if(intent.action.equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)){
                    notificationManager.sendNotification(
                        getString(R.string.notification_description),
                        context,
                        true,
                        fileName)
                }
                else {
                    notificationManager.sendNotification(
                        getString(R.string.notification_description),
                        context,
                        false,
                        fileName)
                }
            }
        }
    }

    private fun download(url: String) {
        if(url.isEmpty()){
            Toast.makeText(
                applicationContext,
                resources.getText(R.string.toast_selectRadioButton),
                Toast.LENGTH_LONG)
                .show()
        }
        else{
            val request =
                DownloadManager.Request(Uri.parse(url))
                    .setTitle(getString(R.string.app_name))
                    .setDescription(getString(R.string.app_description))
                    .setRequiresCharging(false)
                    .setAllowedOverMetered(true)
                    .setAllowedOverRoaming(true)
            val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
            downloadID = downloadManager.enqueue(request)
        }
    }

    companion object {
        const val CHANNEL_ID = "channelId"
    }

    fun onRadioButtonClicked(view: View) {
        if (view is RadioButton) {
            val checked = view.isChecked
            when (view.getId()) {
                R.id.radio_glide -> if (checked) {
                    buttonUrl = "https://github.com/bumptech/glide"
                    fileName = getString(R.string.glide_text)
                }
                R.id.radio_udacity -> if (checked) {
                    buttonUrl = "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter0"
                    fileName = getString(R.string.loadApp_text)
                }
                R.id.radio_retrofit -> if (checked) {
                    buttonUrl = "https://github.com/square/retrofit"
                    fileName = getString(R.string.retrofit_text)
                }
            }
        }
    }

    private fun createChannel(channelId: String, channelName: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            )
            .apply {
                setShowBadge(false)
            }

            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.description = getString(R.string.notification_description)

            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

}
