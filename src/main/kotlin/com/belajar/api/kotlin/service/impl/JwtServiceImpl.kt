package com.belajar.api.kotlin.service.impl

import com.belajar.api.kotlin.constant.StatusMessage
import com.belajar.api.kotlin.entities.JwtClaimsResponse
import com.belajar.api.kotlin.exception.UnauthorizedException
import com.belajar.api.kotlin.model.UserAccount
import com.belajar.api.kotlin.service.JwtService
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import io.jsonwebtoken.security.SignatureAlgorithm
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.GrantedAuthority
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*
import javax.crypto.SecretKey

@Service
class JwtServiceImpl(
    @Value("\${template_api.jwt.secret}") private val JWT_SECRET: String,
    @Value("\${template_api.jwt.issuer}") private val JWT_ISSUER: String,
    @Value("\${template_api.jwt.expiration}") private val JWT_EXPIRATION: Long
) : JwtService {

    private val log = LoggerFactory.getLogger(JwtServiceImpl::class.java)

    internal fun getKey(): SecretKey {
        val keyBytes = Decoders.BASE64URL.decode(JWT_SECRET)
        return Keys.hmacShaKeyFor(keyBytes)
    }

    @Transactional(rollbackFor = [Exception::class])
    override fun generateToken(userAccount: UserAccount): String {
        try {
            val expirationDate = Date(System.currentTimeMillis() + JWT_EXPIRATION)
            return Jwts.builder()
                .subject(userAccount.id.toString())
                .claim("roles", userAccount.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
                .issuer(JWT_ISSUER)
                .issuedAt(Date())
                .expiration(expirationDate)
                .signWith(getKey())
                .compact()
        } catch (e: Exception) {
            log.error("Failed to generate JWT: ${e.message}")
            throw IllegalArgumentException("Error generating JWT", e)
        }

    }

    @Transactional(rollbackFor = [Exception::class])
    override fun verifyJwtToken(token: String): Boolean {
        return try {
            val tokenParse = parseToken(token)
            val claims = Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(tokenParse)
            claims != null && !claims.payload.expiration.before(Date())
        } catch (e: Exception) {
            log.error("Invalid JWT: {}", e.message)
            false
        }
    }

    @Transactional(rollbackFor = [Exception::class])
    override fun getClaimsByToken(token: String): JwtClaimsResponse {
        try {
            val claims = Jwts.parser()
                .verifyWith(getKey())
                .requireIssuer(JWT_ISSUER)
                .build().parseSignedClaims(parseToken(token))
                .payload
            val roles = (claims["roles", List::class.java] as List<*>).filterIsInstance<String>()

            return JwtClaimsResponse(
                userAccountId = claims.subject,
                roles = roles
            )
        } catch (e: Exception) {
            log.error("Error retrieving JWT claims: ${e.message}")
            throw UnauthorizedException(StatusMessage.UNAUTHORIZED)
        }

    }

    private fun parseToken(bearerToken: String): String = bearerToken.replace("Bearer ", "")

}