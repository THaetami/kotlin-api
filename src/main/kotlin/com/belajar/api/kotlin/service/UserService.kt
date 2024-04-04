package com.belajar.api.kotlin.service

import com.belajar.api.kotlin.entities.user.CreateUserRequest
import com.belajar.api.kotlin.entities.user.UpdateUserRequest
import com.belajar.api.kotlin.entities.user.UserResponse

interface UserService {
    fun create(createUserRequest: CreateUserRequest): UserResponse
    fun get(jwt: String?): UserResponse
    fun update(jwt: String?, updateUserRequest: UpdateUserRequest): UserResponse
}