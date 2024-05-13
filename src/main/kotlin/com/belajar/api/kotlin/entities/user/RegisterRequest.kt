package com.belajar.api.kotlin.entities.user

import com.belajar.api.kotlin.annotation.user.UniqueEmail
import com.belajar.api.kotlin.annotation.user.UniqueUsername
import com.belajar.api.kotlin.annotation.user.ValidEmail
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

data class RegisterRequest (

    @field:NotBlank
    @field:Pattern(regexp = "^[a-zA-Z ]+\$", message = "must contain only alphabet characters and spaces")
    @field:Size(min = 4, max = 23)
    val name: String?,

    @NotBlank
    val phone: String?,

    @field:NotBlank
    @field:UniqueEmail
    @field:ValidEmail
    val email: String?,

    @field:NotBlank
    @field:UniqueUsername
    @field:Pattern(regexp = "^[a-zA-Z0-9]+\$", message = "must contain only alphanumeric characters")
    @field:Size(min = 3, max = 8)
    val username: String?,

    @field:NotBlank
    @field:Pattern(regexp = "^[a-zA-Z0-9]+\$", message = "must contain only alphanumeric characters")
    @field:Size(min = 4, max = 8)
    val password: String?

)