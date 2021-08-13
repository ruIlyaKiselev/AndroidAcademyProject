package com.example.androidacademyproject.fragments.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidacademyproject.model.Movie
import com.example.androidacademyproject.repository.IApiRepository
import com.example.androidacademyproject.repository.IRoomRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MoviesListViewModel(
        private val apiRepository: IApiRepository,
        private val roomRepository: IRoomRepository
        ): ViewModel() {
    private val mutableMoviesList = MutableLiveData<List<Movie>>(emptyList())
    val moviesList: LiveData<List<Movie>> = mutableMoviesList

    private val mutableLoadingFlag = MutableLiveData<Boolean>(false)
    val loadingFlag: LiveData<Boolean> = mutableLoadingFlag

    init {
        viewModelScope.launch(Dispatchers.Main) {
            loadMovies()
        }
    }

    private suspend fun loadMovies() {
        mutableMoviesList.value = roomRepository.loadMovies()
        var moviesData: List<Movie> = emptyList()
        mutableLoadingFlag.value = true
        viewModelScope.launch {
            val request = launch(Dispatchers.IO) {
                try {
                    moviesData = apiRepository.loadMovies()
                    launch(Dispatchers.Main) {
                        mutableMoviesList.value = moviesData
                    }
                } catch (e: Exception) {}
            }

            request.join()

            launch(Dispatchers.IO) {
                if (moviesData.isNotEmpty()) {
                    roomRepository.saveMovies(moviesData)
                    moviesData.forEach { movie ->
                        roomRepository.saveGenres(movie.genres)
                    }
                }
            }
        }
        mutableLoadingFlag.value = false
    }
}

