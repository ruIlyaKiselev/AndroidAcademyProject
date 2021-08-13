package com.example.androidacademyproject.fragments.movieslist

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidacademyproject.R
import com.example.androidacademyproject.repository.IApiRepository
import com.example.androidacademyproject.fragments.fragmentlisteners.OnCardClickListener
import com.example.androidacademyproject.providers.MoviesRepositoryProvider
import com.example.androidacademyproject.fragments.viewmodels.MoviesListViewModel
import com.example.androidacademyproject.fragments.viewmodels.MoviesListViewModelFactory
import com.example.androidacademyproject.repository.IRoomRepository
import kotlinx.coroutines.launch

class MoviesListFragment : Fragment() {

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

        val apiMovieRepository = (requireActivity() as MoviesRepositoryProvider).provideMovieRepository()
        val roomMovieRepository = (requireActivity() as MoviesRepositoryProvider).provideRoomRepository()

        initRecyclerView(adapter)
        initMoviesListViewModel(adapter, apiMovieRepository, roomMovieRepository)
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

    private fun initMoviesListViewModel(
            adapter: MoviesAdapter,
            apiRepository: IApiRepository,
            roomRepository: IRoomRepository
    ){
        moviesListViewModel = ViewModelProvider(this,
                MoviesListViewModelFactory(apiRepository, roomRepository)).get(MoviesListViewModel::class.java)

        moviesListViewModel.moviesList.observe(this.viewLifecycleOwner, {
            loadDataToAdapter(adapter)
        })

        moviesListViewModel.loadingFlag.observe(this.viewLifecycleOwner, {
            showProgressBar(it)
        })
    }

    private fun showProgressBar(loadingFlag: Boolean) {
        lifecycleScope.launch {
            val progressBar = view?.findViewById<ProgressBar>(R.id.movies_list_progress_bar)
            val recyclerView = view?.findViewById<RecyclerView>(R.id.movies_list_recycler_view)

            if (loadingFlag) {
                progressBar?.visibility = View.VISIBLE
                //recyclerView?.visibility = View.INVISIBLE
            } else {
                progressBar?.visibility = View.GONE
                //recyclerView?.visibility = View.VISIBLE
            }
        }
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
        fun create() = MoviesListFragment()
    }
}