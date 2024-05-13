package com.belajar.api.kotlin.entities

data class WebErrorResponse<T>(

    val code: Int,

    val status: String,

    val message: T,

)