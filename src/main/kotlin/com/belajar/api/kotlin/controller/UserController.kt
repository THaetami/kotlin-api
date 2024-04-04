package com.belajar.api.kotlin.controller

import com.belajar.api.kotlin.entities.*
import com.belajar.api.kotlin.entities.user.CreateUserRequest
import com.belajar.api.kotlin.entities.user.UpdateUserRequest
import com.belajar.api.kotlin.entities.user.UserResponse
import com.belajar.api.kotlin.service.UserService
import org.springframework.web.bind.annotation.CookieValue
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

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

}