package com.mprajadinata.whatsappclone.listener

import com.mprajadinata.whatsappclone.util.StatusListElement

interface ContactsClickListener {
    fun onContactClicked(name: String?, phone: String?)
}

interface ChatClickListener {
    fun onChatClicked(
        name: String?,
        otherUserId: String?,
        chatsImageUrl: String?,
        chatName: String?
    )
}

interface StatusItemClickListener {
    fun onItemClicked(statusElement: StatusListElement)

}
