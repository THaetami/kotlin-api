package com.belajar.api.kotlin.entities.user

import com.belajar.api.kotlin.validation.ValidationUtil
import jakarta.validation.ConstraintViolationException
import jakarta.validation.Validation
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance


@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
class UpdateUserRequestTest {

    private lateinit var validationUtil: ValidationUtil

    @BeforeAll
    fun setUp() {
        val validator = Validation.buildDefaultValidatorFactory().validator
        validationUtil = ValidationUtil(validator)
    }

    private fun updateUserRequest(
        name: String = "John Doe",
        email: String = "jodoe@example.com",
        password: String = "pass12"
    ): UpdateUserRequest {
        return UpdateUserRequest(name, email, password)
    }

    @Test
    fun `Test valid UpdateUserRequest`() {
        val request = updateUserRequest()

        validationUtil.validate(request)
    }

    @Test
    fun `Test UpdateUserRequest with name blank`() {
        val request = updateUserRequest(name = "")

        assertThrows(ConstraintViolationException::class.java) { validationUtil.validate(request) }
    }

    @Test
    fun `Test UpdateUserRequest with email blank`() {
        val request = updateUserRequest(email = "")
        validationUtil.validate(request)
    }

    @Test
    fun `Test UpdateUserRequest with password blank`() {
        val request = updateUserRequest(password = "")
        validationUtil.validate(request)
    }

    @Test
    fun `Test UpdateUserRequest with email invalid`() {
        val request = updateUserRequest(email = "invalid_email")
        assertThrows(ConstraintViolationException::class.java) { validationUtil.validate(request) }
    }

    @Test
    fun `Test UpdateUserRequest with password invalid`() {
        val request = updateUserRequest(password = "£$12")
        assertThrows(ConstraintViolationException::class.java) { validationUtil.validate(request) }
    }

}