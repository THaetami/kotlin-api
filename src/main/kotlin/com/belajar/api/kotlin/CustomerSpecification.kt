package com.belajar.api.kotlin

import com.belajar.api.kotlin.model.Customer
import jakarta.persistence.criteria.CriteriaBuilder
import jakarta.persistence.criteria.CriteriaQuery
import jakarta.persistence.criteria.Predicate
import jakarta.persistence.criteria.Root
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.domain.Specification
import java.util.*

@Configuration
class CustomerSpecification {
    fun specification(name: String?): Specification<Customer> {
        return Specification { root, query, criteriaBuilder ->
            val predicates = ArrayList<Predicate>()

            if (name != null) {
                val nameFilter = criteriaBuilder.like(criteriaBuilder.upper(root.get("name")), "%" + name.uppercase(
                    Locale.getDefault()
                ) + "%")
                predicates.add(nameFilter)
            }

            query.where(*predicates.toTypedArray()).restriction
        }
    }

}