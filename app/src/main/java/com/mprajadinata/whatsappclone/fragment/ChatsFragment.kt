package com.mprajadinata.whatsappclone.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.mprajadinata.whatsappclone.R
import com.mprajadinata.whatsappclone.adapter.ChatsAdapter
import com.mprajadinata.whatsappclone.listener.ChatClickListener
import kotlinx.android.synthetic.main.fragment_chats.*

class ChatsFragment : Fragment(), ChatClickListener {

    private var chatsAdapter = ChatsAdapter(arrayListOf())

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

        var chatList = arrayListOf(
            "Chat 1",
            "Chat 2",
            "Chat 3",
            "Chat 4",
            "Chat 5",
            "Chat 6",
            "Chat 7",
            "Chat 8",
            "Chat 9",
            "Chat 10",
            "Chat 11",
            "Chat 12",
            "Chat 13",
            "Chat 14",
            "Chat 15"
        )

        chatsAdapter.updateChats(chatList)

    }

    override fun onChatClicked(
        name: String?,
        otherUserId: String?,
        chatsImageUrl: String?,
        chatName: String?
    ) {

        Toast.makeText(context, "$name clicked", Toast.LENGTH_LONG).show()
    }
}