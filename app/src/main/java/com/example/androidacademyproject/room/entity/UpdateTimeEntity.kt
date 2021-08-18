package com.example.androidacademyproject.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.androidacademyproject.room.RoomContract

@Entity(
        tableName = RoomContract.UpdateTime.TABLE_NAME,
        indices = [Index(RoomContract.UpdateTime.COLUMN_NAME_ID)]
)
data class UpdateTimeEntity (
        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = RoomContract.UpdateTime.COLUMN_NAME_ID)
        val id: Int = 0,
        @ColumnInfo(name = RoomContract.UpdateTime.COLUMN_NAME_UPDATE_TIME)
        val lastUpdateTime: String
)