package com.example.androidacademyproject.room

import android.provider.BaseColumns

object RoomContract {
    const val DATABASE_NAME = "AndroidAcademyProject.db"

    object Actor {
        const val TABLE_NAME = "Actor"

        const val COLUMN_NAME_ID = BaseColumns._ID
        const val COLUMN_NAME_NAME = "name"
        const val COLUMN_NAME_IMAGE_BITMAP = "imageBitmap"
    }

    object Genre {
        const val TABLE_NAME = "Genre"

        const val COLUMN_NAME_ID = BaseColumns._ID
        const val COLUMN_NAME_NAME = "name"
    }

    object Movie {
        const val TABLE_NAME = "Movie"

        const val COLUMN_NAME_ID = BaseColumns._ID
        const val COLUMN_NAME_PG_AGE = "pgAge"
        const val COLUMN_NAME_TITLE = "title"
        const val COLUMN_NAME_GENRES = "genres"
        const val COLUMN_NAME_RUNNING_TIME = "runningTime"
        const val COLUMN_NAME_REVIEW_COUNT = "reviewCount"
        const val COLUMN_NAME_IS_LIKED = "isLiked"
        const val COLUMN_NAME_RATING = "rating"
        const val COLUMN_NAME_IMAGE_BITMAP = "imageBitmap"
        const val COLUMN_NAME_DETAIL_IMAGE_BITMAP = "detailImageBitmap"
        const val COLUMN_NAME_STORYLINE = "storyLine"
        const val COLUMN_ACTORS = "actors"
    }

    object UpdateTime {
        const val TABLE_NAME = "UpdateTime"

        const val COLUMN_NAME_ID = BaseColumns._ID
        const val COLUMN_NAME_UPDATE_TIME = "updateTime"
    }
}