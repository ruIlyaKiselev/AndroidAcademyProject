package com.example.androidacademyproject.presentationlayer.moviedetails.viewmodel

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
        viewModelScope.launch(handler + Dispatchers.IO) {

            launch(handler + Dispatchers.Main) {
                mutableMovie.value = roomRepository.loadMovie(movieId)
            }.join()

            var movie: Movie? = null

            movie = apiRepository.loadMovie(movieId)

            if (movie != null) {
                launch(handler + Dispatchers.Main) {
                    mutableMovie.value = movie!!
                }.join()
            }
            if (mutableMovie!!.value!!.actors.isEmpty()) {
                launch {
                    loadActors(movieId)
                }.join()
            }
        }
    }

    private fun loadActors(movieId: Int) {
        val handler = CoroutineExceptionHandler { _, exception ->
            Log.e("MyLog","CoroutineExceptionHandler got $exception")
        }
        viewModelScope.launch(handler + Dispatchers.IO) {
            launch(handler + Dispatchers.Main) {
                mutableLoadingFlag.value = true
            }

            launch(handler + Dispatchers.Main) {
                mutableMovie.value?.actors = roomRepository.loadActors(movieId)
            }.join()

            var actors: List<Actor> = emptyList()

            launch {
                actors = apiRepository.loadActors(movieId)
            }.join()

            Log.d("MyLog", actors.toString())
            if (actors.isNotEmpty()) {
                launch(handler + Dispatchers.Main) {
                    mutableMovie.value?.actors = actors
                }.join()
                roomRepository.saveActors(actors)
                roomRepository.saveMovies(listOf(mutableMovie.value!!))
            }
            launch(handler + Dispatchers.Main) {
                mutableLoadingFlag.value = false
            }
        }
    }
}