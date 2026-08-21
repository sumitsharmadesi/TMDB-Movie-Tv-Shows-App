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

data class MoviePage(
    val page: Int = 1,
    val total_pages: Int = 1,
    val results: List<MovieDto> = arrayListOf());
data class MovieDto(
    val id: Int,
    val title: String? = null,
    val poster_path: String? = null,
    val overview: String? = null,
    val release_date: String? = null,
    val vote_average: Double = 0.0
)

data class TvPage(
    val page: Int = 1,
    val total_pages: Int = 1,
    val results: List<TvDto> = arrayListOf());
data class TvDto(
    val id: Int,
    val name: String? = null,
    val poster_path: String? = null,
    val first_air_date: String? = null,
    val vote_average: Double = 0.0
)

data class PeoplePage(
    val page: Int = 1,
    val total_pages: Int = 1,
    val results: List<PersonDto> = arrayListOf()); data class PersonDto(
    val id: Int,
    val name: String,
    val profile_path: String? = null,
    val known_for: List<KnownDto> = arrayListOf()); data class KnownDto(
    val id: Int,
    val title: String? = null,
    val name: String? = null,
    val media_type: String? = null,
    val poster_path: String? = null
)

data class Genre(
    val id: Int,
    val name: String
); data class Credits(val cast: List<Cast> =arrayListOf()); data class Cast(
    val id: Int,
    val name: String,
    val character: String? = null,
    val profile_path: String? = null
); data class VideoResults(val results: List<Video> =arrayListOf()); data class Video(
    val key: String,
    val site: String,
    val type: String
)

data class MovieDetailDto(
    val id: Int,
    val title: String,
    val tagline: String? = null,
    val overview: String? = null,
    val poster_path: String? = null,
    val backdrop_path: String? = null,
    val release_date: String? = null,
    val runtime: Int? = null,
    val vote_average: Double = 0.0,
    val genres: List<Genre> =arrayListOf(),
    val credits: Credits? = null,
    val similar: MoviePage? = null,
    val videos: VideoResults? = null)

data class TvDetailDto(
    val id: Int,
    val name: String,
    val tagline: String? = null,
    val overview: String? = null,
    val poster_path: String? = null,
    val backdrop_path: String? = null,
    val first_air_date: String? = null,
    val episode_run_time: List<Int> =arrayListOf(),
    val vote_average: Double = 0.0,
    val genres: List<Genre> =arrayListOf(),
    val credits: Credits? = null,
    val similar: TvPage? = null,
    val videos: VideoResults? = null)

data class PersonDetailDto(
    val id: Int,
    val name: String,
    val biography: String? = null,
    val birthday: String? = null,
    val place_of_birth: String? = null,
    val profile_path: String? = null,
    val combined_credits: Combined? = null
); data class Combined(val cast: List<KnownDto> =arrayListOf()); data class SearchPage(val results: List<SearchDto> =arrayListOf()); data class SearchDto(
    val id: Int,
    val media_type: String,
    val title: String? = null,
    val name: String? = null,
    val poster_path: String? = null,
    val profile_path: String? = null,
    val release_date: String? = null,
    val first_air_date: String? = null,
    val vote_average: Double = 0.0
)

enum class MovieCategory{ POPULAR,TOP_RATED,UPCOMING}
enum class TvCategory{ POPULAR,TOP_RATED,AIRING_TODAY}