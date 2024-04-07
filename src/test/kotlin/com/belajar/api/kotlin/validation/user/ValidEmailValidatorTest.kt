package com.belajar.api.kotlin.validation.user

import com.belajar.api.kotlin.annotation.user.ValidEmail
import jakarta.validation.ConstraintValidatorContext
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
class ValidEmailValidatorTest {

    private lateinit var validator: ValidEmailValidator

    @Mock
    private lateinit var constraintValidatorContext: ConstraintValidatorContext

    @BeforeAll
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        validator = ValidEmailValidator()
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
    fun `Test isValid with null email`() {
        assertFalse(validator.isValid(null, constraintValidatorContext))
    }

    @Test
    fun `Test isValid with blank email`() {
        assertFalse(validator.isValid("", constraintValidatorContext))
    }

    @Test
    fun `Test isValid with valid email and context`() {
        val annotation = mockAnnotation()
        Mockito.`when`(constraintValidatorContext.buildConstraintViolationWithTemplate(annotation.message))
            .thenReturn(Mockito.mock())

        assertTrue(validator.isValid("test@example.com", constraintValidatorContext))
    }

    private fun mockAnnotation(): ValidEmail {
        return Mockito.mock(ValidEmail::class.java).apply {
            Mockito.`when`(message).thenReturn("Invalid email format")
        }
    }

}