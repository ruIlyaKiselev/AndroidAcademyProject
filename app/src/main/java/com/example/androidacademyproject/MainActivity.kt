package com.example.androidacademyproject

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.androidacademyproject.repository.ApiMovieRepository
import com.example.androidacademyproject.repository.MovieRepository
import com.example.androidacademyproject.fragments.fragmentlisteners.OnBackClickListener
import com.example.androidacademyproject.fragments.fragmentlisteners.OnCardClickListener
import com.example.androidacademyproject.model.Movie
import com.example.androidacademyproject.providers.MovieRepositoryProvider
import com.example.androidacademyproject.fragments.movieslist.MoviesListFragmentDirections

class MainActivity : AppCompatActivity(),
        OnCardClickListener,
        OnBackClickListener,
        MovieRepositoryProvider {

    private var apiMovieRepository: ApiMovieRepository = ApiMovieRepository()
    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navController = Navigation.findNavController(this, R.id.nav_host_fragment)

        if (savedInstanceState?.containsKey("ApiMovieRepository") == true) {
            apiMovieRepository = savedInstanceState?.getParcelable<ApiMovieRepository>(
                    "ApiMovieRepository"
            ) ?: ApiMovieRepository()
        }
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
        navController.navigate(MoviesListFragmentDirections.actionMoviesListToMoviesDetails(movie.id))
    }

    private fun routeBack() {
        navController.navigateUp()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable("ApiMovieRepository", apiMovieRepository)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        apiMovieRepository = savedInstanceState.getParcelable<ApiMovieRepository>(
                "ApiMovieRepository"
        ) ?: ApiMovieRepository()
    }

    override fun provideMovieRepository(): MovieRepository = apiMovieRepository
}