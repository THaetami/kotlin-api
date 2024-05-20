package com.belajar.api.kotlin.specification

import com.belajar.api.kotlin.entities.bill.SearchBillRequest
import com.belajar.api.kotlin.model.Bill
import com.belajar.api.kotlin.utils.Utilities
import jakarta.persistence.criteria.CriteriaBuilder
import jakarta.persistence.criteria.CriteriaQuery
import jakarta.persistence.criteria.Predicate
import jakarta.persistence.criteria.Root
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.domain.Specification
import java.sql.Timestamp
import java.util.*

@Configuration
class BillSpecification(
    private val utilities: Utilities
) {
    fun specification(request: SearchBillRequest): Specification<Bill> {
        return Specification { root: Root<Bill>, query: CriteriaQuery<*>, criteriaBuilder: CriteriaBuilder ->
            val predicates = mutableListOf<Predicate>()

            request.daily.let {
                val date = utilities.parseDate(it, "yyyy-MM-dd")
                val start = Timestamp(date.time)
                val end = Timestamp(start.toInstant().plusSeconds(24 * 60 * 60 - 1).toEpochMilli())
                end.nanos = 999999999
                predicates.add(criteriaBuilder.between(root.get("transDate"), start, end))
            }

            val startDate = utilities.parseDate(request.weeklyStart, "yyyy-MM-dd")
            val endDate = utilities.parseDate(request.weeklyEnd, "yyyy-MM-dd")
            val start = Timestamp(startDate.time)
            val end = Timestamp(endDate.time)
            end.nanos = 999999999
            predicates.add(criteriaBuilder.between(root.get("transDate"), start, end))

            request.monthly.let {
                val date = utilities.parseDate(it, "yyyy-MM")
                val calendar = Calendar.getInstance()
                calendar.time = date
                val month = calendar[Calendar.MONTH] + 1
                val year = calendar[Calendar.YEAR]

                val monthExpression = criteriaBuilder.function("date_part", Int::class.java, criteriaBuilder.literal("month"), root.get<Any>("transDate"))
                val yearExpression = criteriaBuilder.function("date_part", Int::class.java, criteriaBuilder.literal("year"), root.get<Any>("transDate"))

                val monthFilter = criteriaBuilder.equal(monthExpression, month)
                val yearFilter = criteriaBuilder.equal(yearExpression, year)
                predicates.add(criteriaBuilder.and(monthFilter, yearFilter))
            }

            query.where(*predicates.toTypedArray())
            query.restriction
        }
    }

}

