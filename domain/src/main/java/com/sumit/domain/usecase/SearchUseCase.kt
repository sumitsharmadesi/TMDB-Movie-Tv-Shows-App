package com.sumit.domain.usecase

import com.sumit.domain.model.MediaItem
import com.sumit.domain.repository.TMDBRepository
import javax.inject.Inject

class SearchUseCase @Inject constructor(private val repository: TMDBRepository) {
    suspend operator fun invoke(query: String): List<MediaItem> = if(query.isBlank()) emptyList() else repository.search(query.trim())
}