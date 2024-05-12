package com.belajar.api.kotlin.validation.user

import com.belajar.api.kotlin.annotation.user.UniqueEmail
import com.belajar.api.kotlin.repository.UserAccountRepository
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class UniqueEmailValidator: ConstraintValidator<UniqueEmail, String> {

    @Autowired
    lateinit var userAccountRepository: UserAccountRepository

    override fun isValid(value: String?, context: ConstraintValidatorContext?): Boolean {
        if (value.isNullOrBlank()) {
            return false
        }
        return !userAccountRepository.existsByEmail(value)
    }

}