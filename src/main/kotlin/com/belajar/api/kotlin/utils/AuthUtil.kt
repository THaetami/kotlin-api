package com.belajar.api.kotlin.utils

import com.belajar.api.kotlin.error.UnauthorizedException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import java.util.*
import javax.crypto.SecretKey

const val SECRET = "4bb6d1dfbafb64a681139d1586b6f1160d18159afd57c8c79136d7490630407c"

fun getKey(): SecretKey {
    val keyBytes = Decoders.BASE64URL.decode(SECRET)
    return Keys.hmacShaKeyFor(keyBytes)
}

fun generateJwt(userId: Int): String {
    return Jwts.builder()
        .subject(userId.toString())
        .expiration(Date(System.currentTimeMillis() + 60 * 24 * 1000)) // 1 day
        .signWith(getKey())
        .compact()
}

fun getUserIdFromJwt(jwt: String?): Int {
    if (jwt == null) {
        throw UnauthorizedException("JWT token is null")
    }

    try {
        return parseJwt(jwt)
    } catch (ex: Exception) {
        throw UnauthorizedException("Invalid JWT token")
    }
}

fun parseJwt(jwt: String): Int {
    return Jwts.parser()
        .verifyWith(getKey())
        .build()
        .parseSignedClaims(jwt)
        .payload
        .subject.toInt();
}