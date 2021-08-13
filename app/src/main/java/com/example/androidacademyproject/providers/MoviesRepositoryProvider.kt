package com.example.androidacademyproject.providers

import com.example.androidacademyproject.repository.IRoomRepository
import com.example.androidacademyproject.repository.IApiRepository

internal interface MoviesRepositoryProvider {
    fun provideMovieRepository(): IApiRepository
    fun provideRoomRepository(): IRoomRepository
}