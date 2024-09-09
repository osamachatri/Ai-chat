package com.oussama_chatri.aichat.data.offline.localStorage.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.oussama_chatri.aichat.data.offline.localStorage.entities.Message
import kotlinx.coroutines.flow.Flow

@Dao
interface MessagesDao {
    @Upsert
    suspend fun upsertMessage(message: Message)

    @Delete
    suspend fun deleteMessage(message: Message)

    @Query("SELECT * FROM messages ORDER BY timestamp DESC")
    fun getAllMessages(): Flow<List<Message>>

}