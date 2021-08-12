package com.example.androidacademyproject.fragments.fragmentlisteners

import com.example.androidacademyproject.model.Movie

interface OnCardClickListener {
    fun openMovieDetails(movie: Movie)
}