package com.AbdulRafay.i212582


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView


class BookSession : AppCompatActivity() {

    private lateinit var mentor: Mentors
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.book_session)

        mentor = intent.getParcelableExtra("mentor")!!;
        findViewById<TextView>(R.id.name).text = mentor.name
        findViewById<TextView>(R.id.desc).text = mentor.desc
        val profilePic = findViewById<CircleImageView>(R.id.profilePic)
        Picasso.get().load(mentor.imageURL).into(profilePic);


        val imgbtn: ImageButton = findViewById(R.id.imageButton7)
        imgbtn.setOnClickListener{
            onBackPressed()
        }

        val btn: Button = findViewById(R.id.button)
        btn.setOnClickListener {
            val intent = Intent(this,ReviewFeedback::class.java)
            intent.putExtra("mentor",mentor)
            startActivity(intent)
        }

        val btn2: Button = findViewById(R.id.button1)
        btn2.setOnClickListener {
            startActivity(Intent(this,CommunityLayout::class.java))
        }

        val btn1: Button = findViewById(R.id.button3)
        btn1.setOnClickListener {
            val intent = Intent(this,BookAppointment::class.java)
            intent.putExtra("mentor",mentor)
            startActivity(intent)
        }
    }
}