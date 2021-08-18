package com.example.androidacademyproject.repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Parcelable
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.example.androidacademyproject.networkapi.ApiContract
import com.example.androidacademyproject.networkapi.RetrofitDao
import com.example.androidacademyproject.networkapi.data.*
import com.example.androidacademyproject.model.Actor
import com.example.androidacademyproject.model.Genre
import com.example.androidacademyproject.model.Movie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue
import java.util.*

@Parcelize
class ApiMovieRepository(val context: @RawValue Context): IApiRepository, Parcelable {
    @IgnoredOnParcel
    private val dao: RetrofitDao = RetrofitDao()
    @IgnoredOnParcel
    private val resultList: MutableList<Movie> = LinkedList()

    private suspend fun getPopularMoviesFromApi(
            apiKey: String,
            language: String,
            page: Int
    ): MovieMetaData {
        try {
            val response = dao.getPopularMoviesMetaData(apiKey, language, page)
            return if (response.isSuccessful) {
                response.body() as MovieMetaData
            } else {
                return MovieMetaData(emptyList())
            }
        } catch (e: Exception) {

        }
        return MovieMetaData(emptyList())
    }

    private suspend fun getGenresListFromApi(
            apiKey: String,
            language: String
    ): ApiGenresMetaData {
        try {
            val response = dao.getGenresMetaData(apiKey, language)
            return if (response.isSuccessful) {
                response.body() as ApiGenresMetaData
            } else {
                return ApiGenresMetaData(emptyList())
            }
        } catch (e: Exception) {

        }
        return ApiGenresMetaData(emptyList())
    }

    private suspend fun getMovieCreditsFromApi(
            movieId: Int,
            apiKey: String,
            language: String
    ): MovieCreditsMetaData {
        try {
            val response = dao.getMovieCreditsMetaData(movieId, apiKey, language)
            return if (response.isSuccessful) {
                response.body() as MovieCreditsMetaData
            } else {
                return MovieCreditsMetaData(emptyList())
            }
        } catch (e: Exception) {

        }
        return MovieCreditsMetaData(emptyList())
    }

    private suspend fun getMovieDetailsFromApi(
            movieId: Int,
            apiKey: String,
            language: String
    ): ApiMovieDetails {
        try {
            val response = dao.getMovieDetailsMetaData(movieId, apiKey, language)
            return if (response.isSuccessful) {
                response.body() as ApiMovieDetails
            } else {
                return ApiMovieDetails(0, 0)
            }
        } catch (e: Exception) {

        }
        return ApiMovieDetails(0, 0)
    }

    private suspend fun prepareActorsList(movieId: Int): List<Actor> = withContext(Dispatchers.IO)  {
        val actorsList = getMovieCreditsFromApi(
                movieId,
                ApiContract.API_KEY,
                ApiContract.DEFAULT_LANGUAGE
        ).cast

        val actorsForMovieResult: MutableList<Actor> =  LinkedList<Actor>()

        actorsList.forEach { item ->
            if (item.imageUrl != null) {
                actorsForMovieResult.add(
                        Actor(
                                id = item.id,
                                name = item.name,
                                imageBitmap = getBitmap(ApiContract.IMAGE_BASE_URL + item.imageUrl)
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

        val requestResult = getPopularMoviesFromApi(
            ApiContract.API_KEY,
            ApiContract.DEFAULT_LANGUAGE,
                1
        )

        val genres = getGenresListFromApi(ApiContract.API_KEY, ApiContract.DEFAULT_LANGUAGE).genres

        requestResult.results.forEach { movie ->
            val genresForResult: MutableList<Genre> = LinkedList()
            genres.forEach { apiGenre ->
                if (movie.genreIds.contains(apiGenre.id)) {
                    genresForResult.add(Genre(apiGenre.id, apiGenre.name))
                }
            }

            val movieDuration = getMovieDetailsFromApi(
                    movie.id,
                    ApiContract.API_KEY,
                    ApiContract.DEFAULT_LANGUAGE
            ).runtime

            resultList.add(
                    Movie(
                            id = movie.id,
                            pgAge = if (movie.adult) {13} else {16},
                            title = movie.title,
                            genres = genresForResult,
                            runningTime = movieDuration,
                            reviewCount = movie.voteCount,
                            isLiked = false,
                            rating = movie.voteAverage.toInt() / 2,
                            imageBitmap = getBitmap(ApiContract.IMAGE_BASE_URL + movie.posterPath),
                            detailImageBitmap = getBitmap(ApiContract.IMAGE_BASE_URL + movie.backdropPath),
                            storyLine = movie.overview,
                            actors = emptyList()
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

    private suspend fun getBitmap(imageUri: String): Bitmap {
        val loading = ImageLoader(context)
        val request = ImageRequest.Builder(context)
                .data(imageUri)
                .build()

        val result = (loading.execute(request) as SuccessResult).drawable
        return (result as BitmapDrawable).bitmap
    }
}