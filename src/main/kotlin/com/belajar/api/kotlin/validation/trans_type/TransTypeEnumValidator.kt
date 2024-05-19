package com.belajar.api.kotlin.validation.trans_type

import com.belajar.api.kotlin.annotation.trans_type.ValidTransTypeEnum
import com.belajar.api.kotlin.constant.TransTypeEnum
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext

class TransTypeEnumValidator : ConstraintValidator<ValidTransTypeEnum, String> {
    override fun isValid(value: String?, context: ConstraintValidatorContext): Boolean {
        if (value == null) return false
        return try {
            TransTypeEnum.valueOf(value)
            true
        } catch (e: IllegalArgumentException) {
            false
        }
    }
}
