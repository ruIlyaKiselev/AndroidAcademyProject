package com.example.androidacademyproject.repository

import com.example.androidacademyproject.model.Actor
import com.example.androidacademyproject.model.Genre
import com.example.androidacademyproject.model.Movie

interface IRoomRepository: IApiRepository{
    suspend fun saveMovies(movies: List<Movie>)
    suspend fun saveActors(actors: List<Actor>)
    suspend fun saveGenres(genres: List<Genre>)
}