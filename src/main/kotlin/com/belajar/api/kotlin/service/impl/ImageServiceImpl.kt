package com.belajar.api.kotlin.service.impl

import com.belajar.api.kotlin.constant.StatusMessage
import com.belajar.api.kotlin.exception.BadRequestException
import com.belajar.api.kotlin.exception.NotFoundException
import com.belajar.api.kotlin.model.Image
import com.belajar.api.kotlin.repository.ImageRepository
import com.belajar.api.kotlin.service.ImageService
import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

@Service
class ImageServiceImpl(
    private val imageRepository: ImageRepository,
    @Value("\${template_api.image.path}") private val path: String
): ImageService {

    private lateinit var imagePath: Path

    @PostConstruct
    fun initPath() {
        imagePath = Paths.get(path)
        if (!Files.exists(imagePath)) {
            try {
                Files.createDirectories(imagePath)
            } catch (e: IOException) {
                val errorMessage = "Failed to create image directory: $path"
                println(errorMessage)
            }
        }
    }

    @Transactional(rollbackFor = [Exception::class])
    override fun save(image: MultipartFile): Image {
        if (!listOf("image/jpg", "image/jpeg", "image/png", "image/svg+xml").contains(image.contentType))
            throw BadRequestException("invalid image type")
        val fileName = "${System.currentTimeMillis()}${image.originalFilename}"
        val filePath: Path = imagePath.resolve(fileName)
        Files.copy(image.inputStream, filePath)

        val saved = Image(
            name = fileName,
            path = filePath.toString(),
            size = image.size,
            contentType = image.contentType!!
        )
        return imageRepository.saveAndFlush(saved)
    }

    @Transactional(rollbackFor = [Exception::class])
    override fun softDeleteById(id: String) {
        val image = findById(id)
        imageRepository.softDelete(image.id!!)
    }

    private fun findById(id: String): Image {
        return imageRepository.findById(id).orElseThrow {
            throw NotFoundException(StatusMessage.IMAGE_NOT_FOUND)
        }
    }
}