package com.example.androidacademyproject.model

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
        val imageUrl: String,
        val detailImageUrl: String,
        val storyLine: String,
        var actors: List<Actor>,
) : Serializable