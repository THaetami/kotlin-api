package com.belajar.api.kotlin.service

import com.belajar.api.kotlin.entities.table.TableRequest
import com.belajar.api.kotlin.entities.table.TableResponse
import com.belajar.api.kotlin.model.TableRest

interface TableService {
    fun save(request: TableRequest): TableResponse
    fun getById(id: String): TableResponse
    fun getAll(): List<TableResponse>
    fun update(request: TableRequest, id: String): TableResponse
    fun delete(id: String): String
    fun getByName(name: String): TableRest
}