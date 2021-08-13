package com.example.androidacademyproject.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.androidacademyproject.room.RoomContract

@Entity(
    tableName = RoomContract.Genre.TABLE_NAME,
    indices = [Index(RoomContract.Genre.COLUMN_NAME_ID)]
)
data class GenreEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = RoomContract.Genre.COLUMN_NAME_ID)
    val id: Int,
    @ColumnInfo(name = RoomContract.Genre.COLUMN_NAME_NAME)
    val name: String
)
