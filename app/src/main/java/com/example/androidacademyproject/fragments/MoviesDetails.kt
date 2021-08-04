package com.example.androidacademyproject.fragments

import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.widget.ImageViewCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.androidacademyproject.R
import com.example.androidacademyproject.adapters.ActorsAdapter
import com.example.androidacademyproject.fragmentlisteners.OnBackClickListener
import com.example.androidacademyproject.model.Movie
import com.example.androidacademyproject.providers.MovieRepositoryProvider
import kotlinx.coroutines.launch

class MoviesDetails: Fragment()  {

    private var onBackClickListener: OnBackClickListener? = null

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_movies_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val movieId = arguments?.getSerializable(PARAM_MOVIE_ID) as? Int ?: return
        val list = view.findViewById<RecyclerView>(R.id.movies_details_recycler_view)
        list.layoutManager = LinearLayoutManager(view.context, RecyclerView.HORIZONTAL, false)

        view.findViewById<LinearLayout>(R.id.movies_details_back_composite_button).setOnClickListener {
            onBackClickListener?.backToMoviesList()
        }

        list.adapter = ActorsAdapter()

        lifecycleScope.launch {
            val repository = (requireActivity() as MovieRepositoryProvider).provideMovieRepository()
            val movie = repository.loadMovie(movieId)

            if (movie != null) {
                bindUI(view, movie)
            } else {
                showMovieNotFoundError()
            }
        }
    }

    private fun showMovieNotFoundError() {
        Toast.makeText(requireContext(), R.string.error_movie_not_found, Toast.LENGTH_LONG)
                .show()
    }

    private fun bindUI(view: View, movie: Movie) {
        updateMovieDetailsInfo(movie)
        val adapter =
                view.findViewById<RecyclerView>(R.id.movies_details_recycler_view).adapter as ActorsAdapter
        adapter.submitList(movie.actors)
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

    private fun updateMovieDetailsInfo(movie: Movie) {
//        view?.findViewById<ImageView>(R.id.movies_details_background_image)
//                ?.setImageURI(Uri.parse(movie.detailImageUrl))
        view?.findViewById<ImageView>(R.id.movies_details_background_image)
            ?.load(movie.detailImageUrl)

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

    companion object {
        private const val PARAM_MOVIE_ID = "movie_id"

        fun create(movieId: Int) = MoviesDetails().also {
            val args = bundleOf(
                    PARAM_MOVIE_ID to movieId
            )
            it.arguments = args
        }
    }
}
