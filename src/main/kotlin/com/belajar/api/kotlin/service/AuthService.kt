package com.belajar.api.kotlin.service

import com.belajar.api.kotlin.entities.user.*

interface AuthService {
    fun registerUser(request: RegisterRequest): String
    fun confirm(token: String): String
    fun login(request: LoginRequest): LoginResponse
    fun registerAdmin(request: RegisterRequest): RegisterResponse
    fun resetPassword(token: String, request: ResetPasswordRequest): String
    fun forgotPassword(request: ForgotPasswordRequest): String
}