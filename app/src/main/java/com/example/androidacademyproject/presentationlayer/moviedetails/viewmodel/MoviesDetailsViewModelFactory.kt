package com.example.androidacademyproject.presentationlayer.moviedetails.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.androidacademyproject.repository.IApiRepository
import com.example.androidacademyproject.repository.IRoomRepository

class MoviesDetailsViewModelFactory(
        private val apiRepository: IApiRepository,
        private val roomRepository: IRoomRepository
        ): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MoviesDetailsViewModel(
                apiRepository,
                roomRepository
        ) as T
    }
}