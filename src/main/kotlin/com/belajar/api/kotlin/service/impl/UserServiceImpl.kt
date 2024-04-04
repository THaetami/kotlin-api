package com.belajar.api.kotlin.service.impl

import com.belajar.api.kotlin.entities.*
import com.belajar.api.kotlin.error.NotFoundException
import com.belajar.api.kotlin.error.ValidationCustomException
import com.belajar.api.kotlin.model.User
import com.belajar.api.kotlin.repository.UserRepository
import com.belajar.api.kotlin.service.UserService
import com.belajar.api.kotlin.validation.ValidationUser
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Service
import java.util.*
import com.belajar.api.kotlin.utils.AuthUtil


@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
    val validationUser: ValidationUser,
): UserService {

    val authUtil = AuthUtil()

    override fun create(createUserRequest: CreateUserRequest): UserResponse {

        validationUser.validate(createUserRequest)

        val user = User()

        user.name = createUserRequest.name!!
        user.email = createUserRequest.email!!
        user.password = createUserRequest.password!!
        user.createdAt = Date()

        try {
            userRepository.save(user)

            return UserResponse(
                id = user.id,
                name = user.name,
                createdAt = user.createdAt!!,
                updatedAt = user.updatedAt
            )
        } catch (e: Exception) {
            throw RuntimeException("Failed to create user: ${e.message}")
        }

    }

    override fun authenticate(authUserRequest: AuthUserRequest, response: HttpServletResponse) {

        validationUser.validate(authUserRequest)

        val user = userRepository.getUserByEmail(authUserRequest.email)
            ?: throw NotFoundException("User Not Found")

        val jwt = authUtil.generateJwt(user.id!!)

        val cookie = Cookie("jwt", jwt)
        cookie.isHttpOnly = true

        response.addCookie(cookie)

    }

    override fun get(jwt: String?): UserResponse {

        val userId = authUtil.getUserIdFromJwt(jwt)

        try {
            val user = userRepository.getReferenceById(userId)

            return UserResponse(
                id = user.id,
                name = user.name,
                createdAt = user.createdAt,
                updatedAt = user.updatedAt
            )
        } catch (e: Exception) {
            throw RuntimeException("Failed to retrieve user: ${e.message}")
        }

    }

    override fun update(jwt: String?, updateUserRequest: UpdateUserRequest): UserResponse {
        val userId = authUtil.getUserIdFromJwt(jwt)
        validationUser.validate(updateUserRequest)
        val user = userRepository.getReferenceById(userId)

        if (!updateUserRequest.email.isNullOrBlank()) {
            if (updateUserRequest.email != user.email) {
                if (userRepository.existsByEmail(updateUserRequest.email)) {
                    throw ValidationCustomException("Email is already in use", "email")
                }
                user.email = updateUserRequest.email
            }
        }

        user.apply {
            name = updateUserRequest.name
            password = updateUserRequest.password!!
            updatedAt = Date()
        }

        try {
            userRepository.save(user)

            return UserResponse(
                id = user.id,
                name = user.name,
                createdAt = user.createdAt!!,
                updatedAt = user.updatedAt
            )
        } catch (e: Exception) {
            throw RuntimeException("Failed to updated user: ${e.message}")
        }

    }

    override fun unauthenticate(jwt: String?, response: HttpServletResponse) {
        authUtil.getUserIdFromJwt(jwt)

        authUtil.clearJwtCookie(response)
    }

}