package com.example.mymoviesapp.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.data.ResponseCredits
import com.example.core.data.ResponseDetailMovie
import com.example.core.data.ResultState
import com.example.core.data.local.MoviesEntity
import com.example.core.data.model.Movies
import com.example.core.domain.usecase.MoviesUseCase
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class DetailViewModel(private val moviesUseCase: MoviesUseCase) : ViewModel() {

    private val _detailMovies = MutableLiveData<ResultState<ResponseDetailMovie>>()
    val detailMovies: LiveData<ResultState<ResponseDetailMovie>> = _detailMovies

    private val _crewMovies = MutableLiveData<ResultState<ResponseCredits>>()
    val crewMovies: LiveData<ResultState<ResponseCredits>> = _crewMovies

    private val _favoriteStatus = MutableLiveData<Boolean>()
    val favoriteStatus: LiveData<Boolean> = _favoriteStatus

    private val _favoriteMovie = MutableLiveData<Movies?>() // MutableLiveData to observe favorite movie status
    val favoriteMovie: LiveData<Movies?> = _favoriteMovie

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

    fun insertMovies(movies: Movies) {
        viewModelScope.launch {
            moviesUseCase.insertMovies(listOf(movies))
        }
    }

    fun deleteMovies(id: Int) {
        viewModelScope.launch {
            moviesUseCase.deleteMoviesById(id)
        }
    }

    // Perbaikan pada fungsi ini
    fun getFavoriteUser(id: Int) {
        viewModelScope.launch {
            // Pastikan moviesUseCase.getFavoriteUser(id) mengembalikan Flow<MoviesEntity?>
            moviesUseCase.getFavoriteUser(id).collect { favoriteMovie ->
                _favoriteMovie.postValue(favoriteMovie)// Memasukkan hasil observasi ke LiveData
            }
        }
    }
}

