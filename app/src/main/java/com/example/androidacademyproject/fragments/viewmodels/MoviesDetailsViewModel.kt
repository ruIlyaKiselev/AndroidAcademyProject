package com.example.androidacademyproject.fragments.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidacademyproject.model.Actor
import com.example.androidacademyproject.model.Movie
import com.example.androidacademyproject.repository.IApiRepository
import com.example.androidacademyproject.repository.IRoomRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MoviesDetailsViewModel(
        private val apiRepository: IApiRepository,
        private val roomRepository: IRoomRepository
        ): ViewModel() {
    private var mutableMovie = MutableLiveData<Movie>()
    val movie: LiveData<Movie> = mutableMovie

    private val mutableLoadingFlag = MutableLiveData<Boolean>(false)
    val loadingFlag: LiveData<Boolean> = mutableLoadingFlag

    fun loadMovie(movieId: Int) {
        viewModelScope.launch(Dispatchers.Main) {
            var movie: Movie? = null
            try {
                movie = apiRepository.loadMovie(movieId)
            } catch (e: Exception) {}
            if (movie != null) {
                mutableMovie.value = movie!!
            } else {
                mutableMovie.value = roomRepository.loadMovie(movieId)
            }

            loadActors(movieId)
        }
    }

    private fun loadActors(movieId: Int) {
        mutableLoadingFlag.value = true
        viewModelScope.launch(Dispatchers.Main) {
            var actors: List<Actor> = emptyList()

            val request = launch(Dispatchers.IO) {
                try {
                    actors = apiRepository.loadActors(movieId)

                    if (actors.isEmpty()) {
                        actors = roomRepository.loadActors(movieId)
                    }
                } catch (e: Exception) {
                    actors = roomRepository.loadActors(movieId)
                }
            }

            launch(Dispatchers.IO) {
                if (actors.isNotEmpty()) {
                    roomRepository.saveActors(actors)
                }
            }

            request.join()

            mutableMovie.value?.actors = actors
            mutableLoadingFlag.value = false
        }
    }
}