package com.belajar.api.kotlin.service.impl

import com.belajar.api.kotlin.constant.ApiUrl
import com.belajar.api.kotlin.constant.StatusMessage
import com.belajar.api.kotlin.entities.image.ImageResponse
import com.belajar.api.kotlin.entities.menu.MenuRequest
import com.belajar.api.kotlin.entities.menu.MenuResponse
import com.belajar.api.kotlin.entities.menu.SearchMenuRequest
import com.belajar.api.kotlin.exception.BadRequestException
import com.belajar.api.kotlin.exception.NotFoundException
import com.belajar.api.kotlin.model.Menu
import com.belajar.api.kotlin.repository.MenuRepository
import com.belajar.api.kotlin.service.ImageService
import com.belajar.api.kotlin.service.MenuService
import com.belajar.api.kotlin.specification.MenuSpecification
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile

@Service
class MenuServiceImpl(
    private val imageService: ImageService,
    private val menuRepository: MenuRepository,
    private val specification: MenuSpecification

): MenuService {

    @Transactional(rollbackFor = [Exception::class])
    override fun save(request: MenuRequest, image: MultipartFile): MenuResponse {
        if (request.name.isBlank()) {
            throw BadRequestException("name not blank")
        }
        val imageResult = imageService.save(image)

        val menu = menuRepository.saveAndFlush(
            Menu(
                name = request.name,
                price = request.price,
                image = imageResult
            )
        )
        return convertToResponse(menu)
    }

    @Transactional(rollbackFor = [Exception::class])
    override fun getById(id: String): MenuResponse {
        val menu = findById(id)
        return convertToResponse(menu)
    }

    @Transactional(rollbackFor = [Exception::class])
    override fun getAll(request: SearchMenuRequest): Page<MenuResponse> {
        val menuSpecification = specification.specification(request)

        val sort = Sort.by(Sort.Direction.fromString(request.direction), request.sortBy)
        val page = if (request.page <= 0) 1 else request.page
        val pageable = PageRequest.of(page - 1, request.size, sort)

        val menus = menuRepository.findAll(menuSpecification, pageable)
        return menus.map { menu ->
            convertToResponse(menu)
        }
    }

    @Transactional(rollbackFor = [Exception::class])
    override fun delete(id: String): String {
        val menu = findById(id)
        menuRepository.softDelete(menu.id!!)
        imageService.softDeleteById(menu.image?.id!!)
        return StatusMessage.SUCCESS_DELETE
    }

    private fun convertToResponse(menu: Menu): MenuResponse {
        val imageResponse = menu.image?.let {
            ImageResponse(
                name = it.name,
                url = "${ApiUrl.API_URL}${ApiUrl.MENU_URL}/${it.id}/images"
            )
        }
        return MenuResponse(
            id = menu.id!!,
            name = menu.name,
            price = menu.price,
            image = imageResponse
        )
    }

    private fun findById(id: String): Menu {
        return menuRepository.findById(id).orElseThrow {
            throw NotFoundException(StatusMessage.MENU_NOT_FOUND)
        }
    }
}