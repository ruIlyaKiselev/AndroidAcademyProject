package com.example.androidacademyproject.model

import android.graphics.Bitmap
import java.io.Serializable

data class Movie(
        val id: Int,
        val pgAge: Int,
        val title: String,
        val genres: List<Genre>,
        val runningTime: Int,
        val reviewCount: Int,
        val isLiked: Boolean,
        val rating: Int,
        val imageBitmap: Bitmap,
        val detailImageBitmap: Bitmap,
        val storyLine: String,
        var actors: List<Actor>,
) : Serializable