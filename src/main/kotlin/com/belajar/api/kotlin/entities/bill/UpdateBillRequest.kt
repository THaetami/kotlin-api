package com.belajar.api.kotlin.entities.bill

import jakarta.validation.constraints.NotBlank

data class UpdateBillRequest(
    @field:NotBlank
    var transactionStatus: String,
)
