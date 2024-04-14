package com.belajar.api.kotlin.service.impl

import com.belajar.api.kotlin.entities.user.CreateUserRequest
import com.belajar.api.kotlin.entities.user.UpdateUserRequest
import com.belajar.api.kotlin.entities.user.UserResponse
import com.belajar.api.kotlin.exception.ValidationCustomException
import com.belajar.api.kotlin.model.User
import com.belajar.api.kotlin.repository.UserRepository
import com.belajar.api.kotlin.service.UserService
import com.belajar.api.kotlin.validation.ValidationUtil
import org.springframework.stereotype.Service
import java.util.*
import com.belajar.api.kotlin.utils.AuthUtil
import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException


@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
    val validationUtil: ValidationUtil,
    val authUtil: AuthUtil
): UserService {

    override fun create(createUserRequest: CreateUserRequest): UserResponse {
        validationUtil.validate(createUserRequest)
        val user = createUserFromRequest(createUserRequest)

        try {
            userRepository.save(user)
            return createUserResponse(user)
        } catch (e: Exception) {
            throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to create user: ${e.message}")
        }

    }

    override fun get(jwt: String?): UserResponse {
        val userId = authUtil.getUserIdFromJwt(jwt)

        try {
            val user = userRepository.getReferenceById(userId)
            return createUserResponse(user)
        } catch (e: Exception) {
            throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to retrieve user: ${e.message}")
        }
    }

    override fun update(jwt: String?, updateUserRequest: UpdateUserRequest): UserResponse {
        val userId = authUtil.getUserIdFromJwt(jwt)
        validationUtil.validate(updateUserRequest)

        val user = userRepository.getReferenceById(userId)
        updateUserData(updateUserRequest, user)

        try {
            userRepository.save(user)
            return createUserResponse(user)
        } catch (e: Exception) {
            throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to updated user: ${e.message}")
        }
    }

    private fun updateUserData(updateUserRequest: UpdateUserRequest, user: User) {
        user.apply {
            name = updateUserRequest.name
            updatedAt = Date()
        }
        updatePassIfNotNull(updateUserRequest.password, user)
        updateEmailIfChanged(updateUserRequest.email, user)
    }

    private fun updatePassIfNotNull(password: String?, user: User) {
        if (!password.isNullOrBlank()) {
            user.password = password
        }
    }

    private fun updateEmailIfChanged(email: String?, user: User) {
        if (!email.isNullOrBlank() && email != user.email) {
            if (userRepository.existsByEmail(email)) {
                throw ValidationCustomException("Email has already been taken", "email")
            }
            user.email = email
        }
    }

    private fun createUserResponse(user: User): UserResponse {
        return UserResponse(
            id = user.id,
            name = user.name,
            createdAt = user.createdAt!!,
            updatedAt = user.updatedAt
        )
    }

    internal fun createUserFromRequest(createUserRequest: CreateUserRequest): User {
        return User().apply {
            name = createUserRequest.name!!
            email = createUserRequest.email!!
            password = createUserRequest.password!!
            createdAt = Date()
        }
    }

}