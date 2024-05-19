package com.belajar.api.kotlin.annotation.menu

import com.belajar.api.kotlin.validation.menu.UniqueNameMenuValidator
import jakarta.validation.Constraint
import jakarta.validation.Payload
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@Constraint(validatedBy = [UniqueNameMenuValidator::class])
annotation class UniqueNameMenu(
    val message: String = "Name has already been taken",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)
