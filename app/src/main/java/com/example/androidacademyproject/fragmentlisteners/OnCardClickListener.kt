package com.example.androidacademyproject.fragmentlisteners

import com.example.androidacademyproject.model.Movie

interface OnCardClickListener {
    fun openMovieDetails(movie: Movie)
}