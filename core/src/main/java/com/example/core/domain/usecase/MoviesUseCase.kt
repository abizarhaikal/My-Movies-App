package com.example.core.domain.usecase

import androidx.lifecycle.LiveData
import com.example.core.data.ResponseCredits
import com.example.core.data.ResponseDetailMovie
import com.example.core.data.ResponseDetailSeries
import com.example.core.data.ResultState
import com.example.core.data.ResultsItemActors
import com.example.core.data.ResultsItemNow
import com.example.core.data.ResultsItemSeries
import com.example.core.data.local.MoviesEntity
import com.example.core.data.model.Movies
import kotlinx.coroutines.flow.Flow

interface MoviesUseCase {
    suspend fun getTrendingMovies(): Flow<ResultState<List<ResultsItemNow>>>

    suspend fun getTrendingSeries(): Flow<ResultState<List<ResultsItemSeries>>>

    suspend fun getTrendingActors(): Flow<ResultState<List<ResultsItemActors>>>

    suspend fun getDetailMovies(id: Int) : Flow<ResultState<ResponseDetailMovie>>

    suspend fun getCreditMovies(id: Int) : Flow<ResultState<ResponseCredits>>

    suspend fun getDetailSeries(id: Int) : Flow<ResultState<ResponseDetailSeries>>

    suspend fun getCreditSeries(id: Int) : Flow<ResultState<ResponseCredits>>

    //    GET DATA FROM DATABASE
    suspend fun getAllSavedMovies() : Flow<List<Movies>>

    suspend fun insertMovies(movies: List<Movies>)

    suspend fun deleteMoviesById(id: Int)

    suspend fun getFavoriteUser(id: Int) : Flow<Movies?>
}