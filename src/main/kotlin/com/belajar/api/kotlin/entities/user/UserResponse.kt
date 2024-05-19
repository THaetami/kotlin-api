package com.belajar.api.kotlin.entities.user

import com.belajar.api.kotlin.constant.UserRoleEnum
data class UserResponse <T>(

    val id: Int,

    val email: String,

    val username: String,

    val roles: List<UserRoleEnum?>,

    val createdAt: String,

    val updatedAt: String,

    val token: T,

)
