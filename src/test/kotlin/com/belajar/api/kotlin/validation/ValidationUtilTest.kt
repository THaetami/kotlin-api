package com.belajar.api.kotlin.validation

import jakarta.validation.ConstraintViolation
import jakarta.validation.ConstraintViolationException
import jakarta.validation.Validator
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class ValidationUtilTest {

    @Mock
    private lateinit var validator: Validator

    private lateinit var validationUtil: ValidationUtil

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        validationUtil = ValidationUtil(validator)
    }

    @Test
    fun `test validate method when validation passes`() {
        val anyObject = Any()
        `when`(validator.validate(anyObject)).thenReturn(emptySet())

        validationUtil.validate(anyObject)
    }

    @Test
    fun `test validate method when validation fails`() {
        val anyObject = Any()
        val constraintViolation: ConstraintViolation<Any> = mock()
        `when`(validator.validate(anyObject)).thenReturn(setOf(constraintViolation))

        val exception = assertThrows(ConstraintViolationException::class.java) {
            validationUtil.validate(anyObject)
        }

        assertEquals(1, exception.constraintViolations.size)
        assertEquals(constraintViolation, exception.constraintViolations.first())
    }

    // Helper function to create mock constraint violations
    private inline fun <reified T> mock(): T = Mockito.mock(T::class.java)
}
