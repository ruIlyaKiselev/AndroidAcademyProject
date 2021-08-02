package com.example.androidacademyproject.dataclasses

import android.graphics.drawable.Drawable

data class MovieData(
    val mainImage: Drawable,
    val legalAge: String,
    val isLiked: Boolean,
    val category: String,
    val rating: Double,
    val countReviews: String,
    val movieName: String,
    val movieDuration: String
    )
