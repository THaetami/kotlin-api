package com.belajar.api.kotlin.validation.user

import com.belajar.api.kotlin.annotation.user.PassIfNotBlank
import com.belajar.api.kotlin.annotation.user.UniqueEmail
import com.belajar.api.kotlin.repository.UserRepository
import jakarta.validation.ConstraintValidatorContext
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class UniqueEmailValidatorTest {

    @Mock
    private lateinit var userRepository: UserRepository

    @InjectMocks
    private lateinit var validator: UniqueEmailValidator


    @Mock
    private lateinit var constraintValidatorContext: ConstraintValidatorContext

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        validator = UniqueEmailValidator(userRepository)
    }

    @Test
    fun `Test isValid with unique email`() {
        val email = "test@example.com"
        `when`(userRepository.existsByEmail(email)).thenReturn(false)
        assertTrue(validator.isValid(email, constraintValidatorContext))
    }

    @Test
    fun `Test isValid with non-unique email`() {
        val email = "test@example.com"
        `when`(userRepository.existsByEmail(email)).thenReturn(true)
        assertFalse(validator.isValid(email, constraintValidatorContext))
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
    fun `Test isValid with unique email and context`() {
        val annotation = mockAnnotation()
        `when`(constraintValidatorContext.buildConstraintViolationWithTemplate(annotation.message))
            .thenReturn(Mockito.mock())

        assertTrue(validator.isValid("test@example.com", constraintValidatorContext))
    }

    private fun mockAnnotation(): UniqueEmail {
        return Mockito.mock(UniqueEmail::class.java).apply {
            `when`(message).thenReturn("Email has already been taken")
        }
    }

}