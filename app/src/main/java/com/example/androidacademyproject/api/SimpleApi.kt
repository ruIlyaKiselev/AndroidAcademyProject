package com.example.androidacademyproject.api

import com.example.androidacademyproject.api.data.*
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface SimpleApi {
    @GET("movie/popular")
    suspend fun getPopularMovies(
            @Query("api_key") apiKey: String,
            @Query("language") language: String,
            @Query("page") page: Int,
    ): Response<MovieMetaData>

    @GET("movie/{movieId}/credits")
    suspend fun getMovieCredits(
            @Path("movieId") movieId: Int,
            @Query("api_key") apiKey: String,
            @Query("language") language: String,
    ): Response<MovieCreditsMetaData>

    @GET("genre/movie/list")
    suspend fun getGenresList(
            @Query("api_key") apiKey: String,
            @Query("language") language: String,
    ) : Response<ApiGenresMetaData>

    @GET("movie/{movieId}")
    suspend fun getMovieDetails(
            @Path("movieId") movieId: Int,
            @Query("api_key") apiKey: String,
            @Query("language") language: String,
    ): Response<ApiMovieDetails>
}