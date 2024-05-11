package com.belajar.api.kotlin.validation.user

import com.belajar.api.kotlin.annotation.user.UsernameIfNotBlank
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext

class UsernameIfNotBlankValidator : ConstraintValidator<UsernameIfNotBlank, String> {

    override fun isValid(value: String?, context: ConstraintValidatorContext?): Boolean {
        if (!value.isNullOrBlank()) {
            val regex = Regex("^[a-zA-Z0-9]+\$")
            return regex.matches(value)
        }
        return true
    }

}