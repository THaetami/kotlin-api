package com.belajar.api.kotlin.controller

import com.belajar.api.kotlin.constant.ApiUrl
import com.belajar.api.kotlin.constant.StatusMessage
import com.belajar.api.kotlin.entities.WebResponse
import com.belajar.api.kotlin.entities.user.RegisterRequest
import com.belajar.api.kotlin.entities.user.UpdateUserCurrentRequest
import com.belajar.api.kotlin.entities.user.UserResponse
import com.belajar.api.kotlin.service.UserService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping(ApiUrl.API_URL + ApiUrl.USER_URL)
@Tag(name = "User", description = "User API")
class UserController(
    private val userService: UserService
) {

    @Operation(summary = "Get User Current")
    @SecurityRequirement(name = "Authorization")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'USER') or authenticated")
    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getUserCurrent (): ResponseEntity<WebResponse<UserResponse<String>>> {
        val user = userService.getUserCurrent()
        val response = WebResponse(
            code = HttpStatus.OK.value(),
            status = StatusMessage.SUCCESS_RETRIEVE,
            data = user,
            paginationResponse = null
        )
        return ResponseEntity.status(HttpStatus.OK).body(response)
    }

    @Operation(summary = "Update User Current")
    @SecurityRequirement(name = "Authorization")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER') and !hasRole('SUPER_ADMIN')")
    @PutMapping(consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun updateUserCurrent (@RequestBody request: UpdateUserCurrentRequest): ResponseEntity<WebResponse<UserResponse<String>>> {
        val user = userService.updateUserCurrent(request)
        val response = WebResponse(
            code = HttpStatus.OK.value(),
            status = StatusMessage.SUCCESS_UPDATE,
            data = user,
            paginationResponse = null
        )
        return ResponseEntity.status(HttpStatus.OK).body(response)
    }

    @Operation(summary = "Super admin and Admin get User by username")
    @SecurityRequirement(name = "Authorization")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    @GetMapping(path = ["/{username}"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getUserById (@PathVariable("username") username: String): ResponseEntity<WebResponse<UserResponse<String>>> {
        val result = userService.getUserByUsername(username)
        val response = WebResponse(
            code = HttpStatus.OK.value(),
            status = StatusMessage.SUCCESS_RETRIEVE,
            data = result,
            paginationResponse = null
        )
        return ResponseEntity.status(HttpStatus.OK).body(response)
    }

    @Operation(summary = "Super admin disable or enable Admin and User by id")
    @SecurityRequirement(name = "Authorization")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    @PutMapping(path = ["/{id}"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun disabledOrEnabledUserById (@PathVariable("id") id: String): ResponseEntity<WebResponse<String>> {
        val result = userService.disabledOrEnabledUserById(id.toInt())
        val response = WebResponse(
            code = HttpStatus.OK.value(),
            status = "Success",
            data = result,
            paginationResponse = null
        )
        return ResponseEntity.status(HttpStatus.OK).body(response)
    }

    @Operation(summary = "Super admin and Admin get all User")
    @SecurityRequirement(name = "Authorization")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    @GetMapping(path = ["/all"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getUserAll (): ResponseEntity<WebResponse<List<UserResponse<String>>>> {
        val users = userService.getUserAll()
        val response = WebResponse(
            code = HttpStatus.OK.value(),
            status = StatusMessage.SUCCESS_RETRIEVE,
            data = users,
            paginationResponse = null
        )
        return ResponseEntity.status(HttpStatus.OK).body(response)
    }

    @Operation(summary = "Super admin get all Admin")
    @SecurityRequirement(name = "Authorization")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    @GetMapping(path = ["/admin"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getAdminAll (): ResponseEntity<WebResponse<List<UserResponse<String>>>> {
        val users = userService.getAdminAll()
        val response = WebResponse(
            code = HttpStatus.OK.value(),
            status = StatusMessage.SUCCESS_RETRIEVE,
            data = users,
            paginationResponse = null
        )
        return ResponseEntity.status(HttpStatus.OK).body(response)
    }

    @Operation(summary = "Super admin update Admin by id")
    @SecurityRequirement(name = "Authorization")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    @PutMapping(path = ["/admin/{id}"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun updateAdminById (@PathVariable("id") id: Int, @RequestBody request: RegisterRequest): ResponseEntity<WebResponse<UserResponse<String>>> {
        val user = userService.updateAdminById(id, request)
        val response = WebResponse(
            code = HttpStatus.OK.value(),
            status = StatusMessage.SUCCESS_UPDATE,
            data = user,
            paginationResponse = null
        )
        return ResponseEntity.status(HttpStatus.OK).body(response)
    }

}