package com.belajar.api.kotlin.service.impl

import com.belajar.api.kotlin.entities.AuthUserRequest
import com.belajar.api.kotlin.entities.CreateUserRequest
import com.belajar.api.kotlin.entities.UserResponse
import com.belajar.api.kotlin.error.NotFoundException
import com.belajar.api.kotlin.error.UnauthorizedException
import com.belajar.api.kotlin.model.User
import com.belajar.api.kotlin.repository.UserRepository
import com.belajar.api.kotlin.service.UserService
import com.belajar.api.kotlin.validation.ValidationUser
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Service
import java.util.*
import javax.crypto.SecretKey
import com.belajar.api.kotlin.utils.*


@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
    val validationUser: ValidationUser
): UserService {

    override fun create(createUserRequest: CreateUserRequest): UserResponse {

        validationUser.validate(createUserRequest)

        val user = User()

        user.name = createUserRequest.name!!
        user.email = createUserRequest.email!!
        user.password = createUserRequest.password!!
        user.createdAt = Date()

        userRepository.save(user)

        return UserResponse(
            id = user.id,
            name = user.name,
            createdAt = user.createdAt!!,
            updatedAt = user.updatedAt
        )

    }

    override fun auth(authUserRequest: AuthUserRequest, response: HttpServletResponse) {

        validationUser.validate(authUserRequest)

        val user = userRepository.getUserByEmail(authUserRequest.email)
            ?: throw NotFoundException("User Not Found")

        val jwt = generateJwt(user.id)

        val cookie = Cookie("jwt", jwt)
        cookie.isHttpOnly = true

        response.addCookie(cookie)

    }

    override fun get(jwt: String?): UserResponse {
        val userId = getUserIdFromJwt(jwt)

        val user = userRepository.getReferenceById(userId)
        return UserResponse(
            id = user.id,
            name = user.name,
            createdAt = user.createdAt,
            updatedAt = user.updatedAt
        )
    }

}