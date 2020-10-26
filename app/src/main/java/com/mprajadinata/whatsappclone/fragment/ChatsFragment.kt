package com.mprajadinata.whatsappclone.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.mprajadinata.whatsappclone.MainActivity
import com.mprajadinata.whatsappclone.R
import com.mprajadinata.whatsappclone.activity.ConversationActivity
import com.mprajadinata.whatsappclone.adapter.ChatsAdapter
import com.mprajadinata.whatsappclone.listener.ChatClickListener
import com.mprajadinata.whatsappclone.listener.FailureCallback
import com.mprajadinata.whatsappclone.util.Chat
import com.mprajadinata.whatsappclone.util.DATA_CHATS
import com.mprajadinata.whatsappclone.util.DATA_USERS
import com.mprajadinata.whatsappclone.util.DATA_USER_CHATS
import kotlinx.android.synthetic.main.fragment_chats.*

class ChatsFragment : Fragment(), ChatClickListener {

    private var chatsAdapter = ChatsAdapter(arrayListOf())
    private val firebaseDb = FirebaseFirestore.getInstance()
    private val userId = FirebaseAuth.getInstance().currentUser?.uid
    private var failureCallback: FailureCallback? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chats, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        chatsAdapter.setOnItemClickListener(this)
        rv_chats.apply {
            setHasFixedSize(false)
            layoutManager = LinearLayoutManager(context)
            adapter = chatsAdapter
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

        }

        firebaseDb.collection(DATA_USERS).document(userId!!)
            .addSnapshotListener { documentSnapshot, firebaseFirestoreException ->
                if (firebaseFirestoreException == null) {
                    refreshChat()
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (userId.isNullOrEmpty()) {
            failureCallback?.userError()
        }
    }

    fun newChat(patnerId: String) {
        firebaseDb.collection(DATA_USERS).document(userId!!).get()
            .addOnSuccessListener { userDocument ->
                val userChatPartners = hashMapOf<String, String>()
                if (userDocument[DATA_USER_CHATS] != null &&
                    userDocument[DATA_USER_CHATS] is HashMap<*, *>
                ) {
                    val userDocumentMap = userDocument[DATA_USER_CHATS] as HashMap<String, String>
                    if (userDocumentMap.containsKey(patnerId)) {
                        return@addOnSuccessListener

                    } else {
                        userChatPartners.putAll(userDocumentMap)
                    }
                }

                firebaseDb.collection(DATA_USERS)
                    .document(patnerId)
                    .get()
                    .addOnSuccessListener { partnerDocument ->
                        val partnerChatPartners = hashMapOf<String, String>()
                        if (partnerDocument[DATA_USER_CHATS] != null &&
                            partnerDocument[DATA_USER_CHATS] is HashMap<*, *>
                        ) {
                            val partnerDocumentMap =
                                partnerDocument[DATA_USER_CHATS] as HashMap<String, String>
                            partnerChatPartners.putAll(partnerDocumentMap)
                        }

                        val chatParticipants = arrayListOf(userId, patnerId)
                        val chat = Chat(chatParticipants)
                        val chatRef = firebaseDb.collection(DATA_CHATS).document()
                        val userRef = firebaseDb.collection(DATA_USERS).document(userId)
                        val partnerRef =
                            firebaseDb.collection(DATA_USERS).document(patnerId)
                        userChatPartners[patnerId] = chatRef.id
                        partnerChatPartners[userId] = chatRef.id
                        val batch = firebaseDb.batch()
                        batch.set(chatRef, chat)
                        batch.update(userRef, DATA_USER_CHATS, userChatPartners)
                        batch.update(partnerRef, DATA_USER_CHATS, partnerChatPartners)
                        batch.commit()
                    }

                    .addOnFailureListener { e ->
                        e.printStackTrace()
                    }
            }

            .addOnFailureListener { e ->
                e.printStackTrace()
            }

    }

    private fun refreshChat() {
        firebaseDb.collection(DATA_USERS).document(userId!!).get()
            .addOnSuccessListener {
                if (it.contains(DATA_USER_CHATS)) {
                    val partners = it[DATA_USER_CHATS]
                    val chats = arrayListOf<String>()
                    for (partner in (partners as HashMap<String, String>).keys) {
                        if (partners[partner] != null) {
                            chats.add(partners[partner]!!)
                        }
                    }

                    chatsAdapter.updateChats(chats)
                }
            }

            .addOnFailureListener { e ->
                e.printStackTrace()
            }
    }

    fun setFailureCallbackListener(listener: MainActivity) {

        failureCallback = listener

    }

    override fun onChatClicked(
        chatId: String?,
        otherUserId: String?,
        chatsImageUrl: String?,
        chatName: String?
    ) {

        startActivity(
            ConversationActivity.newIntent(
                context,
                chatId,
                chatsImageUrl,
                otherUserId,
                chatName
            )
        )
    }
}