package com.belajar.api.kotlin.repository

import com.belajar.api.kotlin.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository


@Repository
interface UserRepository: JpaRepository<User, Int> {
    fun existsByEmail(value: String): Boolean
    fun getUserByEmail(email: String): User?
}