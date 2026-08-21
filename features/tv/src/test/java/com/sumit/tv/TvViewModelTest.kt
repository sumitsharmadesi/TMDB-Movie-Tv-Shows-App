package com.sumit.tv

import androidx.paging.PagingData
import com.sumit.domain.model.TvCategory
import com.sumit.domain.repository.TMDBRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import org.junit.Test

class TvViewModelTest {
    private val repository= mockk<TMDBRepository>(relaxed = true)
    @Test
    fun `viewmodel initializes flow from repository`(){
        every { repository.tv(TvCategory.POPULAR) } returns flowOf(PagingData.empty())
        every { repository.tv(TvCategory.TOP_RATED) } returns flowOf(PagingData.empty())
        every { repository.tv(TvCategory.AIRING_TODAY) } returns flowOf(PagingData.empty())
        val viewModel = TvViewModel(repository)
        verify { repository.tv(TvCategory.POPULAR) }
        verify { repository.tv(TvCategory.TOP_RATED) }
        verify { repository.tv(TvCategory.AIRING_TODAY) }
    }
}