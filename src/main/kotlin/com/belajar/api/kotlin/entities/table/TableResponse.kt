package com.belajar.api.kotlin.entities.table

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern

data class TableResponse(
    var id: String,
    var name: String,
)
