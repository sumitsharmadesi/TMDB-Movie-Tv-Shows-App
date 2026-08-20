package com.sumit.details

import com.sumit.domain.model.MediaDetail

data class DetailsState(
    val detail: MediaDetail?=null,
    val loading: Boolean = true,
    val error: String?=null
)