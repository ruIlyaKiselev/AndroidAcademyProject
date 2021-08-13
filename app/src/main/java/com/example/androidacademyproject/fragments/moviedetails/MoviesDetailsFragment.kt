package com.example.androidacademyproject.fragments.moviedetails

import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.androidacademyproject.R
import com.example.androidacademyproject.repository.IApiRepository
import com.example.androidacademyproject.fragments.fragmentlisteners.OnBackClickListener
import com.example.androidacademyproject.model.Movie
import com.example.androidacademyproject.providers.MoviesRepositoryProvider
import com.example.androidacademyproject.fragments.viewmodels.MoviesDetailsViewModel
import com.example.androidacademyproject.fragments.viewmodels.MoviesDetailsViewModelFactory
import com.example.androidacademyproject.repository.IRoomRepository
import kotlinx.coroutines.launch

class MoviesDetailsFragment: Fragment()  {

    lateinit var moviesDetailsViewModel: MoviesDetailsViewModel

    private var onBackClickListener: OnBackClickListener? = null

    val args: MoviesDetailsFragmentArgs by navArgs()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_movies_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val movieId = args.movieId
        val actorsAdapter = ActorsAdapter()
        val apiMovieRepository = (requireActivity() as MoviesRepositoryProvider).provideMovieRepository()
        val roomMovieRepository = (requireActivity() as MoviesRepositoryProvider).provideRoomRepository()

        initBackButton()
        initRecyclerView(actorsAdapter)
        initMoviesDetailsViewModel(movieId, apiMovieRepository, roomMovieRepository)
    }

    private fun initBackButton() {
        view?.findViewById<LinearLayout>(R.id.movies_details_back_composite_button)?.setOnClickListener {
            onBackClickListener?.backToMoviesList()
        }
    }

    private fun initRecyclerView(adapter: ActorsAdapter) {
        val list = view?.findViewById<RecyclerView>(R.id.movies_details_recycler_view)
        list?.layoutManager = LinearLayoutManager(view?.context, RecyclerView.HORIZONTAL, false)

        list?.adapter = adapter
    }

    private fun initMoviesDetailsViewModel(movieId: Int,
                                           apiRepository: IApiRepository,
                                           roomRepository: IRoomRepository
    ) {
        moviesDetailsViewModel = ViewModelProvider(this,
                MoviesDetailsViewModelFactory(apiRepository, roomRepository)).get(MoviesDetailsViewModel::class.java)

        moviesDetailsViewModel.loadMovie(movieId)
        moviesDetailsViewModel.movie.observe(this.viewLifecycleOwner, {
            bindUI(view, moviesDetailsViewModel.movie.value)
        })

        moviesDetailsViewModel.loadingFlag.observe(this.viewLifecycleOwner, {
            showProgressBar(it)
        })
    }

    private fun showProgressBar(loadingFlag: Boolean) {
        lifecycleScope.launch {
            val progressBar = view?.findViewById<ProgressBar>(R.id.movies_details_progress_bar)
            val recyclerView = view?.findViewById<RecyclerView>(R.id.movies_details_recycler_view)

            if (loadingFlag) {
                progressBar?.visibility = View.VISIBLE
                //recyclerView?.visibility = View.INVISIBLE
            } else {
                if (moviesDetailsViewModel.movie.value != null) {
                    bindUI(view, moviesDetailsViewModel.movie.value)
                }
                progressBar?.visibility = View.GONE
                //recyclerView?.visibility = View.VISIBLE
            }
        }
    }

    private fun showMovieNotFoundError() {
        Toast.makeText(requireContext(), R.string.error_movie_not_found, Toast.LENGTH_LONG)
                .show()
    }

    private fun bindUI(view: View?, movie: Movie?) {
        lifecycleScope.launch {
            updateMovieDetailsInfo(movie)
            val adapter =
                    view?.findViewById<RecyclerView>(R.id.movies_details_recycler_view)?.adapter as ActorsAdapter
            adapter.submitList(movie?.actors)
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

    private fun updateMovieDetailsInfo(movie: Movie?) {
        if (movie == null) {
            showMovieNotFoundError()
            return
        }

        view?.findViewById<ImageView>(R.id.movies_details_background_image)
            ?.load(movie.detailImageBitmap)

        view?.findViewById<TextView>(R.id.movies_details_legal_age_text_view)?.text =
                context?.getString(R.string.movies_list_allowed_age_template, movie.pgAge)

        view?.findViewById<TextView>(R.id.movies_details_name_text_view)?.text = movie.title

        view?.findViewById<TextView>(R.id.movies_details_categories_text_view)?.text =
                movie.genres.joinToString { it.name }

        view?.findViewById<TextView>(R.id.movies_details_count_reviews_text_view)?.text =
                context?.getString(R.string.movies_list_reviews_template, movie.reviewCount)
        view?.findViewById<TextView>(R.id.movies_details_storyline_content_text_view)?.text = movie.storyLine

        val starsImages = listOf<ImageView?>(
                view?.findViewById(R.id.movies_details_star_1),
                view?.findViewById(R.id.movies_details_star_2),
                view?.findViewById(R.id.movies_details_star_3),
                view?.findViewById(R.id.movies_details_star_4),
                view?.findViewById(R.id.movies_details_star_5)
        )
        starsImages.forEachIndexed { index, imageView ->
            imageView?.let {
                val colorId =
                        if (movie.rating > index) R.color.pink_star_color else R.color.grey_text_color
                ImageViewCompat.setImageTintList(
                    imageView, ColorStateList.valueOf(
                        ContextCompat.getColor(imageView.context, colorId)
                    )
                )
            }
        }
    }
}
