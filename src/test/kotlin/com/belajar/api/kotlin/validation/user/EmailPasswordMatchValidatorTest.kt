package com.belajar.api.kotlin.validation.user

import com.belajar.api.kotlin.entities.user.AuthUserRequest
import com.belajar.api.kotlin.model.User
import com.belajar.api.kotlin.repository.UserRepository
import jakarta.validation.ConstraintValidatorContext
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
class EmailPasswordMatchValidatorTest {

    @Mock
    private lateinit var userRepository: UserRepository

    private lateinit var validator: EmailPasswordMatchValidator

    @BeforeAll
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        validator = EmailPasswordMatchValidator(userRepository)
        validator.passwordPath = "password"
    }

    @Test
    fun `test isValid when user exists and password matches`() {
        val user = User().apply {
            email ="test@example.com"
            password = "password123"
        }
        val request = AuthUserRequest("test@example.com", "password123")
        `when`(userRepository.getUserByEmail("test@example.com")).thenReturn(user)

        val result = validator.isValid(request, mockConstraintValidatorContext())

        assertTrue(result)
    }

    @Test
    fun `test isValid when user exists but password doesn't match`() {
        val user = User().apply {
            email ="test@example.com"
            password = "password123"
        }
        val request = AuthUserRequest("test@example.com", "wrongpassword")
        `when`(userRepository.getUserByEmail("test@example.com")).thenReturn(user)

        val result = validator.isValid(request, mockConstraintValidatorContext())

        assertFalse(result)
    }

    @Test
    fun `test isValid when user doesn't exist`() {
        val request = AuthUserRequest("nonexistent@example.com", "password123")
        `when`(userRepository.getUserByEmail("nonexistent@example.com")).thenReturn(null)

        val result = validator.isValid(request, mockConstraintValidatorContext())

        assertTrue(result)
    }

    private fun mockConstraintValidatorContext(): ConstraintValidatorContext {
        val constraintValidatorContext = mock(ConstraintValidatorContext::class.java)
        val constraintViolationBuilder = mock(ConstraintValidatorContext.ConstraintViolationBuilder::class.java)
        val nodeBuilderCustomizableContext = mock(ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext::class.java)

        `when`(constraintValidatorContext.buildConstraintViolationWithTemplate(anyString())).thenReturn(constraintViolationBuilder)
        `when`(constraintViolationBuilder.addPropertyNode(anyString())).thenReturn(nodeBuilderCustomizableContext)

        return constraintValidatorContext
    }


}