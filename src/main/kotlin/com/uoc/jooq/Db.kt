/*
 * This file is generated by jOOQ.
 */
package com.uoc.jooq


import com.uoc.jooq.tables.Address
import com.uoc.jooq.tables.Customer
import com.uoc.jooq.tables.Order
import com.uoc.jooq.tables.Orderitem

import kotlin.collections.List

import org.jooq.Catalog
import org.jooq.Table
import org.jooq.impl.SchemaImpl


/**
 * This class is generated by jOOQ.
 */
@Suppress("UNCHECKED_CAST")
open class Db : SchemaImpl("db", DefaultCatalog.DEFAULT_CATALOG) {
    public companion object {

        /**
         * The reference instance of <code>db</code>
         */
        val DB: Db = Db()
    }

    /**
     * The table <code>db.Address</code>.
     */
    val ADDRESS: Address get() = Address.ADDRESS

    /**
     * The table <code>db.Customer</code>.
     */
    val CUSTOMER: Customer get() = Customer.CUSTOMER

    /**
     * The table <code>db.Order</code>.
     */
    val ORDER: Order get() = Order.ORDER

    /**
     * The table <code>db.OrderItem</code>.
     */
    val ORDERITEM: Orderitem get() = Orderitem.ORDERITEM

    override fun getCatalog(): Catalog = DefaultCatalog.DEFAULT_CATALOG

    override fun getTables(): List<Table<*>> = listOf(
        Address.ADDRESS,
        Customer.CUSTOMER,
        Order.ORDER,
        Orderitem.ORDERITEM
    )
}
