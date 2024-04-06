package com.belajar.api.kotlin.model

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

class UserTest {

    @Test
    fun `Test default constructor`() {
        val user = User()

        assertNull(user.id)
        assertEquals("", user.name)
        assertEquals("", user.email)
        assertEquals("", user.password)
        assertNull(user.createdAt)
        assertNull(user.updatedAt)
    }

    @Test
    fun `Test comparePassword`() {
        val user = User().apply {
            password = "password"
        }

        assertTrue(user.comparePassword("password"))
        assertFalse(user.comparePassword("wrong_password"))
    }

    @Test
    fun `Test password encryption`() {
        val plainPassword = "password"
        val user = User().apply {
            password = plainPassword
        }

        assertNotEquals(plainPassword, user.password)
    }

}