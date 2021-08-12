package com.example.androidacademyproject.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidacademyproject.model.Movie
import com.example.androidacademyproject.repository.MovieRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MoviesListViewModel(private val movieRepository: MovieRepository): ViewModel() {
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

        mutableLoadingFlag.value = true
        val request = viewModelScope.launch(Dispatchers.IO) {
            val moviesData = movieRepository.loadMovies()
            launch(Dispatchers.Main) {
                mutableMoviesList.value = moviesData
            }
        }

        request.join()
        mutableLoadingFlag.value = false
    }
}

