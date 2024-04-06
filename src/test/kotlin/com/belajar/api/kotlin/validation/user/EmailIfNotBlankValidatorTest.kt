package com.belajar.api.kotlin.validation.user

import com.belajar.api.kotlin.annotation.user.EmailIfNotBlank
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class EmailIfNotBlankValidatorTest {

    private lateinit var validator: EmailIfNotBlankValidator

    private fun mockAnnotation(): EmailIfNotBlank {
        return mock(EmailIfNotBlank::class.java).apply {
            `when`(message).thenReturn("Invalid email format")
        }
    }

    @Mock
    private lateinit var constraintValidatorContext: ConstraintValidatorContext

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        validator = EmailIfNotBlankValidator()
    }

    @Test
    fun `Test isValid with valid email`() {
        assertTrue(validator.isValid("test@example.com", constraintValidatorContext))
    }

    @Test
    fun `Test isValid with invalid email`() {
        assertFalse(validator.isValid("invalid_email", constraintValidatorContext))
    }

    @Test
    fun `Test isValid with null value`() {
        assertTrue(validator.isValid(null, constraintValidatorContext))
    }

    @Test
    fun `Test isValid with blank value`() {
        assertTrue(validator.isValid("", constraintValidatorContext))
    }

    @Test
    fun `Test isValid with valid email and context`() {
        val annotation = mockAnnotation()
        `when`(constraintValidatorContext.buildConstraintViolationWithTemplate(annotation.message))
            .thenReturn(mock())

        assertTrue(validator.isValid("test@example.com", constraintValidatorContext))
    }

}