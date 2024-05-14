package com.AbdulRafay.i212582

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class signup : AppCompatActivity() {


    private lateinit var mAuth:FirebaseAuth
    private lateinit var database:DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signup_page)

        val email:EditText = findViewById(R.id.textView14)
        val pass:EditText = findViewById(R.id.editTextPassword1)



        mAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().getReference("Users")

        val btn:Button = findViewById(R.id.button3)
        btn.setOnClickListener {


            mAuth.createUserWithEmailAndPassword(
                email.text.toString(),
                pass.text.toString()
            ).addOnSuccessListener {


                writeUserData()


                startActivity(Intent(this, ProfileLayout::class.java))
                finish()
            }.addOnFailureListener {
                    Toast.makeText(this,"Failed To Signup", Toast.LENGTH_LONG).show()
                }
        }
        val txt:TextView = findViewById(R.id.textView22)
        txt.setOnClickListener{
            startActivity(Intent(this,login::class.java))
        }

    }

    private fun writeUserData(){

        val uid = mAuth.currentUser?.uid.toString()

        val name:String = findViewById<EditText>(R.id.textView10).text.toString()
        val contactNo:String = findViewById<EditText>(R.id.textView16).text.toString()
        val country:String = findViewById<Spinner>(R.id.spinner).selectedItem.toString()
        val city:String = findViewById<Spinner>(R.id.spinner1).selectedItem.toString()
        val email:String = findViewById<EditText>(R.id.textView14).text.toString()

        val userdata = UserData(uid, name, email,"" ,contactNo, country, city)

        database.child(uid).setValue(userdata).addOnSuccessListener {
            Toast.makeText(this,"User Added", Toast.LENGTH_LONG).show()

        }.addOnFailureListener{
            Toast.makeText(this,"Failed To Add User", Toast.LENGTH_LONG).show()

        }

    }


}