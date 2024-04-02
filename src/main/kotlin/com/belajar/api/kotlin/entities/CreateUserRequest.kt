package com.belajar.api.kotlin.entities

import com.belajar.api.kotlin.annotation.UniqueEmail
import com.belajar.api.kotlin.annotation.ValidEmail
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size


data class CreateUserRequest(

    @field:NotNull
    @field:Size(min = 3, max = 11)
    val name: String?,

    @field:NotNull
    @field:UniqueEmail
    @field:ValidEmail
    val email: String?,

    @field:NotNull
    @field:Size(min = 4, max = 6)
    val password: String?

)