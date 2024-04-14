package com.belajar.api.kotlin.service.impl

import com.belajar.api.kotlin.entities.user.CreateUserRequest
import com.belajar.api.kotlin.entities.user.UpdateUserRequest
import com.belajar.api.kotlin.entities.user.UserResponse
import com.belajar.api.kotlin.exception.ValidationCustomException
import com.belajar.api.kotlin.model.User
import com.belajar.api.kotlin.repository.UserRepository
import com.belajar.api.kotlin.utils.AuthUtil
import com.belajar.api.kotlin.validation.ValidationUtil
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

    private val tokenJwt = "valid_token"
    private val userId = 1
    private val name = "Jono"
    private val email = "test@example.com"
    private val password = "123pas"
    private val createUserRequest = CreateUserRequest(name, email, password)
    private val updateUserRequest = UpdateUserRequest(name, email, password)

    val user = User().apply {
        id = userId
        name = "Old name"
        email = "old_email@example.com"
        password = "old_password"
        createdAt = Date()
    }

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
        val expectedResponse = UserResponse(user.id, user.name, user.createdAt, user.updatedAt)

        `when`(authUtil.getUserIdFromJwt(tokenJwt)).thenReturn(userId)
        `when`(userRepository.getReferenceById(userId)).thenReturn(user)

        val response = userService.get(tokenJwt)

        verify(authUtil).getUserIdFromJwt(tokenJwt)
        verify(userRepository).getReferenceById(userId)

        assertEquals(expectedResponse, response)
    }

    @Test
    fun `Test get user failed`() {
        `when`(authUtil.getUserIdFromJwt(tokenJwt)).thenReturn(userId)
        `when`(userRepository.getReferenceById(userId)).thenThrow(RuntimeException("Failed to crate user"))

        assertThrows<ResponseStatusException> {
            userService.get(tokenJwt)
        }

        verify(authUtil).getUserIdFromJwt(tokenJwt)
        verify(userRepository).getReferenceById(userId)
    }

    @Test
    fun `Test get user with null jwt`() {
        `when`(authUtil.getUserIdFromJwt(null)).thenThrow(RuntimeException())

        assertThrows<RuntimeException> {
            userService.get(null)
        }

        verify(authUtil).getUserIdFromJwt(null)
    }

    @Test
    fun `Test get user with invalid jwt`() {
        `when`(authUtil.getUserIdFromJwt("invalid_jwt")).thenThrow(RuntimeException())

        assertThrows<RuntimeException> {
            userService.get("invalid_jwt")
        }

        verify(authUtil).getUserIdFromJwt("invalid_jwt")
    }

    @Test
    fun `Test update user success`() {

        `when`(authUtil.getUserIdFromJwt(tokenJwt)).thenReturn(userId)
        `when`(userRepository.getReferenceById(userId)).thenReturn(user)
        `when`(userRepository.existsByEmail(updateUserRequest.email!!)).thenReturn(false)
        `when`(userRepository.save(user)).thenReturn(user)

        val response = userService.update(tokenJwt, updateUserRequest)

        verify(validationUtil).validate(updateUserRequest)
        verify(authUtil).getUserIdFromJwt(tokenJwt)
        verify(userRepository).getReferenceById(userId)
        verify(userRepository).existsByEmail(updateUserRequest.email!!)
        verify(userRepository).save(user)


        assertEquals(user.id, response.id)
        assertEquals(updateUserRequest.name, response.name)
        assertEquals(user.email, updateUserRequest.email)
        assertTrue(user.comparePassword(updateUserRequest.password!!))
        assertEquals(user.createdAt, response.createdAt)
        assertNotNull(user.updatedAt)
        assertNotNull(response.updatedAt)
    }

    @Test
    fun `Test update user with email exists`() {

        `when`(authUtil.getUserIdFromJwt(tokenJwt)).thenReturn(userId)
        `when`(userRepository.getReferenceById(userId)).thenReturn(user)
        `when`(userRepository.existsByEmail(updateUserRequest.email!!)).thenReturn(true)

        assertThrows<ValidationCustomException> {
            userService.update(tokenJwt, updateUserRequest)
        }

        verify(authUtil).getUserIdFromJwt(tokenJwt)
        verify(validationUtil).validate(updateUserRequest)
        verify(userRepository).getReferenceById(userId)
        verify(userRepository).existsByEmail(updateUserRequest.email!!)
    }

    @Test
    fun `Test update user failed`() {

        `when`(authUtil.getUserIdFromJwt(tokenJwt)).thenReturn(userId)
        `when`(userRepository.getReferenceById(userId)).thenReturn(user)
        `when`(userRepository.existsByEmail(updateUserRequest.email!!)).thenReturn(false)
        `when`(userRepository.save(user)).thenThrow(RuntimeException("Failed to updated user"))

        assertThrows<ResponseStatusException> {
            userService.update(tokenJwt, updateUserRequest)
        }

        verify(authUtil).getUserIdFromJwt(tokenJwt)
        verify(validationUtil).validate(updateUserRequest)
        verify(userRepository).getReferenceById(userId)
        verify(userRepository).existsByEmail(updateUserRequest.email!!)
        verify(userRepository).save(user)
    }

    @Test
    fun `Test update user with null jwt`() {
        `when`(authUtil.getUserIdFromJwt(null)).thenThrow(RuntimeException())

        assertThrows<RuntimeException> {
            userService.update(null, updateUserRequest)
        }

        verify(authUtil).getUserIdFromJwt(null)
    }

    @Test
    fun `Test update user with invalid jwt`() {
        `when`(authUtil.getUserIdFromJwt("invalid_jwt")).thenThrow(RuntimeException())

        assertThrows<RuntimeException> {
            userService.update("invalid_jwt", updateUserRequest)
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