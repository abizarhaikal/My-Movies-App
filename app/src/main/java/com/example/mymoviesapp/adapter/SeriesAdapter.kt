package com.example.mymoviesapp.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.core.data.ResultsItemSeries
import com.example.mymoviesapp.databinding.ItemMoviesTrendingBinding
import com.example.mymoviesapp.ui.DetailActivity
import com.example.mymoviesapp.ui.DetailSeriesActivity

class SeriesAdapter : ListAdapter<ResultsItemSeries, SeriesAdapter.SeriesViewHolder>(
    createDiffCallback(
        areItemsTheSame = { oldItem, newItem -> oldItem.id == newItem.id },
        areContentsTheSame = { oldItem, newItem -> oldItem.id == newItem.id }
    )) {
    private val baseImageUrl = "https://image.tmdb.org/t/p/w500"

    private var centerPosition = -1

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SeriesViewHolder {
        val binding =
            ItemMoviesTrendingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SeriesViewHolder(binding)
    }

    inner class SeriesViewHolder(val binding: ItemMoviesTrendingBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: com.example.core.data.ResultsItemSeries) {
            val fullPosterUrl = baseImageUrl + item.posterPath
            Glide.with(itemView.context)
                .load(fullPosterUrl)
                .into(binding.ivMoviesTrending)

            val id = item.id
            binding.viewLayout.setOnClickListener {
                val intent = Intent(itemView.context, DetailSeriesActivity::class.java).apply {
                    putExtra(DetailSeriesActivity.ID_SERIES, id)
                }
                itemView.context.startActivity(intent)
            }
        }
    }

    override fun onBindViewHolder(holder: SeriesViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
        if (position == centerPosition) {
            holder.binding.tvItemTitle.visibility = View.VISIBLE
            holder.binding.viewLayout.visibility = View.VISIBLE
        } else {
            holder.binding.tvItemTitle.visibility = View.GONE
            holder.binding.viewLayout.visibility = View.GONE
        }
    }

    fun updateCenterPosition(position: Int) {
        centerPosition = position
        notifyDataSetChanged()
    }

}