package com.example.androidacademyproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.androidacademyproject.fragmentlisteners.OnBackClickListener
import com.example.androidacademyproject.fragmentlisteners.OnCardClickListener
import com.example.androidacademyproject.fragments.MoviesDetails
import com.example.androidacademyproject.fragments.MoviesList

class MainActivity : AppCompatActivity(), OnCardClickListener, OnBackClickListener {
    var moviesList: MoviesList = MoviesList.newInstance()
    var moviesDetails: MoviesDetails = MoviesDetails.newInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .add(R.id.main_container, moviesList, "moviesList")
                    .commit()
        } else {
            moviesList = supportFragmentManager.findFragmentByTag("moviesList")
                    as MoviesList ?: MoviesList.newInstance()
            if (supportFragmentManager.findFragmentByTag("moviesDetails") != null) {
                moviesDetails = supportFragmentManager.findFragmentByTag("moviesDetails")
                        as MoviesDetails
                        ?: MoviesDetails.newInstance()
            }
        }
    }

    override fun openFragment() {
        supportFragmentManager.beginTransaction()
                .addToBackStack(null)
                .replace(R.id.main_container, moviesDetails, "moviesDetails")
                .commit()
    }

    override fun closeFragment() {
        supportFragmentManager.popBackStack()
    }
}