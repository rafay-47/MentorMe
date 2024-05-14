package com.AbdulRafay.i212582

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.google.android.material.bottomnavigation.BottomNavigationView
import android.content.Intent
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import org.junit.Assert

class MainMenu : AppCompatActivity(), MainMenuMentorAdapter.OnAcceptButtonClickListener {
    private lateinit var auth:FirebaseAuth
    private lateinit var database: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_menu)
        getMentors()

        auth = FirebaseAuth.getInstance()
        val logout:ImageView = findViewById(R.id.logouticon)
        logout.setOnClickListener{
            auth.signOut()
            startActivity(Intent(this,login::class.java))
            finish()
        }
        val uid = auth.currentUser?.uid
        getUser(uid.toString()) { user ->
            if (user != null) {
                findViewById<TextView>(R.id.name).setText(user.name)
            }
        }

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_app_bar)
        bottomNavigationView.selectedItemId = R.id.nav_home
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
                R.id.nav_home -> return@setOnNavigationItemSelectedListener true

            }
            Log.d("MainMenu", "onCreate10: ")
            false
        }
        Log.d("MainMenu", "onCreate11: ")
    }
    private fun getMentors() {
        Log.d("MainMenu", "onCreate2: ")
        val MentorsList = mutableListOf<Mentors>()
        val recyclerView: RecyclerView = findViewById(R.id.mentorRecyclerView)
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = layoutManager
        Log.d("MainMenu", "onCreate3: ")
        database = FirebaseDatabase.getInstance().getReference("Mentors")
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(casesSnapshot: DataSnapshot) {
                Log.d("MainMenu", "onCreate4: ")
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
                Log.d("MainMenu", "onCreate5: ")
                Log.d("MainMenu", MentorsList.size.toString())
                recyclerView.adapter = MainMenuMentorAdapter(MentorsList, this@MainMenu)
                Log.d("MainMenu", "onCreate6: ")

            }

            override fun onCancelled(error: DatabaseError) {
            }
        })

    }
    private fun getUser(id: String, callback: (UserData?) -> Unit) {
        database = FirebaseDatabase.getInstance().getReference("Users")
        database.orderByChild("uid").equalTo(id).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val userDataSnapshot = snapshot.children.firstOrNull()

                val user = userDataSnapshot?.let {
                    UserData(
                        it.child("uid").value.toString(),
                        it.child("name").value.toString(),
                        it.child("email").value.toString(),
                        it.child("imageURL").value.toString(),
                        it.child("contactNo").value.toString(),
                        it.child("country").value.toString(),
                        it.child("city").value.toString(),

                        )
                }
                callback(user)
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
