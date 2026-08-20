package com.sumit.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sumit.domain.model.MediaItem
import com.sumit.domain.usecase.SearchUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class SearchViewModel @Inject constructor(private val search: SearchUseCase): ViewModel() {
    private val query = MutableStateFlow("")
    val results: StateFlow<List<MediaItem>> = query.debounce { 350 }.distinctUntilChanged().flatMapLatest()
    { flow {
            emit( runCatching {
                search(it)
            }.getOrDefault(emptyList()))
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000),emptyList())
    fun setQuery(queryText: String){
        query.value = queryText
    }
}