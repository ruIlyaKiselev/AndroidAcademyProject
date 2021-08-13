package com.example.androidacademyproject.repository

import com.example.androidacademyproject.model.Actor
import com.example.androidacademyproject.model.Movie

interface IApiRepository {
    suspend fun loadMovies(): List<Movie>
    suspend fun loadMovie(movieId: Int): Movie?
    suspend fun loadActors(movieId: Int): List<Actor>
}

