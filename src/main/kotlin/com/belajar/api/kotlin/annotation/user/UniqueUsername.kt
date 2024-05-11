package com.belajar.api.kotlin.annotation.user

import com.belajar.api.kotlin.validation.user.UniqueUsernameValidator
import jakarta.validation.Constraint
import jakarta.validation.Payload
import kotlin.reflect.KClass


@Target(AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@Constraint(validatedBy = [UniqueUsernameValidator::class])
annotation class UniqueUsername(
    val message: String = "Username has already been taken",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)
