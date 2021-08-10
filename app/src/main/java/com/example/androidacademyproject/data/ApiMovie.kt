package com.example.androidacademyproject.data

import com.google.gson.annotations.SerializedName

data class MovieMetaData(
        @SerializedName("results")
        val results: List<MoviesResults>
)

data class MoviesResults(
        val adult: Boolean,
        @SerializedName("backdrop_path")
        val backdropPath: String,
        @SerializedName("genre_ids")
        val genreIds: List<Int>,
        val id: Int,
        val title: String,
        val overview: String,
        @SerializedName("poster_path")
        val posterPath: String,
        @SerializedName("vote_average")
        val voteAverage: Double,
        @SerializedName("vote_count")
        val voteCount: Int,
        val runtime: Int
)