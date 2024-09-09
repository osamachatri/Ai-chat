package com.oussama_chatri.aichat.data.offline.localStorage.repositories

import com.oussama_chatri.aichat.data.offline.localStorage.daos.MessagesDao
import com.oussama_chatri.aichat.data.offline.localStorage.entities.Message
import kotlinx.coroutines.flow.Flow

class MessagesRepository(
    val MessagesDao : MessagesDao
) : MessagesDao {

    override suspend fun upsertMessage(message: Message) = MessagesDao.upsertMessage(message)

    override suspend fun deleteMessage(message: Message) = MessagesDao.deleteMessage(message)

    override fun getAllMessages(): Flow<List<Message>> = MessagesDao.getAllMessages()
}