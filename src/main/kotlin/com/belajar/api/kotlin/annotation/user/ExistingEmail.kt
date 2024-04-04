package com.belajar.api.kotlin.annotation.user

import com.belajar.api.kotlin.validation.user.ExistingEmailValidator
import jakarta.validation.Constraint
import jakarta.validation.Payload
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@Constraint(validatedBy = [ExistingEmailValidator::class])
annotation class ExistingEmail(
    val message: String = "User not found",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)
