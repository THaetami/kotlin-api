package com.belajar.api.kotlin.controller

import com.belajar.api.kotlin.constant.ApiUrl
import com.belajar.api.kotlin.constant.StatusMessage
import com.belajar.api.kotlin.entities.WebResponse
import com.belajar.api.kotlin.entities.bill.BillRequest
import com.belajar.api.kotlin.entities.bill.BillResponse
import com.belajar.api.kotlin.service.BillService
import com.belajar.api.kotlin.utils.Utilities
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(ApiUrl.API_URL + ApiUrl.BILL_URL)
class BillController(
    private val billService: BillService,
    private val utilities: Utilities
) {

    @Operation(summary = "Super admin, Admin and User create new bill")
    @SecurityRequirement(name = "Authorization")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'USER')")
    @PostMapping(
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun save(@RequestBody request: BillRequest): ResponseEntity<WebResponse<BillResponse>> =
        utilities.handleRequest({ billService.save(request) }, HttpStatus.CREATED, StatusMessage.SUCCESS_CREATE)

}