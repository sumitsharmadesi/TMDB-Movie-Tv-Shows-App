package com.sumit.domain.repository

import androidx.paging.PagingData
import com.sumit.domain.model.MediaDetail
import com.sumit.domain.model.MediaItem
import com.sumit.domain.model.MovieCategory
import com.sumit.domain.model.PersonDetail
import com.sumit.domain.model.TvCategory
import kotlinx.coroutines.flow.Flow

interface TMDBRepository {
    suspend fun trendingMovies(): List<MediaItem>
    suspend fun trendingTv(): List<MediaItem>
    fun movies(category: MovieCategory): Flow<PagingData<MediaItem>>
    fun tv(category: TvCategory): Flow<PagingData<MediaItem>>
    fun people():Flow<PagingData<MediaItem>>
    suspend fun movie(id: Int): MediaDetail
    suspend fun tv(id: Int): MediaDetail
    suspend fun person(id: Int): PersonDetail
    suspend fun search(query: String): List<MediaItem>
}
