package com.example.androidacademyproject

import android.app.Application
import com.example.androidacademyproject.repository.ApiMovieRepository
import com.example.androidacademyproject.repository.RoomMovieRepository

class CustomApplication: Application() {
    lateinit var apiMovieRepository: ApiMovieRepository
    lateinit var roomMovieRepository: RoomMovieRepository

    override fun onCreate() {
        super.onCreate()
        apiMovieRepository = ApiMovieRepository(this)
        roomMovieRepository = RoomMovieRepository(this)
    }
}