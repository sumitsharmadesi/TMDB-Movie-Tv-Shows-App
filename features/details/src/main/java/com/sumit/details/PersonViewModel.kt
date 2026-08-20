package com.sumit.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sumit.domain.repository.TMDBRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PersonViewModel @Inject constructor(private val repository: TMDBRepository): ViewModel(){
    private val _state = MutableStateFlow(PersonState())
    val state = _state.asStateFlow()
    fun load(id: Int) = viewModelScope.launch {
        runCatching {
            repository.person(id)
        }.onSuccess {
            _state.value = PersonState(it,false)
        }.onFailure {
            _state.value = PersonState(loading = false, error = "Unable to load person")
        }
    }
}