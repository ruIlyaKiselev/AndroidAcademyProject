package com.example.androidacademyproject.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidacademyproject.fragmentlisteners.OnCardClickListener
import com.example.androidacademyproject.R
import com.example.androidacademyproject.adapters.MoviesAdapter
import com.example.androidacademyproject.dataclasses.MovieData

class MoviesList : Fragment() {

    private var onItemClickListener: OnCardClickListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view =  inflater.inflate(R.layout.fragment_movies_list, container, false)
        val list = view.findViewById<RecyclerView>(R.id.movies_list_recycler_view)

        val moviesList = listOf(
                MovieData(
                        view.resources.getDrawable(R.drawable.movie1),
                        getString(R.string.legal_age_text_view_content1),
                        true,
                        getString(R.string.category_text_view_content1),
                        3.7,
                        getString(R.string.count_reviews_text_view_content1),
                        getString(R.string.movie_name_text_view_content1),
                        getString(R.string.movie_duration_content1),
                ),
                MovieData(
                        view.resources.getDrawable(R.drawable.movie2),
                        getString(R.string.legal_age_text_view_content2),
                        false,
                        getString(R.string.category_text_view_content2),
                        4.8,
                        getString(R.string.count_reviews_text_view_content2),
                        getString(R.string.movie_name_text_view_content2),
                        getString(R.string.movie_duration_content2),
                ),
                MovieData(
                        view.resources.getDrawable(R.drawable.movie3),
                        getString(R.string.legal_age_text_view_content3),
                        false,
                        getString(R.string.category_text_view_content3),
                        4.2,
                        getString(R.string.count_reviews_text_view_content3),
                        getString(R.string.movie_name_text_view_content3),
                        getString(R.string.movie_duration_content3),
                ),
                MovieData(
                        view.resources.getDrawable(R.drawable.movie4),
                        getString(R.string.legal_age_text_view_content4),
                        true,
                        getString(R.string.category_text_view_content4),
                        4.5,
                        getString(R.string.count_reviews_text_view_content4),
                        getString(R.string.movie_name_text_view_content4),
                        getString(R.string.movie_duration_content4),
                ),
                MovieData(
                        view.resources.getDrawable(R.drawable.movie1),
                        getString(R.string.legal_age_text_view_content1),
                        true,
                        getString(R.string.category_text_view_content1),
                        3.7,
                        getString(R.string.count_reviews_text_view_content1),
                        getString(R.string.movie_name_text_view_content1),
                        getString(R.string.movie_duration_content1),
                ),
                MovieData(
                        view.resources.getDrawable(R.drawable.movie2),
                        getString(R.string.legal_age_text_view_content2),
                        false,
                        getString(R.string.category_text_view_content2),
                        4.8,
                        getString(R.string.count_reviews_text_view_content2),
                        getString(R.string.movie_name_text_view_content2),
                        getString(R.string.movie_duration_content2),
                ),
                MovieData(
                        view.resources.getDrawable(R.drawable.movie3),
                        getString(R.string.legal_age_text_view_content3),
                        false,
                        getString(R.string.category_text_view_content3),
                        4.2,
                        getString(R.string.count_reviews_text_view_content3),
                        getString(R.string.movie_name_text_view_content3),
                        getString(R.string.movie_duration_content3),
                ),
                MovieData(
                        view.resources.getDrawable(R.drawable.movie4),
                        getString(R.string.legal_age_text_view_content4),
                        true,
                        getString(R.string.category_text_view_content4),
                        4.5,
                        getString(R.string.count_reviews_text_view_content4),
                        getString(R.string.movie_name_text_view_content4),
                        getString(R.string.movie_duration_content4),
                )
        )

        val adapter = MoviesAdapter(view.context)
        adapter.submitList(moviesList)
        list.adapter = adapter
        list.layoutManager = GridLayoutManager(view.context, 2, RecyclerView.VERTICAL, false)

        view.findViewById<TextView>(R.id.movies_list_page_name_tv).setOnClickListener {
            onItemClickListener?.openFragment()
        }

        return view
    }

    companion object {
        fun newInstance(): MoviesList {
            val args = Bundle()
            val fragment = MoviesList()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is OnCardClickListener) {
            onItemClickListener = context
        }
    }

    override fun onDetach() {
        super.onDetach()

        onItemClickListener = null
    }
}