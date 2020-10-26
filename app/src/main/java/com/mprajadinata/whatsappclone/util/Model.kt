package com.mprajadinata.whatsappclone.util

data class User(

    val email: String? = "",
    val phone: String? = "",
    val name: String? = "",
    val imageUrl: String? = "",
    val status: String? = "",
    val statusUrl: String? = "",
    val statusTime: String? = ""

)

data class Contact(
    val name: String?,
    val phone: String?
)

data class Chat(
    val chatParticipants: ArrayList<String>
)

data class Message(
    val sentBy: String? = "",
    val message: String? = "",
    val messageTime: Long? = 0
)