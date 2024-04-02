package com.belajar.api.kotlin.validation

import com.belajar.api.kotlin.annotation.ValidEmail
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext

class ValidEmailValidator : ConstraintValidator<ValidEmail, String> {
    override fun isValid(value: String?, context: ConstraintValidatorContext?): Boolean {
        if (value == null || value.isBlank()) {
            return false
        }

        val regex = Regex("^\\S+@\\S+\\.\\S+$")
        return regex.matches(value)
    }
}