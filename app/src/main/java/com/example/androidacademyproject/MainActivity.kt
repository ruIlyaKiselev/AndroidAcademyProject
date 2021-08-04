package com.example.androidacademyproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.androidacademyproject.data.JsonMovieRepository
import com.example.androidacademyproject.data.MovieRepository
import com.example.androidacademyproject.fragmentlisteners.OnBackClickListener
import com.example.androidacademyproject.fragmentlisteners.OnCardClickListener
import com.example.androidacademyproject.fragments.MoviesDetails
import com.example.androidacademyproject.fragments.MoviesList
import com.example.androidacademyproject.model.Movie
import com.example.androidacademyproject.providers.MovieRepositoryProvider

class MainActivity : AppCompatActivity(),
        OnCardClickListener,
        OnBackClickListener,
        MovieRepositoryProvider {

    private val jsonMovieRepository = JsonMovieRepository(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            routeToMoviesList()
        }
    }

    override fun openMovieDetails(movie: Movie) {
        routeToMovieDetails(movie)
    }

    override fun backToMoviesList() {
        routeBack()
    }

    private fun routeToMoviesList() {
        supportFragmentManager.beginTransaction()
                .add(
                        R.id.main_container,
                        MoviesList.create(),
                        MoviesList::class.java.simpleName
                )
                .addToBackStack("trans:${MoviesList::class.java.simpleName}")
                .commit()
    }

    private fun routeToMovieDetails(movie: Movie) {
        supportFragmentManager.beginTransaction()
                .replace(
                        R.id.main_container,
                        MoviesDetails.create(movie.id),
                        MoviesDetails::class.java.simpleName
                )
                .addToBackStack("trans:${MoviesDetails::class.java.simpleName}")
                .commit()
    }

    private fun routeBack() {
        supportFragmentManager.popBackStack()
    }

    override fun provideMovieRepository(): MovieRepository = jsonMovieRepository
}