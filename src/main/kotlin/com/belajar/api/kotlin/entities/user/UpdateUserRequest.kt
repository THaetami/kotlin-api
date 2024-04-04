package com.belajar.api.kotlin.entities.user

import com.belajar.api.kotlin.annotation.user.EmailIfNotBlank
import com.belajar.api.kotlin.annotation.user.PassIfNotBlank
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

data class UpdateUserRequest(

    @field:NotBlank
    @field:Pattern(regexp = "^[a-zA-Z ]+\$", message = "must contain only alphabet characters and spaces")
    @field:Size(min = 4, max = 11)
    val name: String,

    @field:EmailIfNotBlank
    val email: String?,

    @field:PassIfNotBlank
    val password: String?

)