package com.belajar.api.kotlin.entities.customer

data class SearchCustomerRequest(
    val name: String,
    val direction: String,
    val sortBy: String,
    val page: Int,
    val size: Int,
)
