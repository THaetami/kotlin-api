package com.belajar.api.kotlin.service

import com.belajar.api.kotlin.entities.bill.BillRequest
import com.belajar.api.kotlin.entities.bill.BillResponse
import com.belajar.api.kotlin.entities.bill.SearchBillRequest
import com.belajar.api.kotlin.entities.bill.UpdateBillRequest
import org.springframework.data.domain.Page


interface BillService {
    fun save(request: BillRequest): BillResponse
    fun getById(id: String): BillResponse
    fun getAll(request: SearchBillRequest): Page<BillResponse>
    fun updateStatusPayment(request: UpdateBillRequest, id: String): String
}