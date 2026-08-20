package com.sumit.details

import com.sumit.domain.model.PersonDetail

data class PersonState(
    val detail: PersonDetail?=null,
    val loading: Boolean = true,
    val error: String?=null
)