package com.example.androidacademyproject.room.entity

import android.graphics.Bitmap
import androidx.room.*
import com.example.androidacademyproject.model.Actor
import com.example.androidacademyproject.model.Genre
import com.example.androidacademyproject.room.RoomContract

@Entity(
    tableName = RoomContract.Movie.TABLE_NAME,
    indices = [Index(RoomContract.Movie.COLUMN_NAME_ID)]
)
data class MovieEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = RoomContract.Movie.COLUMN_NAME_ID)
    val id: Int,
    @ColumnInfo(name = RoomContract.Movie.COLUMN_NAME_PG_AGE)
    val pgAge: Int,
    @ColumnInfo(name = RoomContract.Movie.COLUMN_NAME_TITLE)
    val title: String,
    @ColumnInfo(name = RoomContract.Movie.COLUMN_NAME_GENRES)
    val genres: List<Int>,
    @ColumnInfo(name = RoomContract.Movie.COLUMN_NAME_RUNNING_TIME)
    val runningTime: Int,
    @ColumnInfo(name = RoomContract.Movie.COLUMN_NAME_REVIEW_COUNT)
    val reviewCount: Int,
    @ColumnInfo(name = RoomContract.Movie.COLUMN_NAME_IS_LIKED)
    val isLiked: Boolean,
    @ColumnInfo(name = RoomContract.Movie.COLUMN_NAME_RATING)
    val rating: Int,
    @ColumnInfo(name = RoomContract.Movie.COLUMN_NAME_IMAGE_BITMAP)
    val imageBitmap: Bitmap,
    @ColumnInfo(name = RoomContract.Movie.COLUMN_NAME_DETAIL_IMAGE_BITMAP)
    val detailImageBitmap: Bitmap,
    @ColumnInfo(name = RoomContract.Movie.COLUMN_NAME_STORYLINE)
    val storyLine: String,
    @ColumnInfo(name = RoomContract.Movie.COLUMN_ACTORS)
    var actors: List<Int>,
)
