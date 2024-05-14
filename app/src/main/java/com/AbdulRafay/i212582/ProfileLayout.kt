package com.AbdulRafay.i212582

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.graphics.drawable.toBitmap
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.google.android.gms.common.util.UidVerifier
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import de.hdodenhof.circleimageview.CircleImageView
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.squareup.picasso.Picasso
import java.io.ByteArrayOutputStream
import java.util.UUID


class ProfileLayout : AppCompatActivity() {
    private lateinit var auth:FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var userData: UserData
    private lateinit var profilePic: CircleImageView
    private lateinit var uid:String
    private lateinit var profilePicUri:String
    private lateinit var imageURL:String
    private lateinit var FirebaseStorageRef:FirebaseStorage
    private lateinit var Name:String
    private lateinit var Email:String
    private lateinit var ContactNo:String
    private lateinit var Country:String
    private lateinit var City:String

    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK && result.data != null) {
            val selectedImageUri: Uri? = result.data?.data
            profilePicUri = selectedImageUri.toString()
            profilePic.setImageURI(selectedImageUri)


                selectedImageUri?.let { uri ->
                    val storageRef = FirebaseStorage.getInstance().reference.child("images/${uid}")
                    storageRef.putFile(uri).addOnSuccessListener { taskSnapshot ->
                        taskSnapshot.storage.downloadUrl.addOnSuccessListener { uri ->
                            imageURL = uri.toString()
                            Log.d("GalleryClass", "Image URL: $imageURL")
                            database = FirebaseDatabase.getInstance().getReference("Users")
                            val updatedUserData = hashMapOf<String, Any>(
                                "name" to Name,
                                "email" to Email,
                                "imageURL" to imageURL,
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
                        }
                    }.addOnFailureListener { exception ->
                        Log.e("GalleryClass", "Upload failed", exception)
                    }
                }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile_layout)
        auth = FirebaseAuth.getInstance()
        profilePic = findViewById(R.id.c)

        setProfilePic()



        database = FirebaseDatabase.getInstance().getReference("Users")
        uid = auth.currentUser?.uid.toString()
        database.orderByChild("uid").equalTo(uid).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                val userDataSnapshot = snapshot.children.firstOrNull()

                uid = userDataSnapshot?.child("uid")?.value.toString()
                Name = userDataSnapshot?.child("name")?.value.toString()
                Email = userDataSnapshot?.child("email")?.value.toString()
                ContactNo = userDataSnapshot?.child("contactNo")?.value.toString()
                Country = userDataSnapshot?.child("country")?.value.toString()
                City = userDataSnapshot?.child("city")?.value.toString()

                userData = UserData (uid, Name, Email, imageURL = "", ContactNo, Country, City)

                findViewById<TextView>(R.id.textView).text = userData.name
                findViewById<TextView>(R.id.textView1).text = userData.city
            }
            override fun onCancelled(error: DatabaseError) {
                println("Error getting data: ${error.message}")
            }
        })


        findViewById<ImageView>(R.id.img).setOnClickListener {
            openGallery()
        }

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_app_bar)

        val btn:Button = findViewById(R.id.button)
        btn.setOnClickListener{
            startActivity(Intent(this,BookedSessions::class.java))
        }
        val img:ImageView = findViewById(R.id.imageView)
        img.setOnClickListener{
            startActivity(Intent(this,EditProfile::class.java))
        }

        val imgbtn: ImageButton = findViewById(R.id.imageButton7)
        imgbtn.setOnClickListener{
            onBackPressed()
            bottomNavigationView.selectedItemId = R.id.nav_home

        }


        bottomNavigationView.selectedItemId = R.id.nav_profile

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
                R.id.nav_chat -> {
                    startActivity(Intent(applicationContext, ChatsLayout::class.java))
                    overridePendingTransition(0, 0)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.nav_profile -> return@setOnNavigationItemSelectedListener true

            }
            false
        }
        setProfilePic()

    }
    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        pickImageLauncher.launch(intent)
    }
    private fun setProfilePic() {
        val uid = auth.currentUser?.uid.toString()
        val storage = Firebase.storage
        val storageRef = storage.reference
        val pathReference = storageRef.child("images/${uid}")
        Log.d("ewewe", pathReference.toString())

        pathReference.downloadUrl.addOnSuccessListener { uri ->
            Log.d("ewewe", "Image URL: $uri")
            val tempimageURl = uri.toString()
            Picasso.get().load(tempimageURl).into(profilePic);
        }.addOnFailureListener {
            Log.e("ProfileLayout", "Failed to get image", it)
        }
    }
}