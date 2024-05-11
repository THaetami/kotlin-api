package com.belajar.api.kotlin.service

import com.belajar.api.kotlin.constant.UserRoleEnum
import com.belajar.api.kotlin.model.UserRole

interface UserRoleService {
    fun saveOrGet(userRoleEnum: UserRoleEnum) : UserRole
}