package com.belajar.api.kotlin.model

import com.belajar.api.kotlin.constant.TableName
import com.fasterxml.jackson.annotation.JsonBackReference
import jakarta.persistence.*

@Entity
@Table(name = TableName.M_BILL_DETAIL)
data class BillDetail(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: String? = null,

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "bill_id", nullable = false)
    var bill: Bill,

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "menu_id", nullable = false)
    var menu: Menu,

   @Column(name = "qty", nullable = false)
    var qty: Int,

    @Column(name = "price", nullable = false)
    var price: Long,
)
