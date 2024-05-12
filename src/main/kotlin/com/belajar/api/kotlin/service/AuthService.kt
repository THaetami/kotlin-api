package com.belajar.api.kotlin.service

import com.belajar.api.kotlin.entities.user.LoginRequest
import com.belajar.api.kotlin.entities.user.LoginResponse
import com.belajar.api.kotlin.entities.user.RegisterRequest
import com.belajar.api.kotlin.entities.user.RegisterResponse

interface AuthService {
    fun registerUser(request: RegisterRequest): String
    fun confirm(token: String): String
    fun login(request: LoginRequest): LoginResponse
    fun registerAdmin(request: RegisterRequest): RegisterResponse

}