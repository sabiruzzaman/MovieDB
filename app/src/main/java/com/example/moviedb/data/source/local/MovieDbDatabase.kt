package com.example.moviedb.data.source.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.moviedb.data.model.RoomSavedItems


@Database
    (
    entities = [RoomSavedItems::class],
    version = 2
)
abstract class MovieDbDatabase : RoomDatabase() {

    abstract fun getSavedMoviesDao(): MovieDbDao

    companion object {
        @Volatile
        private var instance: MovieDbDatabase? = null
        private val Lock = Any()


        operator fun invoke(context: Context) = instance ?: synchronized(Lock) {
            instance ?: createDatabase(context).also {
                instance = it
            }
        }

        private fun createDatabase(context: Context) =

            Room.databaseBuilder(
                context.applicationContext,
                MovieDbDatabase::class.java,
                "movie_db.db"
            ).allowMainThreadQueries().build()
    }
}