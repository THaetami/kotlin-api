package com.belajar.api.kotlin.service

import com.belajar.api.kotlin.entities.JwtClaimsResponse
import com.belajar.api.kotlin.model.UserAccount

interface JwtService {
    fun generateToken(userAccount: UserAccount): String
    fun verifyJwtToken(token: String): Boolean
    fun getClaimsByToken(token: String): JwtClaimsResponse
}