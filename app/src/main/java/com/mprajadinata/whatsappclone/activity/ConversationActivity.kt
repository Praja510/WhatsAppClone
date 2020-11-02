package com.mprajadinata.whatsappclone.activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.mprajadinata.whatsappclone.MainActivity
import com.mprajadinata.whatsappclone.R
import com.mprajadinata.whatsappclone.adapter.ConversationAdapter
import com.mprajadinata.whatsappclone.util.*
import kotlinx.android.synthetic.main.activity_conversation.*

class ConversationActivity : AppCompatActivity() {

    private val userId = FirebaseAuth.getInstance().currentUser?.uid
    private val conversationAdapter = ConversationAdapter(arrayListOf(), userId)
    private val firebaseDb = FirebaseFirestore.getInstance()
    private var chatId: String? = null
    private var imageUrl: String? = null
    private var otherUserId: String? = null
    private var chatName: String? = null
    private var phone: String? = null

    companion object {
        private val PARAM_CHAT_ID = "Chat_id"
        private val PARAM_IMAGE_URL = "Image_url"
        private val PARAM_OTHER_USER_ID = "Other_user_id"
        private val PARAM_CHAT_NAME = "Chat_name"

        fun newIntent(
            context: Context?, chatId: String?, imageUrl: String?, otherUserId: String?,
            chatName: String?
        ): Intent {
            val intent = Intent(context, ConversationActivity::class.java)
            intent.putExtra(PARAM_CHAT_ID, chatId)
            intent.putExtra(PARAM_IMAGE_URL, imageUrl)
            intent.putExtra(PARAM_OTHER_USER_ID, otherUserId)
            intent.putExtra(PARAM_CHAT_NAME, chatName)

            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_conversation)

//        btn_back_conversation.setOnClickListener {
//            startActivity(Intent(this, MainActivity::class.java))
//        }

        setSupportActionBar(toolbar_conversation)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        toolbar_conversation.setNavigationOnClickListener { onBackPressed() }

        chatId = intent.extras?.getString(PARAM_CHAT_ID)
        imageUrl = intent.extras?.getString(PARAM_IMAGE_URL)
        chatName = intent.extras?.getString(PARAM_CHAT_NAME)
        otherUserId = intent.extras?.getString(PARAM_OTHER_USER_ID)

        if (chatId.isNullOrEmpty() || userId.isNullOrEmpty()) {
            Toast.makeText(this, "Chat room error", Toast.LENGTH_SHORT).show()
            finish()

        }

        populateImage(this, imageUrl, img_toolbar, R.drawable.ic_user)
        txt_toolbar.text = chatName
        rv_message.apply {
            setHasFixedSize(false)
            layoutManager = LinearLayoutManager(context)
            adapter = conversationAdapter
        }

        firebaseDb.collection(DATA_CHATS)
            .document(chatId!!)
            .collection(DATA_CHAT_MESSAGE)
            .orderBy(DATA_CHAT_MESSAGE_TIME)
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                if (firebaseFirestoreException != null) {
                    firebaseFirestoreException.printStackTrace()
                    return@addSnapshotListener

                } else {
                    if (querySnapshot != null) {
                        for (change in querySnapshot.documentChanges) {

                            when (change.type) {
                                DocumentChange.Type.ADDED -> {
                                    val message = change.document.toObject(Message::class.java)
                                    if (message != null) {
                                        conversationAdapter.addMessage(message)
                                        rv_message.post {
                                            rv_message.smoothScrollToPosition(conversationAdapter.itemCount - 1)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

        imbtn_send.setOnClickListener {
            if (!edt_message.text.isNullOrEmpty()) {
                val message =
                    Message(userId, edt_message.text.toString(), System.currentTimeMillis())

                firebaseDb.collection(DATA_CHATS)
                    .document(chatId!!)
                    .collection(DATA_CHAT_MESSAGE)
                    .document()
                    .set(message)
                edt_message.setText("", TextView.BufferType.EDITABLE)
            }
        }

        firebaseDb.collection(DATA_USERS).document(userId!!).get()
            .addOnSuccessListener {
                val user = it.toObject(User::class.java)
                phone = user?.phone
            }

            .addOnFailureListener { e ->
                e.printStackTrace()
                finish()
            }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_conversation, menu)
        return true

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_profile -> {
                startActivity(Intent(this, ProfilActivity::class.java))
            }

            R.id.action_call -> {
                startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phone")))
            }
        }

        return super.onOptionsItemSelected(item)
    }
}