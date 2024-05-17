package com.belajar.api.kotlin.repository

import com.belajar.api.kotlin.model.Image
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface ImageRepository: JpaRepository<Image, String> {
    @Modifying
    @Query("update Image i set i.deleted = true where i.id = :id")
    fun softDelete(@Param("id") id: String)
}