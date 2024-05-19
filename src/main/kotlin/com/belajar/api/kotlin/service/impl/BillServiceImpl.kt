package com.belajar.api.kotlin.service.impl

import com.belajar.api.kotlin.constant.TransTypeEnum
import com.belajar.api.kotlin.entities.bill.BillRequest
import com.belajar.api.kotlin.entities.bill.BillResponse
import com.belajar.api.kotlin.entities.bill_detail.BillDetailResponse
import com.belajar.api.kotlin.entities.payment.PaymentResponse
import com.belajar.api.kotlin.model.Bill
import com.belajar.api.kotlin.model.BillDetail
import com.belajar.api.kotlin.model.Customer
import com.belajar.api.kotlin.model.TransType
import com.belajar.api.kotlin.repository.BillDetailRepository
import com.belajar.api.kotlin.repository.BillRepository
import com.belajar.api.kotlin.service.*
import com.belajar.api.kotlin.validation.ValidationUtil
import org.slf4j.LoggerFactory
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
    private val billDetailRepository: BillDetailRepository
): BillService {

    private val log = LoggerFactory.getLogger(JwtServiceImpl::class.java)

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
                id = payment.id!!,
                token = payment.token,
                transactionStatus = payment.transactionStatus,
                redirectUrl = payment.redirectUrl
            )
        )
    }
}