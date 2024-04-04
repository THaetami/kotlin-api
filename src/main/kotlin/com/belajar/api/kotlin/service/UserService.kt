package com.belajar.api.kotlin.service

import com.belajar.api.kotlin.entities.*
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.server.reactive.ServerHttpResponse

interface UserService {

    fun create(createUserRequest: CreateUserRequest): UserResponse

    fun authenticate(authUserRequest: AuthUserRequest, response: HttpServletResponse)

    fun get(jwt: String?): UserResponse

    fun update(jwt: String?, updateUserRequest: UpdateUserRequest): UserResponse

    fun unauthenticate(jwt: String?, response: HttpServletResponse)

}