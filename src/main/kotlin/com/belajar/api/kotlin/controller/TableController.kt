package com.belajar.api.kotlin.controller

import com.belajar.api.kotlin.constant.ApiUrl
import com.belajar.api.kotlin.constant.StatusMessage
import com.belajar.api.kotlin.entities.WebResponse
import com.belajar.api.kotlin.entities.table.TableRequest
import com.belajar.api.kotlin.entities.table.TableResponse
import com.belajar.api.kotlin.service.TableService
import com.belajar.api.kotlin.utils.Utilities
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(ApiUrl.API_URL + ApiUrl.TABLE_URL)
@Tag(name = "Table Rest", description = "Table API")
class TableController(
    private val tableService: TableService,
    private val utilities: Utilities
) {

    @Operation(summary = "Super admin and Admin create new table")
    @SecurityRequirement(name = "Authorization")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun save(@RequestBody request: TableRequest): ResponseEntity<WebResponse<TableResponse>> =
        utilities.handleRequest ({ tableService.save(request) }, HttpStatus.CREATED, StatusMessage.SUCCESS_CREATE)

    @Operation(summary = "Super admin, Admin and User get all table")
    @SecurityRequirement(name = "Authorization")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'USER')")
    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getAll(): ResponseEntity<WebResponse<List<TableResponse>>> =
        utilities.handleRequest ({ tableService.getAll() }, HttpStatus.OK, StatusMessage.SUCCESS_RETRIEVE_LIST)

    @Operation(summary = "Super admin, Admin and User get table by id")
    @SecurityRequirement(name = "Authorization")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN', 'USER')")
    @GetMapping(path = ["/{id}"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getById(@PathVariable id: String): ResponseEntity<WebResponse<TableResponse>> =
        utilities.handleRequest ({ tableService.getById(id) }, HttpStatus.OK, StatusMessage.SUCCESS_RETRIEVE)

    @Operation(summary = "Super admin and Admin update table by id")
    @SecurityRequirement(name = "Authorization")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    @PutMapping(path = ["/{id}"], produces = [MediaType.APPLICATION_JSON_VALUE], consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun update(@RequestBody request: TableRequest, @PathVariable id: String): ResponseEntity<WebResponse<TableResponse>> =
        utilities.handleRequest ({ tableService.update(request, id)}, HttpStatus.OK, StatusMessage.SUCCESS_UPDATE)

    @Operation(summary = "Super admin and Admin delete table")
    @SecurityRequirement(name = "Authorization")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    @DeleteMapping(path = ["/{id}"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun delete(@PathVariable id: String): ResponseEntity<WebResponse<String>> =
        utilities.handleRequest ({ tableService.delete(id) }, HttpStatus.OK, StatusMessage.SUCCESS)

}