package com.belajar.api.kotlin.entities.user

import com.belajar.api.kotlin.constant.UserRoleEnum
import com.belajar.api.kotlin.model.UserRole
import java.time.LocalDateTime
import java.util.*

data class UserResponse <T>(

    val id: Int,

    val name: String,

    val email: String,

    val username: String,

    val roles: List<UserRoleEnum?>,

    val createdAt: LocalDateTime,

    val updatedAt: LocalDateTime,

    val token: T,

)
