package com.example.mymoviesapp.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.core.data.ResultState
import com.example.mymoviesapp.R
import com.example.mymoviesapp.adapter.ActorsAdapter
import com.example.mymoviesapp.adapter.BannerAdapter
import com.example.mymoviesapp.adapter.CenterZoomLayoutManager
import com.example.mymoviesapp.adapter.MoviesAdapter
import com.example.mymoviesapp.adapter.SeriesAdapter
import com.example.mymoviesapp.databinding.FragmentHomeBinding
import com.example.mymoviesapp.viewModel.HomeViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var bannerAdapter: BannerAdapter
    private lateinit var moviesAdapter: MoviesAdapter
    private val homeViewModel: HomeViewModel by viewModel()

    private lateinit var seriesAdapter: SeriesAdapter
    private lateinit var actorsAdapter: ActorsAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewPager: ViewPager2 = binding.viewPager

        val banners = listOf(R.drawable.banner_satu, R.drawable.banner_dua, R.drawable.banner_tiga)

        bannerAdapter = BannerAdapter(banners)

        viewPager.adapter = bannerAdapter


        setupRecyclerView()
        setupRecyclerViewSeries()
        setupRecyclerViewActors()
        val handler = Handler(Looper.getMainLooper())
        val updateRunnable = object : Runnable {
            var currentPage = 0
            override fun run() {
                if (currentPage == banners.size) {
                    currentPage = 0
                }
                viewPager.setCurrentItem(currentPage++, true)
                handler.postDelayed(this, 3000)
            }
        }
        handler.post(updateRunnable)
    }

    private fun setupRecyclerViewActors() {
        val recyclerViewActors = binding.rvTopActors
        val linearLayoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerViewActors.layoutManager = linearLayoutManager
        actorsAdapter = ActorsAdapter()
        val snapHelper = PagerSnapHelper()
        recyclerViewActors.adapter = actorsAdapter
        snapHelper.attachToRecyclerView(recyclerViewActors)

        recyclerViewActors.post {
            val startingPoints = 0
            recyclerViewActors.scrollToPosition(startingPoints)
        }
        homeViewModel.trendingActors.observe(viewLifecycleOwner) { result ->
            if (result != null) {
                when (result) {
                    is ResultState.Loading -> {
                        showToast("Loading")
                    }

                    is ResultState.Success -> {
                        showToast("Success")
                        actorsAdapter.submitList(result.data)
                    }

                    is ResultState.Error -> {
                        Log.e("HomeFragment Actors", "Error: ${result.error}")
                    }

                    else -> {
                        Log.e("HomeFragment Actors", "Error: ${result}")
                    }
                }
            }
        }
    }

    private fun setupRecyclerViewSeries() {
        val recyclerViewSeries = binding.viewPagerTv
        val linearLayoutManager = CenterZoomLayoutManager(requireContext())
        recyclerViewSeries.layoutManager = linearLayoutManager
        seriesAdapter = SeriesAdapter()
        val snapHelper = LinearSnapHelper()
        recyclerViewSeries.adapter = seriesAdapter
        snapHelper.attachToRecyclerView(recyclerViewSeries)
        recyclerViewSeries.post {
            val startingPoints = 0
            recyclerViewSeries.scrollToPosition(startingPoints)
        }
        recyclerViewSeries.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    val snapView = snapHelper.findSnapView(linearLayoutManager)
                    if (snapView != null) {
                        val centerPosition = recyclerView.getChildAdapterPosition(snapView)
                        if (centerPosition != RecyclerView.NO_POSITION) {
                            (recyclerView.adapter as SeriesAdapter).updateCenterPosition(
                                centerPosition
                            )
                        }
                    }
                }
            }
        })

        CoroutineScope(Dispatchers.Main).launch {
            homeViewModel.trendingSeries.observe(viewLifecycleOwner) { result ->
                if (result != null) {
                    when (result) {
                        is ResultState.Loading -> {
                            showToast("Loading")
                        }

                        is ResultState.Success -> {
                            showToast("Success")
                            seriesAdapter.submitList(result.data)
                        }

                        is ResultState.Error -> {
                            showToast(result.error)
                        }
                    }
                }
            }
        }
    }

    private fun setupRecyclerView() {
        val recyclerViewMovies = binding.viewPagerMovies
        val linearLayoutManager = CenterZoomLayoutManager(requireContext())
        recyclerViewMovies.layoutManager = linearLayoutManager
        moviesAdapter = MoviesAdapter()
        recyclerViewMovies.adapter = moviesAdapter
        val snapHelper = LinearSnapHelper()

        snapHelper.attachToRecyclerView(recyclerViewMovies)
        recyclerViewMovies.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    val snapView = snapHelper.findSnapView(linearLayoutManager)
                    if (snapView != null) {
                        val centerPosition = recyclerView.getChildAdapterPosition(snapView)
                        if (centerPosition != RecyclerView.NO_POSITION) {
                            (recyclerView.adapter as MoviesAdapter).updateCenterPosition(
                                centerPosition
                            )
                        }
                    }
                }
            }
        })
        CoroutineScope(Dispatchers.Main).launch {
            homeViewModel.trendingMovies.observe(viewLifecycleOwner) { movies ->
                if (movies != null) {
                    when (movies) {
                        is ResultState.Loading -> {
                            showToast("Loading")
                        }

                        is ResultState.Success -> {
                            showToast("Success")
                            moviesAdapter.submitList(movies.data)
                        }

                        is ResultState.Error -> {
                            Log.e("HomeFragment", "Error: ${movies.error}")
                        }

                        else -> {}
                    }
                }
            }
        }

    }


    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()

    }


}