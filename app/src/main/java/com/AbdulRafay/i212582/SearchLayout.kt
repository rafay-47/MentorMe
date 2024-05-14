package com.AbdulRafay.i212582


import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView



class SearchLayout : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.search_layout)
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_app_bar)

        val imgbtn: ImageButton = findViewById(R.id.imageButton6)
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

        val searchView: SearchView = findViewById(R.id.searchView)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                Toast.makeText(this@SearchLayout, query, Toast.LENGTH_SHORT).show()
                val intent = Intent(applicationContext, SearchResults::class.java)
                intent.putExtra("searchquery", query)
                startActivity(intent)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                // Handle text changes in search input
                return false
            }
        })




    }
}


