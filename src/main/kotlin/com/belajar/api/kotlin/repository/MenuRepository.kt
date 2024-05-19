package com.belajar.api.kotlin.repository

import com.belajar.api.kotlin.model.Menu
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface MenuRepository: JpaRepository<Menu, String>, JpaSpecificationExecutor<Menu> {
    @Modifying
    @Query("update Menu m set m.deleted = true where m.id = :id")
    fun softDelete(@Param("id") id: String)

    fun existsByName(name: String): Boolean
}