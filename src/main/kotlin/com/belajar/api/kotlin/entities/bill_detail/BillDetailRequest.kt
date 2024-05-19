package com.belajar.api.kotlin.entities.bill_detail

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class BillDetailRequest(
    @field:NotBlank
    var menuId: String,

    @field:NotNull
    var qty: Int,

    @field:NotNull
    var price: Long,
)
