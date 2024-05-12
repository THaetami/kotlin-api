package com.belajar.api.kotlin.entities.user

import com.belajar.api.kotlin.annotation.user.ValidEmail
import jakarta.validation.constraints.NotBlank

data class ForgotPasswordRequest (
    @field:NotBlank
    @field:ValidEmail
    val email: String?,
)