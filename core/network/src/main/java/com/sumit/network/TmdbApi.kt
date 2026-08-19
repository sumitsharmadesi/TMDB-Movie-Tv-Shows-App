package com.sumit.network

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TmdbApi {
    @GET("trending/movie/day")
    suspend fun trendingMovies(): MoviePage;

    @GET("movie/popular")
    suspend fun popularMovies(@Query("page") p: Int): MoviePage;

    @GET("movie/top_rated")
    suspend fun topRatedMovies(@Query("page") p: Int): MoviePage;

    @GET("movie/upcoming")
    suspend fun upcomingMovies(@Query("page") p: Int): MoviePage;

    @GET("trending/tv/day")
    suspend fun trendingTv(): TvPage;

    @GET("tv/popular")
    suspend fun popularTv(@Query("page") p: Int): TvPage;

    @GET("tv/top_rated")
    suspend fun topRatedTv(@Query("page") p: Int): TvPage;

    @GET("tv/airing_today")
    suspend fun airingTodayTv(@Query("page") p: Int): TvPage;

    @GET("person/popular")
    suspend fun people(@Query("page") p: Int): PeoplePage;

    @GET("movie/{id}")
    suspend fun movie(@Path("id") id: Int): MovieDetailDto;

    @GET("tv/{id}")
    suspend fun tv(@Path("id") id: Int): TvDetailDto;

    @GET("person/{id}")
    suspend fun person(@Path("id") id: Int): PersonDetailDto;

    @GET("search/multi")
    suspend fun search(@Query("query") q: String): SearchPage
}

data class MoviePage(
    val page: Int = 1,
    val total_pages: Int = 1,
    val results: List<MovieDto> = arrayListOf()
);
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
    val results: List<TvDto> = arrayListOf()
);
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
    val results: List<PersonDto> = arrayListOf()
); data class PersonDto(
    val id: Int,
    val name: String,
    val profile_path: String? = null,
    val known_for: List<KnownDto> = arrayListOf()
); data class KnownDto(
    val id: Int,
    val title: String? = null,
    val name: String? = null,
    val media_type: String? = null,
    val poster_path: String? = null
)

data class Genre(
    val id: Int,
    val name: String
); data class Credits(val cast: List<Cast> = arrayListOf()); data class Cast(
    val id: Int,
    val name: String,
    val character: String? = null,
    val profile_path: String? = null
); data class VideoResults(val results: List<Video> = arrayListOf()); data class Video(
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
    val genres: List<Genre> = arrayListOf(),
    val credits: Credits? = null,
    val similar: MoviePage? = null,
    val videos: VideoResults? = null
)

data class TvDetailDto(
    val id: Int,
    val name: String,
    val tagline: String? = null,
    val overview: String? = null,
    val poster_path: String? = null,
    val backdrop_path: String? = null,
    val first_air_date: String? = null,
    val episode_run_time: List<Int> = arrayListOf(),
    val vote_average: Double = 0.0,
    val genres: List<Genre> = arrayListOf(),
    val credits: Credits? = null,
    val similar: TvPage? = null,
    val videos: VideoResults? = null
)

data class PersonDetailDto(
    val id: Int,
    val name: String,
    val biography: String? = null,
    val birthday: String? = null,
    val place_of_birth: String? = null,
    val profile_path: String? = null,
    val combined_credits: Combined? = null
); data class Combined(val cast: List<KnownDto> = arrayListOf()); data class SearchPage(val results: List<SearchDto> = arrayListOf()); data class SearchDto(
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