package com.belajar.api.kotlin.service.impl

import com.belajar.api.kotlin.entities.user.AuthUserRequest
import com.belajar.api.kotlin.exception.NotFoundException
import com.belajar.api.kotlin.model.User
import com.belajar.api.kotlin.repository.UserRepository
import com.belajar.api.kotlin.utils.AuthUtil
import com.belajar.api.kotlin.validation.ValidationUtil
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletResponse
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.*

class AuthServiceImplTest {

    private lateinit var userRepository: UserRepository
    private lateinit var validationUtil: ValidationUtil
    private lateinit var response: HttpServletResponse
    private lateinit var authUtil: AuthUtil
    private lateinit var authService: AuthServiceImpl

    val email = "test@example.com"
    private val password = "validPassword"
    private val authUserRequest = AuthUserRequest(email, password)

    @BeforeEach
    fun setUp() {
        userRepository = mock(UserRepository::class.java)
        validationUtil = mock(ValidationUtil::class.java)
        response = mock(HttpServletResponse::class.java)
        authUtil = mock(AuthUtil::class.java)
        authService = AuthServiceImpl(userRepository, validationUtil, authUtil)
    }

    @Test
    fun `Test authenticate with valid user`() {
        // Arrange
        val user = User().apply { id = 1 }

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