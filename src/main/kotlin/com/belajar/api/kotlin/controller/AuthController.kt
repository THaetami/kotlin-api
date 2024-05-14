package com.belajar.api.kotlin.controller

import com.belajar.api.kotlin.constant.ApiUrl
import com.belajar.api.kotlin.constant.StatusMessage
import com.belajar.api.kotlin.entities.WebResponse
import com.belajar.api.kotlin.entities.user.*
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
    fun regUser(@RequestBody request: RegisterRequest): ResponseEntity<WebResponse<String>> =
        handleRequest ({ authService.registerUser( request) }, HttpStatus.CREATED, "Account registered")

    @Operation(summary = "User email confirmation and activated account")
    @SecurityRequirement(name = "Authorization")
    @PostMapping(path = ["/confirm/{token}"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun emailConfirmation(@PathVariable("token") token: String): ResponseEntity<WebResponse<String>> =
        handleRequest ({ authService.confirm( token) }, HttpStatus.OK, StatusMessage.SUCCESS)

    @Operation(summary = "User forgot password")
    @SecurityRequirement(name = "Authorization")
    @PostMapping(path = ["/forgot-password"], consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun forgotPassword(@RequestBody request: ForgotPasswordRequest): ResponseEntity<WebResponse<String>> =
        handleRequest ({ authService.forgotPassword( request) }, HttpStatus.OK, StatusMessage.SUCCESS)

    @Operation(summary = "User reset password")
    @SecurityRequirement(name = "Authorization")
    @PostMapping(path = ["/reset-password/{token}"], consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun resetPassword(@PathVariable("token") token: String, @RequestBody request: ResetPasswordRequest): ResponseEntity<WebResponse<String>> =
        handleRequest ({ authService.resetPassword( token, request) }, HttpStatus.OK, StatusMessage.SUCCESS)

    @Operation(summary = "Super admin register Admin")
    @SecurityRequirement(name = "Authorization")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @PostMapping(path = ["/reg/admin"], consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun regAdmin(@RequestBody request: RegisterRequest): ResponseEntity<WebResponse<RegisterResponse>> =
        handleRequest ({ authService.registerAdmin( request) }, HttpStatus.CREATED, StatusMessage.SUCCESS_CREATE_USER)

    @Operation(summary = "Login")
    @SecurityRequirement(name = "Authorization")
    @PostMapping(path = ["/login"], consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun login(@RequestBody request: LoginRequest): ResponseEntity<WebResponse<LoginResponse>> =
        handleRequest ({ authService.login(request) }, HttpStatus.OK, StatusMessage.SUCCESS_LOGIN)

    private fun <T> handleRequest(requestHandler: () -> T, status: HttpStatus, message: String): ResponseEntity<WebResponse<T>> {
        val data = requestHandler()
        val response = WebResponse(
            code = status.value(),
            status = message,
            data = data,
            paginationResponse = null
        )
        return ResponseEntity.status(status).body(response)
    }
}