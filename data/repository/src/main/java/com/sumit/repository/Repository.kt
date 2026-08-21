package com.sumit.repository

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.sumit.database.CachedDetails
import com.sumit.database.CachedMedia
import com.sumit.database.CachedPerson
import com.sumit.database.CachedPersonDetail
import com.sumit.database.MovieDao
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
class Repository @Inject constructor(private val api: TmdbApi,private val dao: MovieDao) : TMDBRepository {
    override suspend fun trendingMovies(): List<MediaItem> = runCatching {
        api.trendingMovies().results.map {
            MediaItem(it.id,it.title.orEmpty(),it.poster_path,it.release_date,it.vote_average, MediaType.MOVIE)
        }.also {items->
            dao.upsertMedia(items.map {
                CachedMedia(it.id,it.title,it.posterPath,it.date,it.rating,"MOVIE","TRENDING")
            })
        }
    }.getOrElse {
        dao.getMedia("MOVIE","TRENDING").map {
            MediaItem(it.id,it.title,it.poster,it.date,it.rating, MediaType.MOVIE)
        }
    }

    override suspend fun trendingTv() = runCatching {
        api.trendingTv().results.map {
            MediaItem(it.id,it.name.orEmpty(),it.poster_path, it.first_air_date, it.vote_average, MediaType.TV
            )
        }.also { items ->
            dao.upsertMedia(items.map {
                CachedMedia(it.id,it.title,it.posterPath,it.date,it.rating,"TV","TRENDING")
            })
        }
    }.getOrElse {
        dao.getMedia("TV","TRENDING").map {
            MediaItem(it.id,it.title,it.poster,it.date,it.rating, MediaType.TV)
        }
    }

    override fun movies(category: MovieCategory) = Pager(PagingConfig(20)) {
        object : PagingSource<Int, MediaItem>() {
            override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MediaItem> = runCatching {
                val page = params.key?: 1
                val respose = when(category){
                    MovieCategory.POPULAR->api.popularMovies(page)
                    MovieCategory.TOP_RATED->api.topRatedMovies(page)
                    MovieCategory.UPCOMING->api.upcomingMovies(page)
                }
                val items = respose.results.map {
                    MediaItem(it.id,it.title.orEmpty(),it.poster_path,it.release_date,it.vote_average,
                        MediaType.MOVIE)
                }
                if(page==1){
                    dao.deleteMedia("MOVIE",category.name)
                }
                dao.upsertMedia(items.map {
                    CachedMedia(it.id,it.title,it.posterPath,it.date,it.rating,"MOVIE",category.name,page)
                })
                LoadResult.Page(items,if(page==1) null else page-1,if(page>respose.total_pages) null else page+1)
            }.getOrElse {
                val page = params.key?:1
                if(page==1){
                    val cached = dao.getMedia("MOVIE",category.name)
                    if(cached.isNotEmpty()){
                        return@getOrElse LoadResult.Page(
                            cached.map {
                                MediaItem(it.id,it.title,it.poster,it.date,it.rating, MediaType.MOVIE)
                            },null,null
                        )
                    }
                }
                LoadResult.Error(it)
            }

            override fun getRefreshKey(state: PagingState<Int, MediaItem>) = state.anchorPosition
        }
    }.flow

