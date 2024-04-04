package com.belajar.api.kotlin.service

import com.belajar.api.kotlin.entities.user.AuthUserRequest
import jakarta.servlet.http.HttpServletResponse

interface AuthService {
    fun authenticate(authUserRequest: AuthUserRequest, response: HttpServletResponse)
    fun unauthenticate(jwt: String?, response: HttpServletResponse)
}