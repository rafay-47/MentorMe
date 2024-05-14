package com.AbdulRafay.i212582

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast


class RestPassword : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.reset_password)

        val imgbtn: ImageButton = findViewById(R.id.imageButton)
        imgbtn.setOnClickListener{
            onBackPressed()
        }

        val btn: Button = findViewById(R.id.button7)
        btn.setOnClickListener {
            Toast.makeText(it.context, "Password Reset Completed...", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this,login::class.java))

        }

        val txt:TextView = findViewById(R.id.textView32)
        txt.setOnClickListener{
            startActivity(Intent(this,login::class.java))
        }
    }
}