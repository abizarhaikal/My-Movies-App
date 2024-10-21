package com.example.core.data.remote

import android.util.Log
import com.example.core.data.ResponseCredits
import com.example.core.data.ResponseDetailMovie
import com.example.core.data.ResponseDetailSeries
import com.example.core.data.ResultState
import com.example.core.data.ResultsItemActors
import com.example.core.data.ResultsItemNow
import com.example.core.data.ResultsItemSeries
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class RemoteDataSource(private val apiService: ApiService) {

    suspend fun getTrendingMovies(): Flow<ResultState<List<ResultsItemNow>>> {
        return flow {
            try {
                val response = apiService.getTrendingMovies()
                val dataArray = response.results
                if (dataArray.isNotEmpty()) {
                    emit(ResultState.Success(response.results))
                } else {
                    emit(ResultState.Loading)
                }
            } catch (e: Exception) {
                emit(ResultState.Error(e.toString()))
                Log.e("RemoteDataSource", e.toString())
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getTrendingSeries(): Flow<ResultState<List<ResultsItemSeries>>> {
        return flow {
            try {
                val response = apiService.getTrendingSeries()
                val dataArray = response.results.take(10)
                if (dataArray.isNotEmpty()) {
                    emit(ResultState.Success(response.results))
                } else {
                    emit(ResultState.Loading)
                }
            } catch (e: Exception) {
                emit(ResultState.Error(e.toString()))
                Log.e("RemoteDataSource", e.toString())
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getTrendingActors(): Flow<ResultState<List<ResultsItemActors>>> {
        return flow {
            try {
                val response = apiService.getTrendingActors()
                val dataArray = response.results.take(10)
                if (dataArray.isNotEmpty()) {
                    emit(ResultState.Success(response.results))
                } else {
                    emit(ResultState.Loading)
                }
            } catch (e: Exception) {
                emit(ResultState.Error(e.toString()))
                Log.e("RemoteDataSource", e.toString())
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getDetailMovies(id: Int): Flow<ResultState<ResponseDetailMovie>> {
        return flow {
            try {
                val response = apiService.getDetailMovies(id)
                emit(ResultState.Success(response))
            } catch (e: Exception) {
                emit(ResultState.Error(e.toString()))
                Log.e("RemoteDataSource", e.toString())
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getCreditsMovie(id: Int): Flow<ResultState<ResponseCredits>> {
        return flow {
            try {
                val response = apiService.getCredits(id)
                emit(ResultState.Success(response))
            } catch (e: Exception) {
                emit(ResultState.Error(e.toString()))
                Log.e("RemoteDataSource", e.toString())
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getDetailSeries(id: Int): Flow<ResultState<ResponseDetailSeries>> {
        return flow {
            try {
                val response = apiService.getDetailSeries(id)
                emit(ResultState.Success(response))
            } catch (e: Exception) {
                emit(ResultState.Error(e.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getCreditsSeries(id: Int): Flow<ResultState<ResponseCredits>> {
        return flow {
            try {
                val response = apiService.getCreditsSeries(id)
                emit(ResultState.Success(response))
            } catch (e: Exception) {
                emit(ResultState.Error(e.toString()))
                Log.e("RemoteDataSource", e.toString())
            }
        }.flowOn(Dispatchers.IO)
    }
}
