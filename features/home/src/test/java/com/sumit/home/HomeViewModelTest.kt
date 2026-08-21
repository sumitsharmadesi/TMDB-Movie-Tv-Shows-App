package com.sumit.home

import android.util.Log
import androidx.paging.PagingData
import app.cash.turbine.test
import com.sumit.domain.model.MediaDetail
import com.sumit.domain.model.MediaItem
import com.sumit.domain.model.MediaType
import com.sumit.domain.model.MovieCategory
import com.sumit.domain.model.PersonDetail
import com.sumit.domain.model.PersonalItem
import com.sumit.domain.model.TvCategory
import com.sumit.domain.repository.TMDBRepository
import io.mockk.every
import io.mockk.mockkStatic
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {
    private val testDispatcher = UnconfinedTestDispatcher()
    private val repository = FakeTMDBRepository()

    @Before
    fun setup(){
        Dispatchers.setMain(testDispatcher)
        mockkStatic(Log::class)
        every { Log.e(any(),any(),any()) } returns 0
    }

    @After
    fun tearDown(){
        Dispatchers.resetMain()
    }
    @Test
    fun `refresh loads trending data successfully`()= runTest {
        val movies = listOf(MediaItem(1,"Movie",null,null,0.0, MediaType.MOVIE))
        val tvs = listOf(MediaItem(2,"TV",null,null,0.0, MediaType.TV))
        repository.trendingMovies = movies
        repository.trendingTvs = tvs
        val viewModel = HomeViewModel(repository)
        viewModel.state.test {
            val state = awaitItem()
            assertEquals(movies,state.movies)
            assertEquals(tvs,state.tv)
            assertEquals(false,state.loading)
            assertEquals(null,state.error)
        }
    }
    @Test
    fun `refresh handles error when repository fails`()=runTest {
        repository.shouldFail = true
        val viewModel = HomeViewModel(repository)
        viewModel.state.test {
            val state = awaitItem()
            assertEquals(false,state.loading)
            assertEquals("Network Error",state.error)
        }
    }

}
class FakeTMDBRepository: TMDBRepository{
    var trendingMovies = emptyList<MediaItem>()
    var trendingTvs=emptyList<MediaItem>()
    var shouldFail = false
    override suspend fun trendingMovies(): List<MediaItem> {
        if(shouldFail) throw Exception("Network Error")
        return trendingMovies
    }

    override suspend fun trendingTv(): List<MediaItem> {
        if(shouldFail) throw Exception("Network Error")
        return trendingTvs
    }

    override fun movies(category: MovieCategory): Flow<PagingData<MediaItem>> =TODO()

    override fun tv(category: TvCategory): Flow<PagingData<MediaItem>> =TODO()

    override fun people(): Flow<PagingData<PersonalItem>> =TODO()

    override suspend fun movie(id: Int): MediaDetail =TODO()

    override suspend fun tv(id: Int): MediaDetail =TODO()

    override suspend fun person(id: Int): PersonDetail =TODO()

    override suspend fun search(query: String): List<MediaItem> =TODO()

}