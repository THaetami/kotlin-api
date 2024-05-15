package com.belajar.api.kotlin.entities.table

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern

data class TableRequest(
    @NotBlank
    @field:Pattern(regexp = "^[a-zA-Z ]+\$", message = "must contain only alphabet characters and spaces")
    var name: String,
)
