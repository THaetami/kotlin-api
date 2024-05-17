package com.belajar.api.kotlin.model

import com.belajar.api.kotlin.constant.TableName
import jakarta.persistence.*
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.Where


@Entity
@Table(name = TableName.M_IMAGE)
@SQLDelete(sql = "UPDATE " + TableName.M_IMAGE + " SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
data class Image(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: String? = null,

    @Column(name = "name", nullable = false)
    var name: String,

    @Column(name = "path", nullable = false)
    var path: String,

    @Column(name = "size", nullable = false)
    var size: Long,

    @Column(name = "deleted", nullable = false)
    var deleted: Boolean = false,

    @Column(name = "content_type", nullable = false)
    var contentType: String
)
