/*
 * This file is generated by jOOQ.
 */
package com.uoc.jooq.tables.records


import com.uoc.jooq.tables.Customer

import org.jooq.Record1
import org.jooq.impl.UpdatableRecordImpl


/**
 * This class is generated by jOOQ.
 */
@Suppress("UNCHECKED_CAST")
open class CustomerRecord() : UpdatableRecordImpl<CustomerRecord>(Customer.CUSTOMER) {

    open var id: Int?
        set(value): Unit = set(0, value)
        get(): Int? = get(0) as Int?

    open var name: String?
        set(value): Unit = set(1, value)
        get(): String? = get(1) as String?

    open var email: String?
        set(value): Unit = set(2, value)
        get(): String? = get(2) as String?

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    override fun key(): Record1<Int?> = super.key() as Record1<Int?>

    /**
     * Create a detached, initialised CustomerRecord
     */
    constructor(id: Int? = null, name: String? = null, email: String? = null): this() {
        this.id = id
        this.name = name
        this.email = email
        resetChangedOnNotNull()
    }
}