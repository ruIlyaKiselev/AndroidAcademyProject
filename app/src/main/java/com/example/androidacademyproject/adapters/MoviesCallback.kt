package com.example.androidacademyproject.adapters

import androidx.recyclerview.widget.DiffUtil
import com.example.androidacademyproject.dataclasses.MovieData

class MoviesCallback: DiffUtil.ItemCallback<MovieData>() {
    override fun areItemsTheSame(oldItem: MovieData, newItem: MovieData): Boolean {
        return oldItem.movieName == newItem.movieName
    }

    override fun areContentsTheSame(oldItem: MovieData, newItem: MovieData): Boolean {
        return oldItem == newItem
    }
}