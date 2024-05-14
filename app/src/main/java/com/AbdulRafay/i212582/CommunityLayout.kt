package com.AbdulRafay.i212582

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.bottomnavigation.BottomNavigationView


class CommunityLayout : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.community_layout)

        val imgbtn: ImageButton = findViewById(R.id.imageButton7)
        imgbtn.setOnClickListener{
            onBackPressed()
        }

        val img: ImageView = findViewById(R.id.img5)
        img.setOnClickListener{
            startActivity(Intent(this,PhotoLayout::class.java))
        }

        val img2: ImageView = findViewById(R.id.imageView21)
        img2.setOnClickListener{
            startActivity(Intent(this,VoiceCall::class.java))
        }

        val img3: ImageView = findViewById(R.id.imageView20)
        img3.setOnClickListener{
            startActivity(Intent(this,VideoCall::class.java))
        }

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_app_bar)
        bottomNavigationView.selectedItemId = R.id.nav_chat

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_search -> {
                    startActivity(Intent(applicationContext, SearchLayout::class.java))
                    overridePendingTransition(0, 0)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.nav_add -> {
                    startActivity(Intent(applicationContext, AddMentor::class.java))
                    overridePendingTransition(0, 0)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.nav_home -> {
                    startActivity(Intent(applicationContext, MainMenu::class.java))
                    overridePendingTransition(0, 0)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.nav_profile -> {
                    startActivity(Intent(applicationContext, ProfileLayout::class.java))
                    overridePendingTransition(0, 0)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.nav_chat -> return@setOnNavigationItemSelectedListener true

            }
            false
        }



    }
}