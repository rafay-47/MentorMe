package com.AbdulRafay.i212582

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.google.android.material.bottomnavigation.BottomNavigationView
import android.content.Intent
import android.widget.ImageButton
import androidx.annotation.NonNull
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class SearchResults : AppCompatActivity(), SearchResultsMentorAdapter.OnAcceptButtonClickListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.search_results)
        val searchQuery = intent.getStringExtra("searchquery")

        getSearchResults(searchQuery!!)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_app_bar)

        val imgbtn: ImageButton = findViewById(R.id.imageButton7)
        imgbtn.setOnClickListener{
            onBackPressed()
            bottomNavigationView.selectedItemId = R.id.nav_home
        }

        bottomNavigationView.selectedItemId = R.id.nav_search
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(applicationContext, MainMenu::class.java))
                    overridePendingTransition(0, 0)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.nav_add -> {
                    startActivity(Intent(applicationContext, AddMentor::class.java))
                    overridePendingTransition(0, 0)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.nav_chat -> {
                    startActivity(Intent(applicationContext, ChatsLayout::class.java))
                    overridePendingTransition(0, 0)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.nav_profile -> {
                    startActivity(Intent(applicationContext, ProfileLayout::class.java))
                    overridePendingTransition(0, 0)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.nav_search -> return@setOnNavigationItemSelectedListener true

            }
            false
        }





    }

    private fun getSearchResults(searchQuery: String){

        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager

        val MentorsList = mutableListOf<Mentors>()

        val database = FirebaseDatabase.getInstance().getReference("Mentors")
        database.orderByChild("name").equalTo(searchQuery).addListenerForSingleValueEvent(object : ValueEventListener {
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
                recyclerView.adapter = SearchResultsMentorAdapter(MentorsList, this@SearchResults)

            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    override fun onAcceptButtonClick(mentor: Mentors) {
        val intent = Intent(this,BookSession::class.java)
        intent.putExtra("mentor", mentor)
        startActivity(intent)
    }
}