    override fun tv(category: TvCategory) = Pager(PagingConfig(20)) {
        object : PagingSource<Int, MediaItem>() {
            override suspend fun load(params: LoadParams<Int>):LoadResult<Int, MediaItem> = runCatching {
                val page = params.key?:1
                val response = when(category){
                    TvCategory.POPULAR->api.popularTv(page)
                    TvCategory.TOP_RATED->api.topRatedTv(page)
                    TvCategory.AIRING_TODAY->api.airingTodayTv(page)
                }
                val items = response.results.map {
                    MediaItem(it.id,it.name.orEmpty(),it.poster_path,it.first_air_date,it.vote_average, MediaType.TV)
                }
                if (page==1){
                    dao.deleteMedia("TV",category.name)
                }
                dao.upsertMedia(items.map {
                    CachedMedia(it.id,it.title,it.posterPath,it.date,it.rating,"TV",category.name,page)
                })
                LoadResult.Page(items, if (page==1) null else page -1,if(page==response.total_pages)null else page+1)
            }.getOrElse {
                val page = params.key?:1
                if(page==1){
                    val cached = dao.getMedia("TV",category.name)
                    if(cached.isNotEmpty()){
                        return@getOrElse LoadResult.Page(
                            cached.map {
                                MediaItem(it.id,it.title,it.poster,it.date,it.rating, MediaType.TV)
                            },null,null
                        )
                    }
                }
                LoadResult.Error(it)
            }

            override fun getRefreshKey(state: PagingState<Int, MediaItem>) = state.anchorPosition
        }
    }.flow

    override fun people() = Pager(PagingConfig(20)) {
        object : PagingSource<Int, PersonalItem>() {
            override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PersonalItem> = runCatching {
                val page = params.key?:1
                val response = api.people(page)
                val items = response.results.map {
                    PersonalItem(it.id,it.name,it.profile_path,it.known_for.firstOrNull()?.name.orEmpty())
                }
                dao.upsertPeople(items.map {
                    CachedPerson(it.id,it.name,it.profilePath,it.knownFor,"POPULAR",page)
                })
                LoadResult.Page(items,if(page==1)null else page-1,if(page==response.total_pages)null else page+1)
            }.getOrElse {
                val page = params.key?:1
                if(page==1){
                    val cached = dao.getPeople("POPULAR")
                    if(cached.isNotEmpty()){
                        return@getOrElse LoadResult.Page(
                            cached.map {
                                PersonalItem(it.id,it.name,it.profilePath,it.knownFor)
                            },null,null
                        )
                    }
                }
                LoadResult.Error(it)
            }

            override fun getRefreshKey(state: PagingState<Int, PersonalItem>) = state.anchorPosition
        }
    }.flow

    override suspend fun movie(id: Int) = runCatching {
        api.movie(id).let { d ->
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
        }.also {detail ->
            dao.upsertDetail(CachedDetails(
                detail.id, detail.title, detail.tagline, detail.overview, detail.posterPath, detail.backDropPath,
                detail.date, detail.runtime, detail.rating, detail.genres.joinToString(","), "MOVIE"
            ))
        }
    }.getOrElse {
        dao.getDetail(id,"MOVIE")?.let { d->
            MediaDetail(
                d.id, d.title, d.tagline, d.overview, d.posterPath, d.backdropPath, d.date, d.runtime, d.rating,
                d.genres.split(",").filter { it.isNotBlank() }, emptyList(), emptyList(), null, MediaType.MOVIE
            )
        }?: throw it
    }

    override suspend fun tv(id: Int) = runCatching {
        api.tv(id).let { d ->
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
        }.also { detail ->
            dao.upsertDetail(CachedDetails(
                detail.id, detail.title, detail.tagline, detail.overview, detail.posterPath, detail.backDropPath,
                detail.date, detail.runtime, detail.rating, detail.genres.joinToString(","), "TV"
            ))
        }
    }.getOrElse {
        dao.getDetail(id,"TV")?.let { d->
            MediaDetail(
                d.id, d.title, d.tagline, d.overview, d.posterPath, d.backdropPath, d.date, d.runtime, d.rating,
                d.genres.split(",").filter { it.isNotBlank() }, emptyList(), emptyList(), null, MediaType.TV
            )
        }?:throw it
    }

    override suspend fun person(id: Int) = runCatching {
        api.person(id).let { d ->
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
        }.also { detail ->
            dao.upsertPersonDetail(CachedPersonDetail(
                detail.id, detail.name, detail.biography, detail.birthday, detail.birthPlace, detail.profilePath
            ))
        }
    }.getOrElse {
        dao.getPersonDetail(id)?.let { d->
            PersonDetail(d.id, d.name, d.biography, d.birthday, d.birthplace, d.profilePath, emptyList())
        }?:throw it
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