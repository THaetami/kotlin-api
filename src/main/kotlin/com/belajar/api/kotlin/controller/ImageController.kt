package com.belajar.api.kotlin.controller

import com.belajar.api.kotlin.constant.ApiUrl
import com.belajar.api.kotlin.service.ImageService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.core.io.Resource
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.nio.file.Files

@RestController
@RequestMapping(ApiUrl.API_URL + ApiUrl.MENU_URL)
@Tag(name = "Image", description = "Image API")
class ImageController(
    private val imageService: ImageService
) {

    @Operation(summary = "Get image by id")
    @GetMapping(
        path = ["/{id}/images"],
    )
    fun getById(@PathVariable id: String): ResponseEntity<Resource> {
        val image: Resource = imageService.getById(id)
        val contentType = Files.probeContentType(image.file.toPath()) ?: MediaType.APPLICATION_OCTET_STREAM_VALUE
        val headerValue = "attachment; filename=${image.filename}"
        return ResponseEntity.status(HttpStatus.OK)
            .header(HttpHeaders.CONTENT_DISPOSITION, headerValue)
            .header(HttpHeaders.CONTENT_TYPE, contentType)
            .body(image)
    }

}