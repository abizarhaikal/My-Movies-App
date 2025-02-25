package com.example.mymoviesapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.core.data.GenresItemSeries
import com.example.mymoviesapp.databinding.ItemsGenreBinding

class GenresAdapter : ListAdapter<GenresItemSeries, GenresAdapter.GenresViewHolder>(createDiffCallback(
    areItemsTheSame = { oldItem, newItem -> oldItem.id == newItem.id },
    areContentsTheSame = { oldItem, newItem -> oldItem.id == newItem.id }
)) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): GenresAdapter.GenresViewHolder {
        val binding = ItemsGenreBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GenresViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GenresAdapter.GenresViewHolder, position: Int) {
        val genres = getItem(position)
        holder.bind(genres)
    }

    inner class GenresViewHolder(val binding: ItemsGenreBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(genres: GenresItemSeries) {
            binding.tvGenres.text = genres.name
        }
    }

}