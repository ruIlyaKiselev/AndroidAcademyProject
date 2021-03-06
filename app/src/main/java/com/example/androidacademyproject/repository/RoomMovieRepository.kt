package com.example.androidacademyproject.repository

import android.content.Context
import android.os.Parcelable
import android.util.Log
import com.example.androidacademyproject.model.Actor
import com.example.androidacademyproject.model.Genre
import com.example.androidacademyproject.model.Movie
import com.example.androidacademyproject.room.MoviesDatabase
import com.example.androidacademyproject.room.RoomContract
import com.example.androidacademyproject.room.entity.ActorEntity
import com.example.androidacademyproject.room.entity.GenreEntity
import com.example.androidacademyproject.room.entity.MovieEntity
import com.example.androidacademyproject.room.entity.UpdateTimeEntity
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.parcelize.IgnoredOnParcel
import java.security.Timestamp
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.format.DateTimeFormatter
import java.util.*

@Parcelize
class RoomMovieRepository(val context: @RawValue Context): IRoomRepository, Parcelable {
    @IgnoredOnParcel
    private val moviesDatabase = MoviesDatabase.create(context)

    @IgnoredOnParcel
    private val resultList: MutableList<Movie> = LinkedList()

    @IgnoredOnParcel
    private var resultGenres: List<Genre> = LinkedList()

    override suspend fun loadMovies(): List<Movie> = withContext(Dispatchers.IO) {
        if (resultList.isNotEmpty()) {
            return@withContext resultList
        }

        val moviesFromDb: List<MovieEntity> = moviesDatabase.dao.getAllMovies()

        return@withContext convertMovies(moviesFromDb)
    }

    override suspend fun loadMovie(movieId: Int): Movie?  = withContext(Dispatchers.IO) {
        if (resultList.size == 0) {
            loadMovies()
        }

        return@withContext resultList.find { movie ->
            movie.id == movieId
        }
    }

    override suspend fun loadActors(movieId: Int): List<Actor> = withContext(Dispatchers.IO)  {
        val actorsForResult: MutableList<Actor> = LinkedList<Actor>()
        val movie = loadMovie(movieId)

        movie?.actors?.forEach { actor ->
            val actorEntity = moviesDatabase.dao.getActor(actor.id)
            actorsForResult.add(
                Actor(
                        id = actorEntity.id,
                        name = actorEntity.name,
                        imageBitmap = actorEntity.imageBitmap
                )
            )
        }
        return@withContext actorsForResult
    }

    private suspend fun convertMovies(actorsToConvert: List<MovieEntity>)
    : List<Movie> = withContext(Dispatchers.IO) {
        actorsToConvert.forEach { movieEntity ->
            val listIdGenres: List<Int> = movieEntity.genres
            val genresForResult: List<Genre> = loadGenres().filter {
                listIdGenres.contains(it.id)
            }

            resultList.add(
                    Movie(
                            id = movieEntity.id,
                            pgAge = movieEntity.pgAge,
                            title = movieEntity.title,
                            genres = genresForResult,
                            runningTime = movieEntity.runningTime,
                            reviewCount = movieEntity.reviewCount,
                            isLiked = movieEntity.isLiked,
                            rating = movieEntity.rating,
                            imageBitmap = movieEntity.imageBitmap,
                            detailImageBitmap = movieEntity.detailImageBitmap,
                            storyLine = movieEntity.storyLine,
                            actors = movieEntity.actors.map { actorId ->
                                val actorEntity = moviesDatabase.dao.getActor(actorId)
                                    Actor(
                                            actorEntity.id,
                                            actorEntity.name,
                                            actorEntity.imageBitmap
                                    )
                            }
                    )
            )
        }

        return@withContext resultList
    }

    private suspend fun loadGenres(): List<Genre> = withContext(Dispatchers.IO) {
        if (resultGenres.isNotEmpty()) {
            return@withContext resultGenres
        }

        val genresFromDatabase: List<GenreEntity> = moviesDatabase.dao.getAllGenres()
        resultGenres = genresFromDatabase.map {
            Genre(
                    id = it.id,
                    name = it.name)
        }

        return@withContext resultGenres
    }

    override suspend fun saveMovies(movies: List<Movie>) {
        if (movies.size == 1) {
            val movie = movies.first()
            moviesDatabase.dao.insertMovie(
                    MovieEntity(
                            id = movie.id,
                            pgAge = movie.pgAge,
                            title = movie.title,
                            genres = movie.genres.map { genre ->
                                genre.id
                            },
                            runningTime = movie.runningTime,
                            reviewCount = movie.reviewCount,
                            isLiked = movie.isLiked,
                            rating = movie.rating,
                            imageBitmap = movie.imageBitmap,
                            detailImageBitmap = movie.detailImageBitmap,
                            storyLine = movie.storyLine,
                            actors = movie.actors.map { actor ->
                                actor.id
                            }
                    )
            )
            return
        }

        moviesDatabase.dao.insertMovies(movies.map { movie ->
            MovieEntity(
                    id = movie.id,
                    pgAge = movie.pgAge,
                    title = movie.title,
                    genres = movie.genres.map { genre ->
                        genre.id
                    },
                    runningTime = movie.runningTime,
                    reviewCount = movie.reviewCount,
                    isLiked = movie.isLiked,
                    rating = movie.rating,
                    imageBitmap = movie.imageBitmap,
                    detailImageBitmap = movie.detailImageBitmap,
                    storyLine = movie.storyLine,
                    actors = movie.actors.map { actor ->
                        actor.id
                    }
            )
        })
    }

    override suspend fun saveActors(actors: List<Actor>) {
        moviesDatabase.dao.insertActors(actors.map { actor ->
            ActorEntity(
                    id = actor.id,
                    name = actor.name,
                    imageBitmap = actor.imageBitmap
            )
        })
    }

    override suspend fun saveGenres(genres: List<Genre>) {
        moviesDatabase.dao.insertGenres(genres.map { genre ->
            GenreEntity(
                    id = genre.id,
                    name = genre.name
            )
        })
    }

    suspend fun setUpdateTime() {
        moviesDatabase.dao.insertUpdateTime(
                UpdateTimeEntity(
                        lastUpdateTime = SimpleDateFormat("dd/M/yyyy hh:mm:ss").format(Date())
                )
        )
    }

    suspend fun getUpdateTimes(): List<UpdateTimeEntity> {
        return moviesDatabase.dao.getUpdateTimes()
    }
}