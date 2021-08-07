package com.example.androidacademyproject.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidacademyproject.data.MovieRepository
import com.example.androidacademyproject.model.Movie
import kotlinx.coroutines.launch

class MoviesListViewModel(private val repository: MovieRepository): ViewModel() {
    private val mutableMoviesList = MutableLiveData<List<Movie>>(emptyList())

    val moviesList: LiveData<List<Movie>> = mutableMoviesList

    init {
        loadMovies()
    }

    private fun loadMovies() {
        viewModelScope.launch {
            val moviesData = repository.loadMovies()
            mutableMoviesList.value = moviesData
        }
    }
}