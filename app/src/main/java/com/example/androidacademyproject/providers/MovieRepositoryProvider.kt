package com.example.androidacademyproject.providers

import com.example.androidacademyproject.data.MovieRepository

internal interface MovieRepositoryProvider {
    fun provideMovieRepository(): MovieRepository
}