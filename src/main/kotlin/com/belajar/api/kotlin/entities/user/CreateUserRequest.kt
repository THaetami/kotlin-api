package com.belajar.api.kotlin.entities.user

import com.belajar.api.kotlin.annotation.user.UniqueEmail
import com.belajar.api.kotlin.annotation.user.ValidEmail
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size


data class CreateUserRequest(

    @field:NotBlank
    @field:Pattern(regexp = "^[a-zA-Z ]+\$", message = "must contain only alphabet characters and spaces")
    @field:Size(min = 4, max = 11)
    val name: String?,

    @field:NotBlank
    @field:UniqueEmail
    @field:ValidEmail
    val email: String?,

    @field:NotBlank
    @field:Pattern(regexp = "^[a-zA-Z0-9]+\$", message = "must contain only alphanumeric characters")
    @field:Size(min = 4, max = 6)
    val password: String?

)