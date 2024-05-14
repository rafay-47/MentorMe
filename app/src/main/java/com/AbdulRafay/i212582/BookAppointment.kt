package com.AbdulRafay.i212582


import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.CalendarView
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

data class Appointments(
    val id:String,
    val mentorId: String,
    val date: String,
    val price: String,
    val uid: String
)

class BookAppointment : AppCompatActivity() {

    private lateinit var date:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.book_appointment)

        val auth = FirebaseAuth.getInstance()

        val mentor: Mentors = intent.getParcelableExtra("mentor")!!;

        findViewById<TextView>(R.id.name).text = mentor.name
        findViewById<TextView>(R.id.price).text = "$${mentor.price}/Session"
        Picasso.get().load(mentor.imageURL).into(findViewById<CircleImageView>(R.id.profilePic))

        findViewById<CalendarView>(R.id.cal).setOnDateChangeListener { view, year, month, dayOfMonth ->
            date = dayOfMonth.toString() + "/" + (month + 1) + "/" + year
            Toast.makeText(this, "Date: $date", Toast.LENGTH_SHORT).show()
        }

        val imgbtn: ImageButton = findViewById(R.id.imageButton7)
        imgbtn.setOnClickListener{
            onBackPressed()
        }

        val img1:CircleImageView = findViewById(R.id.m)
        img1.setOnClickListener{
            val intent = Intent(this,JohnCooperChat::class.java)
            intent.putExtra("mentor",mentor)
            startActivity(intent)
        }

        val img2:CircleImageView = findViewById(R.id.c)
        img2.setOnClickListener{
            startActivity(Intent(this,VoiceCall::class.java))
        }

        val img3:CircleImageView = findViewById(R.id.vc)
        img3.setOnClickListener{
            startActivity(Intent(this,VideoCall::class.java))
        }

        val btn: Button = findViewById(R.id.bookAppointment)
        btn.setOnClickListener{
            val date = findViewById<CalendarView>(R.id.cal).date
            val uid = auth.currentUser?.uid.toString()

            val database = FirebaseDatabase.getInstance().getReference("Appointments")
            val key = database.push().key.toString()
            val appointment = Appointments(key, mentor.id, date.toString(), mentor.price, uid)
            database.child(key).setValue(appointment).addOnSuccessListener {
                Toast.makeText(this, "Appointment Booked...", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MainMenu::class.java))
            }
        }
    }
}