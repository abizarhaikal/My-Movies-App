package com.example.mymoviesapp.favorite.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.core.data.model.Movies
import com.example.mymoviesapp.adapter.createDiffCallback
import com.example.mymoviesapp.favorite.databinding.ItemsFavoriteBinding

class FavoriteAdapter :
    ListAdapter<Movies, FavoriteAdapter.FavoriteViewHolder>(createDiffCallback(
        areItemsTheSame = { oldItem, newItem -> oldItem.id == newItem.id },
        areContentsTheSame = { oldItem, newItem -> oldItem.id == newItem.id }
    )) {

    private val baseImageUrl = "https://image.tmdb.org/t/p/w500"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val binding =
            ItemsFavoriteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteViewHolder(binding)
    }

    inner class FavoriteViewHolder(private val binding: ItemsFavoriteBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: Movies) {
            binding.tvTitleFavorite.text = movie.title
            val fullImage = baseImageUrl + movie.posterPath
            Glide.with(itemView)
                .load(fullImage)
                .into(binding.ivFavorite)
            // Jika kamu memiliki data tahun, misalnya:
            binding.tvSummary.text = movie.overview
        }
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(getItem(position)) // Mengambil item berdasarkan posisi
    }
}
