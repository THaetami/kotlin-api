package com.belajar.api.kotlin.repository

import com.belajar.api.kotlin.model.User
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class UserRepositoryTest {

    @Mock
    lateinit var userRepository: UserRepository

    @Test
    fun `Test existsByEmail returns true`() {
        val email = "test@example.com"
        `when`(userRepository.existsByEmail(email)).thenReturn(true)

        val result = userRepository.existsByEmail(email)

        assertEquals(true, result)
    }

    @Test
    fun `Test existsByEmail returns false`() {
        val email = "test@example.com"
        `when`(userRepository.existsByEmail(email)).thenReturn(false)

        val result = userRepository.existsByEmail(email)

        assertEquals(false, result)
    }

    @Test
    fun `Test getUserByEmail returns user`() {
        val email = "test@example.com"
        val user = User().apply {
            id = 1
            name = "Test User"
            this.email = email
            password = "password"
        }
        `when`(userRepository.getUserByEmail(email)).thenReturn(user)

        val result = userRepository.getUserByEmail(email)

        assertEquals(user, result)
    }

}
