package com.belajar.api.kotlin.entities.customer

import jakarta.validation.constraints.NotBlank

data class UpdateCustomerRequest(
    @NotBlank
    var id: String,

    @NotBlank
    var name: String,

    var phone: String
)