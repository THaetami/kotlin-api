package com.belajar.api.kotlin.entities

import java.util.Date

data class UserResponse (

    val id: Int?,

    val name: String,

    val createdAt: Date?,

    val updatedAt: Date?

)