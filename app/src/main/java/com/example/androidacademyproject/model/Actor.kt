package com.example.androidacademyproject.model
import android.graphics.Bitmap
import java.io.Serializable

data class Actor(
        val id: Int,
        val name: String,
        val imageBitmap: Bitmap,
) : Serializable