package com.example.mymoviesapp.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.core.data.ResultsItemNow
import com.example.mymoviesapp.databinding.ItemMoviesTrendingBinding
import com.example.mymoviesapp.ui.DetailActivity

class MoviesAdapter :
    ListAdapter<ResultsItemNow, MoviesAdapter.MoviesViewHolder>(
        createDiffCallback(
        areItemsTheSame = { oldItem, newItem -> oldItem.id == newItem.id },
        areContentsTheSame = { oldItem, newItem -> oldItem.id == newItem.id }
    )) {

    private val baseImageUrl = "https://image.tmdb.org/t/p/w500"

    private var centerPosition = -1
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MoviesViewHolder {
        val binding =
            ItemMoviesTrendingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MoviesViewHolder(binding)
    }

    inner class MoviesViewHolder(val binding: ItemMoviesTrendingBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(movies: com.example.core.data.ResultsItemNow) {
            val fullPosterUrl = baseImageUrl + movies.posterPath
            Glide.with(itemView.context)
                .load(fullPosterUrl)
                .into(binding.ivMoviesTrending)

            binding.viewLayout.setOnClickListener {
                val id = movies.id
                val intent = Intent(itemView.context, DetailActivity::class.java).apply {
                    putExtra(DetailActivity.ID_MOVIES, id)
                }
                itemView.context.startActivity(intent)
            }
        }
    }

    override fun onBindViewHolder(holder: MoviesViewHolder, position: Int) {
        val listMovies = getItem(position)
        holder.bind(listMovies)
        if (position == centerPosition) {
            holder.binding.tvItemTitle.visibility = View.VISIBLE
            holder.binding.viewLayout.visibility = View.VISIBLE
        } else {
            holder.binding.tvItemTitle.visibility = View.GONE
            holder.binding.viewLayout.visibility = View.GONE
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateCenterPosition(position: Int) {
        centerPosition = position
        notifyDataSetChanged()
    }

}