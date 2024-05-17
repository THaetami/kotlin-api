package com.belajar.api.kotlin.service

import com.belajar.api.kotlin.model.Image
import org.springframework.web.multipart.MultipartFile

interface ImageService {
    fun save(image: MultipartFile): Image
    fun softDeleteById(id: String)
}