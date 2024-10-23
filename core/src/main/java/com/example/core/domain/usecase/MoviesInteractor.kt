package com.example.core.domain.usecase

import com.example.core.data.ResponseCredits
import com.example.core.data.ResponseDetailMovie
import com.example.core.data.ResponseDetailSeries
import com.example.core.data.ResultState
import com.example.core.data.ResultsItemActors
import com.example.core.data.ResultsItemNow
import com.example.core.data.ResultsItemSeries
import com.example.core.data.model.Movies
import com.example.core.domain.repository.IMoviesRepository
import kotlinx.coroutines.flow.Flow

class MoviesInteractor(private val moviesRepository: IMoviesRepository) : MoviesUseCase {
    override suspend fun getTrendingMovies(): Flow<ResultState<List<ResultsItemNow>>> {
        return moviesRepository.getTrendingMovies()
    }

    override suspend fun getTrendingSeries(): Flow<ResultState<List<ResultsItemSeries>>> {
        return moviesRepository.getTrendingTv()
    }

    override suspend fun getTrendingActors(): Flow<ResultState<List<ResultsItemActors>>> {
        return moviesRepository.getTrendingActors()
    }

    override suspend fun getDetailMovies(id: Int): Flow<ResultState<ResponseDetailMovie>> {
        return moviesRepository.getDetailMovies(id)
    }

    override suspend fun getCreditMovies(id: Int): Flow<ResultState<ResponseCredits>> {
        return moviesRepository.getCreditsMovies(id)
    }

    override suspend fun getDetailSeries(id: Int): Flow<ResultState<ResponseDetailSeries>> {
        return moviesRepository.getDetailSeries(id)
    }

    override suspend fun getCreditSeries(id: Int): Flow<ResultState<ResponseCredits>> {
        return moviesRepository.getCreditsSeries(id)
    }

    override suspend fun getAllSavedMovies(): Flow<List<Movies>> {
        return moviesRepository.getAllSavedMovies()
    }

    override suspend fun insertMovies(movies: List<Movies>) {
        return moviesRepository.insertMovies(movies)
    }

    override suspend fun deleteMoviesById(id: Int) {
        return moviesRepository.deleteMoviesById(id)
    }

    override suspend fun getFavoriteUser(id: Int): Flow<Movies?> {
        return moviesRepository.getFavoriteUser(id)
    }

}