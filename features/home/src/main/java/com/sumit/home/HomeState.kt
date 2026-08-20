package com.sumit.home

import com.sumit.domain.model.MediaItem

data class HomeState(
    val movies: List<MediaItem> = emptyList(),
    val tv: List<MediaItem> = emptyList(),
    val loading: Boolean= true,
    val error: String? = null
)