package com.AbdulRafay.i212582


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast


class BookedSessions : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.booked_sessions)

        val img:ImageButton = findViewById(R.id.imageButton7)
        img.setOnClickListener{
            onBackPressed()
        }
    }
}