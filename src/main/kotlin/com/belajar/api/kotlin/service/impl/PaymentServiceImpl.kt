package com.belajar.api.kotlin.service.impl

import com.belajar.api.kotlin.model.Bill
import com.belajar.api.kotlin.model.Payment
import com.belajar.api.kotlin.repository.PaymentRepository
import com.belajar.api.kotlin.service.PaymentService
import com.midtrans.httpclient.SnapApi
import com.midtrans.httpclient.error.MidtransError
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import com.midtrans.Midtrans
import org.springframework.transaction.annotation.Transactional

@Service
class PaymentServiceImpl(
    private val paymentRepository: PaymentRepository,
    @Value("\${midtrans.api.key}") private val key: String,
): PaymentService {

    init {
        // Set Midtrans server key
        Midtrans.serverKey = key
        // Set Midtrans environment
        Midtrans.isProduction = false
    }

    private val log = LoggerFactory.getLogger(PaymentServiceImpl::class.java)

    @Transactional(rollbackFor = [Exception::class])
    override fun createPayment(bill: Bill): Payment {

        val amount = bill.billDetails?.sumOf { value ->
            value.price * value.qty
        } ?: 0L

        val paymentItemDetailRequest = bill.billDetails?.map { billDetail ->
            mapOf(
                "id" to billDetail.id!!,
                "name" to billDetail.menu.name,
                "price" to billDetail.price,
                "quantity" to billDetail.qty
            )
        } ?: emptyList()

        val transactionDetails = mapOf(
            "order_id" to bill.id,
            "gross_amount" to amount.toString()
        )

        val customerDetails = mapOf(
            "first_name" to bill.customer.name,
            "phone" to bill.customer.phone
        )

        val requestBody = mapOf(
            "transaction_details" to transactionDetails,
            "customer_details" to customerDetails,
            "credit_card" to mapOf("secure" to true),
            "item_details" to paymentItemDetailRequest
        )

        val (transactionToken, redirectUrl) = try {
            val transactionToken = SnapApi.createTransactionToken(requestBody)
            val redirectUrl = SnapApi.createTransactionRedirectUrl(requestBody)
            transactionToken to redirectUrl
        } catch (e: MidtransError) {
            log.error("Error creating transaction token or redirect URL: ${e.message}")
            throw RuntimeException("Error creating transaction token or redirect URL", e)
        }

        val payment = Payment(
            token = transactionToken,
            redirectUrl = redirectUrl,
            transactionStatus = "ordered"
        )
        paymentRepository.saveAndFlush(payment)
        return payment
    }

}