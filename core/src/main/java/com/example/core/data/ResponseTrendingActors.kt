package com.example.core.data

import com.google.gson.annotations.SerializedName

data class ResponseTrendingActors(

    @field:SerializedName("page")
	val page: Int,

    @field:SerializedName("total_pages")
	val totalPages: Int,

    @field:SerializedName("results")
	val results: List<ResultsItemActors>,

    @field:SerializedName("total_results")
	val totalResults: Int
)

data class ResultsItemActors(

    @field:SerializedName("gender")
	val gender: Int,

    @field:SerializedName("media_type")
	val mediaType: String,

    @field:SerializedName("known_for_department")
	val knownForDepartment: String,

    @field:SerializedName("original_name")
	val originalName: String,

    @field:SerializedName("popularity")
	val popularity: Any,

    @field:SerializedName("known_for")
	val knownFor: List<KnownForItem>,

    @field:SerializedName("name")
	val name: String,

    @field:SerializedName("profile_path")
	val profilePath: String,

    @field:SerializedName("id")
	val id: Int,

    @field:SerializedName("adult")
	val adult: Boolean
)

data class KnownForItem(

	@field:SerializedName("overview")
	val overview: String,

	@field:SerializedName("original_language")
	val originalLanguage: String,

	@field:SerializedName("original_title")
	val originalTitle: String,

	@field:SerializedName("video")
	val video: Boolean,

	@field:SerializedName("title")
	val title: String,

	@field:SerializedName("genre_ids")
	val genreIds: List<Int>,

	@field:SerializedName("poster_path")
	val posterPath: String,

	@field:SerializedName("backdrop_path")
	val backdropPath: String,

	@field:SerializedName("media_type")
	val mediaType: String,

	@field:SerializedName("release_date")
	val releaseDate: String,

	@field:SerializedName("popularity")
	val popularity: Any,

	@field:SerializedName("vote_average")
	val voteAverage: Any,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("adult")
	val adult: Boolean,

	@field:SerializedName("vote_count")
	val voteCount: Int,

	@field:SerializedName("first_air_date")
	val firstAirDate: String,

	@field:SerializedName("origin_country")
	val originCountry: List<String>,

	@field:SerializedName("original_name")
	val originalName: String,

	@field:SerializedName("name")
	val name: String
)
