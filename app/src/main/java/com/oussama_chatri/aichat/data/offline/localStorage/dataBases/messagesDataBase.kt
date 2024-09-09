package com.oussama_chatri.aichat.data.offline.localStorage.dataBases

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.oussama_chatri.aichat.data.offline.localStorage.daos.MessagesDao
import com.oussama_chatri.aichat.data.offline.localStorage.entities.Message

@Database(entities = [Message::class], version = 1, exportSchema = false)
abstract class MessagesDataBase : RoomDatabase() {

    abstract fun messagesDao(): MessagesDao

    companion object {
        @Volatile
        private var Instance: MessagesDataBase? = null

        fun getDatabase(context: Context): MessagesDataBase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, MessagesDataBase::class.java, "Messages")
                    .fallbackToDestructiveMigration()
                    .build().also { Instance = it }
            }
        }
    }
}