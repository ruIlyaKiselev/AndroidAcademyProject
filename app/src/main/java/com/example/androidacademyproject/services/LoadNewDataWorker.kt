package com.example.androidacademyproject.services

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.androidacademyproject.CustomApplication
import com.example.androidacademyproject.model.Movie
import com.example.androidacademyproject.repository.ApiMovieRepository
import com.example.androidacademyproject.repository.RoomMovieRepository
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class LoadNewDataWorker(context: Context, params: WorkerParameters): Worker(context, params), CoroutineScope{
    private val customApplication = applicationContext as CustomApplication

    val roomMovieRepository = RoomMovieRepository(context)
    val apiMovieRepository = ApiMovieRepository(context)

    private var job: Job = Job()
    override val coroutineContext: CoroutineContext get() = Dispatchers.IO + job

    override fun doWork(): Result {


        launch {
            val movies = loadAllMoviesData()
            Log.d("MyLog111", movies.size.toString())
        }

        return Result.success()
    }

    suspend fun loadAllMoviesData(): List<Movie> = withContext(Dispatchers.IO) {
        var movies: List<Movie> = emptyList()

        launch {
            movies = apiMovieRepository.loadMovies()
            movies.forEach { movie ->
                movie.actors = apiMovieRepository.loadActors(movie.id)
            }
        }.join()

        return@withContext movies
    }

    suspend fun saveAllMoviesData() = withContext(Dispatchers.IO) {

    }
}