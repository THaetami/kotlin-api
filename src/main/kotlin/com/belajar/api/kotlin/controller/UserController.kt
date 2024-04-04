package com.belajar.api.kotlin.controller

import com.belajar.api.kotlin.entities.*
import com.belajar.api.kotlin.service.UserService
import jakarta.servlet.http.HttpServletResponse
import org.springframework.web.bind.annotation.CookieValue
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
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
    fun authenticate(@RequestBody body: AuthUserRequest, response: HttpServletResponse): WebResponse<String> {
        userServices.authenticate(body, response)

        return WebResponse(
            code = 200,
            status = "OK",
            data = "Login success!!"
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

    @PutMapping("user")
    fun updateUser(@CookieValue("jwt") jwt: String?, @RequestBody body: UpdateUserRequest): WebResponse<UserResponse> {
        val userResponse = userServices.update(jwt, body)

        return WebResponse(
            code = 200,
            status = "OK",
            data = userResponse
        )
    }

    @DeleteMapping("auth")
    fun unauthenticate(@CookieValue("jwt") jwt: String?, response: HttpServletResponse): WebResponse<String> {
        userServices.unauthenticate(jwt, response)

        return WebResponse(
            code = 200,
            status = "OK",
            data = "Logout success!!"
        )
    }

}