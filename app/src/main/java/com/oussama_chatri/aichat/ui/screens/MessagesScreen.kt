package com.oussama_chatri.aichat.ui.screens

import android.graphics.Bitmap
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.oussama_chatri.aichat.UiState
import com.oussama_chatri.aichat.data.offline.localStorage.entities.Message
import com.oussama_chatri.aichat.ui.components.Message
import com.oussama_chatri.aichat.ui.components.MessageInput
import com.oussama_chatri.aichat.ui.viewModels.MessageViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun MessagesScreen(
    viewModel: MessageViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    // Collecting the messages from the repository
    val messages = remember { mutableStateListOf<Message>() }
    LaunchedEffect(Unit) {
        viewModel.getAllMessages().collectLatest { messageList ->
            messages.clear()
            messages.addAll(messageList)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        verticalArrangement = Arrangement.Bottom
    ) {
        // Messages List
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            reverseLayout = true // Reverse layout to have the most recent messages at the bottom
        ) {
            items(messages) { message ->
                Message(
                    text = message.text,
                    sender = message.sender,
                    timestamp = message.timestamp
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        // Message Input Field
        MessageInput(onMessageSent = { userMessage ->
            val message = com.oussama_chatri.aichat.data.offline.localStorage.entities.Message(
                text = userMessage,
                sender = "User", // Assuming sender is always "User" here
                timestamp = System.currentTimeMillis()
            )
            viewModel.upsert(message)

            // Optionally send the prompt to the AI model
            viewModel.sendPrompt(
                bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888),
                prompt = userMessage
            )
        })
    }

    // Handling UI State changes
    when (uiState) {
        is UiState.Loading -> {
            CircularProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
        }
        is UiState.Error -> {
            Text(
                text = "Error: ${(uiState as UiState.Error).errorMessage}",
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                textAlign = TextAlign.Center
            )
        }
        is UiState.Success -> {
            // Optionally, display the response from the generative model as a new message
            val responseMessage = com.oussama_chatri.aichat.data.offline.localStorage.entities.Message(
                text = (uiState as UiState.Success).outputText,
                sender = "AI", // Assuming "AI" as the sender for the generative model
                timestamp = System.currentTimeMillis()
            )
            viewModel.upsert(responseMessage)
        }
        else -> {
            // Do nothing for initial state
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MessagesScreenPreview() {
    MessagesScreen()
}
