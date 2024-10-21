package com.example.mymoviesapp.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.data.ResponseCredits
import com.example.core.data.ResponseDetailMovie
import com.example.core.data.ResponseDetailSeries
import com.example.core.data.ResultState
import com.example.core.domain.usecase.MoviesUseCase
import kotlinx.coroutines.launch

class DetailViewModel(private val moviesUseCase: MoviesUseCase) : ViewModel() {

    private val _detailMovies = MutableLiveData<ResultState<ResponseDetailMovie>>()
    val detailMovies: LiveData<ResultState<ResponseDetailMovie>> = _detailMovies

    private val _crewMovies = MutableLiveData<ResultState<ResponseCredits>>()
    val crewMovies: LiveData<ResultState<ResponseCredits>> = _crewMovies


    fun getCreditMovies(id: Int) {
        viewModelScope.launch {
            moviesUseCase.getCreditMovies(id).collect { result ->
                _crewMovies.value = result
            }
        }
    }

    fun getDetailMovies(id: Int) {
        viewModelScope.launch {
            moviesUseCase.getDetailMovies(id).collect { result ->
                _detailMovies.value = result
            }
        }
    }
}
