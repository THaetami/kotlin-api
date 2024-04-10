package com.belajar.api.kotlin.entities.user


import com.belajar.api.kotlin.validation.ValidationUtil
import jakarta.validation.ConstraintViolationException
import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest


@SpringBootTest
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
    fun `Test valid CreateUserRequest`() {
        val request = createUserRequest()

        validationUtil.validate(request)
    }

//    @Test
//    fun `Test invalid CreateUserRequest with blank email`() {
//        val invalidRequest = createUserRequest(email = "  ")
//
//        assertThrows<ConstraintViolationException> {
//            validationUtil.validate(invalidRequest)
//        }
//    }

    @Test
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
    fun `Test invalid CreateUserRequest with invalid email`() {
        val invalidRequest = createUserRequest(email = "invalid_email")
        assertThrows<ConstraintViolationException> {
            validationUtil.validate(invalidRequest)
        }
    }

//    @Test
//    fun `Test invalid CreateUserRequest with blank name`() {
//        val invalidRequest = createUserRequest(name = "  ")
//        assertThrows<ConstraintViolationException> {
//            validationUtil.validate(invalidRequest)
//        }
//    }

    @Test
    fun `Test invalid CreateUserRequest with invalid name`() {
        val invalidRequest = createUserRequest(name = "^&£*  ")
        assertThrows<ConstraintViolationException> {
            validationUtil.validate(invalidRequest)
        }
    }

//    @Test
//    fun `Test invalid CreateUserRequest with invalid name length`() {
//        val invalidRequest = createUserRequest(name = "22")
//        assertThrows<ConstraintViolationException> {
//            validationUtil.validate(invalidRequest)
//        }
//    }

//    @Test
//    fun `Test invalid CreateUserRequest with blank password`() {
//        val invalidRequest = createUserRequest(password = "")
//        assertThrows<ConstraintViolationException> {
//            validationUtil.validate(invalidRequest)
//        }
//    }

    @Test
    fun `Test invalid CreateUserRequest with invalid password`() {
        val invalidRequest = createUserRequest(password = "^&£*  ")
        assertThrows<ConstraintViolationException> {
            validationUtil.validate(invalidRequest)
        }
    }

//    @Test
//    fun `Test invalid CreateUserRequest with invalid password length`() {
//        val invalidRequest = createUserRequest(name = "2A")
//        assertThrows<ConstraintViolationException> {
//            validationUtil.validate(invalidRequest)
//        }
//    }

}