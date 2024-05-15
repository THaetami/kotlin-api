package com.belajar.api.kotlin.model

import com.belajar.api.kotlin.constant.TableName
import com.belajar.api.kotlin.constant.UserRoleEnum
import jakarta.persistence.*
import jakarta.persistence.Table

@Entity
@Table(name = TableName.M_USER_ROLE)
data class UserRole(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: String? = null,

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    var role: UserRoleEnum? = null
)
