package com.belajar.api.kotlin.entities.user

import com.belajar.api.kotlin.annotation.user.PassIfNotBlank
import com.belajar.api.kotlin.annotation.user.UniqueUsername
import com.belajar.api.kotlin.annotation.user.UsernameIfNotBlank
import com.belajar.api.kotlin.annotation.user.ValidEmail
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

data class UpdateUserCurrentRequest (

    @field:NotBlank
    @field:Pattern(regexp = "^[a-zA-Z ]+\$", message = "must contain only alphabet characters and spaces")
    @field:Size(min = 4, max = 11)
    val name: String?,

    @field:NotBlank
    @field:ValidEmail
    val email: String?,

    @field:UsernameIfNotBlank
    val username: String?,

    @field:NotBlank
    @field:Size(min = 4, max = 8)
    @field:Pattern(regexp = "^[a-zA-Z0-9]+\$", message = "must contain only alphanumeric characters")
    val passwordConfirmation: String?

)
