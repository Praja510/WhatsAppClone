package com.mprajadinata.whatsappclone.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mprajadinata.whatsappclone.R
import com.mprajadinata.whatsappclone.fragment.ChatsFragment
import com.mprajadinata.whatsappclone.listener.ChatClickListener
import com.mprajadinata.whatsappclone.util.populateImage
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_chats.*

class ChatsAdapter(val chats: ArrayList<String>) :
    RecyclerView.Adapter<ChatsAdapter.ChatsViewHolder>() {

    private var chatClickListener: ChatClickListener? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = ChatsViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_chats, parent, false)
    )

    override fun getItemCount() = chats.size


    override fun onBindViewHolder(holder: ChatsAdapter.ChatsViewHolder, position: Int) {
        holder.bindItem(chats[position], chatClickListener)
    }

    fun setOnItemClickListener(listener: ChatsFragment) {
        chatClickListener = listener
        notifyDataSetChanged()
    }

    fun updateChats(updateChats: ArrayList<String>) {
        chats.clear()
        chats.addAll(updateChats)
        notifyDataSetChanged()
    }

    class ChatsViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView), LayoutContainer {

        fun bindItem(chatId: String, listener: ChatClickListener?) {
            populateImage(img_chats.context, "", img_chats, R.drawable.ic_user)
            txt_chats.text = chatId

        }
    }
}