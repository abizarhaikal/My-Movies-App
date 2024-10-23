package com.example.core.data.model

import android.graphics.Movie
import com.example.core.data.local.MoviesEntity

object DataMapper {

    fun mapEntityToDomain(input : MoviesEntity) : Movies {
        return Movies(
            id = input.id,
            posterPath = input.posterPath,
            title = input.title,
            overview = input.overview,
            isFavorite = input.isFavorite
        )
    }

    fun mapDomainToEntity(input: Movies) : MoviesEntity{
        return MoviesEntity(
            id = input.id,
            posterPath = input.posterPath,
            title = input.title,
            overview = input.overview,
            isFavorite = input.isFavorite
        )
    }
}