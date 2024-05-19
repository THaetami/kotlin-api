package com.belajar.api.kotlin.validation.menu

import com.belajar.api.kotlin.annotation.menu.UniqueNameMenu
import com.belajar.api.kotlin.repository.MenuRepository
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class UniqueNameMenuValidator: ConstraintValidator<UniqueNameMenu, String> {

    @Autowired
    lateinit var menuRepository: MenuRepository
    override fun isValid(value: String?, context: ConstraintValidatorContext?): Boolean {
        if (value.isNullOrBlank()) {
            return false
        }
        return !menuRepository.existsByName(value)
    }

}