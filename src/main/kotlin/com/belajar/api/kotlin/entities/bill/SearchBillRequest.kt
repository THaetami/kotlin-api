package com.belajar.api.kotlin.entities.bill

data class SearchBillRequest(
    var daily: String,
    var weeklyStart: String,
    var weeklyEnd: String,
    var monthly: String,
    var direction: String,
    var sortBy: String,
    var page: Int,
    var size: Int,
)
