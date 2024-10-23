package com.example.mymoviesapp.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.core.data.ResponseDetailMovie
import com.example.core.data.ResultState
import com.example.core.data.local.MoviesEntity
import com.example.core.data.model.Movies
import com.example.mymoviesapp.R
import com.example.mymoviesapp.adapter.CrewAdapter
import com.example.mymoviesapp.adapter.GenresAdapter
import com.example.mymoviesapp.databinding.ActivityDetailBinding
import com.example.mymoviesapp.viewModel.DetailViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private val detailViewModel: DetailViewModel by viewModel()
    private val url = "https://image.tmdb.org/t/p/w500"
    private lateinit var genresAdapter: GenresAdapter
    private lateinit var crewAdapter: CrewAdapter

    private var isFavorite: Boolean = false
    private var moviesEntity: Movies? = null


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom)
            insets
        }

        val moviesId = intent.getIntExtra(ID_MOVIES, 0)

        // Observasi detail film
        detailViewModel.detailMovies.observe(this) { result ->
            when (result) {
                is ResultState.Loading -> showToast("Loading")
                is ResultState.Success -> {
                    val movieDetail = result.data
                    // Set up tampilan detail movie
                    setupMovieDetail(movieDetail)

                    // Ambil status favorite menggunakan LiveData
                    detailViewModel.getFavoriteUser(moviesId)

                    // Ambil status favorite menggunakan Flow dengan coroutine
                    lifecycleScope.launch {
                        detailViewModel.favoriteMovie.observe(this@DetailActivity) { favoriteMovie ->
                            Log.d("DetailActivity", "Favorite Movie: ${favoriteMovie?.isFavorite}")
                            if (favoriteMovie?.isFavorite != null) {
                                binding.btnLike.setCompoundDrawablesWithIntrinsicBounds(
                                    R.drawable.ic_favorite, 0, 0, 0
                                )
                                binding.btnLike.setBackgroundColor(getColor(R.color.md_theme_error))
                                isFavorite = true
                            } else {
                                binding.btnLike.setCompoundDrawablesWithIntrinsicBounds(
                                    R.drawable.ic_favorite_border, 0, 0, 0
                                )
                                binding.btnLike.setBackgroundColor(getColor(R.color.md_theme_background))
                                isFavorite = false
                            }
                        }

                    }

                    // Set onClick untuk tombol favorit
                    binding.btnLike.setOnClickListener {
                        toggleFavorite(moviesId, movieDetail)
                    }
                }

                is ResultState.Error -> {
                    Log.e("Detail Activity", "Error: ${result.error}")
                    showToast("Error")
                }
            }
        }

        // Observasi crew film
        detailViewModel.crewMovies.observe(this) { result ->
            when (result) {
                is ResultState.Loading -> showToast("Loading")
                is ResultState.Success -> {
                    crewAdapter.submitList(result.data.cast)
                    showToast("Success")
                }

                is ResultState.Error -> {
                    Log.e("Detail Activity", "Error: ${result.error}")
                    showToast("Error")
                }
            }
        }

        // Fetch data film dan crew
        detailViewModel.getDetailMovies(moviesId)
        detailViewModel.getCreditMovies(moviesId)

        // Set up recycler view
        setRecyclerViewGenres()
        setRecyclerViewCredits()
    }


    @RequiresApi(Build.VERSION_CODES.M)
    private fun toggleFavorite(moviesId: Int, movieDetail: ResponseDetailMovie) {
        val newFavoriteState = !isFavorite!!
        moviesEntity = Movies(
            id = movieDetail.id,
            posterPath = movieDetail.posterPath,
            title = movieDetail.title,
            overview = movieDetail.overview,
            isFavorite = newFavoriteState
        )

        if (newFavoriteState) {
            detailViewModel.insertMovies(moviesEntity!!)
            binding.btnLike.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_favorite, 0, 0, 0)
            binding.btnLike.setBackgroundColor(getColor(R.color.md_theme_error))
            showToast("Added to Favorites")
            isFavorite = true
        } else {
            detailViewModel.deleteMovies(moviesId)
            binding.btnLike.setCompoundDrawablesWithIntrinsicBounds(
                R.drawable.ic_favorite_border,
                0,
                0,
                0
            )
            binding.btnLike.setBackgroundColor(getColor(R.color.md_theme_background))
            showToast("Removed from Favorites")
            isFavorite = false
        }

        isFavorite = newFavoriteState
    }

    private fun setupMovieDetail(movieDetail: ResponseDetailMovie) {
        val fullImageUrl = url + movieDetail.posterPath
        Glide.with(this).load(fullImageUrl).into(binding.imageBanner)

        genresAdapter.submitList(movieDetail.genres.take(2)) // Ambil dua genre
        binding.tvTitle.text = movieDetail.title
        binding.tvReleasePremiere.text = formatReleaseDate(movieDetail.releaseDate)
        binding.tvSummary.text = movieDetail.overview
        binding.tvProductionField.text =
            movieDetail.productionCountries.joinToString(", ") { it.name }
        binding.tvDuration.text = formatRuntime(movieDetail.runtime)
        binding.tvYear.text = formatReleaseYear(movieDetail.releaseDate)
        binding.tvRate.text = formatPopularity(movieDetail.popularity)

        setButtonPlay(movieDetail.homepage)
    }

    private fun setRecyclerViewCredits() {
        val recyclerViewCredits = binding.rvCrewFilm
        recyclerViewCredits.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        crewAdapter = CrewAdapter()
        recyclerViewCredits.adapter = crewAdapter
    }

    private fun setRecyclerViewGenres() {
        val recyclerViewGenres = binding.rvGenres
        recyclerViewGenres.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        genresAdapter = GenresAdapter()
        recyclerViewGenres.adapter = genresAdapter
        recyclerViewGenres.isLayoutFrozen = true
    }

    private fun formatReleaseDate(releaseDate: String): String {
        val fullDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val targetFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        val date = fullDateFormat.parse(releaseDate) ?: Date()
        return targetFormat.format(date)
    }

    @SuppressLint("DefaultLocale")
    private fun formatRuntime(runtime: Int): String {
        val hours = runtime / 60
        val minutes = runtime % 60
        return if (hours > 0) {
            String.format("• %d hour(s) %02d minute(s)", hours, minutes)
        } else {
            String.format("• %02d minute(s)", minutes)
        }
    }

    private fun formatReleaseYear(releaseYear: String): String {
        val yearFormat = SimpleDateFormat("yyyy", Locale.getDefault())
        val fullDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = fullDateFormat.parse(releaseYear) ?: Date()
        return yearFormat.format(date)
    }

    @SuppressLint("DefaultLocale")
    private fun formatPopularity(popularity: Double): String {
        return when {
            popularity >= 1_000_000 -> String.format("%.1f M", popularity / 1_000_000)
            popularity >= 1_000 -> String.format("%.1f K", popularity / 1000)
            else -> popularity.toString()
        }
    }

    private fun setButtonPlay(linkUrl: String) {
        binding.btnPlay.setOnClickListener {
            if (linkUrl.isNotEmpty()) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(linkUrl))
                startActivity(intent)
            } else {
                showToast("Link URL is empty")
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val ID_MOVIES = "id_movies"
    }
}