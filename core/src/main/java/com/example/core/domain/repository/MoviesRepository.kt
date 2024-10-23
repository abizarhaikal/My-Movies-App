package com.example.core.domain.repository

import androidx.lifecycle.LiveData
import com.example.core.data.ResponseCredits
import com.example.core.data.ResponseDetailMovie
import com.example.core.data.ResponseDetailSeries
import com.example.core.data.ResultState
import com.example.core.data.ResultsItemActors
import com.example.core.data.ResultsItemNow
import com.example.core.data.ResultsItemSeries
import com.example.core.data.local.LocalDataSource
import com.example.core.data.local.MoviesEntity
import com.example.core.data.model.DataMapper
import com.example.core.data.model.Movies
import com.example.core.data.remote.RemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MoviesRepository(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
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

    override suspend fun getAllSavedMovies(): Flow<List<Movies>> {
        return localDataSource.getAllSavedMovies().map { moviesEntity ->
            moviesEntity.map { DataMapper.mapEntityToDomain(it) }
        }
    }

    override suspend fun insertMovies(movies: List<Movies>) {
        val moviesEntities = movies.map { DataMapper.mapDomainToEntity(it) }
        localDataSource.insertMovies(moviesEntities)
    }

    override suspend fun deleteMoviesById(id: Int) {
        return localDataSource.deleteMoviesById(id)
    }

    override suspend fun getFavoriteUser(id: Int): Flow<Movies?> {
        return localDataSource.getFavoriteStatus(id).map { moviesEntity ->
            moviesEntity?.let { DataMapper.mapEntityToDomain(it) }
        }
    }

}