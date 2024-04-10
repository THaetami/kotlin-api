package com.belajar.api.kotlin.entities.user


import com.belajar.api.kotlin.validation.ValidationUtil
import jakarta.validation.ConstraintViolationException
import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest


@SpringBootTest
@TestMethodOrder(value = MethodOrderer.OrderAnnotation::class)
class CreateUserRequestTest {

    @Autowired
    private lateinit var validationUtil: ValidationUtil

    private fun createUserRequest(
        name: String = "John Doe",
        email: String = "jodoe@example.com",
        password: String = "123pas"
    ): CreateUserRequest {
        return CreateUserRequest(name, email, password)
    }

    @Test
    @Order(1)
    fun `Test valid CreateUserRequest`() {
        val request = createUserRequest()

        validationUtil.validate(request)
    }

    @Test
    @Order(2)
    fun `Test invalid CreateUserRequest with blank name`() {
        val invalidRequest = createUserRequest(name = "  ")
        val exception = assertThrows<ConstraintViolationException> { validationUtil.validate(invalidRequest) }

        val numberOfExceptions = exception.constraintViolations.size

        Assertions.assertEquals(2, numberOfExceptions)
    }

    @Test
    @Order(3)
    fun `Test invalid CreateUserRequest with invalid name`() {
        val invalidRequest = createUserRequest(name = "^&£*  ")
        assertThrows<ConstraintViolationException> {
            validationUtil.validate(invalidRequest)
        }
    }

    @Test
    @Order(4)
    fun `Test invalid CreateUserRequest with invalid name length`() {
        val invalidRequest = createUserRequest(name = "22")
        val exception = assertThrows<ConstraintViolationException> { validationUtil.validate(invalidRequest) }

        val numberOfExceptions = exception.constraintViolations.size

        Assertions.assertEquals(2, numberOfExceptions)
    }

    @Test
    @Order(5)
    fun `Test invalid CreateUserRequest with blank email`() {
        val invalidRequest = createUserRequest(email = "  ")

        val exception = assertThrows<ConstraintViolationException> { validationUtil.validate(invalidRequest) }

        val numberOfExceptions = exception.constraintViolations.size

        Assertions.assertEquals(3, numberOfExceptions)
    }

    @Test
    @Order(6)
    fun `test invalid CreateUserRequest with existing email`() {
        val invalidRequest = CreateUserRequest(
            name = "Jane Doe",
            email = "john.doe@example.com",
            password = "password"
        )
        assertThrows<ConstraintViolationException> {
            validationUtil.validate(invalidRequest)
        }
    }

    @Test
    @Order(7)
    fun `Test invalid CreateUserRequest with invalid email`() {
        val invalidRequest = createUserRequest(email = "invalid_email")
        assertThrows<ConstraintViolationException> {
            validationUtil.validate(invalidRequest)
        }
    }

    @Test
    @Order(8)
    fun `Test invalid CreateUserRequest with blank password`() {
        val invalidRequest = createUserRequest(password = "")
        val exception = assertThrows<ConstraintViolationException> { validationUtil.validate(invalidRequest) }

        val numberOfExceptions = exception.constraintViolations.size

        Assertions.assertEquals(3, numberOfExceptions)
    }

    @Test
    @Order(9)
    fun `Test invalid CreateUserRequest with invalid password`() {
        val invalidRequest = createUserRequest(password = "^&£*  ")
        assertThrows<ConstraintViolationException> {
            validationUtil.validate(invalidRequest)
        }
    }

    @Test
    @Order(10)
    fun `Test invalid CreateUserRequest with invalid password length`() {
        val invalidRequest = createUserRequest(password = "2A")

        assertThrows<ConstraintViolationException> {
            validationUtil.validate(invalidRequest)
        }
    }

}