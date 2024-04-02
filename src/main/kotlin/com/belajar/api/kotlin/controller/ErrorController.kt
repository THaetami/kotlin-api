package com.belajar.api.kotlin.controller

import com.belajar.api.kotlin.entities.WebResponse
import com.belajar.api.kotlin.error.NotFoundException
import com.belajar.api.kotlin.error.UnauthorizedException
import jakarta.validation.ConstraintViolationException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ErrorController {

    @ExceptionHandler(value = [
        ConstraintViolationException::class
    ])
    fun validationHandler(constraintViolationException: ConstraintViolationException): WebResponse<Any> {
        val errorDetails = constraintViolationException.constraintViolations.map { violation ->
            mapOf(
                "path" to violation.propertyPath.toString(),
                "message" to violation.message
            )
        }
        return WebResponse(
            code = 422,
            status = "UNPROCESSABLE ENTITY",
            data = errorDetails
        )
    }

    @ExceptionHandler(value = [NotFoundException::class])
    fun notFound(notFoundException: NotFoundException): WebResponse<Any> {
        return WebResponse(
            code = 404,
            status = "NOT FOUND",
            data = notFoundException.message ?: "Not Found"
        )
    }

    @ExceptionHandler(value = [UnauthorizedException::class])
    fun unauthorized(unauthorizedException: UnauthorizedException): WebResponse<String> {
        return WebResponse(
            code = 401,
            status = "UNAUTHORIZED",
            data = unauthorizedException.message ?: ""
        )
    }

}