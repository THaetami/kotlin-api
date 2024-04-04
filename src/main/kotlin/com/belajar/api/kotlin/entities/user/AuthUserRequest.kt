package com.belajar.api.kotlin.entities.user

import com.belajar.api.kotlin.annotation.user.EmailPasswordMatch
import com.belajar.api.kotlin.annotation.user.ValidEmail
import jakarta.validation.constraints.NotBlank

@EmailPasswordMatch(passwordPath = "password")
data class AuthUserRequest (

    @field:NotBlank
    @field:ValidEmail
    val email: String,

    @field:NotBlank
    val password: String

)