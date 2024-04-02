package com.belajar.api.kotlin.controller

import com.belajar.api.kotlin.entities.AuthUserRequest
import com.belajar.api.kotlin.entities.CreateUserRequest
import com.belajar.api.kotlin.entities.UserResponse
import com.belajar.api.kotlin.entities.WebResponse
import com.belajar.api.kotlin.service.UserService
import jakarta.servlet.http.HttpServletResponse
import org.springframework.web.bind.annotation.CookieValue
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ServerWebExchange

@RestController
@RequestMapping("api")
class UserController(val userServices: UserService) {

    @PostMapping("user")
    fun createUser(@RequestBody body: CreateUserRequest): WebResponse<UserResponse> {
        val userResponse = userServices.create(body)

        return WebResponse(
            code = 200,
            status = "OK",
            data = userResponse
        )
    }

    @PostMapping("auth")
    fun authUser(@RequestBody body: AuthUserRequest, response: HttpServletResponse): WebResponse<String> {
        userServices.auth(body, response)

        return WebResponse(
            code = 200,
            status = "OK",
            data = "Login success"
        )
    }

    @GetMapping("user")
    fun getUser(@CookieValue("jwt") jwt: String?): WebResponse<UserResponse> {

        val userResponse = userServices.get(jwt)

        return WebResponse(
            code = 200,
            status = "OK",
            data = userResponse
        )

    }

}