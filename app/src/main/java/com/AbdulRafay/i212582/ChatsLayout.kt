package com.AbdulRafay.i212582

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class ChatsLayout : AppCompatActivity(), ChatsLayoutAdapter.OnAcceptButtonClickListener{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.chats_layout)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_app_bar1)
        getChats()
//        val imgbtn: ImageButton = findViewById(R.id.imageButton7)
//        imgbtn.setOnClickListener{
//            onBackPressed()
//            bottomNavigationView.selectedItemId = R.id.nav_home
//        }
//
//        val img2: ImageView = findViewById(R.id.imageView13)
//        img2.setOnClickListener{
//            startActivity(Intent(this,CommunityLayout::class.java))
//        }



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

    private fun getChats(){

        val recyclerView: RecyclerView = findViewById(R.id.chatsRecyclerView)
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager

        val MentorsList = mutableListOf<Mentors>()

        val database = FirebaseDatabase.getInstance().getReference("Mentors")
        database.addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(casesSnapshot: DataSnapshot) {

                for (caseSnapshot in casesSnapshot.children) {
                    val id = caseSnapshot.child("id").getValue(String::class.java)
                    val name = caseSnapshot.child("name").getValue(String::class.java)
                    val price = caseSnapshot.child("price").getValue(String::class.java)
                    val desc = caseSnapshot.child("desc").getValue(String::class.java)
                    val imageURL = caseSnapshot.child("imageURL").getValue(String::class.java)

                    if (id != null && desc != null && name != null && price != null && imageURL != null) {
                        MentorsList.add(Mentors(id, name, desc, price, imageURL))
                    }
                }
                Toast.makeText(this@ChatsLayout, MentorsList.size.toString(), Toast.LENGTH_SHORT).show()
                recyclerView.adapter = ChatsLayoutAdapter(MentorsList, this@ChatsLayout)
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    override fun onAcceptButtonClick(mentor: Mentors) {
        val intent = Intent(this, JohnCooperChat::class.java)
        intent.putExtra("mentor", mentor)
        startActivity(intent)
    }
}