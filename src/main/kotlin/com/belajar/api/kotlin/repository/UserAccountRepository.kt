package com.belajar.api.kotlin.repository

import com.belajar.api.kotlin.model.UserAccount
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface UserAccountRepository: JpaRepository<UserAccount, Int> {
    fun findByUsername(username: String): Optional<UserAccount>
    fun existsByUsername(username: String): Boolean
    fun getUserByUsername(username: String): UserAccount?
}