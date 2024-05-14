package com.AbdulRafay.i212582


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView


class ForgotPassword : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.forgot_password)

        val txt:TextView = findViewById(R.id.textView32)
        txt.setOnClickListener{
            startActivity(Intent(this,login::class.java))
        }

        val btn:Button = findViewById(R.id.button6)
        btn.setOnClickListener {
            startActivity(Intent(this,RestPassword::class.java))
        }

        val imgbtn:ImageButton = findViewById(R.id.imageButton2)
        imgbtn.setOnClickListener{
            onBackPressed()
        }





    }
}