package com.uoc.repository

import com.uoc.domain.Address
import com.uoc.domain.AddressId
import com.uoc.jooq.tables.references.ADDRESS
import com.uoc.jooq.tables.references.CUSTOMER
import jakarta.inject.Singleton
import org.jooq.DSLContext
import org.jooq.SQLDialect
import org.jooq.impl.DSL
import javax.sql.DataSource

interface AddressRepository {

    fun storeAddress(address: Address): Result<AddressId>
}

@Singleton
class AddressRepositoryImpl(
    dataSource: DataSource
): AddressRepository {

    private var dslContext: DSLContext = DSL.using(dataSource, SQLDialect.MYSQL)

    override fun storeAddress(address: Address): Result<AddressId> {
        val executeResult = dslContext.insertInto(ADDRESS)
            .set(ADDRESS.ID, address.addressId.value)
            .set(ADDRESS.STREET, address.street)
            .set(ADDRESS.CITY, address.city)
            .set(ADDRESS.STATE, address.state)
            .set(ADDRESS.COUNTRY, address.country)
            .set(ADDRESS.POSTALCODE, address.postalCode)
            .execute()
        return when (executeResult) {
            1 -> Result.success(address.addressId)
            else -> Result.failure(RuntimeException("Failed to store customer"))
        }
    }
}