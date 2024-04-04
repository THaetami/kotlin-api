package com.belajar.api.kotlin.validation

import com.belajar.api.kotlin.annotation.EmailIfNotBlank
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext

class EmailIfNotBlankValidator : ConstraintValidator<EmailIfNotBlank, String> {
    override fun isValid(value: String?, context: ConstraintValidatorContext?): Boolean {
        if (!value.isNullOrBlank()) {
            val regex = Regex("^\\S+@\\S+\\.\\S+$")
            return regex.matches(value)
        }
        return true
    }
}