package com.example.androidacademyproject.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.androidacademyproject.R
import com.example.androidacademyproject.model.Movie


class MoviesAdapter(private val onClickCard: (item: Movie) -> Unit) :
        ListAdapter<Movie, MoviesAdapter.ViewHolder>(MoviesCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.view_holder_movie, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), onClickCard)
    }

    class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

        private var mainImageView: ImageView? = view.findViewById(R.id.movies_list_film_main_image)
        private var legalAgeTextView: TextView? = view.findViewById(R.id.movies_list_legal_age_tv)
        private var likeImageView: ImageView? = view.findViewById(R.id.movies_list_like_iv)
        private var categoryTextView: TextView? = view.findViewById(R.id.movies_list_category_tv)
        private val starsImages: List<ImageView?> = listOf(
                itemView.findViewById(R.id.movies_list_star_1),
                itemView.findViewById(R.id.movies_list_star_2),
                itemView.findViewById(R.id.movies_list_star_3),
                itemView.findViewById(R.id.movies_list_star_4),
                itemView.findViewById(R.id.movies_list_star_5)
        )
        private var countReviewsTextView: TextView? = view.findViewById(R.id.movies_list_count_reviews_tv)
        private var movieNameTextView: TextView? = view.findViewById(R.id.movies_list_film_name_tv)
        private var movieDurationTextView: TextView? = view.findViewById(R.id.movies_list_film_duration_tv)

        fun bind(data: Movie, onClickCard: (item: Movie) -> Unit) {
            val pinkStarColor = ResourcesCompat.getColor(view.resources, R.color.pink_star_color, null)

//            mainImageView?.setImageURI(Uri.parse(data.imageUrl))
            mainImageView?.load(data.imageUrl)
            legalAgeTextView?.text = itemView.context.getString(
                    R.string.movies_list_allowed_age_template,
                    data.pgAge
            )

            if (data.isLiked) {
                likeImageView?.setColorFilter(pinkStarColor)
            } else {
                likeImageView?.setColorFilter(Color.WHITE)
            }

            categoryTextView?.text = data.genres.joinToString { it.name }

            paintStarsByRating(view, data.rating.toDouble(), starsImages)

            countReviewsTextView?.text = itemView.context.getString(
                    R.string.movies_list_reviews_template,
                    data.reviewCount
            )

            movieNameTextView?.text = data.title
            movieDurationTextView?.text = itemView.context.getString(
                    R.string.movies_list_film_time,
                    data.runningTime
            )

            itemView.setOnClickListener {
                onClickCard(data)
            }
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
}