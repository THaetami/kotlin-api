package com.belajar.api.kotlin.entities.user

import com.belajar.api.kotlin.model.User
import com.belajar.api.kotlin.repository.UserRepository
import com.belajar.api.kotlin.validation.ValidationUtil
import jakarta.validation.ConstraintViolationException
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean


@SpringBootTest
class AuthUserRequestTest {

    @Autowired
    private lateinit var validationUtil: ValidationUtil

    @MockBean
    private lateinit var userRepository: UserRepository

    private fun authUserRequest(email: String, password: String): AuthUserRequest {
        return AuthUserRequest(email, password)
    }

    @Test
    fun `Test valid AuthUserRequest`() {
        val request = authUserRequest("valid@example.com", "validPassword")
        assertDoesNotThrow { validationUtil.validate(request) }
    }

    @Test
    fun `Test invalid AuthUserRequest with email and password doesn't match`() {
        val email = "user@example.com"
        val password = "incorrectPassword"

        val user = User()
        user.email = email
        user.password = "correctPassword"
        `when`(userRepository.getUserByEmail(email)).thenReturn(user)

        val userRequest = authUserRequest(email, password)
        assertThrows<ConstraintViolationException> { validationUtil.validate(userRequest) }
    }


//    @Test
//    fun `Test invalid CreateUserRequest with blank email`() {
//        val user = authUserRequest("", "validPassword")
//        assertThrows<ConstraintViolationException> { validationUtil.validate(user) }
//    }

    @Test
    fun `Test invalid CreateUserRequest with invalid email`() {
        val user = authUserRequest("invalid_email", "validPassword")
        assertThrows<ConstraintViolationException> { validationUtil.validate(user) }
    }

    @Test
    fun `Test invalid CreateUserRequest with blank password`() {
        val user = authUserRequest("user@example.com", "")
        assertThrows<ConstraintViolationException> { validationUtil.validate(user) }
    }

}