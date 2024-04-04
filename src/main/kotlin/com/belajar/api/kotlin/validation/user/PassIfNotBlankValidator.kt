package com.belajar.api.kotlin.validation.user

import com.belajar.api.kotlin.annotation.user.PassIfNotBlank
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext

class PassIfNotBlankValidator : ConstraintValidator<PassIfNotBlank, String> {

    override fun isValid(value: String?, context: ConstraintValidatorContext?): Boolean {
        if (!value.isNullOrBlank()) {
            val regex = Regex("^[a-zA-Z0-9]+\$")
            return regex.matches(value) && value.length in 4..6
        }

        return true
    }

}