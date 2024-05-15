package com.belajar.api.kotlin.service

import com.belajar.api.kotlin.entities.customer.*
import com.belajar.api.kotlin.entities.table.TableRequest
import com.belajar.api.kotlin.entities.table.TableResponse
import com.belajar.api.kotlin.model.Customer
import org.springframework.data.domain.Page


interface TableService {
    fun save(request: TableRequest): TableResponse
    fun getById(id: String): TableResponse
    fun getAll(): List<TableResponse>
    fun update(request: TableRequest, id: String): TableResponse
    fun delete(id: String): String
}