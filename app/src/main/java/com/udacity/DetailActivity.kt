package com.udacity

import android.app.NotificationManager
import android.content.Intent
import android.graphics.Color.RED
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_detail.*
import kotlinx.android.synthetic.main.content_main.*

class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)

        val bundle = intent.extras
        val fileName = bundle?.getString("fileName")
        val isDownloaded = bundle?.getBoolean("isDownloaded")

        val fileNameView = findViewById<TextView>(R.id.fileName_value)
        val statusView = findViewById<TextView>(R.id.status_value)
        fileNameView.text = fileName
        if (isDownloaded == true){
            statusView.text = "Success"
        }
        else{
            statusView.text = "Failed"
            statusView.setTextColor(RED)
        }

        val notificationManager = ContextCompat.getSystemService(
            applicationContext,
            NotificationManager::class.java
        ) as NotificationManager
        notificationManager.cancelNotifications()

        ok_button.setOnClickListener{
            val mainIntent= Intent(applicationContext,MainActivity::class.java)
            startActivity(mainIntent)
        }
    }
}
