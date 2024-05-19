package com.belajar.api.kotlin.model

import com.belajar.api.kotlin.constant.TableName
import jakarta.persistence.*

@Entity
@Table(name = TableName.M_PAYMENT)
data class Payment(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: String? = null,

    @Column(name = "token")
    var token: String,

    @Column(name = "redirect_url")
    var redirectUrl: String,

    @Column(name = "transaction_status")
    val transactionStatus: String,

    @OneToOne(mappedBy = "payment")
    var bill: Bill? = null,
)
