package com.belajar.api.kotlin.service

import com.belajar.api.kotlin.entities.customer.*
import com.belajar.api.kotlin.model.Customer
import org.springframework.data.domain.Page


interface CustomerService {
    fun saveAccount(request: NewAccountRequest): Customer
    fun saveOrGet(request: CustomerRequest): CustomerResponse
    fun saveBulk(requests: List<CustomerRequest>): List<CustomerResponse>
    fun getById(id: String): CustomerResponse
    fun getAll(request: SearchCustomerRequest): Page<CustomerResponse>
    fun update(request: UpdateCustomerRequest): CustomerResponse
    fun delete(id: String)
}