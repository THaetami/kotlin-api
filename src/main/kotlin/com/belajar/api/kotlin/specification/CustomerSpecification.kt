package com.belajar.api.kotlin.specification

import com.belajar.api.kotlin.entities.customer.SearchCustomerRequest
import com.belajar.api.kotlin.model.Customer
import jakarta.persistence.criteria.Predicate
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.domain.Specification
import java.util.*

@Configuration
class CustomerSpecification {
    fun specification(request: SearchCustomerRequest?): Specification<Customer> {
        return Specification { root, query, criteriaBuilder ->
            val predicates = ArrayList<Predicate>()

            request?.name?.let {
                val nameFilter = criteriaBuilder.like(criteriaBuilder.upper(root.get("name")), "%${it.uppercase()}%")
                predicates.add(nameFilter)
            }

            query.where(*predicates.toTypedArray()).restriction
        }
    }

}