package com.sumit.tv

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.sumit.domain.model.MediaItem
import com.sumit.domain.model.TvCategory
import com.sumit.domain.repository.TMDBRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class TvViewModel @Inject constructor(repository: TMDBRepository): ViewModel(){
    val popular: Flow<PagingData<MediaItem>> = repository.tv(TvCategory.POPULAR).cachedIn(viewModelScope)
    val topRated = repository.tv(TvCategory.TOP_RATED).cachedIn(viewModelScope)
    val upcoming = repository.tv(TvCategory.AIRING_TODAY).cachedIn(viewModelScope)
}