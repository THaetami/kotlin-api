package com.belajar.api.kotlin.controller

import com.belajar.api.kotlin.entities.user.AuthUserRequest
import com.belajar.api.kotlin.entities.user.CreateUserRequest
import com.belajar.api.kotlin.entities.user.UpdateUserRequest
import com.belajar.api.kotlin.model.User
import com.belajar.api.kotlin.repository.UserRepository
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.Cookie
import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.transaction.annotation.Transactional
import java.util.*


@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(value = MethodOrderer.OrderAnnotation::class)
class UserControllerTest {

    @Autowired private lateinit var mockMvc: MockMvc

    @Autowired private lateinit var objectMapper: ObjectMapper

    @Autowired private lateinit var userRepository: UserRepository

    private lateinit var mockUser: User

    val urlUser = "/api/user"
    val urlAuth = "/api/auth"
    val contentType = MediaType.APPLICATION_JSON
    val name = "test name"
    val email = "testmail@gmail.com"
    val password = "123pas"
    val nameUpdate = "name update"
    val emailUpdate = "update@gmail.com"
    val passwordUpdate = "123upt"

    private fun createUser(): User {
        return User().apply {
            name = "old name"
            email = "emailexists@gmail.com"
            password = "123pas"
            createdAt = Date()
        }
    }

    @BeforeAll
    @Transactional
    fun setUp() {
        mockUser = createUser()
        userRepository.save(mockUser)
    }

    @AfterAll
    @Transactional
    fun deleteUser() = userRepository.deleteAll()

    private fun performPostUserRequest(request: CreateUserRequest) = mockMvc.perform( MockMvcRequestBuilders.post(urlUser)
        .contentType(contentType)
        .content(objectMapper.writeValueAsString(request)))

    private fun performUpdateUserRequest(request: UpdateUserRequest, jwt: Cookie?) = mockMvc.perform( MockMvcRequestBuilders.put(urlUser)
        .cookie(jwt)
        .contentType(contentType)
        .content(objectMapper.writeValueAsString(request)))

    private fun performPostAuthRequest(request: AuthUserRequest) = mockMvc.perform( MockMvcRequestBuilders.post(urlAuth)
        .contentType(contentType)
        .content(objectMapper.writeValueAsString(request)))

    @Test
    @Order(1)
    fun `Should response 200 and new user`() {
        val request = CreateUserRequest(name, email, password)

        performPostUserRequest(request)
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.code").value(200))
            .andExpect(jsonPath("$.status").value("OK"))
            .andExpect(jsonPath("$.data.id").exists())
            .andExpect(jsonPath("$.data.name").value(request.name))
            .andExpect(jsonPath("$.data.createdAt").exists())
            .andExpect(jsonPath("$.data.updatedAt").isEmpty)
    }

    @Test
    @Order(2)
    fun `Should response 422 when create user payload is unprocessable`() {
        val request = CreateUserRequest("", "invalid_email", "&* 2")

        performPostUserRequest(request)
            .andExpect(status().isUnprocessableEntity)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.code").value(422))
            .andExpect(jsonPath("$.status").value("UNPROCESSABLE_ENTITY"))
            .andExpect(jsonPath("$.data[*].path").exists())
            .andExpect(jsonPath("$.data[*].message").exists())
    }

    @Test
    @Order(3)
    fun `Should response 422 when create user with email exists`() {
        val request = CreateUserRequest(name, email, password)

        performPostUserRequest(request)
            .andExpect(status().isUnprocessableEntity)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.code").value(422))
            .andExpect(jsonPath("$.status").value("UNPROCESSABLE_ENTITY"))
            .andExpect(jsonPath("$.data[0].path").value("email"))
            .andExpect(jsonPath("$.data[0].message").value("Email has already been taken"))
    }

    @Test
    @Order(4)
    fun `Should response 200 with user details`() {
        val request = AuthUserRequest(email, password)

        val response = performPostAuthRequest(request)
            .andReturn().response as MockHttpServletResponse

        val jwtToken = response.getCookie("jwt")

        mockMvc.perform(get(urlUser).cookie(jwtToken))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.code").value(200))
            .andExpect(jsonPath("$.status").value("OK"))
            .andExpect(jsonPath("$.data.id").exists())
            .andExpect(jsonPath("$.data.name").exists())
            .andExpect(jsonPath("$.data.createdAt").exists())
            .andExpect(jsonPath("$.data.updatedAt").isEmpty)
    }

    @Test
    @Order(5)
    fun `Should response 401 when get user with invalid jwt token`() {
        val cookie = Cookie("jwt", "invalid_jwt")

        mockMvc.perform(get(urlUser).cookie(cookie))
            .andExpect(status().isUnauthorized)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.code").value(401))
            .andExpect(jsonPath("$.status").value("UNAUTHORIZED"))
            .andExpect(jsonPath("$.data").value("Invalid JWT token"))
    }

