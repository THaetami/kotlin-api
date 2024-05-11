package com.belajar.api.kotlin.annotation.user

import com.belajar.api.kotlin.validation.user.UsernameIfNotBlankValidator
import jakarta.validation.Constraint
import jakarta.validation.Payload
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@Constraint(validatedBy = [UsernameIfNotBlankValidator::class])
annotation class UsernameIfNotBlank(
    val message: String = "must contain only alphanumeric characters",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)