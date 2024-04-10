package com.belajar.api.kotlin.entities.user

import com.belajar.api.kotlin.validation.ValidationUtil
import jakarta.validation.ConstraintViolationException
import jakarta.validation.Validation
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*


@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(value = MethodOrderer.OrderAnnotation::class)
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
    @Order(1)
    fun `Test valid UpdateUserRequest`() {
        val request = updateUserRequest()
        validationUtil.validate(request)
    }

    @Test
    @Order(2)
    fun `Test UpdateUserRequest with blank name`() {
        val invalidRequest = updateUserRequest(name = "")
        val exception = assertThrows<ConstraintViolationException> { validationUtil.validate(invalidRequest) }

        val numberOfExceptions = exception.constraintViolations.size

        assertEquals(3, numberOfExceptions)
    }

    @Test
    @Order(3)
    fun `Test invalid CreateUserRequest with invalid name`() {
        val invalidRequest = updateUserRequest(name = "^&£*  ")
        assertThrows(ConstraintViolationException::class.java) { validationUtil.validate(invalidRequest) }
    }

    @Test
    @Order(4)
    fun `Test invalid CreateUserRequest with invalid name length`() {
        val invalidRequest = updateUserRequest(name = "2A ")
        val exception = assertThrows<ConstraintViolationException> { validationUtil.validate(invalidRequest) }

        val numberOfExceptions = exception.constraintViolations.size

        assertEquals(2, numberOfExceptions)
    }

    @Test
    @Order(5)
    fun `Test UpdateUserRequest with email invalid`() {
        val invalidRequest = updateUserRequest(email = "invalid_email")
        assertThrows(ConstraintViolationException::class.java) { validationUtil.validate(invalidRequest) }
    }

    @Test
    @Order(6)
    fun `Test UpdateUserRequest with password invalid`() {
        val invalidRequest = updateUserRequest(password = "£$12")
        assertThrows(ConstraintViolationException::class.java) { validationUtil.validate(invalidRequest) }
    }

}