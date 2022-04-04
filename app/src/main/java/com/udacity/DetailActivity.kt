package com.udacity

import android.content.Intent
import android.graphics.Color.RED
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_detail.*

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
        ok_button.setOnClickListener{
            val mainIntent= Intent(applicationContext,MainActivity::class.java)
            startActivity(mainIntent)
        }
    }
}
