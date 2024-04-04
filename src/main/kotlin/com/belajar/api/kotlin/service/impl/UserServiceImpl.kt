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


@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
    val validationUtil: ValidationUtil,
): UserService {

    val authUtil = AuthUtil()

    override fun create(createUserRequest: CreateUserRequest): UserResponse {
        validationUtil.validate(createUserRequest)

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
        validationUtil.validate(updateUserRequest)
        val user = userRepository.getReferenceById(userId)

        if (!updateUserRequest.email.isNullOrBlank()) {
            if (updateUserRequest.email != user.email) {
                if (userRepository.existsByEmail(updateUserRequest.email)) {
                    throw ValidationCustomException("Email has already been taken", "email")
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

}