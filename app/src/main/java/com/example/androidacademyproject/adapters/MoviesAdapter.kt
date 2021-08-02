package com.example.androidacademyproject.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.androidacademyproject.dataclasses.MovieData
import com.example.androidacademyproject.R


class MoviesAdapter(
        private val context: Context,
) : ListAdapter<MovieData, MoviesAdapter.ViewHolder>(MoviesCallback()) {

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        private var mainImageView: ImageView?
        private var legalAgeTextView: TextView?
        private var likeImageView: ImageView?
        private var categoryTextView: TextView?
        private var star1ImageView: ImageView?
        private var star2ImageView: ImageView?
        private var star3ImageView: ImageView?
        private var star4ImageView: ImageView?
        private var star5ImageView: ImageView?
        private var countReviewsTextView: TextView?
        private var movieNameTextView: TextView?
        private var movieDurationTextView: TextView?
        private var starList: List<ImageView?>

        init {
            mainImageView = view.findViewById(R.id.movies_list_film_main_image)
            legalAgeTextView = view.findViewById(R.id.movies_list_legal_age_tv)
            likeImageView = view.findViewById(R.id.movies_list_like_iv)
            categoryTextView = view.findViewById(R.id.movies_list_category_tv)
            star1ImageView = view.findViewById(R.id.movies_list_star_1)
            star2ImageView = view.findViewById(R.id.movies_list_star_2)
            star3ImageView = view.findViewById(R.id.movies_list_star_3)
            star4ImageView = view.findViewById(R.id.movies_list_star_4)
            star5ImageView = view.findViewById(R.id.movies_list_star_5)
            countReviewsTextView = view.findViewById(R.id.movies_list_count_reviews_tv)
            movieNameTextView = view.findViewById(R.id.movies_list_film_name_tv)
            movieDurationTextView = view.findViewById(R.id.movies_list_film_duration_tv)

            starList = listOf(
                    star1ImageView,
                    star2ImageView,
                    star3ImageView,
                    star4ImageView,
                    star5ImageView
            )
        }

        fun bind(data: MovieData) {
            val pinkStarColor = ResourcesCompat.getColor(view.resources, R.color.pink_star_color, null)

            mainImageView?.setImageDrawable(data.mainImage)
            legalAgeTextView?.text = data.legalAge

            if (data.isLiked) {
                likeImageView?.setColorFilter(pinkStarColor)
            } else {
                likeImageView?.setColorFilter(Color.WHITE)
            }

            categoryTextView?.text = data.category

            paintStarsByRating(view, data.rating, starList)

            countReviewsTextView?.text = data.countReviews
            movieNameTextView?.text = data.movieName
            movieDurationTextView?.text = data.movieDuration
        }

        private fun paintStarsByRating(view: View, rating: Double, starList: List<ImageView?>) {
            val pinkStarColor = ResourcesCompat.getColor(view.resources, R.color.pink_star_color, null)
            val greyStarColor = ResourcesCompat.getColor(view.resources, R.color.grey_text_color, null)

            for (element in starList) {
                element?.setColorFilter(greyStarColor)
            }

            val starsToPaint = (rating + 0.5).toInt()

            for (i in 0 until starsToPaint) {
                starList[i]?.setColorFilter(pinkStarColor)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.view_holder_movie, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}