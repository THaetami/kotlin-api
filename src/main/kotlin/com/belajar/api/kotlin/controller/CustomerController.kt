package com.belajar.api.kotlin.controller

import com.belajar.api.kotlin.constant.ApiUrl
import com.belajar.api.kotlin.constant.StatusMessage
import com.belajar.api.kotlin.entities.PaginationResponse
import com.belajar.api.kotlin.entities.WebResponse
import com.belajar.api.kotlin.entities.customer.CustomerRequest
import com.belajar.api.kotlin.entities.customer.CustomerResponse
import com.belajar.api.kotlin.entities.customer.SearchCustomerRequest
import com.belajar.api.kotlin.entities.customer.UpdateCustomerRequest
import com.belajar.api.kotlin.service.CustomerService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(ApiUrl.API_URL + ApiUrl.CUSTOMER_URL)
class CustomerController(
    val customerService: CustomerService
) {

    @Operation(summary = "Super admin and Admin create new customer")
    @SecurityRequirement(name = "Authorization")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    @PostMapping(
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun save(@RequestBody request: CustomerRequest): ResponseEntity<WebResponse<CustomerResponse>> {
        val customerResponse = customerService.saveOrGet(request)
        val response = WebResponse(
            code = HttpStatus.CREATED.value(),
            status = "Customer successfully created",
            data = customerResponse,
            paginationResponse = null
        )
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }

    @Operation(summary = "Super admin and Admin create new list of customer")
    @SecurityRequirement(name = "Authorization")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    @PostMapping(
        path = ["/bulk"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun saveBulk(@RequestBody request: List<CustomerRequest>): ResponseEntity<WebResponse<List<CustomerResponse>>> {
        val customers = customerService.saveBulk(request)
        val response = WebResponse(
            code = HttpStatus.CREATED.value(),
            status = "List of customer successfully created",
            data = customers,
            paginationResponse = null,
        )
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }

    @Operation(summary = "Super admin and Admin get customer by id")
    @SecurityRequirement(name = "Authorization")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN')")
    @GetMapping(
        path = ["/{id}"],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun getById(@PathVariable id: String): ResponseEntity<WebResponse<CustomerResponse>> {
        val customer = customerService.getById(id)
        val response = WebResponse(
            code = HttpStatus.OK.value(),
            status = "Customer succesfully retrieved",
            data = customer,
            paginationResponse = null
        )
        return ResponseEntity.status(HttpStatus.OK).body(response)
    }

    @Operation(summary = "Super admin and Admin get all customer")
    @SecurityRequirement(name = "Authorization")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getAll(
        @RequestParam(name = "name", required = true) name: String,
        @RequestParam(name = "direction", defaultValue = "asc") direction: String,
        @RequestParam(name = "sortBy", defaultValue = "name") sortBy: String,
        @RequestParam(name = "page", defaultValue = "1") page: Int,
        @RequestParam(name = "size", defaultValue = "10") size: Int
    ): ResponseEntity<WebResponse<List<CustomerResponse>>> {
        val request = SearchCustomerRequest(
            name = name,
            direction = direction,
            sortBy = sortBy,
            page = page,
            size = size,
        )
        val customerResponse = customerService.getAll(request)
        val paginationResponse = PaginationResponse(
            totalPages = customerResponse.totalPages,
            totalElement = customerResponse.totalElements,
            page = customerResponse.number + 1,
            size = customerResponse.size,
            hasNext = customerResponse.hasNext(),
            hasPrevious = customerResponse.hasPrevious()
        )
        val response = WebResponse(
            code = HttpStatus.OK.value(),
            status = "List of customer successfully retrieved",
            data = customerResponse.content,
            paginationResponse = paginationResponse
        )
        return ResponseEntity.status(HttpStatus.OK).body(response)

    }

    @Operation(summary = "Super admin and Admin update customer")
    @SecurityRequirement(name = "Authorization")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    @PutMapping(
        produces = [MediaType.APPLICATION_JSON_VALUE],
        consumes = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun update(@RequestBody request: UpdateCustomerRequest): ResponseEntity<WebResponse<CustomerResponse>> {
        val customer = customerService.update(request)
        val response = WebResponse(
            code = HttpStatus.OK.value(),
            status = "Customer updated",
            data = customer,
            paginationResponse = null
        )
        return ResponseEntity.status(HttpStatus.OK).body(response)
    }

    @Operation(summary = "Super admin and Admin delete customer")
    @SecurityRequirement(name = "Authorization")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    @DeleteMapping(
        path = ["/{id}"]
    )
    fun delete(@PathVariable id: String): ResponseEntity<WebResponse<String>> {
        customerService.delete(id)
        val response = WebResponse(
            code = HttpStatus.OK.value(),
            status = "Customer updated",
            data = "Customer deleted",
            paginationResponse = null
        )
        return ResponseEntity.status(HttpStatus.OK).body(response)
    }


}