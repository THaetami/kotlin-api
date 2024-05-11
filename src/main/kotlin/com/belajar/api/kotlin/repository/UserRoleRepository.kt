package com.belajar.api.kotlin.repository

import com.belajar.api.kotlin.constant.UserRoleEnum
import com.belajar.api.kotlin.model.UserRole
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface UserRoleRepository : JpaRepository<UserRole, String> {
    fun findByRole(role: UserRoleEnum): Optional<UserRole>
}