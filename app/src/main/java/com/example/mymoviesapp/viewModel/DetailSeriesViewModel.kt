package com.example.mymoviesapp.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.data.ResponseCredits
import com.example.core.data.ResponseDetailSeries
import com.example.core.data.ResultState
import com.example.core.domain.usecase.MoviesUseCase
import kotlinx.coroutines.launch

class DetailSeriesViewModel(val moviesUseCase: MoviesUseCase) : ViewModel() {


    private val _detailSeries = MutableLiveData<ResultState<ResponseDetailSeries>>()
    val detailSeries: LiveData<ResultState<ResponseDetailSeries>> = _detailSeries

    private val _crewMovies = MutableLiveData<ResultState<ResponseCredits>>()
    val crewMovies: LiveData<ResultState<ResponseCredits>> = _crewMovies

    fun getCreditMovies(id: Int) {
        viewModelScope.launch {
            moviesUseCase.getCreditSeries(id).collect { result ->
                _crewMovies.value = result
            }
        }
    }
    fun getDetailSeries(id: Int) {
        viewModelScope.launch {
            moviesUseCase.getDetailSeries(id).collect { result ->
                _detailSeries.value = result
            }
        }
    }
}