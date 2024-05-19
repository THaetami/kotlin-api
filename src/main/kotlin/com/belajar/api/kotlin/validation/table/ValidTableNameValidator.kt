package com.belajar.api.kotlin.validation.table

import com.belajar.api.kotlin.annotation.table.ValidTableName
import com.belajar.api.kotlin.repository.TableRepository
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class ValidTableNameValidator : ConstraintValidator<ValidTableName, String> {

    @Autowired
    private lateinit var tableRepository: TableRepository

    override fun isValid(value: String?, context: ConstraintValidatorContext): Boolean {
        if (value.isNullOrBlank()) return false
        return tableRepository.existsByName(value)
    }
}