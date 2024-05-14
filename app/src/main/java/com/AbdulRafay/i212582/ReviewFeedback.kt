package com.AbdulRafay.i212582


import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.RatingBar
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

data class Feedbacks(
    val id: String,
    val mentorId: String,
    val uid: String,
    val rating: String,
    val Desc: String
)

class ReviewFeedback : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.review_layout)

        val auth = FirebaseAuth.getInstance()
        val mentor = intent.getParcelableExtra<Mentors>("mentor")


        val imgbtn: ImageButton = findViewById(R.id.imageButton7)
        imgbtn.setOnClickListener{
            onBackPressed()
        }

        val btn: Button = findViewById(R.id.button3)
        btn.setOnClickListener {
            val database = FirebaseDatabase.getInstance().getReference("Feedbacks")
            val key = database.push().key.toString()
            val Feedback = Feedbacks(
                key,
                mentor!!.id,
                auth.currentUser?.uid.toString(),
                findViewById<RatingBar>(R.id.ratingBar).rating.toString(),
                findViewById<EditText>(R.id.desc).text.toString()
            )
            database.child(key).setValue(Feedback).addOnSuccessListener {
                Toast.makeText(this, "Feedback Submitted...", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MainMenu::class.java))
            }
        }
    }
}