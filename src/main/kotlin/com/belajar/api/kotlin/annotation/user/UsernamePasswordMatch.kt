package com.belajar.api.kotlin.annotation.user


import com.belajar.api.kotlin.validation.user.UsernamePasswordMatchValidator
import jakarta.validation.Constraint
import jakarta.validation.Payload
import kotlin.reflect.KClass

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@Constraint(validatedBy = [UsernamePasswordMatchValidator::class])
annotation class UsernamePasswordMatch(
    val message: String = "Invalid password",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = [],
    val passwordPath: String = "password"
)