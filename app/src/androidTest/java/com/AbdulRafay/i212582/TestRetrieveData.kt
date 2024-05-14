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
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

class FirebaseDatabaseTest {

    private lateinit var database: DatabaseReference
    private lateinit var auth:FirebaseAuth

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        FirebaseApp.initializeApp(context)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference.child("Users")
    }

    @Test
    fun testRealtimeDataRetrieve() {
        val latch = CountDownLatch(1)

        val uid = auth.currentUser?.uid
        getUser(uid.toString()) { user ->
            if (user != null) {
                Log.d("User", user.uid)
                assertEquals(uid, user.uid)
                latch.countDown()
            }
        }

        latch.await(5, TimeUnit.SECONDS)
    }
    private fun getUser(id: String, callback: (UserData?) -> Unit) {
        database = FirebaseDatabase.getInstance().getReference("Users")
        database.orderByChild("uid").equalTo(id).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val userDataSnapshot = snapshot.children.firstOrNull()

                val user = userDataSnapshot?.let {
                    UserData(
                        it.child("uid").value.toString(),
                        it.child("name").value.toString(),
                        it.child("email").value.toString(),
                        it.child("imageURL").value.toString(),
                        it.child("contactNo").value.toString(),
                        it.child("country").value.toString(),
                        it.child("city").value.toString(),

                    )
                }
                callback(user)
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
}
