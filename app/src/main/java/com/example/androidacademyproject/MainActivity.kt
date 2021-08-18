package com.example.androidacademyproject

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.example.androidacademyproject.repository.ApiMovieRepository
import com.example.androidacademyproject.repository.IApiRepository
import com.example.androidacademyproject.fragments.fragmentlisteners.OnBackClickListener
import com.example.androidacademyproject.fragments.fragmentlisteners.OnCardClickListener
import com.example.androidacademyproject.model.Movie
import com.example.androidacademyproject.providers.MoviesRepositoryProvider
import com.example.androidacademyproject.fragments.movieslist.MoviesListFragmentDirections
import com.example.androidacademyproject.repository.RoomMovieRepository
import com.example.androidacademyproject.repository.IRoomRepository
import com.example.androidacademyproject.services.LoadNewDataWorker

class MainActivity : AppCompatActivity(),
        OnCardClickListener,
        OnBackClickListener,
        MoviesRepositoryProvider {

    private lateinit var apiMovieRepository: ApiMovieRepository
    private lateinit var roomMovieRepository: RoomMovieRepository
    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        apiMovieRepository = (application as CustomApplication).apiMovieRepository
        roomMovieRepository= (application as CustomApplication).roomMovieRepository

        val simpleRequest = OneTimeWorkRequest.Builder(LoadNewDataWorker::class.java).build()
//        WorkManager.getInstance(applicationContext)
//            .enqueue(simpleRequest)

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

    override fun provideMovieRepository(): IApiRepository = apiMovieRepository
    override fun provideRoomRepository(): IRoomRepository = roomMovieRepository
}