package com.belajar.api.kotlin.entities

import com.belajar.api.kotlin.annotation.EmailIfNotBlank
import com.belajar.api.kotlin.annotation.PassIfNotBlank
import com.belajar.api.kotlin.annotation.UniqueEmail
import com.belajar.api.kotlin.annotation.ValidEmail
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