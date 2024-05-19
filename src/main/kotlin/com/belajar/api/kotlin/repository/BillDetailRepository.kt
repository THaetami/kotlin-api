package com.belajar.api.kotlin.repository

import com.belajar.api.kotlin.model.BillDetail
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface BillDetailRepository: JpaRepository<BillDetail, String>