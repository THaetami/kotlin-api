package com.belajar.api.kotlin.entities.customer

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern

data class UpdateCustomerRequest(
    @NotBlank
    var name: String,

    @field:Pattern(regexp = "^[0-9 ]+\$", message = "must contain only numeric characters and spaces")
    var phone: String
)