package com.example.androidacademyproject.presentationlayer.moviedetails

import androidx.recyclerview.widget.DiffUtil
import com.example.androidacademyproject.model.Actor

class ActorsCallback: DiffUtil.ItemCallback<Actor>() {
    override fun areItemsTheSame(oldItem: Actor, newItem: Actor): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Actor, newItem: Actor): Boolean {
        return oldItem == newItem
    }
}
