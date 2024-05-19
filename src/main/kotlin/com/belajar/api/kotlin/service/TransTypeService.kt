package com.belajar.api.kotlin.service

import com.belajar.api.kotlin.entities.trans_type.TransTypeResponse

interface TransTypeService {
    fun getById(id: String): TransTypeResponse
}