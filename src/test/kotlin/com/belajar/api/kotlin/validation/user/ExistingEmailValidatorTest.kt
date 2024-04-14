package com.belajar.api.kotlin.validation.user

import com.belajar.api.kotlin.annotation.user.ExistingEmail
import com.belajar.api.kotlin.repository.UserRepository
import jakarta.validation.ConstraintValidatorContext
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
class ExistingEmailValidatorTest {

    @Mock
    private lateinit var userRepository: UserRepository

    @InjectMocks
    private lateinit var validator: ExistingEmailValidator

    @Mock
    private lateinit var constraintValidatorContext: ConstraintValidatorContext

    @BeforeAll
    fun setUp() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun `Test isValid with exist email`() {
        val email = "test@example.com"
        `when`(userRepository.existsByEmail(email)).thenReturn(false)
        assertFalse(validator.isValid(email, constraintValidatorContext))
    }

    @Test
    fun `Test isValid with no exist email`() {
        val email = "test@example.com"
        `when`(userRepository.existsByEmail(email)).thenReturn(true)
        assertTrue(validator.isValid(email, constraintValidatorContext))
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
    fun `Test isValid with exist email and context`() {
        val annotation = mockAnnotation()
        `when`(constraintValidatorContext.buildConstraintViolationWithTemplate(annotation.message))
            .thenReturn(Mockito.mock())

        assertFalse(validator.isValid("test@example.com", constraintValidatorContext))
    }

    private fun mockAnnotation(): ExistingEmail {
        return Mockito.mock(ExistingEmail::class.java).apply {
            `when`(message).thenReturn("User not found")
        }
    }

}