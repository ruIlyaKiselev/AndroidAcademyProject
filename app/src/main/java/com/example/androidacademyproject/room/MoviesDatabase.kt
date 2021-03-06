package com.example.androidacademyproject.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.androidacademyproject.room.entity.ActorEntity
import com.example.androidacademyproject.room.entity.GenreEntity
import com.example.androidacademyproject.room.entity.MovieEntity
import com.example.androidacademyproject.room.entity.UpdateTimeEntity

@Database(entities = [
    ActorEntity::class,
    GenreEntity::class,
    MovieEntity::class,
    UpdateTimeEntity::class],
        version = 1)
@TypeConverters(Converters::class)
abstract class MoviesDatabase: RoomDatabase() {
    abstract val dao: MoviesDao

    companion object {
        fun create(applicationContext: Context): MoviesDatabase {

            val database = Room.databaseBuilder(
                    applicationContext,
                    MoviesDatabase::class.java,
                    RoomContract.DATABASE_NAME
            )
                    .fallbackToDestructiveMigration()
                    .build()

            return database
        }
    }
}