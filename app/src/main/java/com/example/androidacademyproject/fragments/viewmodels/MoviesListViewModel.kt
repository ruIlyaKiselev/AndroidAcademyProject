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

class MoviesListViewModel(
        private val apiRepository: IApiRepository,
        private val roomRepository: IRoomRepository
        ): ViewModel() {
    private val mutableMoviesList = MutableLiveData<List<Movie>>(emptyList())
    val moviesList: LiveData<List<Movie>> = mutableMoviesList

    private val mutableLoadingFlag = MutableLiveData<Boolean>(false)
    val loadingFlag: LiveData<Boolean> = mutableLoadingFlag

    init {
        val handler = CoroutineExceptionHandler { _, exception ->
            Log.e("MyLog","CoroutineExceptionHandler got $exception")
        }
        viewModelScope.launch(handler + Dispatchers.Main) {
            loadMovies()
        }
    }

    private suspend fun loadMovies() {
        val handler = CoroutineExceptionHandler { _, exception ->
            Log.e("MyLog","CoroutineExceptionHandler got $exception")
        }

        var moviesData: List<Movie> = emptyList()
        mutableLoadingFlag.value = true
        viewModelScope.launch(Dispatchers.IO) {
            moviesData = roomRepository.loadMovies()

            if (moviesData.isNotEmpty()) {
                launch (Dispatchers.Main) {
                    mutableMoviesList.value = moviesData
                }.join()
            }

            moviesData = apiRepository.loadMovies()

            if (moviesData.isNotEmpty()) {
                launch(handler + Dispatchers.IO) {
                    launch(handler + Dispatchers.Main) {
                        mutableMoviesList.value = moviesData
                    }
                    roomRepository.saveMovies(moviesData)
                    moviesData.forEach { movie ->
                        roomRepository.saveGenres(movie.genres)
                    }
                }
            }
        }.join()

        mutableLoadingFlag.value = false

        loadActors(moviesData)
    }

    private suspend fun loadActors(moviesData: List<Movie>) {
        viewModelScope.launch(Dispatchers.IO) {
            moviesData.forEach { movie ->
                val handler = CoroutineExceptionHandler { _, exception ->
                    Log.e("MyLog", "CoroutineExceptionHandler got $exception")
                }
                viewModelScope.launch(handler + Dispatchers.IO) {

                    launch(handler + Dispatchers.Main) {
                        mutableMoviesList.value = roomRepository.loadMovies()
                    }.join()

                    var actors: List<Actor> = emptyList()

                    launch(handler + Dispatchers.IO) {
                        actors = apiRepository.loadActors(movie.id)
                    }.join()

                    Log.d("MyLog", actors.toString())
                    if (actors.isNotEmpty()) {
                        movie.actors = actors
                        launch(handler + Dispatchers.Main) {
                            mutableMoviesList.value = moviesData
                        }.join()
                        launch(handler + Dispatchers.IO) {
                            roomRepository.saveActors(actors)
                        }
                        launch(handler + Dispatchers.IO) {
                            roomRepository.saveMovies(listOf(movie))
                        }
                    }
                }
            }
        }
    }
}

