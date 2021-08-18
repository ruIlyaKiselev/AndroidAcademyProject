package com.example.androidacademyproject.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.androidacademyproject.room.entity.ActorEntity
import com.example.androidacademyproject.room.entity.GenreEntity
import com.example.androidacademyproject.room.entity.MovieEntity
import com.google.android.material.circularreveal.CircularRevealHelper
import retrofit2.http.GET

@Dao
interface MoviesDao {
    @Query("SELECT * FROM actor ORDER BY _id ASC")
    suspend fun getAllActors(): List<ActorEntity>
    @Query("SELECT * FROM actor WHERE _id == :id")
    suspend fun getActor(id: Int): ActorEntity
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertActor(actor: ActorEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertActors(actors: List<ActorEntity>)
    @Query("DELETE FROM actor WHERE _id == :id")
    suspend fun deleteActor(id: Int)
    @Query("DELETE FROM actor")
    suspend fun deleteAllActors()

    @Query("SELECT * FROM movie ORDER BY _id ASC")
    suspend fun getAllMovies(): List<MovieEntity>
    @Query("SELECT * FROM movie WHERE _id == :id")
    suspend fun getMovie(id: Int): MovieEntity
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(movie: MovieEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovies(movies: List<MovieEntity>)
    @Query("DELETE FROM movie WHERE _id == :id")
    suspend fun deleteMovie(id: Int)
    @Query("DELETE FROM movie")
    suspend fun deleteAllMovies()

    @Query("SELECT * FROM genre ORDER BY _id ASC")
    suspend fun getAllGenres(): List<GenreEntity>
    @Query("SELECT * FROM genre WHERE _id == :id")
    suspend fun getGenre(id: Int): GenreEntity
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGenre(genre: GenreEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGenres(genres: List<GenreEntity>)
    @Query("DELETE FROM genre WHERE _id == :id")
    suspend fun deleteGenre(id: Int)
    @Query("DELETE FROM genre")
    suspend fun deleteAllGenres()
}
