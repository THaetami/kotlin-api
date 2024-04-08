package com.belajar.api.kotlin.entities.user

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.util.*

class UserResponseTest {

    @Test
    fun `Test user response properties`() {
        // Arrange
        val id = 1
        val name = "John Doe"
        val createdAt = Date()
        val updatedAt = Date()

        // Act
        val userResponse = UserResponse(id, name, createdAt, updatedAt)

        // Assert
        assertEquals(id, userResponse.id)
        assertEquals(name, userResponse.name)
        assertEquals(createdAt, userResponse.createdAt)
        assertEquals(updatedAt, userResponse.updatedAt)
    }

    @Test
    fun `test user response properties with null createdAt and updatedAt`() {
        // Arrange

        val name = "John Doe"

        // Act
        val userResponse = UserResponse(null, name, null, null)

        // Assert
        assertEquals(null, userResponse.id)
        assertEquals(name, userResponse.name)
        assertEquals(null, userResponse.createdAt)
        assertEquals(null, userResponse.updatedAt)
    }

}