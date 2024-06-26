/*
 * This file is generated by jOOQ.
 */
package com.uoc.jooq.enums


import org.jooq.Catalog
import org.jooq.EnumType
import org.jooq.Schema


/**
 * This class is generated by jOOQ.
 */
@Suppress("UNCHECKED_CAST")
enum class OrderStatus(@get:JvmName("literal") public val literal: String) : EnumType {
    CREATED("CREATED"),
    PREPARING("PREPARING"),
    SHIPPED("SHIPPED"),
    DELIVERED("DELIVERED"),
    CANCELLED("CANCELLED");
    override fun getCatalog(): Catalog? = null
    override fun getSchema(): Schema? = null
    override fun getName(): String? = null
    override fun getLiteral(): String = literal
}
