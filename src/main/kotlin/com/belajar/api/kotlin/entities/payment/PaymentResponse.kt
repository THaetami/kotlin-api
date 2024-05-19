package com.belajar.api.kotlin.entities.payment

data class PaymentResponse(
    var id: String,
    var token: String,
    var redirectUrl: String,
    var transactionStatus: String,
)
