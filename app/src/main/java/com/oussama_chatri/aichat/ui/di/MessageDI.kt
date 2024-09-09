package com.oussama_chatri.aichat.ui.di

import android.content.Context
import com.oussama_chatri.aichat.data.offline.localStorage.dataBases.MessagesDataBase
import com.oussama_chatri.aichat.data.offline.localStorage.repositories.MessagesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MessageDI {

    @Singleton
    @Provides
    fun getMessagesRepository(
        @ApplicationContext context : Context
    ): MessagesRepository{
        return MessagesRepository(MessagesDataBase.getDatabase(context).messagesDao())
    }
}