package com.sumit.people

import androidx.paging.PagingData
import com.sumit.domain.repository.TMDBRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import org.junit.Test

class PeopleViewModelTest {
    private val repository = mockk<TMDBRepository>(relaxed = true)
    @Test
    fun `viewModel initializes people flow from repository`(){
        every { repository.people() } returns flowOf(PagingData.empty())
        val viewModel = PeopleViewModel(repository)
        verify { repository.people() }
    }
}