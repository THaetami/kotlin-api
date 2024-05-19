package com.belajar.api.kotlin.model

import com.belajar.api.kotlin.constant.TableName
import com.belajar.api.kotlin.constant.TransTypeEnum
import jakarta.persistence.*

@Entity
@Table(name = TableName.M_TRANS_TYPE)
data class TransType(
    @Id
    @Enumerated(EnumType.STRING)
    val id: TransTypeEnum,

    @Column(name = "description", nullable = false)
    var description: String,
)
