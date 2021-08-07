package com.example.androidacademyproject.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidacademyproject.data.MovieRepository
import com.example.androidacademyproject.model.Movie
import kotlinx.coroutines.launch

class MoviesDetailsViewModel(private val repository: MovieRepository): ViewModel() {
    private val mutableMovie = MutableLiveData<Movie>()
    val movie: LiveData<Movie> = mutableMovie

    fun loadMovie(movieId: Int) {
        viewModelScope.launch {
             mutableMovie.value = repository.loadMovie(movieId)
        }
    }
}