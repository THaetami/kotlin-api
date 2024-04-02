package com.belajar.api.kotlin.validation

import com.belajar.api.kotlin.annotation.EmailPasswordMatch
import com.belajar.api.kotlin.entities.AuthUserRequest
import com.belajar.api.kotlin.repository.UserRepository
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext

class EmailPasswordMatchValidator(
    private val userRepository: UserRepository
) : ConstraintValidator<EmailPasswordMatch, AuthUserRequest> {

    override fun isValid(value: AuthUserRequest, context: ConstraintValidatorContext): Boolean {

        // Mengambil user berdasarkan email dari repository
        val user = userRepository.getUserByEmail(value.email)

        // Jika email dan password tidak cocok
        if (user != null) {
            if (!user.comparePassword(value.password)) {
                return false
            }
        }

        return true
    }
}

