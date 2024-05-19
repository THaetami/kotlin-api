package com.belajar.api.kotlin.utils

import com.belajar.api.kotlin.entities.WebResponse
import com.belajar.api.kotlin.exception.UnauthorizedException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component

@Component
class Utilities {

     internal fun <T> handleRequest(requestHandler: () -> T, status: HttpStatus, message: String): ResponseEntity<WebResponse<T>> {
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