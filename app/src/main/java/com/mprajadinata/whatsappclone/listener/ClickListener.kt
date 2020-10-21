package com.mprajadinata.whatsappclone.listener

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