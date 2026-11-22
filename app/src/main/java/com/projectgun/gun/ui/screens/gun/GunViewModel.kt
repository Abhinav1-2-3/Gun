package com.projectgun.gun.ui.screens.gun

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.projectgun.gun.data.model.ChatMessage
import com.projectgun.gun.data.repository.ChatRepository
import com.projectgun.gun.domain.context.ContextEngine
import com.projectgun.gun.domain.personality.PersonalityEngine
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class GunUiState(
    val messages: List<ChatMessage> = emptyList(),
    val isGunTyping: Boolean = false,
    val quickPrompts: List<String> = listOf(
        "Analyse my progress",
        "Evaluate my consistency",
        "Prepare today's workout",
        "Review my recovery",
        "Show my personal records",
        "I'm feeling exhausted",
        "Who are you, Gun?"
    )
)

class GunViewModel(
    private val chatRepository: ChatRepository,
    private val contextEngine: ContextEngine,
    private val personalityEngine: PersonalityEngine
) : ViewModel() {

    private val _uiState = MutableStateFlow(GunUiState())
    val uiState: StateFlow<GunUiState> = _uiState.asStateFlow()

    init {
        observeMessages()
    }

    private fun observeMessages() {
        viewModelScope.launch {
            chatRepository.allMessages.collect { list ->
                if (list.isEmpty()) {
                    // Seed initial Gun greeting
                    val context = contextEngine.assembleContext()
                    val initialGreeting = "I am Gun Park. If you stepped into my training system, it means you want results instead of sweet talk. Speak clearly. What do you have to report or ask?"
                    chatRepository.postGunMessage(initialGreeting)
                } else {
                    _uiState.value = _uiState.value.copy(messages = list)
                }
            }
        }
    }

    fun sendMessage(userText: String) {
        if (userText.isBlank()) return

        viewModelScope.launch {
            // Post user message
            chatRepository.postUserMessage(userText)

            // Assemble real local context
            val context = contextEngine.assembleContext()

            // Run personality engine
            val gunResponse = personalityEngine.processInput(userText, context)

            // Post Gun reply
            chatRepository.postGunMessage(gunResponse.reply, gunResponse.actionPrompt)
        }
    }

    fun clearChat() {
        viewModelScope.launch {
            chatRepository.clearHistory()
        }
    }
}

class GunViewModelFactory(
    private val chatRepository: ChatRepository,
    private val contextEngine: ContextEngine,
    private val personalityEngine: PersonalityEngine
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return GunViewModel(chatRepository, contextEngine, personalityEngine) as T
    }
}
