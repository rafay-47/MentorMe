package com.AbdulRafay.i212582

import android.app.AlertDialog
import android.content.Intent
import android.media.MediaRecorder
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import java.io.File
import java.io.IOException
import java.util.UUID

class JohnCooperChat : AppCompatActivity(), MessageAdapter.MessageClickListener {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var messageRecyclerView: RecyclerView
    private lateinit var messageBox: EditText
    private lateinit var messageList: ArrayList<Message>
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var ReceiverName: TextView
    private lateinit var mentor: Mentors
    private lateinit var imageUri: String
    private lateinit var MessageimageUri: Uri

    private var Room: String? = null
    val takePicture = registerForActivityResult(ActivityResultContracts.TakePicture()) { success: Boolean ->
        if (success) {
            Toast.makeText(this, "Picture taken successfully", Toast.LENGTH_SHORT).show()
            sendMessage(MessageimageUri.toString(),"image")
        }

    }

    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK && result.data != null) {
            val selectedImageUri: Uri? = result.data?.data
            imageUri = selectedImageUri.toString()

            selectedImageUri?.let { uri ->
                val uniqueID = UUID.randomUUID().toString()
                val storageRef = FirebaseStorage.getInstance().reference.child("Chats/images/${uniqueID}")
                storageRef.putFile(uri).addOnSuccessListener { taskSnapshot ->
                    taskSnapshot.storage.downloadUrl.addOnSuccessListener { uri ->
                        val imageURL = uri.toString()
                        sendMessage(imageURL,"image")
                    }
                }.addOnFailureListener { exception ->
                    Log.e("GalleryClass", "Upload failed", exception)
                }
            }
        }
    }

    private val pickVideoLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK && result.data != null) {
            val selectedImageUri: Uri? = result.data?.data
            imageUri = selectedImageUri.toString()

            selectedImageUri?.let { uri ->
                val uniqueID = UUID.randomUUID().toString()
                val storageRef = FirebaseStorage.getInstance().reference.child("Chats/Videos/${uniqueID}")
                storageRef.putFile(uri).addOnSuccessListener { taskSnapshot ->
                    taskSnapshot.storage.downloadUrl.addOnSuccessListener { uri ->
                        val imageURL = uri.toString()
                        sendMessage(imageURL,"video")
                    }
                }.addOnFailureListener { exception ->
                    Log.e("GalleryClass", "Upload failed", exception)
                }
            }
        }
    }

    private val pickFileLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK && result.data != null) {
            val selectedFileUri: Uri? = result.data?.data
            imageUri = selectedFileUri.toString()

            selectedFileUri?.let { uri ->
                val uniqueID = UUID.randomUUID().toString()
                val storageRef = FirebaseStorage.getInstance().reference.child("Chats/Files/${uniqueID}")
                storageRef.putFile(uri).addOnSuccessListener { taskSnapshot ->
                    taskSnapshot.storage.downloadUrl.addOnSuccessListener { uri ->
                        val imageURL = uri.toString()
                        sendMessage(imageURL,"file")
                    }
                }.addOnFailureListener { exception ->
                    Log.e("GalleryClass", "Upload failed", exception)
                }
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.john_cooper_chat)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().getReference("Chats")

        mentor = intent.getParcelableExtra<Mentors>("mentor")!!



        ReceiverName = findViewById(R.id.name)
        if (ReceiverName.text == "")
            ReceiverName.text = mentor.name

        val senderid = auth.currentUser?.uid
        Room = mentor.id + senderid


        messageRecyclerView = findViewById(R.id.chatRecyclerView)
        messageBox = findViewById(R.id.messageBox)
        messageList = ArrayList()
        messageAdapter = MessageAdapter(this, messageList, this)


        val layoutManager = LinearLayoutManager(this)
        layoutManager.stackFromEnd = true
        messageRecyclerView.layoutManager = layoutManager
        messageRecyclerView.adapter = messageAdapter


        database.child(Room!!).child("messages").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                messageList.clear()
                for (postSnapshot in snapshot.children) {
                    val message = postSnapshot.getValue(Message::class.java)
                    message?.let { messageList.add(it) }
                }
                messageAdapter.notifyDataSetChanged()
                messageRecyclerView.scrollToPosition(messageList.size - 1)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("JohnCooperChat", "Failed to read messages: ${error.message}")
            }
        })
        MessageimageUri = createImageURI()
        findViewById<ImageView>(R.id.camera).setOnClickListener {
            takePicture.launch(MessageimageUri)

        }
        findViewById<ImageView>(R.id.file).setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "*/*"  // Set MIME type to filter files (optional)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            pickFileLauncher.launch(intent)

        }
        findViewById<ImageView>(R.id.gallery).setOnClickListener {
            openGallery()
        }
        val img2: ImageView = findViewById(R.id.imageView21)
        img2.setOnClickListener{
            startActivity(Intent(this,VoiceCall::class.java))
        }

        val img3: ImageView = findViewById(R.id.imageView20)
        img3.setOnClickListener{
            startActivity(Intent(this,VideoCall::class.java))
        }

        findViewById<ImageView>(R.id.mic).setOnClickListener {
            startRecording()
        }
        findViewById<ImageView>(R.id.micOff).setOnClickListener {
            stopRecording()
        }

        findViewById<ImageView>(R.id.send).setOnClickListener {
            val messageText = messageBox.text.toString()
            sendMessage(messageText,"text")

        }

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_app_bar)
        bottomNavigationView.selectedItemId = R.id.nav_chat

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_search -> startActivity(Intent(applicationContext, SearchLayout::class.java))
                R.id.nav_add -> startActivity(Intent(applicationContext, AddMentor::class.java))
                R.id.nav_home -> startActivity(Intent(applicationContext, MainMenu::class.java))
                R.id.nav_profile -> startActivity(Intent(applicationContext, ProfileLayout::class.java))
                R.id.nav_chat -> return@setOnNavigationItemSelectedListener true
            }
            overridePendingTransition(0, 0)
            true
        }
    }

    private var mediaRecorder: MediaRecorder? = null
    private var audioFile: File? = null

    fun showRecordingDialog() {
        val builder = AlertDialog.Builder(this@JohnCooperChat)
        builder.setTitle("Audio Recorder")

        builder.setPositiveButton("Start") { dialog, which ->
            startRecording()
        }

        builder.setNegativeButton("Cancel") { dialog, which ->
            dialog.dismiss()
        }

        builder.setOnDismissListener {
            stopRecording()
        }

        builder.show()
    }

    private fun startRecording() {
        try {
            audioFile = File(this.externalCacheDir?.absolutePath, "audio_${UUID.randomUUID()}.3gp")
            mediaRecorder = MediaRecorder().apply {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
                setOutputFile(audioFile?.absolutePath)
                setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
                prepare()
                start()
            }
        } catch (e: IOException) {
            Toast.makeText(this@JohnCooperChat, "Recording failed", Toast.LENGTH_SHORT).show()
        }
    }

    private fun stopRecording() {
        try {
            mediaRecorder?.apply {
                stop()
                release()
            }
            if (audioFile != null) {
                uploadAudioToFirestore(audioFile!!)
            }
        } catch (e: RuntimeException) {
            // Handle stop failure
            Toast.makeText(this@JohnCooperChat, "Failed to stop recording", Toast.LENGTH_SHORT).show()
        }
    }


    private fun uploadAudioToFirestore(audioFile: File) {
        val uniqueID = UUID.randomUUID().toString()
        val uri = Uri.fromFile(audioFile)
        val storageRef = FirebaseStorage.getInstance().reference.child("images/Chats/${uniqueID}")
        storageRef.putFile(uri).addOnSuccessListener { taskSnapshot ->
            taskSnapshot.storage.downloadUrl.addOnSuccessListener { uri ->
                val audioURL = uri.toString()
                sendMessage(audioURL,"text")
            }
        }.addOnFailureListener { exception ->
            Log.e("GalleryClass", "Upload failed", exception)
        }
    }

    private fun sendMessage(message: String, messageType:String) {
        val senderid = auth.currentUser?.uid
        val key = database.child(Room!!).child("messages").push().key
        if (!key.isNullOrEmpty() && message.isNotBlank()) {
            val message = Message(key, message,messageType, senderid, mentor.id)
            database.child(Room!!).child("messages").child(key).setValue(message)
            messageBox.setText("")
        }
    }

    override fun onMessageClick(view: View, message: Message) {
        val popupMenu = PopupMenu(this, view)
        popupMenu.inflate(R.menu.message_options)
        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.edit_message -> {
                    showEditMessageDialog(message)
                    true
                }
                R.id.remove_message -> {
                    database.child(Room!!).child("messages").child(message.messageid!!).removeValue()
                    true
                }
                else -> false
            }
        }
        popupMenu.gravity = Gravity.END
        popupMenu.show()
    }
    private fun openGallery() {
        val dialog = AlertDialog.Builder(this@JohnCooperChat)
            .setTitle("Image or Video")
            .setPositiveButton("Image") { dialog, _ ->
                val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                pickImageLauncher.launch(intent)
                dialog.dismiss()
            }
            .setNegativeButton("Video") { dialog, _ ->
                val intent = Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
                pickVideoLauncher.launch(intent)
                dialog.dismiss()
            }.create()
        dialog.show()

    }
    private fun showEditMessageDialog(message: Message) {
        val editText = EditText(this)
        editText.setText(message.message)
        val dialog = AlertDialog.Builder(this@JohnCooperChat)
            .setTitle("Edit Message")
            .setView(editText)
            .setPositiveButton("Save") { dialog, _ ->
                val updatedMessageText = editText.text.toString()
                if (updatedMessageText.isNotBlank()) {
                    val updatedMessage = mapOf(
                        "messageid" to message.messageid,
                        "message" to updatedMessageText,
                        "receiverid" to message.receiverid,
                        "senderid" to message.senderid
                    )
                    database.child(Room!!).child("messages").child(message.messageid!!)
                        .updateChildren(updatedMessage)
                        .addOnSuccessListener {
                            Toast.makeText(this@JohnCooperChat, "Message updated", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener {
                            Toast.makeText(this@JohnCooperChat, "Failed to update message", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    Toast.makeText(this@JohnCooperChat, "Message cannot be empty", Toast.LENGTH_SHORT).show()
                }
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .create()

        dialog.show()
    }
    private fun createImageURI(): Uri {
        val image = File(filesDir, "camera_photos.png")
        return FileProvider.getUriForFile(this, "com.AbdulRafay.i212582.FileProvider", image)
    }

}