package com.belajar.api.kotlin.controller

import com.belajar.api.kotlin.constant.StatusMessage
import com.belajar.api.kotlin.entities.WebErrorResponse
import com.belajar.api.kotlin.entities.WebResponse
import com.belajar.api.kotlin.exception.*
import jakarta.validation.ConstraintViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.AccessDeniedException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ErrorController {

    @ExceptionHandler(value = [ConstraintViolationException::class])
    fun handleConstraintViolationException( exception: ConstraintViolationException): ResponseEntity<WebErrorResponse<Any>> =
        createErrorResponse( HttpStatus.UNPROCESSABLE_ENTITY, exception.constraintViolations.map { violation ->
            mapOf("path" to violation.propertyPath.toString(), "message" to violation.message)
        })

    @ExceptionHandler(value = [NotFoundException::class])
    fun handleNotFoundException(exception: NotFoundException): ResponseEntity<WebErrorResponse<Any>> =
        createErrorResponse(HttpStatus.NOT_FOUND, exception.message ?: "Not Found")

    @ExceptionHandler(value = [UnauthorizedException::class])
    fun handleUnauthorizedException( exception: UnauthorizedException ): ResponseEntity<WebErrorResponse<String>> =
        createErrorResponse(HttpStatus.UNAUTHORIZED, exception.message ?: "")

    @ExceptionHandler(AccessDeniedException::class)
    fun handleAccessDeniedException(ex: AccessDeniedException?): ResponseEntity<WebErrorResponse<String>> =
        createErrorResponse(HttpStatus.FORBIDDEN, ex?.message ?: "")

    @ExceptionHandler(value = [ForbiddenException::class])
    fun handleForbiddenException( exception: ForbiddenException ): ResponseEntity<WebErrorResponse<String>> =
        createErrorResponse(HttpStatus.FORBIDDEN, exception.message ?: "")

    @ExceptionHandler(value = [BadRequestException::class])
    fun handleForbiddenException( exception: BadRequestException ): ResponseEntity<WebErrorResponse<String>> =
        createErrorResponse(HttpStatus.BAD_REQUEST, exception.message ?: "")

    @ExceptionHandler(value = [ValidationCustomException::class])
    fun handleValidationCustomException( exception: ValidationCustomException ): ResponseEntity<WebErrorResponse<Any>> =
        createErrorResponse( HttpStatus.UNPROCESSABLE_ENTITY, listOf(
            mapOf("path" to exception.path, "message" to exception.message)
        ))

    private fun <T> createErrorResponse( status: HttpStatus, data: T ): ResponseEntity<WebErrorResponse<T>> =
        ResponseEntity.status(status).body(
            WebErrorResponse(
                code = status.value(),
                status = status.name,
                message = data,
            )
        )

}