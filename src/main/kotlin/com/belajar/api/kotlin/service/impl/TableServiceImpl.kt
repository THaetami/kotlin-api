package com.belajar.api.kotlin.service.impl

import com.belajar.api.kotlin.CustomerSpecification
import com.belajar.api.kotlin.constant.StatusMessage
import com.belajar.api.kotlin.entities.customer.*
import com.belajar.api.kotlin.entities.table.TableRequest
import com.belajar.api.kotlin.entities.table.TableResponse
import com.belajar.api.kotlin.exception.BadRequestException
import com.belajar.api.kotlin.exception.NotFoundException
import com.belajar.api.kotlin.exception.ValidationCustomException
import com.belajar.api.kotlin.model.Customer
import com.belajar.api.kotlin.model.TableRest
import com.belajar.api.kotlin.repository.CustomerRepository
import com.belajar.api.kotlin.repository.TableRepository
import com.belajar.api.kotlin.service.CustomerService
import com.belajar.api.kotlin.service.TableService
import com.belajar.api.kotlin.validation.ValidationUtil
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException

@Service
@Transactional(rollbackFor = [Exception::class])
class TableServiceImpl(
    private val validationUtil: ValidationUtil,
    private val tableRepository: TableRepository
): TableService {

    @Transactional(rollbackFor = [Exception::class])
    override fun save(request: TableRequest): TableResponse {
        val table = tableRepository.saveAndFlush(
            TableRest(
                name = request.name,
            )
        )
        return createTableResponse(table)
    }

    @Transactional(rollbackFor = [Exception::class])
    override fun getById(id: String): TableResponse {
        val table = getTableById(id)
        return createTableResponse(table)
    }

    @Transactional(rollbackFor = [Exception::class])
    override fun getAll(): List<TableResponse> {
        val tables = tableRepository.findAll()
        return tables.map { table ->
            createTableResponse(table)
        }
    }

    @Transactional(rollbackFor = [Exception::class])
    override fun update(request: TableRequest, id: String): TableResponse {
        validationUtil.validate(request)
        val table = getTableById(id)
        table.name = request.name
        tableRepository.saveAndFlush(table)
        return createTableResponse(table)
    }

    @Transactional(rollbackFor = [Exception::class])
    override fun delete(id: String): String {
        val table = getTableById(id)
        tableRepository.softDelete(table.id.toString())
        return StatusMessage.SUCCESS_DELETE
    }

    private fun createTableResponse(table: TableRest): TableResponse {
        return TableResponse(
            id = table.id!!,
            name = table.name!!,
        )
    }

    private fun getTableById(id: String): TableRest {
        return tableRepository.findById(id).orElseThrow {
            throw NotFoundException(StatusMessage.TABLE_NOT_FOUND)
        }
    }

}