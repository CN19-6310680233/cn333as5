package com.example.phonebook.database.utils

import android.content.Context
import androidx.room.*
import com.example.mynotes.database.schema.TagDao
import com.example.phonebook.database.models.ContactDbModel
import com.example.phonebook.database.models.TagDbModel
import com.example.phonebook.database.schema.ContactDao

@Database(entities = [ContactDbModel::class, TagDbModel::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun contactDao(): ContactDao
    abstract fun tagDao(): TagDao

    companion object {
        private const val DATABASE_NAME = "phonebook-db"
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            var instance = INSTANCE
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DATABASE_NAME
                ).build()

                INSTANCE = instance
            }

            return instance
        }
    }
}