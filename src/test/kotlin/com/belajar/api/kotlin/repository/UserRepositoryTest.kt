package com.belajar.api.kotlin.repository

import com.belajar.api.kotlin.model.User
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest


@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
class UserRepositoryTest {

    @Autowired
    private lateinit var userRepository: UserRepository

    private lateinit var mockUser: User

    private fun prepareUser(email: String): User {
        return User().apply {
            name = "name"
            this.email = email
            password = "password"
        }
    }

    @BeforeAll
    fun setUp() {
        mockUser = prepareUser("email1")
        userRepository.save(mockUser)
    }

    @Test
    fun `it should return true when user exists by email`() {
        val exists = userRepository.existsByEmail("email1")
        assertTrue(exists)
    }

    @Test
    fun `it should return false when user not exists by email`() {
        val exists = userRepository.existsByEmail("not@exist.com")
        assertFalse(exists)
    }

    @Test
    fun `it should return user by email`() {
        val foundUser = userRepository.getUserByEmail("email1")

        assertNotNull(foundUser)

        assertEquals(mockUser.name, foundUser?.name)
        assertEquals(mockUser.email, foundUser?.email)
        assertEquals(mockUser.password, foundUser?.password)
    }

    @Test
    fun `it should return null if user does not exist by email`() {
        val foundUser = userRepository.getUserByEmail("notfound@example.com")
        assertNull(foundUser)
    }

    @AfterAll
    fun tearDown() {
        userRepository.delete(mockUser)
    }

}
