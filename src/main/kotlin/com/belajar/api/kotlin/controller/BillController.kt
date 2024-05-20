package com.belajar.api.kotlin.controller

import com.belajar.api.kotlin.constant.ApiUrl
import com.belajar.api.kotlin.constant.StatusMessage
import com.belajar.api.kotlin.entities.PaginationResponse
import com.belajar.api.kotlin.entities.WebResponse
import com.belajar.api.kotlin.entities.bill.BillRequest
import com.belajar.api.kotlin.entities.bill.BillResponse
import com.belajar.api.kotlin.entities.bill.SearchBillRequest
import com.belajar.api.kotlin.entities.bill.UpdateBillRequest
import com.belajar.api.kotlin.service.BillService
import com.belajar.api.kotlin.service.PdfService
import com.belajar.api.kotlin.service.impl.PdfServiceImpl
import com.belajar.api.kotlin.utils.Utilities
import com.fasterxml.jackson.annotation.JsonFormat
import com.lowagie.text.DocumentException
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.io.IOException

@RestController
@RequestMapping(ApiUrl.API_URL + ApiUrl.BILL_URL)
@Tag(name = "Bill", description = "Bill API")
class BillController(
    private val billService: BillService,
    private val utilities: Utilities,
) {
    private lateinit var billResponseList: List<BillResponse>

    @Operation(summary = "Super admin, Admin and User create new bill")
    @SecurityRequirement(name = "Authorization")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'USER')")
    @PostMapping(
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun save(@RequestBody request: BillRequest): ResponseEntity<WebResponse<BillResponse>> =
        utilities.handleRequest({ billService.save(request) }, HttpStatus.CREATED, StatusMessage.SUCCESS_CREATE)

    @Operation(summary = "Super admin and Admin get bill by id")
    @SecurityRequirement(name = "Authorization")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    @GetMapping(
        path = ["/{id}"],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun getById(@PathVariable id: String): ResponseEntity<WebResponse<BillResponse>> =
        utilities.handleRequest({ billService.getById(id) }, HttpStatus.OK, StatusMessage.SUCCESS_RETRIEVE)

    @Operation(summary = "Super admin and Admin get all bill")
    @SecurityRequirement(name = "Authorization")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    @GetMapping(
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun getAll(
        @RequestParam(name = "daily", required = false) @JsonFormat(pattern = "yyyy-MM-dd") daily: String,
        @RequestParam(name = "weekStart", required = false) @JsonFormat(pattern = "yyyy-MM-dd") weeklyStart: String,
        @RequestParam(name = "weekEnd", required = false) @JsonFormat(pattern = "yyyy-MM-dd") weeklyEnd: String,
        @RequestParam(name = "monthly", required = false) @JsonFormat(pattern = "yyyy-MM") monthly: String,
        @RequestParam(name = "direction", defaultValue = "asc") direction: String,
        @RequestParam(name = "sortBy", defaultValue = "transDate") sortBy: String,
        @RequestParam(name = "page", defaultValue = "1") page: Int,
        @RequestParam(name = "size", defaultValue = "10") size: Int,
    ): ResponseEntity<WebResponse<List<BillResponse>>> {
        val request = SearchBillRequest(
            daily,
            weeklyStart,
            weeklyEnd,
            monthly,
            direction,
            sortBy,
            page,
            size,
        )
        val billResponse = billService.getAll(request)
        billResponseList = billResponse.content
        val paginationResponse = PaginationResponse(
            totalPages = billResponse.totalPages,
            totalElement = billResponse.totalElements,
            page = billResponse.number + 1,
            size = billResponse.size,
            hasNext = billResponse.hasNext(),
            hasPrevious = billResponse.hasPrevious()
        )
        val response = WebResponse(
            code = HttpStatus.OK.value(),
            status = StatusMessage.SUCCESS_RETRIEVE_LIST,
            data = billResponse.content,
            paginationResponse = paginationResponse
        )
        return ResponseEntity.status(HttpStatus.OK).body(response)
    }

    @Operation(summary = "Super admin and Admin export bill to pdf")
    @SecurityRequirement(name = "Authorization")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    @GetMapping(path = ["/export/pdf"])
    @Throws(DocumentException::class, IOException::class)
    fun exportBillPdf(response: HttpServletResponse) {
        response.contentType = "application/pdf"
        val headerValue = "attachment; filename=bills.pdf"
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, headerValue)
        val pdfService: PdfService = PdfServiceImpl(billResponseList)
        pdfService.export(response)
    }

    @Operation(summary = "Super admin and Admin update status bill")
    @SecurityRequirement(name = "Authorization")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'USER')")
    @PostMapping(
        path = ["/status/{id}"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun updateStatus(@RequestBody request: UpdateBillRequest, @PathVariable id: String): ResponseEntity<WebResponse<String>> =
        utilities.handleRequest({ billService.updateStatusPayment(request, id) }, HttpStatus.OK, StatusMessage.SUCCESS)


}