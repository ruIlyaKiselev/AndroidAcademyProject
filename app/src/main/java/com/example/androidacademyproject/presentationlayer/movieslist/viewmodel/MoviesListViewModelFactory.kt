package com.example.androidacademyproject.presentationlayer.movieslist.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.androidacademyproject.repository.IApiRepository
import com.example.androidacademyproject.repository.IRoomRepository

class MoviesListViewModelFactory(
        private val apiRepository: IApiRepository,
        private val roomRepository: IRoomRepository
        ): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MoviesListViewModel(apiRepository, roomRepository) as T
    }
}