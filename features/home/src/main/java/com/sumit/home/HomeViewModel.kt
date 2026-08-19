package com.sumit.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sumit.domain.repository.TMDBRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: TMDBRepository): ViewModel(){
    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()
    init {
        refresh()
    }
    fun refresh() = viewModelScope.launch {
        runCatching {
            HomeState(
                repository.trendingMovies(),
                repository.trendingTv(),false
            )
        }.onSuccess { _state.value = it }
            .onFailure { error ->
                Log.e(
                    "TMDB_ERROR",
                    "Failed to load home data",
                    error
                )

                _state.value = HomeState(
                    loading = false,
                    error = error.message ?: "Unable to load data"
                )
            }
    }

}