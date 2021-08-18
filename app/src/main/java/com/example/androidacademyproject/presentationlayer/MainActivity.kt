package com.example.androidacademyproject.presentationlayer

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.work.*
import com.example.androidacademyproject.CustomApplication
import com.example.androidacademyproject.R
import com.example.androidacademyproject.repository.ApiMovieRepository
import com.example.androidacademyproject.repository.IApiRepository
import com.example.androidacademyproject.presentationlayer.fragmentlisteners.OnBackClickListener
import com.example.androidacademyproject.presentationlayer.fragmentlisteners.OnCardClickListener
import com.example.androidacademyproject.model.Movie
import com.example.androidacademyproject.providers.MoviesRepositoryProvider
import com.example.androidacademyproject.presentationlayer.movieslist.MoviesListFragmentDirections
import com.example.androidacademyproject.repository.RoomMovieRepository
import com.example.androidacademyproject.repository.IRoomRepository
import com.example.androidacademyproject.services.LoadNewDataWorker
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

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

        showLastUpdateTime()
        //stopUpdateMoviesWorkers()
        startUpdateMoviesWorker()

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

    private fun showLastUpdateTime() {
        var lastUpdateTime = ""
        lifecycleScope.launch {
            val updateTimes = roomMovieRepository.getUpdateTimes()
            if (updateTimes.isNotEmpty()) {
                lastUpdateTime = updateTimes.last().lastUpdateTime
                Toast.makeText(applicationContext, "Last update time: $lastUpdateTime", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun startUpdateMoviesWorker() {
        val workerConstraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.NOT_ROAMING)
                .setRequiresCharging(true)
                .build()

        val simpleRequest = PeriodicWorkRequest
                .Builder(LoadNewDataWorker::class.java, 8, TimeUnit.HOURS)
                .addTag("LoadNewDataWorker")
                .setConstraints(workerConstraints)
                .build()


        val workManager = WorkManager.getInstance(applicationContext)
        workManager.enqueueUniquePeriodicWork("LoadNewDataWorker", ExistingPeriodicWorkPolicy.KEEP, simpleRequest)
    }

    private fun stopUpdateMoviesWorkers() {
        WorkManager.getInstance(applicationContext).cancelAllWorkByTag("LoadNewDataWorker")
    }

    override fun provideMovieRepository(): IApiRepository = apiMovieRepository
    override fun provideRoomRepository(): IRoomRepository = roomMovieRepository
}