package com.AbdulRafay.i212582

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth


class login : AppCompatActivity() {
    private lateinit var mAuth:FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_page)

        val txt:TextView = findViewById(R.id.textView9)
        txt.setOnClickListener {
            startActivity(Intent(this,signup::class.java))
        }

        val txt2:TextView = findViewById(R.id.textView7)
        txt2.setOnClickListener {
            startActivity(Intent(this,ForgotPassword::class.java))
        }

        mAuth = FirebaseAuth.getInstance()
        val email: EditText = findViewById(R.id.editTextTextEmailAddress)
        val pass: EditText = findViewById(R.id.editTextPassword)

        val btn:Button = findViewById(R.id.button2)
        btn.setOnClickListener {
            mAuth.signInWithEmailAndPassword(
                email.text.toString(),
                pass.text.toString()
            ).addOnSuccessListener {
                startActivity(Intent(this, MainMenu::class.java))
                finish()
            }.addOnFailureListener {
                Toast.makeText(this,"Failed To Sign In", Toast.LENGTH_LONG).show()
            }
        }
    }
}