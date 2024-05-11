package com.belajar.api.kotlin.service

import com.belajar.api.kotlin.entities.user.RegisterRequest
import com.belajar.api.kotlin.entities.user.UpdateUserCurrentRequest
import com.belajar.api.kotlin.entities.user.UserResponse
import com.belajar.api.kotlin.model.UserAccount

interface UserService {
    fun getUserById(id: Int): UserAccount
    fun getUserByUsername(username: String): UserResponse<String>
    fun getUserCurrent(): UserResponse<String>
    fun updateUserCurrent(updateUserCurrentRequest: UpdateUserCurrentRequest): UserResponse<String>
    fun disabledOrEnabledUserById(id: Int): String
    fun getUserAll(): List<UserResponse<String>>
    fun getAdminAll(): List<UserResponse<String>>
    fun updateAdminById(id: Int, request: RegisterRequest): UserResponse<String>
}