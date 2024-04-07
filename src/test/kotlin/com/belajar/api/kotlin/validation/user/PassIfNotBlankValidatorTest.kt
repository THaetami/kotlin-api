package com.belajar.api.kotlin.validation.user

import com.belajar.api.kotlin.annotation.user.PassIfNotBlank
import jakarta.validation.ConstraintValidatorContext
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
class PassIfNotBlankValidatorTest {

    private lateinit var validator: PassIfNotBlankValidator

    @Mock
    private lateinit var constraintValidatorContext: ConstraintValidatorContext

    @BeforeAll
    fun setUp() {
        println("before ")
        MockitoAnnotations.openMocks(this)
        validator = PassIfNotBlankValidator()
    }

    @Test
    fun `Test isValid with format password valid`() {
        assertTrue(validator.isValid("tata12", constraintValidatorContext))
    }

    @Test
    fun `Test isValid with format password invalid`() {
        assertFalse(validator.isValid("tat£$ ", constraintValidatorContext))
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
            .thenReturn(Mockito.mock())

        assertTrue(validator.isValid("ab345", constraintValidatorContext))
    }

    private fun mockAnnotation(): PassIfNotBlank {
        return Mockito.mock(PassIfNotBlank::class.java).apply {
            `when`(message).thenReturn("must contain only alphanumeric characters and size must be between")
        }
    }

}