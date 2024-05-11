package com.belajar.api.kotlin.service

import com.belajar.api.kotlin.exception.NotFoundException
import com.belajar.api.kotlin.model.UserAccount
import com.belajar.api.kotlin.repository.UserAccountRepository
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import java.util.*

@Service
class MyUserDetailsService(val userAccountRepository: UserAccountRepository) : UserDetailsService {
    override fun loadUserByUsername(username: String): UserAccount {
        // Mencari user berdasarkan username dan mengelola Optional
        val optionalUser: Optional<UserAccount> = userAccountRepository.findByUsername(username)
        return optionalUser.orElseThrow { NotFoundException("User with username $username not found") }
    }
}

