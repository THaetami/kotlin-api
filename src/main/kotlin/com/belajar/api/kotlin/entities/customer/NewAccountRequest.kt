package com.belajar.api.kotlin.entities.customer

import com.belajar.api.kotlin.model.UserAccount

data class NewAccountRequest (
    var name: String? = null,
    val phone: String? = null,
    val userAccount: UserAccount? = null
)

