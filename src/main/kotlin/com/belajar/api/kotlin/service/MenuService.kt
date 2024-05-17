package com.belajar.api.kotlin.service

import com.belajar.api.kotlin.entities.menu.MenuRequest
import com.belajar.api.kotlin.entities.menu.MenuResponse
import com.belajar.api.kotlin.entities.menu.SearchMenuRequest
import org.springframework.data.domain.Page
import org.springframework.web.multipart.MultipartFile

interface MenuService {
    fun save(request: MenuRequest, image: MultipartFile): MenuResponse
    fun getById(id: String): MenuResponse
    fun getAll(request: SearchMenuRequest): Page<MenuResponse>
    fun delete(id: String): String
}