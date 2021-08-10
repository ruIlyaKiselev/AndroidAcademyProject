package com.example.androidacademyproject.repository

import com.example.androidacademyproject.api.RetrofitInstance
import com.example.androidacademyproject.data.*
import retrofit2.Response

class Repository {

    suspend fun getPopularMoviesMetaData(apiKey: String, language: String, page: Int)
    : Response<MovieMetaData> {
        return RetrofitInstance.api.getPopularMovies(apiKey, language, page)
    }

    suspend fun getMovieCreditsMetaData(movieId: Int, apiKey: String, language: String)
    : Response<MovieCreditsMetaData> {
        return RetrofitInstance.api.getMovieCredits(movieId, apiKey, language)
    }

    suspend fun getGenresMetaData(apiKey: String, language: String)
            : Response<ApiGenresMetaData> {
        return RetrofitInstance.api.getGenresList(apiKey, language)
    }

    suspend fun getMovieDetailsMetaData(movieId: Int, apiKey: String, language: String)
            : Response<ApiMovieDetails> {
        return RetrofitInstance.api.getMovieDetails(movieId, apiKey, language)
    }
}