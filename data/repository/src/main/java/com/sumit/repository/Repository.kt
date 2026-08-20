package com.sumit.repository

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.sumit.domain.model.MediaDetail
import com.sumit.domain.model.MediaItem
import com.sumit.domain.model.MediaType
import com.sumit.domain.model.MovieCategory
import com.sumit.domain.model.PersonDetail
import com.sumit.domain.model.PersonalItem
import com.sumit.domain.model.TvCategory
import com.sumit.domain.repository.TMDBRepository
import com.sumit.network.TmdbApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.div
import kotlin.rem
import kotlin.text.map
import kotlin.text.orEmpty

@Singleton
class Repository @Inject constructor(private val api: TmdbApi) : TMDBRepository {
    override suspend fun trendingMovies(): List<MediaItem> {
        Log.d("TMDB_DEBUG", "Repository: trendingMovies() START")

        val response = api.trendingMovies()

        Log.d(
            "TMDB_DEBUG",
            "Repository: trendingMovies() RESPONSE = ${response.results.size} items"
        )

        return response.results.map {
            MediaItem(
                it.id,
                it.title.orEmpty(),
                it.poster_path,
                it.release_date,
                it.vote_average,
                MediaType.MOVIE
            )
        }
    }

    override suspend fun trendingTv() = api.trendingTv().results.map {
        MediaItem(
            it.id,
            it.name.orEmpty(),
            it.poster_path,
            it.first_air_date,
            it.vote_average,
            MediaType.TV
        )
    }

    override fun movies(c: MovieCategory) = Pager(PagingConfig(20)) {
        object : PagingSource<Int, MediaItem>() {
            override suspend fun load(p: LoadParams<Int>) = runCatching {
                val n = p.key ?: 1;
                val r = when (c) {
                    MovieCategory.POPULAR -> api.popularMovies(n); MovieCategory.TOP_RATED -> api.topRatedMovies(
                        n
                    ); MovieCategory.UPCOMING -> api.upcomingMovies(n)
                }; LoadResult.Page(r.results.map {
                MediaItem(
                    it.id,
                    it.title.orEmpty(),
                    it.poster_path,
                    it.release_date,
                    it.vote_average,
                    MediaType.MOVIE
                )
            }, if (n == 1) null else n - 1, if (n >= r.total_pages) null else n + 1)
            }.getOrElse { LoadResult.Error(it) };

            override fun getRefreshKey(s: PagingState<Int, MediaItem>) = s.anchorPosition
        }
    }.flow

    override fun tv(c: TvCategory) = Pager(PagingConfig(20)) {
        object : PagingSource<Int, MediaItem>() {
            override suspend fun load(p: LoadParams<Int>) = runCatching {
                val n = p.key ?: 1;
                val r = when (c) {
                    TvCategory.POPULAR -> api.popularTv(n); TvCategory.TOP_RATED -> api.topRatedTv(n); TvCategory.AIRING_TODAY -> api.airingTodayTv(
                        n
                    )
                }; LoadResult.Page(r.results.map {
                MediaItem(
                    it.id,
                    it.name.orEmpty(),
                    it.poster_path,
                    it.first_air_date,
                    it.vote_average,
                    MediaType.TV
                )
            }, if (n == 1) null else n - 1, if (n >= r.total_pages) null else n + 1)
            }.getOrElse { LoadResult.Error(it) };

            override fun getRefreshKey(s: PagingState<Int, MediaItem>) = s.anchorPosition
        }
    }.flow

    override fun people() = Pager(PagingConfig(20)) {
        object : PagingSource<Int, PersonalItem>() {
            override suspend fun load(p: LoadParams<Int>) = runCatching {
                val n = p.key ?: 1;
                val r = api.people(n); LoadResult.Page(r.results.map {
                PersonalItem(
                    it.id,
                    it.name,
                    it.profile_path,
                    it.known_for.firstOrNull()?.title ?: it.known_for.firstOrNull()?.name.orEmpty()
                )
            }, if (n == 1) null else n - 1, if (n >= r.total_pages) null else n + 1)
            }.getOrElse { LoadResult.Error(it) };

            override fun getRefreshKey(s: PagingState<Int, PersonalItem>) = s.anchorPosition
        }
    }.flow

    override suspend fun movie(id: Int) = api.movie(id).let { d ->
        MediaDetail(
            d.id,
            d.title,
            d.tagline,
            d.overview,
            d.poster_path,
            d.backdrop_path,
            d.release_date,
            d.runtime?.let { "${it / 60}h ${it % 60}m" },
            d.vote_average,
            d.genres.map { it.name },
            d.credits?.cast.orEmpty()
                .map { PersonalItem(it.id, it.name, it.profile_path, it.character.orEmpty()) },
            d.similar?.results.orEmpty().map {
                MediaItem(
                    it.id,
                    it.title.orEmpty(),
                    it.poster_path,
                    it.release_date,
                    it.vote_average,
                    MediaType.MOVIE
                )
            },
            d.videos?.results?.firstOrNull { it.site == "YouTube" && it.type == "Trailer" }?.key,
            MediaType.MOVIE
        )
    }

    override suspend fun tv(id: Int) = api.tv(id).let { d ->
        MediaDetail(
            d.id,
            d.name,
            d.tagline,
            d.overview,
            d.poster_path,
            d.backdrop_path,
            d.first_air_date,
            d.episode_run_time.firstOrNull()?.let { "$it min/episode" },
            d.vote_average,
            d.genres.map { it.name },
            d.credits?.cast.orEmpty()
                .map { PersonalItem(it.id, it.name, it.profile_path, it.character.orEmpty()) },
            d.similar?.results.orEmpty().map {
                MediaItem(
                    it.id,
                    it.name.orEmpty(),
                    it.poster_path,
                    it.first_air_date,
                    it.vote_average,
                    MediaType.TV
                )
            },
            d.videos?.results?.firstOrNull { it.site == "YouTube" && it.type == "Trailer" }?.key,
            MediaType.TV
        )
    }

    override suspend fun person(id: Int) = api.person(id).let { d ->
        PersonDetail(
            d.id,
            d.name,
            d.biography,
            d.birthday,
            d.place_of_birth,
            d.profile_path,
            d.combined_credits?.cast.orEmpty().map {
                MediaItem(
                    it.id,
                    it.title ?: it.name.orEmpty(),
                    it.poster_path,
                    null,
                    0.0,
                    if (it.media_type == "tv") MediaType.TV else MediaType.MOVIE
                )
            })
    }

    override suspend fun search(query: String) = api.search(query).results.map {
        MediaItem(
            it.id,
            it.title ?: it.name.orEmpty(),
            it.poster_path,
            it.release_date ?: it.first_air_date,
            it.vote_average,
            when (it.media_type) {
                "tv" -> MediaType.TV; "person" -> MediaType.PERSON; else -> MediaType.MOVIE
            }
        )
    }
}
@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule{
    @Provides
    @Singleton
    fun repository(repository: Repository): TMDBRepository = repository
}