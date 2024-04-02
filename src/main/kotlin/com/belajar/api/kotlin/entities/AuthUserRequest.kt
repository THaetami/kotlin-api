package com.belajar.api.kotlin.entities

import com.belajar.api.kotlin.annotation.EmailPasswordMatch
import com.belajar.api.kotlin.annotation.ValidEmail
import jakarta.validation.constraints.NotBlank

@EmailPasswordMatch(passwordPath = "password")
data class AuthUserRequest (

    @field:NotBlank
    @field:ValidEmail
    val email: String,

    @field:NotBlank
    val password: String

)