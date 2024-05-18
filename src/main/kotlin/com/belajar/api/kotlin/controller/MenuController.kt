package com.belajar.api.kotlin.controller

import com.belajar.api.kotlin.constant.ApiUrl
import com.belajar.api.kotlin.constant.StatusMessage
import com.belajar.api.kotlin.entities.PaginationResponse
import com.belajar.api.kotlin.entities.WebResponse
import com.belajar.api.kotlin.entities.menu.MenuRequest
import com.belajar.api.kotlin.entities.menu.MenuResponse
import com.belajar.api.kotlin.entities.menu.SearchMenuRequest
import com.belajar.api.kotlin.service.MenuService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile


@RestController
@RequestMapping(ApiUrl.API_URL + ApiUrl.MENU_URL)
@Tag(name = "Menu", description = "Menu API")
class MenuController(
    val menuService: MenuService,
) {

    @Operation(summary = "Super admin, and Admin create new menu")
    @SecurityRequirement(name = "Authorization")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    @PostMapping(
        consumes = [MediaType.MULTIPART_FORM_DATA_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun save(
        @ModelAttribute menuRequest: MenuRequest,
        @RequestParam("image") image: MultipartFile,
    ): ResponseEntity<WebResponse<MenuResponse>> =
        handleRequest({ menuService.save(menuRequest, image) }, HttpStatus.CREATED, StatusMessage.SUCCESS_CREATE)

    @Operation(summary = "Super admin and Admin create new list of menu")
    @SecurityRequirement(name = "Authorization")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    @PostMapping(path = ["/bulk"], consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun saveBulk(@RequestBody requests: List<MenuRequest>): ResponseEntity<WebResponse<List<MenuResponse>>> =
        handleRequest ({ menuService.saveBulk(requests) }, HttpStatus.CREATED, StatusMessage.SUCCESS_CREATE_LIST)

    @Operation(summary = "Super admin, Admin and User get menu by id")
    @SecurityRequirement(name = "Authorization")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'USER')")
    @GetMapping(path = ["/{id}"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getById(
        @PathVariable id: String
    ): ResponseEntity<WebResponse<MenuResponse>> =
        handleRequest({ menuService.getById(id) }, HttpStatus.OK, StatusMessage.SUCCESS_RETRIEVE)

    @Operation(summary = "Super admin, and Admin update menu by id")
    @SecurityRequirement(name = "Authorization")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    @PutMapping(
        path = ["/{id}"],
        consumes = [MediaType.MULTIPART_FORM_DATA_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun updateById(
        @PathVariable id: String,
        @ModelAttribute menuRequest: MenuRequest,
        @RequestParam("image") updateImage: MultipartFile?,
    ): ResponseEntity<WebResponse<MenuResponse>> =
        handleRequest({ menuService.updateById(menuRequest, updateImage, id) }, HttpStatus.OK, StatusMessage.SUCCESS_UPDATE)

    @Operation(summary = "Super admin, Admin and User get all menu")
    @SecurityRequirement(name = "Authorization")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'USER')")
    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getAll(
        @RequestParam(name = "name", required = false) name: String,
        @RequestParam(name = "minPrice", required = false) minPrice: Long,
        @RequestParam(name = "maxPrice", required = false) maxPrice: Long,
        @RequestParam(name = "direction", defaultValue = "asc") direction: String,
        @RequestParam(name = "sortBy", defaultValue = "name") sortBy: String,
        @RequestParam(name = "page", defaultValue = "1") page: Int,
        @RequestParam(name = "size", defaultValue = "10") size: Int
    ): ResponseEntity<WebResponse<List<MenuResponse>>> {
        val request = SearchMenuRequest(
            name = name,
            minPrice = minPrice,
            maxPrice = maxPrice,
            direction = direction,
            sortBy = sortBy,
            page = page,
            size = size,
        )
        val menuResponse = menuService.getAll(request)
        val paginationResponse = PaginationResponse(
            totalPages = menuResponse.totalPages,
            totalElement = menuResponse.totalElements,
            page = menuResponse.number + 1,
            size = menuResponse.size,
            hasNext = menuResponse.hasNext(),
            hasPrevious = menuResponse.hasPrevious()
        )
        val response = WebResponse(
            code = HttpStatus.OK.value(),
            status = StatusMessage.SUCCESS_RETRIEVE_LIST,
            data = menuResponse.content,
            paginationResponse = paginationResponse
        )
        return ResponseEntity.status(HttpStatus.OK).body(response)

    }

    @Operation(summary = "Super admin and Admin delete menu by id")
    @SecurityRequirement(name = "Authorization")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    @DeleteMapping(path = ["/{id}"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun delete(@PathVariable id: String): ResponseEntity<WebResponse<String>> =
        handleRequest ({ menuService.delete(id) }, HttpStatus.OK, StatusMessage.SUCCESS)

    private fun <T> handleRequest(requestHandler: () -> T, status: HttpStatus, message: String): ResponseEntity<WebResponse<T>> {
        val data = requestHandler()
        val response = WebResponse(
            code = status.value(),
            status = message,
            data = data,
            paginationResponse = null
        )
        return ResponseEntity.status(status).body(response)
    }

}