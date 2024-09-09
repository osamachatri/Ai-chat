package com.oussama_chatri.aichat.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun Message(
    text: String,
    sender: String,
    timestamp: Long = System.currentTimeMillis()
) {
    val dateFormatter = remember {
        java.text.SimpleDateFormat("HH:mm", java.util.Locale.getDefault())
    }
    val time = dateFormatter.format(java.util.Date(timestamp))

    Column(
        Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalAlignment = if (sender == "User") Alignment.End else Alignment.Start
    ) {
        Card(
            shape = RoundedCornerShape(8.dp),
            backgroundColor = if (sender == "User") Color(0xFF5BA7FF) else Color.LightGray,
            modifier = Modifier
                .padding(4.dp)
                .wrapContentWidth()
        ) {
            Column(
                Modifier
                    .padding(8.dp)
            ) {
                Text(text = text, color = if (sender == "User") Color.White else Color.Black)
                Text(
                    text = time,
                    style = MaterialTheme.typography.caption,
                    color = if (sender == "User") Color.White.copy(alpha = 0.7f) else Color.Black.copy(alpha = 0.7f),
                    modifier = Modifier.align(Alignment.End)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MessagePreview() {
    Column {
        Message(
            text = "Hello, today is Sunday as you know, so gimme new code for the last function which I sent you.",
            sender = "User"
        )
        Message(
            text = "Sure, here's the updated code for you!",
            sender = "Other"
        )
    }
}
