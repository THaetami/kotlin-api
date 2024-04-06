package com.belajar.api.kotlin.utils

import com.belajar.api.kotlin.exception.UnauthorizedException
import io.jsonwebtoken.Jwts
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletResponse
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertThrows
import org.mockito.Mockito.*

class AuthUtilTest {

    private lateinit var authUtil: AuthUtil

    @BeforeEach
    fun setUp() {
        authUtil = AuthUtil()
    }

    @Test
    fun `Test generateJwt`() {
        val userId = 123
        val jwt = authUtil.generateJwt(userId)
        val parsedJwt = Jwts.parser()
            .verifyWith(authUtil.getKey())
            .build()
            .parseSignedClaims(jwt)


        assertEquals(userId.toString(), parsedJwt.payload.subject)
    }

    @Test
    fun `Test getUserIdFromJwt with valid token`() {
        val userId = 123
        val jwt = authUtil.generateJwt(userId)

        assertEquals(userId, authUtil.getUserIdFromJwt(jwt))
    }

    @Test
    fun `Test getUserIdFromJwt with null token`() {
        val jwt: String? = null
        val expectedException = assertThrows(UnauthorizedException::class.java) {
            authUtil.getUserIdFromJwt(jwt)
        }

        assertEquals("JWT token is null", expectedException.message)
    }

    @Test
    fun `Test getUserIdFromJwt with invalid token`() {
        val jwt: String = "invalid token"
        val expectedException = assertThrows(UnauthorizedException::class.java) {
            authUtil.getUserIdFromJwt(jwt)
        }

        assertEquals("Invalid JWT token", expectedException.message)
    }

    @Test
    fun `Test clearJwtCookie`() {
        val response = mock(HttpServletResponse::class.java)
        authUtil.clearJwtCookie(response)

        verify(response, times(1)).addCookie(any(Cookie::class.java))
    }

}