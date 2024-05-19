package com.belajar.api.kotlin.repository

import com.belajar.api.kotlin.model.TableRest
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface TableRepository : JpaRepository<TableRest, String> {
    @Modifying
    @Query("update TableRest t set t.deleted = true where t.id = :id")
    fun softDelete(@Param("id") id: String)

    fun findByNameLikeIgnoreCase(name: String): Optional<TableRest>
    fun existsByName(name: String): Boolean
}