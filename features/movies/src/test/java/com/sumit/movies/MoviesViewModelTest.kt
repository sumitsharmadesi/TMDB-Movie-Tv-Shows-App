package com.sumit.movies

import androidx.paging.PagingData
import com.sumit.domain.model.MovieCategory
import com.sumit.domain.repository.TMDBRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import org.junit.Test

class MoviesViewModelTest {
    private val repository = mockk<TMDBRepository>(relaxed = true)
    @Test
    fun `viewModel initializes flow from repository`(){
        every { repository.movies(MovieCategory.POPULAR) } returns flowOf(PagingData.empty())
        every { repository.movies(MovieCategory.UPCOMING) } returns flowOf(PagingData.empty())
        every { repository.movies(MovieCategory.TOP_RATED) } returns flowOf(PagingData.empty())
        val viewModel = MoviesViewModel(repository)
        verify { repository.movies(MovieCategory.POPULAR) }
        verify { repository.movies(MovieCategory.UPCOMING) }
        verify { repository.movies(MovieCategory.TOP_RATED) }
    }
}