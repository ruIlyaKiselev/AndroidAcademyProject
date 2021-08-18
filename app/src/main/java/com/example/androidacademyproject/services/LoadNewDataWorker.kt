package com.example.androidacademyproject.services

import android.content.Context
import android.util.Log
import androidx.lifecycle.viewModelScope
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.androidacademyproject.CustomApplication
import com.example.androidacademyproject.model.Actor
import com.example.androidacademyproject.model.Movie
import com.example.androidacademyproject.repository.ApiMovieRepository
import com.example.androidacademyproject.repository.RoomMovieRepository
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class LoadNewDataWorker(context: Context, params: WorkerParameters): Worker(context, params), CoroutineScope{
    private val customApplication = applicationContext as CustomApplication

    val roomRepository = customApplication.roomMovieRepository
    val apiRepository = ApiMovieRepository(applicationContext)

    private var job: Job = Job()
    override val coroutineContext: CoroutineContext get() = Dispatchers.IO + job

    override fun doWork(): Result {

        launch {
            loadMovies()
            roomRepository.setUpdateTime()
        }

        return Result.success()
    }

    private suspend fun loadMovies() {
        val handler = CoroutineExceptionHandler { _, exception ->
            Log.e("MyLog","CoroutineExceptionHandler got $exception")
        }

        var moviesData: List<Movie> = emptyList()
        coroutineScope {
            launch(handler + Dispatchers.IO) {
                moviesData = apiRepository.loadMovies()

                if (moviesData.isNotEmpty()) {
                    launch(handler + Dispatchers.IO) {
                        roomRepository.saveMovies(moviesData)
                        moviesData.forEach { movie ->
                            roomRepository.saveGenres(movie.genres)
                        }
                    }
                }
            }.join()
        }

        loadActors(moviesData)
        roomRepository.saveMovies(moviesData)
        roomRepository.setUpdateTime()
    }

    private fun loadActors(moviesData: List<Movie>) {
        val handler = CoroutineExceptionHandler { _, exception ->
            Log.e("MyLog","CoroutineExceptionHandler got $exception")
        }

        launch(handler + Dispatchers.IO) {
            moviesData.forEach { movie ->
                CoroutineExceptionHandler { _, exception ->
                    Log.e("MyLog", "CoroutineExceptionHandler got $exception")
                }
                launch(handler + Dispatchers.IO) {
                    var actors: List<Actor> = emptyList()

                    launch(handler + Dispatchers.IO) {
                        actors = apiRepository.loadActors(movie.id)
                    }.join()

                    Log.d("MyLog", actors.toString())
                    if (actors.isNotEmpty()) {
                        movie.actors = actors
                        roomRepository.saveActors(actors)
                    }
                }
            }
        }
    }
}