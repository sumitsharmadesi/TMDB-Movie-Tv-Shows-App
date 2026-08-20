package com.sumit.people

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.sumit.domain.model.PersonalItem
import com.sumit.domain.repository.TMDBRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class PeopleViewModel @Inject constructor(repository: TMDBRepository): ViewModel(){
    val people: Flow<PagingData<PersonalItem>> = repository.people().cachedIn(viewModelScope)
}