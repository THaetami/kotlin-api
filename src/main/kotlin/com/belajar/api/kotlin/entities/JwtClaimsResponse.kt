package com.belajar.api.kotlin.entities

data class  JwtClaimsResponse(
    var userAccountId: String? = null,
    var roles: List<String>? = null
)
