package com.example.androidacademyproject.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidacademyproject.model.Actor
import com.example.androidacademyproject.model.Movie
import com.example.androidacademyproject.repository.MovieRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MoviesDetailsViewModel(private val repository: MovieRepository): ViewModel() {
    private var mutableMovie = MutableLiveData<Movie>()
    val movie: LiveData<Movie> = mutableMovie

    private val mutableLoadingFlag = MutableLiveData<Boolean>(false)
    val loadingFlag: LiveData<Boolean> = mutableLoadingFlag

    fun loadMovie(movieId: Int) {
        viewModelScope.launch(Dispatchers.Main) {
            mutableMovie.value = repository.loadMovie(movieId)
            loadActors(movieId)
        }
    }

    private fun loadActors(movieId: Int) {
        viewModelScope.launch(Dispatchers.Main) {
            mutableLoadingFlag.value = true
            var actors: List<Actor> = emptyList()

            val request = viewModelScope.launch(Dispatchers.IO) {
                actors = repository.loadActors(movieId)
            }

            request.join()

            mutableMovie.value?.actors = actors
            mutableLoadingFlag.value = false
        }
    }
}