package com.belajar.api.kotlin.service.impl

import com.belajar.api.kotlin.entities.user.AuthUserRequest
import com.belajar.api.kotlin.exception.NotFoundException
import com.belajar.api.kotlin.exception.UnauthorizedException
import com.belajar.api.kotlin.model.User
import com.belajar.api.kotlin.repository.UserRepository
import com.belajar.api.kotlin.utils.AuthUtil
import com.belajar.api.kotlin.validation.ValidationUtil
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletResponse
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.ArgumentCaptor
import org.mockito.Mockito.*
import java.util.*

class AuthServiceImplTest {

    private val userRepository: UserRepository = mock(UserRepository::class.java)
    private val validationUtil: ValidationUtil = mock(ValidationUtil::class.java)
    private val response: HttpServletResponse = mock(HttpServletResponse::class.java)
    private val authUtil: AuthUtil = mock(AuthUtil::class.java)
    private val authService = AuthServiceImpl(userRepository, validationUtil, authUtil)

    val email = "test@example.com"
    private val password = "validPassword"
    private val authUserRequest = AuthUserRequest(email, password)

    @Test
    fun `Test authenticate with valid user`() {
        // Arrange
        val user = User()
        user.id = 1
        `when`(userRepository.getUserByEmail(email)).thenReturn(user)
        `when`(authUtil.generateJwt(user.id!!)).thenReturn("testJwt")

        // Act
        authService.authenticate(authUserRequest, response)

        // Assert
        verify(validationUtil).validate(authUserRequest)
        verify(userRepository).getUserByEmail(email)
        verify(authUtil).generateJwt(user.id!!)
        verify(response).addCookie(any(Cookie::class.java))
    }

    @Test
    fun `Test authenticate with invalid user`() {
        // Arrange
        `when`(userRepository.getUserByEmail(email)).thenReturn(null)

        // Act and Assert
        assertThrows<NotFoundException> {
            authService.authenticate(authUserRequest, response)
        }
        verify(validationUtil).validate(authUserRequest)
        verify(userRepository).getUserByEmail(email)
        verifyNoInteractions(response)
    }

    @Test
    fun `Test unauthenticate with valid jwt`() {
        `when`(authUtil.getUserIdFromJwt(anyString())).thenReturn(1)

        authService.unauthenticate("valid_jwt", response)
        verify(authUtil).getUserIdFromJwt("valid_jwt")
        verify(authUtil).clearJwtCookie(response)
    }

    @Test
    fun `Test unauthenticate with null jwt`() {
        `when`(authUtil.getUserIdFromJwt(null)).thenReturn(0)

        authService.unauthenticate(null, response)
        verify(authUtil).getUserIdFromJwt(null)
        verifyNoInteractions(response)
    }

    @Test
    fun `Test unauthenticate with invalid jwt`() {
        `when`(authUtil.getUserIdFromJwt("invalid_jwt")).thenReturn(0)

        authService.unauthenticate("invalid_jwt", response)
        verify(authUtil).getUserIdFromJwt("invalid_jwt")
        verifyNoInteractions(response)
    }

}