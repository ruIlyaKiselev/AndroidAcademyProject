package com.example.androidacademyproject.repository

import android.os.Parcelable
import com.example.androidacademyproject.api.RetrofitDao
import com.example.androidacademyproject.api.data.*
import com.example.androidacademyproject.model.Actor
import com.example.androidacademyproject.model.Genre
import com.example.androidacademyproject.model.Movie
import com.example.androidacademyproject.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
class ApiMovieRepository: MovieRepository, Parcelable {
    private val repository: RetrofitDao = RetrofitDao()
    private val resultList: MutableList<Movie> = LinkedList()

    private suspend fun getPopularMoviesFromApi(apiKey: String,
                                                language: String,
                                                page: Int
    ): MovieMetaData {
        val response = repository.getPopularMoviesMetaData(apiKey, language, page)
        return if (response.isSuccessful) {
             response.body() as MovieMetaData
        } else {
            return MovieMetaData(emptyList())
        }
    }

    private suspend fun getGenresListFromApi(
            apiKey: String,
            language: String
    ): ApiGenresMetaData {
        val response = repository.getGenresMetaData(apiKey, language)
        return if (response.isSuccessful) {
            response.body() as ApiGenresMetaData
        } else {
            return ApiGenresMetaData(emptyList())
        }
    }

    private suspend fun getMovieCreditsFromApi(movieId: Int,
                                               apiKey: String,
                                               language: String
    ): MovieCreditsMetaData {
        val response = repository.getMovieCreditsMetaData(movieId, apiKey, language)
        return if (response.isSuccessful) {
            response.body() as MovieCreditsMetaData
        } else {
            return MovieCreditsMetaData(emptyList())
        }
    }

    private suspend fun getMovieDetailsFromApi(movieId: Int,
                                               apiKey: String,
                                               language: String
    ): ApiMovieDetails {
        val response = repository.getMovieDetailsMetaData(movieId, apiKey, language)
        return if (response.isSuccessful) {
            response.body() as ApiMovieDetails
        } else {
            return ApiMovieDetails(0, 0)
        }
    }

    suspend fun prepareActorsList(movieId: Int): List<Actor> = withContext(Dispatchers.IO)  {
        val actorsList = getMovieCreditsFromApi(
                movieId,
                Constants.API_KEY,
                Constants.DEFAULT_LANGUAGE
        ).cast

        val actorsForMovieResult: MutableList<Actor> =  LinkedList<Actor>()

        actorsList.forEach { item ->
            if (item.imageUrl != null) {
                actorsForMovieResult.add(
                        Actor(
                                item.id,
                                item.name,
                                Constants.IMAGE_BASE_URL + item.imageUrl
                        )

                )
            }
        }

        return@withContext actorsForMovieResult
    }

    override suspend fun loadMovies(): List<Movie> = withContext(Dispatchers.IO) {
        if (resultList.size != 0) {
            return@withContext resultList
        }

        val requestResult = getPopularMoviesFromApi(Constants.API_KEY, Constants.DEFAULT_LANGUAGE, 1)

        val genres = getGenresListFromApi(Constants.API_KEY, Constants.DEFAULT_LANGUAGE).genres

        requestResult.results.forEach { movie ->
            val genresForResult: MutableList<Genre> = LinkedList()
            genres.forEach { apiGenre ->
                if (movie.genreIds.contains(apiGenre.id)) {
                    genresForResult.add(Genre(apiGenre.id, apiGenre.name))
                }
            }

            val movieDuration = getMovieDetailsFromApi(
                    movie.id,
                    Constants.API_KEY,
                    Constants.DEFAULT_LANGUAGE
            ).runtime

            //val actorsForMovieResult = prepareActorsList(movie)

            resultList.add(
                    Movie(
                        movie.id,
                        if (movie.adult) {13} else {16},
                        movie.title,
                        genresForResult,
                            movieDuration,
                        movie.voteCount,
                        false,
                        movie.voteAverage.toInt() / 2,
                            Constants.IMAGE_BASE_URL + movie.posterPath,
                            Constants.IMAGE_BASE_URL + movie.backdropPath,
                        movie.overview,
//                        actorsForMovieResult
                        emptyList()
                    )
            )
        }
        return@withContext resultList
    }

    override suspend fun loadMovie(movieId: Int): Movie? = withContext(Dispatchers.IO) {
        if (resultList.size == 0) {
            loadMovies()
        }

        return@withContext resultList.find { movie ->
            movie.id == movieId
        }
    }

    override suspend fun loadActors(movieId: Int): List<Actor> {
        return prepareActorsList(movieId)
    }
}