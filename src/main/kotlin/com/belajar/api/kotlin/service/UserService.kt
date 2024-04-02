package com.belajar.api.kotlin.service

import com.belajar.api.kotlin.entities.AuthUserRequest
import com.belajar.api.kotlin.entities.CreateUserRequest
import com.belajar.api.kotlin.entities.UserResponse
import com.belajar.api.kotlin.entities.WebResponse
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.server.reactive.ServerHttpResponse

interface UserService {

    fun create(createUserRequest: CreateUserRequest): UserResponse

    fun auth(authUserRequest: AuthUserRequest, response: HttpServletResponse)

    fun get(jwt: String?): UserResponse

}