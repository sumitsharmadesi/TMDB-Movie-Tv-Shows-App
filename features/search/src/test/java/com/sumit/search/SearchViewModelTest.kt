package com.sumit.search

import app.cash.turbine.test
import com.sumit.domain.model.MediaItem
import com.sumit.domain.model.MediaType
import com.sumit.domain.usecase.SearchUseCase
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SearchViewModelTest {
    private val testDispatcher = StandardTestDispatcher()
    private val searchUseCase = mockk<SearchUseCase>()
    @Before
    fun setup(){
        Dispatchers.setMain(testDispatcher)
    }
    @Before
    fun tearDown(){
        Dispatchers.resetMain()
    }
    @Test
    fun `searching emits results after debounce`()= runTest {
        val results = listOf(MediaItem(1,"Search Result",null,null,0.0, MediaType.MOVIE))
        coEvery { searchUseCase("query") } returns results
        val viewModel = SearchViewModel(searchUseCase)
        viewModel.results.test {
            assertEquals(emptyList<MediaItem>(),awaitItem())
            viewModel.setQuery("query")
            advanceTimeBy(400)
            assertEquals(results,awaitItem())
        }
    }
}