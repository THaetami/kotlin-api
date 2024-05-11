package com.belajar.api.kotlin.service.impl

import com.belajar.api.kotlin.constant.UserRoleEnum
import com.belajar.api.kotlin.entities.user.LoginRequest
import com.belajar.api.kotlin.entities.user.LoginResponse
import com.belajar.api.kotlin.entities.user.RegisterRequest
import com.belajar.api.kotlin.entities.user.RegisterResponse
import com.belajar.api.kotlin.exception.NotFoundException
import com.belajar.api.kotlin.model.UserAccount
import com.belajar.api.kotlin.model.UserRole
import com.belajar.api.kotlin.repository.UserAccountRepository
import com.belajar.api.kotlin.service.AuthService
import com.belajar.api.kotlin.service.JwtService
import com.belajar.api.kotlin.service.UserRoleService
import com.belajar.api.kotlin.validation.ValidationUtil
import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AuthServiceImpl(
    private val userAccountRepository: UserAccountRepository,
    private val userRoleService: UserRoleService,
    private val passwordEncoder: PasswordEncoder,
    private val authenticationManager: AuthenticationManager,
    private val jwtService: JwtService,
    val validationUtil: ValidationUtil
): AuthService {

    @Value("\${template_api.super-admin.name}")
    private lateinit var superAdminName: String

    @Value("\${template_api.super-admin.email}")
    private lateinit var superAdminEmail: String

    @Value("\${template_api.super-admin.username}")
    private lateinit var superAdminUsername: String

    @Value("\${template_api.super-admin.password}")
    private lateinit var superAdminPassword: String

    @Transactional(rollbackFor = [Exception::class])
    @PostConstruct
    fun initSuperAdmin() {
        val account = userAccountRepository.findByUsername(superAdminUsername)
        if (account.isPresent) return

        val superAdmin = userRoleService.saveOrGet(UserRoleEnum.ROLE_SUPER_ADMIN)
        val admin = userRoleService.saveOrGet(UserRoleEnum.ROLE_ADMIN)
        val user = userRoleService.saveOrGet(UserRoleEnum.ROLE_USER)

        userAccountRepository.saveAndFlush(UserAccount(
            name = superAdminName,
            email = superAdminEmail,
            username = superAdminUsername,
            password = passwordEncoder.encode(superAdminPassword),
            roles = listOf(superAdmin, admin, user),
            isEnable = true
        ))
    }

    @Transactional(rollbackFor = [Exception::class])
    override fun registerUser(request: RegisterRequest): RegisterResponse {
        validationUtil.validate(request)
        val userRole = userRoleService.saveOrGet(UserRoleEnum.ROLE_USER)
        return getRegisterResponse(request, listOf(userRole))
    }

    @Transactional(rollbackFor = [Exception::class])
    override fun login(request: LoginRequest): LoginResponse {
        validationUtil.validate(request)

        val authentication = UsernamePasswordAuthenticationToken(request.username, request.password)
        val authenticate = authenticationManager.authenticate(authentication)
        SecurityContextHolder.getContext().authentication = authenticate
        val userAccount = authenticate.principal as UserAccount

        val token = jwtService.generateToken(userAccount)
        val roles = userAccount.authorities.map { it.authority }
        return LoginResponse(
            username = userAccount.username,
            token = token,
            roles = roles
        )
    }

    @Transactional(rollbackFor = [Exception::class])
    override fun registerAdmin(request: RegisterRequest): RegisterResponse {
        validationUtil.validate(request)
        val userRole = userRoleService.saveOrGet(UserRoleEnum.ROLE_USER)
        val adminRole = userRoleService.saveOrGet(UserRoleEnum.ROLE_ADMIN)
        return getRegisterResponse(request, listOf(userRole, adminRole))
    }

    private fun getRegisterResponse(request: RegisterRequest, userRoles: List<UserRole>): RegisterResponse {
        val hashedPassword = passwordEncoder.encode(request.password)

        val userAccount = userAccountRepository.saveAndFlush(
            UserAccount(
                name = request.name!!,
                email = request.email!!,
                username = request.username!!,
                password = hashedPassword,
                roles = userRoles,
                isEnable = true
            )
        )

        val roles: List<String> = userAccount.roles.map { role ->
            role.role?.name ?: "Unknown"
        }

        return RegisterResponse(
            username = userAccount.username,
            roles = roles
        )
    }


}