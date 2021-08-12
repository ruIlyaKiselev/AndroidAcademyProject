package com.example.androidacademyproject.providers

import com.example.androidacademyproject.repository.MovieRepository

internal interface MovieRepositoryProvider {
    fun provideMovieRepository(): MovieRepository
}