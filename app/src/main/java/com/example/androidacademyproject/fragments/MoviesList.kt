package com.example.androidacademyproject.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidacademyproject.R
import com.example.androidacademyproject.adapters.MoviesAdapter
import com.example.androidacademyproject.data.MovieRepository
import com.example.androidacademyproject.fragmentlisteners.OnCardClickListener
import com.example.androidacademyproject.providers.MovieRepositoryProvider
import com.example.androidacademyproject.viewmodels.MoviesListViewModel
import com.example.androidacademyproject.viewmodels.MoviesListViewModelFactory
import kotlinx.coroutines.launch

class MoviesList : Fragment() {

    lateinit var moviesListViewModel: MoviesListViewModel

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

        val adapter = MoviesAdapter { movieData ->
            onItemClickListener?.openMovieDetails(movieData)
        }

        val movieRepository = (requireActivity() as MovieRepositoryProvider).provideMovieRepository()

        initRecyclerView(adapter)
        initMoviesListViewModel(adapter, movieRepository)
    }

    private fun initRecyclerView(adapter: MoviesAdapter) {
        val list = view?.findViewById<RecyclerView>(R.id.movies_list_recycler_view)
        list?.layoutManager = GridLayoutManager(
                view?.context,
                2,
                RecyclerView.VERTICAL,
                false
        )

        list?.adapter = adapter
    }

    private fun initMoviesListViewModel(adapter: MoviesAdapter, movieRepository: MovieRepository) {
        moviesListViewModel = ViewModelProvider(this,
                MoviesListViewModelFactory(movieRepository)).get(MoviesListViewModel::class.java)

        moviesListViewModel.moviesList.observe(this.viewLifecycleOwner, {
            loadDataToAdapter(adapter)
        })
    }

    private fun loadDataToAdapter(adapter: MoviesAdapter) {
        lifecycleScope.launch {
            val moviesData = moviesListViewModel.moviesList
            adapter.submitList(moviesData.value)
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
        fun create() = MoviesList()
    }
}