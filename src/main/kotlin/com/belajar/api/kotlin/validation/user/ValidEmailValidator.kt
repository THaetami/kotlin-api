package com.belajar.api.kotlin.validation.user

import com.belajar.api.kotlin.annotation.user.ValidEmail
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext

class ValidEmailValidator : ConstraintValidator<ValidEmail, String> {

    override fun isValid(value: String?, context: ConstraintValidatorContext?): Boolean {
        if (value.isNullOrBlank()) {
            return false
        }
        val regex = Regex("^\\S+@\\S+\\.\\S+$")
        return regex.matches(value)
    }

}