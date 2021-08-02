package com.example.androidacademyproject.adapters

import androidx.recyclerview.widget.DiffUtil
import com.example.androidacademyproject.dataclasses.ActorData

class ActorsCallback: DiffUtil.ItemCallback<ActorData>() {
    override fun areItemsTheSame(oldItem: ActorData, newItem: ActorData): Boolean {
        return oldItem.actorName == newItem.actorName
    }

    override fun areContentsTheSame(oldItem: ActorData, newItem: ActorData): Boolean {
        return oldItem == newItem
    }
}
