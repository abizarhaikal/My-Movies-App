package com.example.mymoviesapp.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.data.ResultState
import com.example.core.data.ResultsItemActors
import com.example.core.data.ResultsItemNow
import com.example.core.data.ResultsItemSeries
import kotlinx.coroutines.launch

class HomeViewModel(moviesUseCase: com.example.core.domain.usecase.MoviesUseCase) : ViewModel() {

    private val _trendingMovies = MutableLiveData<ResultState<List<ResultsItemNow>>>()
    val trendingMovies: LiveData<ResultState<List<ResultsItemNow>>> = _trendingMovies

    private val _trendingSeries = MutableLiveData<ResultState<List<ResultsItemSeries>>>()
    val trendingSeries : LiveData<ResultState<List<ResultsItemSeries>>> = _trendingSeries

    private val _trendingActors = MutableLiveData<ResultState<List<ResultsItemActors>>>()
    val trendingActors : LiveData<ResultState<List<ResultsItemActors>>> = _trendingActors

    init {
        viewModelScope.launch {
            moviesUseCase.getTrendingMovies().collect { result ->
                _trendingMovies.value = result
            }
            moviesUseCase.getTrendingSeries().collect { result ->
                _trendingSeries.value = result
            }
            moviesUseCase.getTrendingActors().collect { result ->
                _trendingActors.value = result
            }
        }

    }
}