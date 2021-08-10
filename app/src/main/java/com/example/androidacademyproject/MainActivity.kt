package com.example.androidacademyproject

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.androidacademyproject.data.ApiMovieRepository
import com.example.androidacademyproject.data.JsonMovieRepository
import com.example.androidacademyproject.data.MovieRepository
import com.example.androidacademyproject.fragmentlisteners.OnBackClickListener
import com.example.androidacademyproject.fragmentlisteners.OnCardClickListener
import com.example.androidacademyproject.fragments.MoviesListDirections
import com.example.androidacademyproject.model.Movie
import com.example.androidacademyproject.providers.MovieRepositoryProvider

class MainActivity : AppCompatActivity(),
        OnCardClickListener,
        OnBackClickListener,
        MovieRepositoryProvider {

    private val jsonMovieRepository = JsonMovieRepository(this)
    private val apiMovieRepository = ApiMovieRepository()
    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navController = Navigation.findNavController(this, R.id.nav_host_fragment)
    }

    override fun openMovieDetails(movie: Movie) {
        routeToMovieDetails(movie)
    }

    override fun backToMoviesList() {
        routeBack()
    }

    private fun routeToMoviesList() {
        navController.navigate(R.id.action_moviesDetails_to_moviesList)
    }

    private fun routeToMovieDetails(movie: Movie) {
        navController.navigate(MoviesListDirections.actionMoviesListToMoviesDetails(movie.id))
    }

    private fun routeBack() {
        navController.navigateUp()
    }

    override fun provideMovieRepository(): MovieRepository = apiMovieRepository
}