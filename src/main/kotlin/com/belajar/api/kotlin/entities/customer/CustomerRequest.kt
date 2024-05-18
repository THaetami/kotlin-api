package com.belajar.api.kotlin.entities.customer

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern

data class CustomerRequest(
    @field:NotBlank
    @field:Pattern(regexp = "^[a-zA-Z ]+\$", message = "must contain only alphabet characters and spaces")
    val name: String,

    @field:Pattern(regexp = "^[0-9 ]+\$", message = "must contain only numeric characters and spaces")
    val phoneNumber: String,
)

