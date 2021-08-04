package com.example.androidacademyproject.data

import com.example.androidacademyproject.model.Movie

interface MovieRepository {
    suspend fun loadMovies(): List<Movie>
    suspend fun loadMovie(movieId: Int): Movie?
}

