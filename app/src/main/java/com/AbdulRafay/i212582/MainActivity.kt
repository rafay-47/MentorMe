package com.AbdulRafay.i212582

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService


class MainActivity : AppCompatActivity() {

    private lateinit var auth:FirebaseAuth
    private lateinit var database: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().getReference("Users")
        database.keepSynced(true)

        Handler().postDelayed({
            if(auth.currentUser!=null){
                startActivity(Intent(this, ProfileLayout::class.java))
                finish()
            } else {
                val i = Intent(this, login::class.java)
                startActivity(i)
                finish()
            }


        }, 5000)


    }

    private fun getFCMToken(){
        FirebaseMessaging.getInstance().token.addOnSuccessListener { token ->
            val Token:String = token
        }
    }
}