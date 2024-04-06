package com.belajar.api.kotlin.validation.user

import jakarta.validation.ConstraintValidatorContext
import org.hibernate.validator.internal.engine.constraintvalidation.ConstraintValidatorContextImpl
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class ValidEmailValidatorTest {

    private lateinit var validator: ValidEmailValidator


    @Mock
    private lateinit var constraintValidatorContext: ConstraintValidatorContext

    @BeforeEach
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

}