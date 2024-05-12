package com.belajar.api.kotlin.entities.user

import com.belajar.api.kotlin.annotation.user.ValidEmail
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

data class ResetPasswordRequest (

    @field:NotBlank
    @field:Pattern(regexp = "^[a-zA-Z0-9]+\$", message = "must contain only alphanumeric characters")
    @field:Size(min = 4, max = 8)
    val password: String?

)
