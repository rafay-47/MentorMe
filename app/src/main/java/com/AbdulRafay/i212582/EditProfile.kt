package com.AbdulRafay.i212582

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.ListView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import org.checkerframework.framework.qual.DefaultQualifier


class EditProfile : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var userData: UserData
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_profile)



        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().getReference("Users")
        val uid = auth.currentUser?.uid.toString()
        database.orderByChild("uid").equalTo(uid).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                val userDataSnapshot = snapshot.children.firstOrNull()

                userData = UserData (
                    uid = userDataSnapshot?.child("uid")?.value.toString(),
                    name = userDataSnapshot?.child("name")?.value.toString(),
                    email = userDataSnapshot?.child("email")?.value.toString(),
                    imageURL = userDataSnapshot?.child("ImageURL")?.value.toString(),
                    contactNo = userDataSnapshot?.child("contactNo")?.value.toString(),
                    country = userDataSnapshot?.child("country")?.value.toString(),
                    city = userDataSnapshot?.child("city")?.value.toString()
                )

                findViewById<TextView>(R.id.textView10).text = userData.name
                findViewById<TextView>(R.id.textView14).text = userData.email
                findViewById<TextView>(R.id.textView16).text = userData.contactNo
                findViewById<Spinner>(R.id.spinner).setSelection(resources.getStringArray(R.array.country_items).indexOf(userData.country))
                findViewById<Spinner>(R.id.spinner1).setSelection(resources.getStringArray(R.array.city_items).indexOf(userData.city))
            }
            override fun onCancelled(error: DatabaseError) {
                println("Error getting data: ${error.message}")
            }
        })




        val imgbtn: ImageButton = findViewById(R.id.imageButton7)
        imgbtn.setOnClickListener{
            onBackPressed()
            finish()
        }

        val btn: Button = findViewById(R.id.button3)
        btn.setOnClickListener{

            val Name:String = findViewById<TextView>(R.id.textView10).text.toString()
            val Email:String = findViewById<TextView>(R.id.textView14).text.toString()
            val ContactNo:String = findViewById<TextView>(R.id.textView16).text.toString()
            val Country:String = findViewById<Spinner>(R.id.spinner).selectedItem.toString()
            val City:String = findViewById<Spinner>(R.id.spinner1).selectedItem.toString()

            val updatedUserData = hashMapOf<String, Any>(
                "name" to Name,
                "email" to Email,
                "contactNo" to ContactNo,
                "country" to Country,
                "city" to City
            )
            database.child(uid).updateChildren(updatedUserData).addOnSuccessListener {
                Toast.makeText(this, "Profile Edited...", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this,ProfileLayout::class.java))
                finish()
            }.addOnFailureListener {
                Toast.makeText(this, "Failed...", Toast.LENGTH_SHORT).show()
            }
            //onBackPressed()
        }

    }
}