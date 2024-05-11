package com.belajar.api.kotlin.annotation.user

import com.belajar.api.kotlin.validation.user.ValidEmailValidator
import jakarta.validation.Constraint
import jakarta.validation.Payload
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@Constraint(validatedBy = [ValidEmailValidator::class])
annotation class ValidEmail(
    val message: String = "Invalid email format",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)
