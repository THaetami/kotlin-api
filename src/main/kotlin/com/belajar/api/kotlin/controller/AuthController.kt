package com.belajar.api.kotlin.controller

import com.belajar.api.kotlin.entities.*
import com.belajar.api.kotlin.entities.user.AuthUserRequest
import com.belajar.api.kotlin.service.AuthService
import jakarta.servlet.http.HttpServletResponse
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("api")
class AuthController(val authService: AuthService) {

    @PostMapping("auth")
    fun authenticate(@RequestBody body: AuthUserRequest, response: HttpServletResponse): WebResponse<String> {
        authService.authenticate(body, response)

        return WebResponse(
            code = 200,
            status = "OK",
            data = "Login success!!"
        )
    }

    @DeleteMapping("auth")
    fun unauthenticate(@CookieValue("jwt") jwt: String?, response: HttpServletResponse): WebResponse<String> {
        authService.unauthenticate(jwt, response)

        return WebResponse(
            code = 200,
            status = "OK",
            data = "Logout success!!"
        )
    }

}