package com.example.androidacademyproject.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidacademyproject.fragmentlisteners.OnBackClickListener
import com.example.androidacademyproject.R
import com.example.androidacademyproject.adapters.ActorsAdapter
import com.example.androidacademyproject.adapters.MoviesAdapter
import com.example.androidacademyproject.dataclasses.ActorData


class MoviesDetails: Fragment()  {

    private var onBackClickListener: OnBackClickListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view =  inflater.inflate(R.layout.fragment_movies_details, container, false)
        val list = view.findViewById<RecyclerView>(R.id.movies_details_recycler_view)

        val actorsList = listOf(
                ActorData(
                        view.resources.getDrawable(R.drawable.actor1),
                        getString(R.string.actor_1_text_view_content)
                ),
                ActorData(
                        view.resources.getDrawable(R.drawable.actor2),
                        getString(R.string.actor_2_text_view_content)
                ),
                ActorData(
                        view.resources.getDrawable(R.drawable.actor3),
                        getString(R.string.actor_3_text_view_content)
                ),
                ActorData(
                        view.resources.getDrawable(R.drawable.actor4),
                        getString(R.string.actor_4_text_view_content)
                )
        )

        view?.findViewById<LinearLayout>(R.id.back_composite_button)?.setOnClickListener {
            onBackClickListener?.closeFragment()
        }

        val adapter = ActorsAdapter(view.context)
        adapter.submitList(actorsList)
        list.adapter = adapter
        list.layoutManager = GridLayoutManager(view.context, 1, RecyclerView.HORIZONTAL, false)

        return view
    }

    companion object {
        fun newInstance(): MoviesDetails {
            val args = Bundle()
            val fragment = MoviesDetails()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is OnBackClickListener) {
            onBackClickListener = context
        }
    }

    override fun onDetach() {
        super.onDetach()

        onBackClickListener = null
    }

}