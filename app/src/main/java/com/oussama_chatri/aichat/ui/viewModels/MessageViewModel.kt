package com.oussama_chatri.aichat.ui.viewModels

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import com.oussama_chatri.aichat.BuildConfig
import com.oussama_chatri.aichat.UiState
import com.oussama_chatri.aichat.data.offline.localStorage.entities.Message
import com.oussama_chatri.aichat.data.offline.localStorage.repositories.MessagesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MessageViewModel @Inject constructor(
    private val messagesRepository: MessagesRepository
) : ViewModel() {
    private val _uiState: MutableStateFlow<UiState> =
        MutableStateFlow(UiState.Initial)
    val uiState: StateFlow<UiState> =
        _uiState.asStateFlow()

    fun getAllMessages() : Flow<List<Message>> {
        return messagesRepository.getAllMessages()
    }

    fun upsert(message: Message){
        viewModelScope.launch {
            messagesRepository.upsertMessage(message)
        }
    }

    fun delete(message: Message){
        viewModelScope.launch {
            messagesRepository.deleteMessage(message)
        }
    }

    private val generativeModel = GenerativeModel(
        modelName = "gemini-1.5-flash",
        apiKey = BuildConfig.apiKey
    )

    fun sendPrompt(
        bitmap: Bitmap,
        prompt: String
    ) {
        _uiState.value = UiState.Loading

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = generativeModel.generateContent(
                    content {
                        text(prompt)
                    }
                )
                response.text?.let { outputContent ->
                    _uiState.value = UiState.Success(outputContent)
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.localizedMessage ?: "")
            }
        }
    }
}