package com.example.core.di


import androidx.room.Room
import com.example.core.data.local.LocalDataSource
import com.example.core.domain.repository.MoviesRepository
import com.example.core.data.local.database.MoviesDatabase
import com.example.core.data.remote.RemoteDataSource
import com.example.core.domain.repository.IMoviesRepository
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val networkModule = module {
    single {
        val token = "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIyOTAwNGE2MDYyYmIyNjJhNzU5YjAyMWVjMDk0N2M4ZiIsIm5iZiI6MTcyNjUwNjM0MS43NDc5NzEsInN1YiI6IjY2ZTMxNWZiMjgwNDhkOTJkZWY5MGYwMyIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.1hJOsg_fJK3Mk0cQ_x9MOalONrK8f3OmxNWqzuYz6Js"
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .addInterceptor { chain ->
                val originalRequest = chain.request()
                val requestWithAuth = originalRequest.newBuilder()
                    .addHeader("Authorization", "Bearer $token")
                    .build()
                chain.proceed(requestWithAuth)
            }
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .build()
    }

    single {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(get())
            .build()
        retrofit.create(com.example.core.data.remote.ApiService::class.java)
    }
}

val repositoryModule = module {
    single { RemoteDataSource(get()) }
    single { LocalDataSource(get()) }
    single<IMoviesRepository> { MoviesRepository(get(), get()) }
}

val databaseModule = module {
    single {
        Room.databaseBuilder(
            get(),
            MoviesDatabase::class.java,
            "Movies.db"
        ).fallbackToDestructiveMigration().build()
    }
    single {
        get<MoviesDatabase>().moviesDao()
    }
}