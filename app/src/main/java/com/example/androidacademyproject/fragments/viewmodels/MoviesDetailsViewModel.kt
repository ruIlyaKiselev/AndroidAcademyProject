package com.example.androidacademyproject.fragments.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidacademyproject.model.Actor
import com.example.androidacademyproject.model.Movie
import com.example.androidacademyproject.repository.IApiRepository
import com.example.androidacademyproject.repository.IRoomRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
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
        val handler = CoroutineExceptionHandler { _, exception ->
            Log.e("MyLog","CoroutineExceptionHandler got $exception")
        }
        var movie: Movie? = null
        viewModelScope.launch(handler + Dispatchers.IO) {
            movie = apiRepository.loadMovie(movieId)
        }

        viewModelScope.launch(handler + Dispatchers.Main) {
            if (movie != null) {
                mutableMovie.value = movie!!
            } else {
                viewModelScope.launch(handler + Dispatchers.IO) {
                    movie = roomRepository.loadMovie(movieId)
                }.join()
                mutableMovie.value = movie!!
            }
            if (mutableMovie!!.value!!.actors.isEmpty()) {
                loadActors(movieId)
            }
        }
    }

    private fun loadActors(movieId: Int) {
        val handler = CoroutineExceptionHandler { _, exception ->
            Log.e("MyLog","CoroutineExceptionHandler got $exception")
        }
        mutableLoadingFlag.value = true
        viewModelScope.launch(handler + Dispatchers.IO) {
            var actors: List<Actor> = emptyList()

            launch(handler + Dispatchers.Main) {
                mutableMovie.value = roomRepository.loadMovie(movieId)
            }

            launch(handler + Dispatchers.IO) {
                actors = apiRepository.loadActors(movieId)
                if (actors.isNotEmpty()) {
                    roomRepository.saveActors(actors)
                }
            }.join()

            launch(handler + Dispatchers.Main) {
                if (actors.isNotEmpty()) {
                    mutableMovie.value?.actors = actors
                    launch(handler + Dispatchers.IO) {
                        roomRepository.saveMovies(listOf(mutableMovie.value!!))
                    }
                }
                mutableLoadingFlag.value = false
            }
        }
    }
}