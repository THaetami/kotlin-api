package com.belajar.api.kotlin.specification

import com.belajar.api.kotlin.entities.menu.SearchMenuRequest
import com.belajar.api.kotlin.model.Menu
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.domain.Specification
import jakarta.persistence.criteria.Predicate

@Configuration
class MenuSpecification {
    fun specification(request: SearchMenuRequest): Specification<Menu> {
        return Specification<Menu> { root, query, criteriaBuilder ->
            val predicates = mutableListOf<Predicate>()

            request.name.let {
                val nameFilter = criteriaBuilder.like(criteriaBuilder.upper(root.get("name")), "%${it.uppercase()}%")
                predicates.add(nameFilter)
            }

            val newMinPrice = request.minPrice
            val newMaxPrice = request.maxPrice
            val priceFilter = criteriaBuilder.between(root.get("price"), newMinPrice, newMaxPrice)
            predicates.add(priceFilter)

            query.where(*predicates.toTypedArray()).restriction
        }
    }
}