package com.belajar.api.kotlin.controller

import com.belajar.api.kotlin.entities.WebResponse
import com.belajar.api.kotlin.exception.NotFoundException
import com.belajar.api.kotlin.exception.UnauthorizedException
import com.belajar.api.kotlin.exception.ValidationCustomException
import jakarta.validation.ConstraintViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ErrorController {

    @ExceptionHandler(value = [ConstraintViolationException::class])
    fun handleConstraintViolationException( exception: ConstraintViolationException): ResponseEntity<WebResponse<Any>> =
        createErrorResponse( HttpStatus.UNPROCESSABLE_ENTITY, exception.constraintViolations.map { violation ->
            mapOf("path" to violation.propertyPath.toString(), "message" to violation.message)
        })

    @ExceptionHandler(value = [NotFoundException::class])
    fun handleNotFoundException(exception: NotFoundException): ResponseEntity<WebResponse<Any>> =
        createErrorResponse(HttpStatus.NOT_FOUND, exception.message ?: "Not Found")

    @ExceptionHandler(value = [UnauthorizedException::class])
    fun handleUnauthorizedException( exception: UnauthorizedException ): ResponseEntity<WebResponse<String>> =
        createErrorResponse(HttpStatus.UNAUTHORIZED, exception.message ?: "")

    @ExceptionHandler(value = [ValidationCustomException::class])
    fun handleValidationCustomException( exception: ValidationCustomException ): ResponseEntity<WebResponse<Any>> =
        createErrorResponse( HttpStatus.UNPROCESSABLE_ENTITY, listOf(
            mapOf("path" to exception.path, "message" to exception.message)
        ))

    private fun <T> createErrorResponse( status: HttpStatus, data: T ): ResponseEntity<WebResponse<T>> =
        ResponseEntity.status(status).body(
            WebResponse(
                code = status.value(),
                status = status.name,
                data = data
            )
        )

}