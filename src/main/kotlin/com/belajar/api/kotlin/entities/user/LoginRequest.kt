package com.belajar.api.kotlin.entities.user

import com.belajar.api.kotlin.annotation.user.UsernamePasswordMatch
import jakarta.validation.constraints.NotBlank

@UsernamePasswordMatch(passwordPath = "password")
data class LoginRequest(

    @field:NotBlank
    val username: String?,

    @field:NotBlank
    val password: String?

)
