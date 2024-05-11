package com.belajar.api.kotlin.entities

data class WebResponse<T>(

    val code: Int,

    val status: String,

    val data: T,

)