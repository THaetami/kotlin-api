package com.belajar.api.kotlin.service.impl

import com.belajar.api.kotlin.constant.UserRoleEnum
import com.belajar.api.kotlin.model.UserRole
import com.belajar.api.kotlin.repository.UserRoleRepository
import com.belajar.api.kotlin.service.UserRoleService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserRoleServiceImp (
    private val userRoleRepository: UserRoleRepository
) : UserRoleService {

    @Transactional(rollbackFor = [Exception::class])
    override fun saveOrGet(userRoleEnum: UserRoleEnum): UserRole {
        return userRoleRepository.findByRole(userRoleEnum).orElseGet {
            userRoleRepository.saveAndFlush(UserRole(role = userRoleEnum))
        }
    }

}