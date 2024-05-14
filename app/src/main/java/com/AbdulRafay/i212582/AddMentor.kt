package com.AbdulRafay.i212582


import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import de.hdodenhof.circleimageview.CircleImageView


class AddMentor : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var userData: UserData
    private lateinit var profilePic: ImageView
    private lateinit var uid:String
    private lateinit var profilePicUri:String
    private lateinit var imageURL:String
    private lateinit var name:String
    private lateinit var desc:String
    private lateinit var price:String
    private lateinit var mentorId: String


    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK && result.data != null) {
            findViewById<ImageView>(R.id.button2).background = null
            val selectedImageUri: Uri? = result.data?.data
            profilePicUri = selectedImageUri.toString()
            profilePic.setImageURI(selectedImageUri)
            Log.d("AddMentor", "addMentor1: $uid")
            database = FirebaseDatabase.getInstance().getReference("Mentors")
            mentorId = database.push().key.toString()
            selectedImageUri?.let { uri ->
                val storageRef = FirebaseStorage.getInstance().reference.child("images/${mentorId}")
                storageRef.putFile(uri).addOnSuccessListener { taskSnapshot ->
                    taskSnapshot.storage.downloadUrl.addOnSuccessListener { uri ->
                        imageURL = uri.toString()
                        Log.d("AddMentor", "addMentor2: $uid")

                    }
                }.addOnFailureListener { exception ->
                    Log.e("GalleryClass", "Upload failed", exception)
                }
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_mentor)
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_app_bar)

        auth = FirebaseAuth.getInstance()
        uid = auth.currentUser?.uid.toString()
        profilePic = findViewById(R.id.button2)
        val imgbtn: ImageButton = findViewById(R.id.imageButton7)
        imgbtn.setOnClickListener{
            onBackPressed()
            bottomNavigationView.selectedItemId = R.id.nav_home
        }

        val btn1:Button = findViewById(R.id.button1)
        btn1.setOnClickListener{
            startActivity(Intent(this,VideoLayout::class.java))
        }

        val btn: Button = findViewById(R.id.button3)
        btn.setOnClickListener {
            addMentor()
            startActivity(Intent(this,MainMenu::class.java))

        }

        profilePic.setOnClickListener{
            openGallery()
        }

        bottomNavigationView.selectedItemId = R.id.nav_add
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(applicationContext, MainMenu::class.java))
                    overridePendingTransition(0, 0)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.nav_search -> {
                    startActivity(Intent(applicationContext, SearchLayout::class.java))
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
                R.id.nav_add -> return@setOnNavigationItemSelectedListener true

            }
            false
        }

    }
    private fun addMentor(){

        name = findViewById<TextView>(R.id.name).text.toString()
        desc = findViewById<TextView>(R.id.desc).text.toString()
        price = findViewById<TextView>(R.id.price).text.toString()

        database = FirebaseDatabase.getInstance().getReference("Mentors")
        val mentor = Mentors(mentorId, name, desc, price, imageURL)
        database.child(mentorId).setValue(mentor).addOnSuccessListener {
            Toast.makeText(this, "Mentor Added...", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener{
            Toast.makeText(this, "Failed To Add Mentor...", Toast.LENGTH_SHORT).show()
        }

    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        pickImageLauncher.launch(intent)
    }
}