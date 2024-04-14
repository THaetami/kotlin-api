package com.belajar.api.kotlin.validation.user

import com.belajar.api.kotlin.annotation.user.ExistingEmail
import com.belajar.api.kotlin.repository.UserRepository
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import org.springframework.beans.factory.annotation.Autowired

class ExistingEmailValidator : ConstraintValidator<ExistingEmail, String> {

    @Autowired
    lateinit var userRepository: UserRepository

    override fun isValid(value: String?, context: ConstraintValidatorContext?): Boolean {
        if (value.isNullOrBlank()) {
            return false
        }
        return userRepository.existsByEmail(value)
    }
}
