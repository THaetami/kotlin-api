package com.belajar.api.kotlin.entities.customer

import jakarta.validation.constraints.NotBlank

data class CustomerRequest(
    @NotBlank
    val name: String,

    val phoneNumber: String,
)

