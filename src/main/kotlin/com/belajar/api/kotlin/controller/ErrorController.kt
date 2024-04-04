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
    fun validationHandler(constraintViolationException: ConstraintViolationException): ResponseEntity<WebResponse<Any>> {
        val errorDetails = constraintViolationException.constraintViolations.map { violation ->
            mapOf(
                "path" to violation.propertyPath.toString(),
                "message" to violation.message
            )
        }
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(
            WebResponse(
                code = HttpStatus.UNPROCESSABLE_ENTITY.value(),
                status = HttpStatus.UNPROCESSABLE_ENTITY.name,
                data = errorDetails
            )
        )
    }

    @ExceptionHandler(value = [NotFoundException::class])
    fun notFound(notFoundException: NotFoundException): ResponseEntity<WebResponse<Any>> {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
            WebResponse(
                code = HttpStatus.NOT_FOUND.value(),
                status = HttpStatus.NOT_FOUND.name,
                data = notFoundException.message ?: "Not Found"
            )
        )
    }

    @ExceptionHandler(value = [UnauthorizedException::class])
    fun unauthorized(unauthorizedException: UnauthorizedException): ResponseEntity<WebResponse<String>> {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
            WebResponse(
                code = HttpStatus.UNAUTHORIZED.value(),
                status = HttpStatus.UNAUTHORIZED.name,
                data = unauthorizedException.message ?: ""
            )
        )
    }

    @ExceptionHandler(value = [ValidationCustomException::class])
    fun validationCustom(validationCustomException: ValidationCustomException): ResponseEntity<WebResponse<Any>> {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(
            WebResponse(
                code = HttpStatus.UNPROCESSABLE_ENTITY.value(),
                status = HttpStatus.UNPROCESSABLE_ENTITY.name,
                data = arrayOf(
                    mapOf(
                        "path" to validationCustomException.path,
                        "message" to validationCustomException.message
                    )
                )
            )
        )
    }
}