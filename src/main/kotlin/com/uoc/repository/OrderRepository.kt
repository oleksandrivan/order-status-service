package com.uoc.repository

import com.uoc.domain.*
import com.uoc.jooq.tables.records.OrderRecord
import com.uoc.jooq.tables.references.ORDER
import com.uoc.jooq.tables.references.ORDERITEM
import jakarta.inject.Singleton
import org.jooq.DSLContext
import org.jooq.SQLDialect
import org.jooq.impl.DSL
import java.time.LocalDateTime
import javax.sql.DataSource

interface OrderRepository {

    fun findOrder(orderId: OrderId): Result<Order>
    fun storeOrder(order: Order): Result<OrderId>
    fun updateOrder(orderId: OrderId, orderStatus: OrderStatus): Result<OrderId>
}

@Singleton
class OrderRepositoryImpl(
    dataSource: DataSource
) : OrderRepository {

    private var dslContext: DSLContext = DSL.using(dataSource, SQLDialect.MYSQL)

    override fun findOrder(orderId: OrderId): Result<Order> {
        val items: List<OrderItem> = dslContext.select()
            .from(ORDERITEM)
            .where(ORDERITEM.ORDERID.eq(orderId.value))
            .fetch()
            .map { record -> OrderItem(record.get(ORDERITEM.PRODUCTID)!!, record.get(ORDERITEM.QUANTITY)!!)}
        val record = dslContext.select()
            .from(ORDER)
            .where(ORDER.ID.eq(orderId.value))
            .fetchAny()!!
            .into(OrderRecord::class.java)
            .map { it as OrderRecord }
        return Result.success(record.toDomain(items))
    }


    override fun storeOrder(order: Order): Result<OrderId> {
        val orderResult = dslContext.insertInto(ORDER)
            .set(ORDER.ID, order.orderId.value)
            .set(ORDER.CUSTOMERID, order.customerId.value)
            .set(ORDER.SHIPPINGADDRESS, order.shippingAddress.value)
            .set(ORDER.STATUS, com.uoc.jooq.enums.OrderStatus.valueOf(order.status.name))
            .set(ORDER.CREATEDAT, order.createdAt)
            .set(ORDER.UPDATEDAT, order.updatedAt)
            .execute()

        val orderItemsResult = order.items.map { item ->
            dslContext.insertInto(ORDERITEM)
                .set(ORDERITEM.ORDERID, order.orderId.value)
                .set(ORDERITEM.PRODUCTID, item.productId)
                .set(ORDERITEM.QUANTITY, item.quantity)
                .execute()
        }.sum()

        return if(orderResult == 1 && orderItemsResult == order.items.size){
            Result.success(order.orderId)
        } else{
            Result.failure(RuntimeException("Failed to store order"))
        }
    }

    override fun updateOrder(orderId: OrderId, orderStatus: OrderStatus): Result<OrderId> {
        val orderResult = dslContext.update(ORDER)
            .set(ORDER.STATUS, com.uoc.jooq.enums.OrderStatus.valueOf(orderStatus.name))
            .set(ORDER.UPDATEDAT, LocalDateTime.now())
            .where(ORDER.ID.eq(orderId.value))
            .execute()
        return when (orderResult) {
            1 -> Result.success(orderId)
            else -> Result.failure(RuntimeException("Failed to store customer"))
        }
    }

    companion object{

        private fun OrderRecord.toDomain(items: List<OrderItem>): Order {
            return Order(
                orderId = OrderId(this.id!!),
                customerId = CustomerId(this.customerid!!),
                items = items,
                shippingAddress = AddressId(this.shippingaddress!!),
                status = OrderStatus.valueOf(this.status!!.name),
                createdAt = this.createdat!!,
                updatedAt = this.updatedat!!
            )
        }
    }
}
