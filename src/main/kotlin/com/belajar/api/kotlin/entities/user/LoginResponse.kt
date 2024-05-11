package com.belajar.api.kotlin.entities.user

data class LoginResponse(

    var username: String? = null,

    var token: String? = null,

    val roles: List<String>? = null

)
