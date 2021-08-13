package com.example.androidacademyproject.room.entity

import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.androidacademyproject.room.RoomContract

@Entity(
    tableName = RoomContract.Actor.TABLE_NAME,
    indices = [Index(RoomContract.Actor.COLUMN_NAME_ID)]
)
data class ActorEntity (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = RoomContract.Actor.COLUMN_NAME_ID)
    val id: Int = 0,
    @ColumnInfo(name = RoomContract.Actor.COLUMN_NAME_NAME)
    val name: String,
    @ColumnInfo(name = RoomContract.Actor.COLUMN_NAME_IMAGE_BITMAP)
    val imageBitmap: Bitmap
)