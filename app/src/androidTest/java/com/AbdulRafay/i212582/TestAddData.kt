package com.AbdulRafay.i212582
import android.content.Context
import android.util.Log
import androidx.test.core.app.ApplicationProvider
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import junit.framework.TestCase
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

class FirebaseAddDataTest {

    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        FirebaseApp.initializeApp(context)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference.child("Users")
    }

    @Test
    fun testRealtimeDatabaseAdd() {
        val latch = CountDownLatch(1)

        val mentorId = "123"
        val name = "Abdul Rafay"
        val desc = "I am a mentor"
        val price = "1000"

        database = FirebaseDatabase.getInstance().getReference("Mentors")
        val mentor = Mentors(mentorId, name, desc, price, "imageURL")
        database.child(mentorId).setValue(mentor).addOnCompleteListener() {
            TestCase.assertTrue(it.isSuccessful)
            latch.countDown()
        }.addOnFailureListener{

        }

        latch.await(5, TimeUnit.SECONDS)
    }

}