    @Test
    @Order(6)
    fun `Should response 401 when get user without jwt token`() {

        mockMvc.perform(get(urlUser))
            .andExpect(status().isUnauthorized)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.code").value(401))
            .andExpect(jsonPath("$.status").value("UNAUTHORIZED"))
            .andExpect(jsonPath("$.data").value("JWT token is null"))
    }

    @Test
    @Order(7)
    fun `Should response 200 when update user success`() {
        val authRequest = AuthUserRequest(email, password)
        val updateUserRequest = UpdateUserRequest(nameUpdate, emailUpdate, passwordUpdate)

        val response = performPostAuthRequest(authRequest)
            .andReturn().response as MockHttpServletResponse

        val jwtToken = response.getCookie("jwt")

        performUpdateUserRequest(updateUserRequest, jwtToken)
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.code").value(200))
            .andExpect(jsonPath("$.status").value("OK"))
            .andExpect(jsonPath("$.data.id").exists())
            .andExpect(jsonPath("$.data.name").value(updateUserRequest.name))
            .andExpect(jsonPath("$.data.createdAt").exists())
            .andExpect(jsonPath("$.data.updatedAt").exists())
    }

    @Test
    @Order(8)
    fun `Should respond with 200 when updating a user successfully without changing the email and password`() {
        val authRequest = AuthUserRequest(emailUpdate, passwordUpdate)
        val updateUserRequest = UpdateUserRequest("name change", "", "")

        val response = performPostAuthRequest(authRequest)
            .andReturn().response as MockHttpServletResponse

        val jwtToken = response.getCookie("jwt")

        performUpdateUserRequest(updateUserRequest, jwtToken)
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.code").value(200))
            .andExpect(jsonPath("$.status").value("OK"))
            .andExpect(jsonPath("$.data.id").exists())
            .andExpect(jsonPath("$.data.name").value(updateUserRequest.name))
            .andExpect(jsonPath("$.data.createdAt").exists())
            .andExpect(jsonPath("$.data.updatedAt").exists())
    }

    @Test
    @Order(9)
    fun `Should response 422 when update user payload is unprocessable`() {
        val authRequest = AuthUserRequest(emailUpdate, passwordUpdate)
        val updateUserRequest = UpdateUserRequest("", "invalid_email", "&^$ *1")

        val response = performPostAuthRequest(authRequest)
            .andReturn().response as MockHttpServletResponse

        val jwtToken = response.getCookie("jwt")

        performUpdateUserRequest(updateUserRequest, jwtToken)
            .andExpect(status().isUnprocessableEntity)
            .andExpect(jsonPath("$.code").value(422))
            .andExpect(jsonPath("$.status").value("UNPROCESSABLE_ENTITY"))
            .andExpect(jsonPath("$.data[*].path").exists())
            .andExpect(jsonPath("$.data[*].message").exists())
    }

    @Test
    @Order(10)
    fun `Should response 422 when update user with email exists`() {
        val authRequest = AuthUserRequest(emailUpdate, passwordUpdate)
        val updateUserRequest = UpdateUserRequest(name, "emailexists@gmail.com", password)

        val response = performPostAuthRequest(authRequest)
            .andReturn().response as MockHttpServletResponse

        val jwtToken = response.getCookie("jwt")

        performUpdateUserRequest(updateUserRequest, jwtToken)
            .andExpect(status().isUnprocessableEntity)
            .andExpect(jsonPath("$.code").value(422))
            .andExpect(jsonPath("$.status").value("UNPROCESSABLE_ENTITY"))
            .andExpect(jsonPath("$.data[0].path").value("email"))
            .andExpect(jsonPath("$.data[0].message").value("Email has already been taken"))
    }

    @Test
    @Order(11)
    fun `Should response 401 when update user with invalid jwt token`() {
        val cookie = Cookie("jwt", "invalid_jwt")
        val updateUserRequest = UpdateUserRequest(name, email, password)

        performUpdateUserRequest(updateUserRequest, cookie)
            .andExpect(status().isUnauthorized)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.code").value(401))
            .andExpect(jsonPath("$.status").value("UNAUTHORIZED"))
            .andExpect(jsonPath("$.data").value("Invalid JWT token"))
    }

    @Test
    @Order(12)
    fun `Should response 401 when update user without jwt token`() {
        val updateUserRequest = UpdateUserRequest(name, email, password)

        mockMvc.perform( MockMvcRequestBuilders.put(urlUser)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(updateUserRequest)))
            .andExpect(status().isUnauthorized)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.code").value(401))
            .andExpect(jsonPath("$.status").value("UNAUTHORIZED"))
            .andExpect(jsonPath("$.data").value("JWT token is null"))
    }

}