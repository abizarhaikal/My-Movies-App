package com.example.mymoviesapp.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.core.data.ResultState
import com.example.mymoviesapp.R
import com.example.mymoviesapp.adapter.CastSeriesAdapter
import com.example.mymoviesapp.databinding.ActivityDetailSeriesBinding
import com.example.mymoviesapp.viewModel.DetailSeriesViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DetailSeriesActivity : AppCompatActivity() {

    private val detailSeriesViewModel: DetailSeriesViewModel by viewModel()
    private lateinit var binding: ActivityDetailSeriesBinding
    private val url = "https://image.tmdb.org/t/p/w500"
    private lateinit var crewAdapter: CastSeriesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDetailSeriesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val seriesId = intent.getIntExtra(ID_SERIES, 0)
        detailSeriesViewModel.getDetailSeries(seriesId)

        detailSeriesViewModel.getCreditMovies(seriesId)
        setRecyclerViewCredits()
        seeDetails()
        seeDetailsCrew()
    }

    private fun seeDetailsCrew() {
        detailSeriesViewModel.crewMovies.observe(this) { result ->
            if (result != null) {
                when (result) {
                    is ResultState.Loading -> {
                        showToast("Loading")
                    }

                    is ResultState.Success -> {
                        showToast("Success")
                        crewAdapter.submitList(result.data.cast)
                    }

                    is ResultState.Error -> {
                        Log.e("Detail Activity", "Error: ${result.error}")
                        showToast("Error")
                    }
                }
            }
        }
    }

    private fun setRecyclerViewCredits() {
        val recyclerViewCredits = binding.rvCrewFilm
        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerViewCredits.layoutManager = linearLayoutManager
        crewAdapter = CastSeriesAdapter()
        recyclerViewCredits.adapter = crewAdapter
    }


    private fun seeDetails() {
        detailSeriesViewModel.detailSeries.observe(this) { result ->
            if (result != null) {
                when (result) {
                    is ResultState.Loading -> {
                        showToast("Loading")
                    }

                    is ResultState.Success -> {
                        showToast("Success")
                        val image = result.data.posterPath
                        val fullImageUrl = url + image
                        showImage(fullImageUrl)
                        Glide.with(this)
                            .load(fullImageUrl)
                            .into(binding.imageBanner)
                        binding.tvTitle.text = result.data.name
                        val rate = result.data.popularity
                        val releaseYear = result.data.firstAirDate
                        val runtime = result.data.episodeRunTime.map {
                            it
                        }.joinToString { ", " }
                        val country = result.data.productionCountries.map {
                            it.name
                        }.joinToString(", ")
                        binding.tvReleasePremiere.text = formatReleaseDate(releaseYear)
                        binding.tvSummary.text = result.data.overview
                        binding.tvProductionField.text = country.toString()
                        binding.tvDuration.text = runtime
                        binding.tvYear.text = formatReleaseYear(releaseYear)
                        binding.tvRate.text = formatPopularity(rate)

                        val linkUrl = result.data.homepage
                        setButtonPlay(linkUrl)
                    }

                    is ResultState.Error -> {
                        Log.e("Detail Activity", "Error: ${result.error}")
                        showToast("Error")
                    }
                }
            }
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

    private fun formatReleaseDate(releaseDate: String): String {
        val fullDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val targetFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        val date = fullDateFormat.parse(releaseDate) ?: Date()
        return targetFormat.format(date)

    }

    @SuppressLint("DefaultLocale")
    private fun formatRuntime(runtime: Int): String {
        val hours = runtime / 60
        val minute = runtime % 60
        return if (hours > 0) {
            String.format("•%d hour(s) %02d minute(s)", hours, minute)
        } else {
            String.format("•%02d minute(s)", minute)
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

    private fun showImage(image: String) {
        Glide.with(this)
            .asBitmap()
            .load(image)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {

                    Palette.from(resource).generate { pallete ->
                        pallete?.dominantSwatch?.let { swatch ->
                            val color = swatch.rgb

                            this@DetailSeriesActivity.window.statusBarColor = color
                        }
                    }
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                    //do Nothing
                }
            })

    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()


    }


    companion object {
        const val ID_SERIES = "id_series"
    }
}