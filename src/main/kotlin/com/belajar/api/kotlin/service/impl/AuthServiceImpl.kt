package com.belajar.api.kotlin.service.impl

import com.belajar.api.kotlin.entities.user.AuthUserRequest
import com.belajar.api.kotlin.exception.NotFoundException
import com.belajar.api.kotlin.repository.UserRepository
import com.belajar.api.kotlin.service.AuthService
import com.belajar.api.kotlin.utils.AuthUtil
import com.belajar.api.kotlin.validation.ValidationUtil
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Service

@Service
class AuthServiceImpl(
    private val userRepository: UserRepository,
    val validationUtil: ValidationUtil,
): AuthService {

    val authUtil = AuthUtil()

    override fun authenticate(authUserRequest: AuthUserRequest, response: HttpServletResponse) {
        validationUtil.validate(authUserRequest)

        val user = userRepository.getUserByEmail(authUserRequest.email)
            ?: throw NotFoundException("User Not Found")


        val jwt = authUtil.generateJwt(user.id!!)

        val cookie = Cookie("jwt", jwt)
        cookie.isHttpOnly = true

        response.addCookie(cookie)
    }

    override fun unauthenticate(jwt: String?, response: HttpServletResponse) {
        authUtil.getUserIdFromJwt(jwt)

        authUtil.clearJwtCookie(response)
    }

}