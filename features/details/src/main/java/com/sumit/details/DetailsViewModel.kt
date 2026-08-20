package com.sumit.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sumit.domain.model.MediaType
import com.sumit.domain.repository.TMDBRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(private val repository: TMDBRepository): ViewModel(){
    private val _state = MutableStateFlow(DetailsState())
    val state = _state.asStateFlow()
    fun load(id: Int, type: MediaType) =viewModelScope.launch {
        runCatching {
            if(type== MediaType.MOVIE) repository.movie(id)
            else repository.tv(id)
        }.onSuccess {
            _state.value = DetailsState(it,false)
        }.onFailure {
            _state.value = DetailsState(loading = false, error = "Unable to load details")
        }
    }
}