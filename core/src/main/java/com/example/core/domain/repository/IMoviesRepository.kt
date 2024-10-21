package com.example.core.domain.repository

import com.example.core.data.ResponseCredits
import com.example.core.data.ResponseDetailMovie
import com.example.core.data.ResponseDetailSeries
import com.example.core.data.ResultState
import com.example.core.data.ResultsItemActors
import com.example.core.data.ResultsItemNow
import com.example.core.data.ResultsItemSeries
import kotlinx.coroutines.flow.Flow

interface IMoviesRepository {

    suspend fun getTrendingMovies(): Flow<ResultState<List<ResultsItemNow>>>

    suspend fun getTrendingTv(): Flow<ResultState<List<ResultsItemSeries>>>

    suspend fun getTrendingActors(): Flow<ResultState<List<ResultsItemActors>>>

    suspend fun getDetailMovies(id: Int): Flow<ResultState<ResponseDetailMovie>>

    suspend fun getCreditsMovies(id: Int): Flow<ResultState<ResponseCredits>>

    suspend fun getDetailSeries(id: Int) : Flow<ResultState<ResponseDetailSeries>>

    suspend fun getCreditsSeries(id: Int) : Flow<ResultState<ResponseCredits>>
}