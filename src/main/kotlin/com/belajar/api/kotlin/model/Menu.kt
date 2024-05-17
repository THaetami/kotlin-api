package com.belajar.api.kotlin.model

import com.belajar.api.kotlin.constant.TableName
import jakarta.persistence.*
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.Where

@Entity
@Table(name = TableName.M_MENU)
@SQLDelete(sql = "UPDATE " + TableName.M_MENU + " SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
data class Menu(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: String? = null,

    @Column(name = "name", nullable = false, unique = true)
    var name: String,

    @Column(name = "price", nullable = false)
    var price: Long,

    @Column(name = "deleted", nullable = false)
    var deleted: Boolean = false,

    @OneToOne
    @JoinColumn(name = "image_id", unique = true)
    var image: Image? = null
)
