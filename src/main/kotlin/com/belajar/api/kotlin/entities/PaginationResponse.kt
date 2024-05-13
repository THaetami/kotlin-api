package com.belajar.api.kotlin.entities

data class PaginationResponse(
    var totalPages: Int? = null,
    val totalElement: Long? = null,
    val page: Int? = null,
    val size: Int? = null,
    val hasNext: Boolean? = null,
    val hasPrevious: Boolean? = null,
)
