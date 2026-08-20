package com.sumit.movies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.sumit.domain.model.MediaItem
import com.sumit.domain.model.MovieCategory
import com.sumit.domain.repository.TMDBRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(repository: TMDBRepository): ViewModel(){
    val popular: Flow<PagingData<MediaItem>> = repository.movies(MovieCategory.POPULAR).cachedIn(viewModelScope)
    val topRated = repository.movies(MovieCategory.TOP_RATED).cachedIn(viewModelScope)
    val upcoming = repository.movies(MovieCategory.UPCOMING).cachedIn(viewModelScope)
}