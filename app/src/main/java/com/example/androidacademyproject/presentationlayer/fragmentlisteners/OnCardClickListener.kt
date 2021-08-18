package com.example.androidacademyproject.presentationlayer.fragmentlisteners

import com.example.androidacademyproject.model.Movie

interface OnCardClickListener {
    fun openMovieDetails(movie: Movie)
}