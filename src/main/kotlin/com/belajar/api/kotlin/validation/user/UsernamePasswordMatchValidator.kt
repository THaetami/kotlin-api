package com.belajar.api.kotlin.validation.user

import com.belajar.api.kotlin.annotation.user.UsernamePasswordMatch
import com.belajar.api.kotlin.entities.user.LoginRequest
import com.belajar.api.kotlin.repository.UserAccountRepository
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import org.springframework.beans.factory.annotation.Autowired

class UsernamePasswordMatchValidator : ConstraintValidator<UsernamePasswordMatch, LoginRequest> {

    @Autowired
    lateinit var userAccountRepository: UserAccountRepository

    private lateinit var passwordPath: String

    override fun initialize(constraintAnnotation: UsernamePasswordMatch) {
        passwordPath = constraintAnnotation.passwordPath
    }

    override fun isValid(value: LoginRequest, context: ConstraintValidatorContext): Boolean {
        val user = value.username?.let { userAccountRepository.getUserByUsername(it) }

        if (user != null) {
            if (!value.password?.let { user.comparePassword(it) }!!) {
                context.disableDefaultConstraintViolation()
                context.buildConstraintViolationWithTemplate("Password doesn't match")
                    .addPropertyNode(passwordPath)
                    .addConstraintViolation()
                return false
            }
        }

        return true
    }

}