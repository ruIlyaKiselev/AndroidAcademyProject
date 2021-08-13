package com.example.androidacademyproject.fragments.moviedetails

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.androidacademyproject.R
import com.example.androidacademyproject.model.Actor

class ActorsAdapter: ListAdapter<Actor, ActorsAdapter.ViewHolder>(ActorsCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.view_holder_actor, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val actorImage: ImageView? = itemView.findViewById(R.id.actor_image_view)
        private val actorName: TextView? = itemView.findViewById(R.id.actor_name_text_view)

        fun bind(data: Actor) {
            actorImage?.load(data.imageBitmap)
            actorName?.text = data.name
        }
    }
}