package com.example.androidacademyproject.fragments.movieslist

import androidx.recyclerview.widget.DiffUtil
import com.example.androidacademyproject.model.Movie

class MoviesCallback: DiffUtil.ItemCallback<Movie>() {
    override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return oldItem == newItem
    }
}