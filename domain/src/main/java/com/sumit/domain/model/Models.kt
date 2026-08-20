package com.sumit.domain.model

data class MediaItem(
    val id: Int,
    val title:String,
    val posterPath: String?,
    val date: String?,
    val rating: Double,
    val type: MediaType
)
enum class MediaType{ MOVIE,TV,PERSON}
data class PersonalItem(
    val id: Int,
    val name: String,
    val profilePath: String?,
    val knownFor: String
)
data class PersonDetail(
    val id: Int,
    val name: String,
    val biography: String?,
    val birthday: String?,
    val birthPlace: String?,
    val profilePath: String?,
    val knownFor: List<MediaItem>
)
data class MediaDetail(
    val id: Int,
    val title: String,
    val tagline: String?,
    val overview: String?,
    val posterPath: String?,
    val backDropPath: String?,
    val date: String?,
    val runtime: String?,
    val rating: Double,
    val genres: List<String>,
    val cast: List<PersonalItem>,
    val similar: List<MediaItem>,
    val trailerKey: String?,
    val type: MediaType
)
enum class MovieCategory{ POPULAR,TOP_RATED,UPCOMING}
enum class TvCategory{ POPULAR,TOP_RATED,AIRING_TODAY}