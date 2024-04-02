package com.belajar.api.kotlin.repository

import com.belajar.api.kotlin.model.User
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface UserRepository: JpaRepository<User, Int> {
    fun existsByEmail(value: String): Boolean
    fun findByEmail(email: String): Optional<User>
    fun getUserByEmail(email: String): User?
}