package com.belajar.api.kotlin.service.impl

import com.belajar.api.kotlin.constant.StatusMessage
import com.belajar.api.kotlin.constant.TransTypeEnum
import com.belajar.api.kotlin.entities.bill.BillRequest
import com.belajar.api.kotlin.entities.bill.BillResponse
import com.belajar.api.kotlin.entities.bill.SearchBillRequest
import com.belajar.api.kotlin.entities.bill.UpdateBillRequest
import com.belajar.api.kotlin.entities.bill_detail.BillDetailResponse
import com.belajar.api.kotlin.entities.payment.PaymentResponse
import com.belajar.api.kotlin.exception.NotFoundException
import com.belajar.api.kotlin.model.Bill
import com.belajar.api.kotlin.model.BillDetail
import com.belajar.api.kotlin.model.Customer
import com.belajar.api.kotlin.model.TransType
import com.belajar.api.kotlin.repository.BillDetailRepository
import com.belajar.api.kotlin.repository.BillRepository
import com.belajar.api.kotlin.service.*
import com.belajar.api.kotlin.specification.BillSpecification
import com.belajar.api.kotlin.validation.ValidationUtil
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.util.Date

@Service
class BillServiceImpl(
    private val validationUtil: ValidationUtil,
    private val customerService: CustomerService,
    private val transTypeService: TransTypeService,
    private val tableService: TableService,
    private val billRepository: BillRepository,
    private val menuService: MenuService,
    private val paymentService: PaymentService,
    private val billDetailRepository: BillDetailRepository,
    private val specification: BillSpecification
): BillService {

    @Transactional(rollbackFor = [Exception::class])
    override fun save(request: BillRequest): BillResponse {
        validationUtil.validate(request)

        val customer: Customer = customerService.getCustomerByNameAndPhone(request.customerName, request.customerPhone)
            ?: customerService.save(request.customerName, request.customerPhone)

        val transType = transTypeService.getById(request.transType)

        val table = tableService.getByName(request.tableName)

        val bill = Bill(
            customer = customer,
            transDate = Date.from(Instant.now()),
            table = table,
            transType = TransType(
                id = TransTypeEnum.valueOf(transType.id),
                description = transType.description
            )
        )
        billRepository.saveAndFlush(bill)

        val billDetails = request.billRequest.map { billDetailRequest ->
            validationUtil.validate(billDetailRequest)
            BillDetail(
                bill = bill,
                menu = menuService.getMenuById(billDetailRequest.menuId),
                qty = billDetailRequest.qty,
                price = billDetailRequest.price
            )
        }
        billDetailRepository.saveAll(billDetails)
        bill.billDetails = billDetails

        val payment = paymentService.createPayment(bill)
        bill.payment = payment
        billRepository.saveAndFlush(bill)

        return createBillResponse(bill)
    }

    @Transactional(rollbackFor = [Exception::class])
    override fun getById(id: String): BillResponse {
        val bill = findById(id)
        return createBillResponse(bill)
    }

    @Transactional(rollbackFor = [Exception::class])
    override fun getAll(request: SearchBillRequest): Page<BillResponse> {
        val billSpecification = specification.specification(request)

        val sort = Sort.by(Sort.Direction.fromString(request.direction), request.sortBy)
        val page = if (request.page <= 0) 1 else request.page
        val pageable = PageRequest.of(page - 1, request.size, sort)

        val bills = billRepository.findAll(billSpecification, pageable)
        println(bills)
        return bills.map { bill ->
            createBillResponse(bill)
        }
    }

    @Transactional(rollbackFor = [Exception::class])
    override fun updateStatusPayment(request: UpdateBillRequest, id: String): String {
        val bill = findById(id)
        val payment = bill.payment
        if (payment != null) {
            payment.transactionStatus = request.transactionStatus
        }
        return StatusMessage.SUCCESS_UPDATE
    }

    private fun findById(id: String): Bill {
        return billRepository.findById(id).orElseThrow {
            throw NotFoundException(StatusMessage.BILL_NOT_FOUND)
        }
    }

    private fun createBillResponse(bill: Bill): BillResponse {
        return BillResponse(
            id = bill.id!!,
            transDate = bill.transDate.toString(),
            customerId = bill.customer.id!!,
            customerName = bill.customer.name,
            tableName = bill.table.name,
            transType = bill.transType.id.toString(),
            billDetails = bill.billDetails!!.map { billDetail ->
                BillDetailResponse(
                    id = billDetail.id!!,
                    menuId = billDetail.menu.id!!,
                    qty = billDetail.qty,
                    price = billDetail.price
                )
            },
            payment = PaymentResponse(
                id = bill.payment?.id!!,
                token = bill.payment!!.token,
                transactionStatus = bill.payment!!.transactionStatus,
                redirectUrl = bill.payment!!.redirectUrl
            )
        )
    }

}