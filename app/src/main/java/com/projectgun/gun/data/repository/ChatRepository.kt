package com.projectgun.gun.data.repository

import com.projectgun.gun.data.local.dao.ChatDao
import com.projectgun.gun.data.model.ChatMessage
import com.projectgun.gun.data.model.MessageSender
import kotlinx.coroutines.flow.Flow

class ChatRepository(private val chatDao: ChatDao) {
    val allMessages: Flow<List<ChatMessage>> = chatDao.getAllMessages()

    suspend fun getRecentMessages(limit: Int = 20): List<ChatMessage> =
        chatDao.getRecentMessages(limit)

    suspend fun postUserMessage(text: String, intent: String? = null): Long {
        return chatDao.insertMessage(
            ChatMessage(
                sender = MessageSender.USER,
                content = text,
                intentDetected = intent,
                timestamp = System.currentTimeMillis()
            )
        )
    }

    suspend fun postGunMessage(text: String, actionPrompt: String? = null): Long {
        return chatDao.insertMessage(
            ChatMessage(
                sender = MessageSender.GUN,
                content = text,
                timestamp = System.currentTimeMillis(),
                actionPrompt = actionPrompt
            )
        )
    }

    suspend fun clearHistory() = chatDao.clearMessages()
}
