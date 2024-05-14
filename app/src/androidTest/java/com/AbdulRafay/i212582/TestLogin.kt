
import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FirebaseAuthTest {

    private lateinit var auth: FirebaseAuth

    @Before
    fun setup() {
        val context: Context = ApplicationProvider.getApplicationContext()
        FirebaseApp.initializeApp(context)

        auth = Firebase.auth
    }

    @Test
    fun testFirebaseSignInWithExistingUser() {
        auth.signInWithEmailAndPassword("a@g.com", "12345678")
            .addOnCompleteListener { signInTask ->
                assertTrue(signInTask.isSuccessful)
            }
    }
}
