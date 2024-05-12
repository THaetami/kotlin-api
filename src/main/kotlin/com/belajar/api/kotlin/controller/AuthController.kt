package com.belajar.api.kotlin.controller

import com.belajar.api.kotlin.constant.ApiUrl
import com.belajar.api.kotlin.constant.StatusMessage
import com.belajar.api.kotlin.entities.WebResponse
import com.belajar.api.kotlin.entities.user.LoginRequest
import com.belajar.api.kotlin.entities.user.LoginResponse
import com.belajar.api.kotlin.entities.user.RegisterRequest
import com.belajar.api.kotlin.entities.user.RegisterResponse
import com.belajar.api.kotlin.service.AuthService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping(ApiUrl.API_URL + ApiUrl.AUTH_URL)
@Tag(name = "Auth", description = "Auth API")
class AuthController(
    private val authService: AuthService
) {

    @Operation(summary = "Guest register User")
    @SecurityRequirement(name = "Authorization")
    @PostMapping(path = ["/reg/user"], consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun regUser (@RequestBody request: RegisterRequest): ResponseEntity<WebResponse<String>> {
        val message = authService.registerUser(request)
        val response = WebResponse(
            code = HttpStatus.CREATED.value(),
            status = "User registered",
            data = message
        )
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }

    @Operation(summary = "User email confirmation")
    @SecurityRequirement(name = "Authorization")
    @PostMapping(path = ["/confirm/{token}"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun emailConfirmation (@PathVariable("token") token: String): ResponseEntity<WebResponse<String>> {
        val message = authService.confirm(token)
        val response = WebResponse(
            code = HttpStatus.OK.value(),
            status = "Success",
            data = message
        )
        return ResponseEntity.status(HttpStatus.OK).body(response)
    }

    @Operation(summary = "Super admin register Admin")
    @SecurityRequirement(name = "Authorization")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @PostMapping(path = ["/reg/admin"], consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun regAdmin (@RequestBody request: RegisterRequest): ResponseEntity<WebResponse<RegisterResponse>> {
        val registerResponse = authService.registerAdmin(request)
        val response = WebResponse(
            code = HttpStatus.CREATED.value(),
            status = StatusMessage.SUCCESS_CREATE,
            data = registerResponse
        )
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }

    @Operation(summary = "Login")
    @SecurityRequirement(name = "Authorization")
    @PostMapping(path = ["/login"], consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun login (@RequestBody request: LoginRequest): ResponseEntity<WebResponse<LoginResponse>> {
        val loginResponse = authService.login(request)
        val response = WebResponse(
            code = HttpStatus.OK.value(),
            status = StatusMessage.SUCCESS_LOGIN,
            data = loginResponse
        )
        return ResponseEntity.status(HttpStatus.OK).body(response)
    }

}