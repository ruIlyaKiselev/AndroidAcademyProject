package com.example.androidacademyproject.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidacademyproject.fragmentlisteners.OnCardClickListener
import com.example.androidacademyproject.R
import com.example.androidacademyproject.adapters.MoviesAdapter
import com.example.androidacademyproject.providers.MovieRepositoryProvider
import kotlinx.coroutines.launch

class MoviesList : Fragment() {

    private var onItemClickListener: OnCardClickListener? = null

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?)
    : View? {
        return inflater.inflate(R.layout.fragment_movies_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val list = view.findViewById<RecyclerView>(R.id.movies_list_recycler_view)
        list.layoutManager = GridLayoutManager(
                view.context,
                2,
                RecyclerView.VERTICAL,
                false
        )

        val adapter = MoviesAdapter { movieData ->
            onItemClickListener?.openMovieDetails(movieData)
        }

        list.adapter = adapter
        loadDataToAdapter(adapter)
    }

    private fun loadDataToAdapter(adapter: MoviesAdapter) {
        val repository = (requireActivity() as MovieRepositoryProvider).provideMovieRepository()
        lifecycleScope.launch {
            val moviesData = repository.loadMovies()

            adapter.submitList(moviesData)
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

    companion object {
        fun newInstance(): MoviesList {
            val args = Bundle()
            val fragment = MoviesList()
            fragment.arguments = args
            return fragment
        }

        fun create() = MoviesList()
    }
}