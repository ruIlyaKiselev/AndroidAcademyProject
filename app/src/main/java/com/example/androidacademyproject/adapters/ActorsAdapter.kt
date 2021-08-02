package com.example.androidacademyproject.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.androidacademyproject.R
import com.example.androidacademyproject.dataclasses.ActorData

class ActorsAdapter(
        private val context: Context,
) : ListAdapter<ActorData, ActorsAdapter.ViewHolder>(ActorsCallback()) {

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        private var actorImage: ImageView?
        private var actorName: TextView?

        init {
            actorImage = view.findViewById(R.id.actor_image_view)
            actorName = view.findViewById(R.id.actor_name_text_view)
        }

        fun bind(data: ActorData) {
            actorImage?.setImageDrawable(data.actorImage)
            actorName?.text = data.actorName
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActorsAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.view_holder_actor, parent, false)

        return ActorsAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ActorsAdapter.ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}