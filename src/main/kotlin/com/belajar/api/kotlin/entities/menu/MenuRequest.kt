package com.belajar.api.kotlin.entities.menu

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.springframework.web.multipart.MultipartFile

data class MenuRequest(
    @NotBlank
    @NotNull
    var name: String,

    @NotNull
    var price: Long,
)
