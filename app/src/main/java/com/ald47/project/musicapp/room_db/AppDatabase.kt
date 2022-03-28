package com.ald47.project.musicapp.room_db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ald47.project.musicapp.modeles.Album
import com.ald47.project.musicapp.modeles.Artiste

@Database(entities = [Album::class,Artiste::class], version = 3)

abstract class AppDatabase : RoomDatabase() {

    abstract fun musicAppDao(): MusicAppDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context,
            AppDatabase::class.java, "music_app.db"
        ).build()
    }

}