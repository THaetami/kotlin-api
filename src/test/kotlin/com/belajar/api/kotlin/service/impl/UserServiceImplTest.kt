package com.belajar.api.kotlin.service.impl

import com.belajar.api.kotlin.entities.user.CreateUserRequest
import com.belajar.api.kotlin.entities.user.UpdateUserRequest
import com.belajar.api.kotlin.entities.user.UserResponse
import com.belajar.api.kotlin.exception.UnauthorizedException
import com.belajar.api.kotlin.model.User
import com.belajar.api.kotlin.repository.UserRepository
import com.belajar.api.kotlin.utils.AuthUtil
import com.belajar.api.kotlin.validation.ValidationUtil
import jakarta.validation.ConstraintViolationException
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.springframework.web.server.ResponseStatusException
import java.util.*


class UserServiceImplTest {

    @Mock
    private lateinit var userRepository: UserRepository

    @Mock
    private lateinit var validationUtil: ValidationUtil

    @Mock
    private lateinit var authUtil: AuthUtil

    @InjectMocks
    private lateinit var userService: UserServiceImpl

    @Mock
    private lateinit var userResponse: UserResponse


    private val name = "Jono"
    private val email = "test@example.com"
    private val password = "123pas"
    private val createUserRequest = CreateUserRequest(name, email, password)
    private val updateUserRequest = UpdateUserRequest(name, email, password)

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun `Test create user success`() {
        val user = User().apply {
            name = createUserRequest.name!!
            email = createUserRequest.email!!
            password = createUserRequest.password!!
            createdAt = Date()
        }

        `when`(userRepository.save(any(User::class.java))).thenReturn(user)

        val response = userService.create(createUserRequest)

        verify(validationUtil).validate(createUserRequest)
        verify(userRepository).save(any(User::class.java))

        assertEquals(response.id, user.id)
        assertEquals(response.name, user.name)
        assertNotNull(response.createdAt)
        assertEquals(response.updatedAt, user.updatedAt)
    }

    @Test
    fun `Test create user failed`() {
        `when`(userRepository.save(any(User::class.java))).thenThrow(RuntimeException("Failed to crate user"))

        assertThrows<ResponseStatusException> {
            userService.create(createUserRequest)
        }
    }

    @Test
    fun `Test get user success`() {
        val userId = 1
        val tokenJwt = "token_jwt"
        val user = User().apply {
            id = userId
            name = "tami"
            createdAt = Date()
        }
        val expectedResponse = UserResponse(user.id, user.name, user.createdAt, user.updatedAt)

        `when`(authUtil.getUserIdFromJwt(tokenJwt)).thenReturn(userId)
        `when`(userRepository.getReferenceById(userId)).thenReturn(user)

        val response = userService.get(tokenJwt)

        verify(authUtil).getUserIdFromJwt(tokenJwt)
        verify(userRepository).getReferenceById(userId)

        assertEquals(expectedResponse, response)
    }

    @Test
    fun `Test get user with null jwt`() {
        `when`(authUtil.getUserIdFromJwt(null)).thenReturn(0)

        assertThrows<ResponseStatusException> {
            userService.get(null)
        }

        verify(authUtil).getUserIdFromJwt(null)
    }

    @Test
    fun `Test get user with invalid jwt`() {
        `when`(authUtil.getUserIdFromJwt("invalid_jwt")).thenReturn(0)

        assertThrows<ResponseStatusException> {
            userService.get("invalid_jwt")
        }

        verify(authUtil).getUserIdFromJwt("invalid_jwt")
    }

    @Test
    fun `Test createUserFromRequest`() {
        val createdUser = userService.createUserFromRequest(createUserRequest)

        assertEquals(createUserRequest.name, createdUser.name)
        assertEquals(createUserRequest.email, createdUser.email)
        assertNotEquals(createUserRequest.password, createdUser.password)
        assertNotNull(createdUser.password)
        assertNotNull(createdUser.createdAt)
    }

}