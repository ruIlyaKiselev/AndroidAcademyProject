package com.example.androidacademyproject

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.androidacademyproject.repository.ApiMovieRepository
import com.example.androidacademyproject.repository.IApiRepository
import com.example.androidacademyproject.fragments.fragmentlisteners.OnBackClickListener
import com.example.androidacademyproject.fragments.fragmentlisteners.OnCardClickListener
import com.example.androidacademyproject.model.Movie
import com.example.androidacademyproject.providers.MoviesRepositoryProvider
import com.example.androidacademyproject.fragments.movieslist.MoviesListFragmentDirections
import com.example.androidacademyproject.repository.RoomMovieRepository
import com.example.androidacademyproject.repository.IRoomRepository

class MainActivity : AppCompatActivity(),
        OnCardClickListener,
        OnBackClickListener,
        MoviesRepositoryProvider {

    private var apiMovieRepository: ApiMovieRepository = ApiMovieRepository(this)
    //private var roomMovieRepository: RoomMovieRepository = RoomMovieRepository(applicationContext)
    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState?.containsKey("ApiMovieRepository") == true) {
            apiMovieRepository = savedInstanceState?.getParcelable<ApiMovieRepository>(
                    "ApiMovieRepository"
            ) ?: ApiMovieRepository(this)
        }

//        if (savedInstanceState?.containsKey("RoomMovieRepository") == true) {
//            roomMovieRepository = savedInstanceState?.getParcelable<RoomMovieRepository>(
//                    "RoomMovieRepository"
//            ) ?: RoomMovieRepository(this)
//        }

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
        navController.navigate(MoviesListFragmentDirections.actionMoviesListToMoviesDetails(movie.id))
    }

    private fun routeBack() {
        navController.navigateUp()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable("ApiMovieRepository", apiMovieRepository)
//        outState.putParcelable("RoomMovieRepository", roomMovieRepository)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        apiMovieRepository = savedInstanceState.getParcelable<ApiMovieRepository>(
                "ApiMovieRepository"
        ) ?: ApiMovieRepository(this)

//        roomMovieRepository = savedInstanceState.getParcelable<RoomMovieRepository>(
//                "RoomMovieRepository"
//        ) ?: RoomMovieRepository(this)
    }

    override fun provideMovieRepository(): IApiRepository = apiMovieRepository
    override fun provideRoomRepository(): IRoomRepository = RoomMovieRepository(this)
}