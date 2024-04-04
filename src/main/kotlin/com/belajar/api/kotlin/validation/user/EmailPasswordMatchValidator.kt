package com.belajar.api.kotlin.validation.user

import com.belajar.api.kotlin.annotation.user.EmailPasswordMatch
import com.belajar.api.kotlin.entities.user.AuthUserRequest
import com.belajar.api.kotlin.repository.UserRepository
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext

class EmailPasswordMatchValidator(
    private val userRepository: UserRepository
) : ConstraintValidator<EmailPasswordMatch, AuthUserRequest> {

    private lateinit var passwordPath: String

    override fun initialize(constraintAnnotation: EmailPasswordMatch) {
        passwordPath = constraintAnnotation.passwordPath
    }

    override fun isValid(value: AuthUserRequest, context: ConstraintValidatorContext): Boolean {
        val user = userRepository.getUserByEmail(value.email)

        if (user != null) {
            if (!user.comparePassword(value.password)) {
                context.disableDefaultConstraintViolation()
                context.buildConstraintViolationWithTemplate("Password doesn't match")
                    .addPropertyNode(passwordPath)
                    .addConstraintViolation()
                return false
            }
        }

        return true
    }

}

