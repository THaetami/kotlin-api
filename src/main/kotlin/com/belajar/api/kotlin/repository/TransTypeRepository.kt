package com.belajar.api.kotlin.repository

import com.belajar.api.kotlin.constant.TransTypeEnum
import com.belajar.api.kotlin.model.TransType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface TransTypeRepository: JpaRepository<TransType, TransTypeEnum> {
    @Query(value = "SELECT * FROM trans_type WHERE id = :id", nativeQuery = true)
    fun findTransTypeByEnumId(@Param("id") id: String): Optional<TransType>
}