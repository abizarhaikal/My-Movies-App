package com.example.core.data

import com.example.core.data.remote.RemoteDataSource
import com.example.core.domain.repository.IMoviesRepository
import kotlinx.coroutines.flow.Flow

class MoviesRepository(
    private val remoteDataSource: RemoteDataSource
) : IMoviesRepository {
    override suspend fun getTrendingMovies(): Flow<ResultState<List<ResultsItemNow>>> {
        return remoteDataSource.getTrendingMovies()
    }

    override suspend fun getTrendingTv(): Flow<ResultState<List<ResultsItemSeries>>> {
        return remoteDataSource.getTrendingSeries()
    }

    override suspend fun getTrendingActors(): Flow<ResultState<List<ResultsItemActors>>> {
        return remoteDataSource.getTrendingActors()
    }

    override suspend fun getDetailMovies(id: Int): Flow<ResultState<ResponseDetailMovie>> {
        return remoteDataSource.getDetailMovies(id)
    }

    override suspend fun getCreditsMovies(id: Int): Flow<ResultState<ResponseCredits>> {
        return remoteDataSource.getCreditsMovie(id)
    }

    override suspend fun getDetailSeries(id: Int): Flow<ResultState<ResponseDetailSeries>> {
        return remoteDataSource.getDetailSeries(id)
    }

    override suspend fun getCreditsSeries(id: Int): Flow<ResultState<ResponseCredits>> {
        return remoteDataSource.getCreditsSeries(id)
    }

}