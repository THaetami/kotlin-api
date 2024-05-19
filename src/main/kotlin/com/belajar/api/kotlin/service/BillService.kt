package com.belajar.api.kotlin.service

import com.belajar.api.kotlin.entities.bill.BillRequest
import com.belajar.api.kotlin.entities.bill.BillResponse

interface BillService {
    fun save(request: BillRequest): BillResponse
}