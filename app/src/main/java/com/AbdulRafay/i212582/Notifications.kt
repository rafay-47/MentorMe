package com.AbdulRafay.i212582


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Im
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast


class Notifications : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.notifications)

        val img:ImageButton = findViewById(R.id.imageButton7)
        img.setOnClickListener{
            onBackPressed()
        }
    }
}