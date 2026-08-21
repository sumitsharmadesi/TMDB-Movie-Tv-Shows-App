package com.sumit.details

import app.cash.turbine.test
import com.sumit.domain.model.MediaDetail
import com.sumit.domain.model.MediaType
import com.sumit.domain.model.PersonDetail
import com.sumit.domain.repository.TMDBRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DetailsViewModelTest {
    private val testDispatcher = UnconfinedTestDispatcher()
    private val repository = mockk<TMDBRepository>()
    @Before
    fun setup(){
        Dispatchers.setMain(testDispatcher)
    }
    @After
    fun tearDown(){
        Dispatchers.resetMain()
    }
    @Test
    fun `DetailViewModel load movie detail successfully`() = runTest {
        val detail = mockk<MediaDetail>()
        coEvery { repository.movie(1) } returns detail
        val viewModel = DetailsViewModel(repository)
        viewModel.load(1, MediaType.MOVIE)

        viewModel.state.test {
            val state = awaitItem()
            assertEquals(detail, state.detail)
            assertEquals(false, state.loading)
        }
    }
    @Test
    fun `PersonViewModel load person detail successfully`() = runTest {
        val detail = mockk<PersonDetail>()
        coEvery { repository.person(1) } returns detail
        val viewModel = PersonViewModel(repository)
        viewModel.load(1)
        viewModel.state.test {
            val state = awaitItem()
            assertEquals(detail,state.detail)
            assertEquals(false,state.loading)
        }
    }
}