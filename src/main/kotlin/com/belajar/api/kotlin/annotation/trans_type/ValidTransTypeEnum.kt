package com.belajar.api.kotlin.annotation.trans_type

import com.belajar.api.kotlin.validation.trans_type.TransTypeEnumValidator
import jakarta.validation.Constraint
import jakarta.validation.Payload
import kotlin.reflect.KClass

@MustBeDocumented
@Constraint(validatedBy = [TransTypeEnumValidator::class])
@Target(AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class ValidTransTypeEnum(
    val message: String = "Invalid transaction type",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)

