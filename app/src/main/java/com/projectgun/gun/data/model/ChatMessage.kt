package com.projectgun.gun.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class MessageSender {
    USER, GUN
}

@Entity(tableName = "chat_messages")
data class ChatMessage(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val sender: MessageSender,
    val content: String,
    val intentDetected: String? = null,
    val timestamp: Long = System.currentTimeMillis(),
    val actionPrompt: String? = null
)
