package com.uoc.repository

import com.uoc.domain.Customer
import com.uoc.domain.CustomerId
import com.uoc.jooq.tables.references.CUSTOMER
import jakarta.inject.Singleton
import org.jooq.DSLContext
import org.jooq.SQLDialect
import org.jooq.impl.DSL
import javax.sql.DataSource

interface CustomerRepository {
    fun storeCustomer(customer: Customer): Result<CustomerId>
}

@Singleton
class CustomerRepositoryImpl(
    dataSource: DataSource
): CustomerRepository {

    private var dslContext: DSLContext = DSL.using(dataSource, SQLDialect.MYSQL)

    override fun storeCustomer(customer: Customer): Result<CustomerId> {
        val executeResult = dslContext.insertInto(CUSTOMER)
            .set(CUSTOMER.ID, customer.customerId.value)
            .set(CUSTOMER.NAME, customer.name)
            .set(CUSTOMER.EMAIL, customer.email)
            .execute()
        return when (executeResult) {
            1 -> Result.success(customer.customerId)
            else -> Result.failure(RuntimeException("Failed to store customer"))
        }
    }
}