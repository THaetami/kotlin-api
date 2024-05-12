package com.belajar.api.kotlin.annotation.user

import com.belajar.api.kotlin.validation.user.UniqueEmailValidator
import jakarta.validation.Constraint
import jakarta.validation.Payload
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@Constraint(validatedBy = [UniqueEmailValidator::class])
annotation class UniqueEmail(
    val message: String = "Email has already been taken",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)
