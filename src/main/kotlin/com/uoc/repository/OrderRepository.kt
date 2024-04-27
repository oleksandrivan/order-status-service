package com.uoc.repository

import com.uoc.domain.*
import com.uoc.jooq.tables.records.OrderRecord
import com.uoc.jooq.tables.references.ORDER
import com.uoc.jooq.tables.references.ORDERITEM
import jakarta.inject.Singleton
import org.jooq.DSLContext
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface OrderRepository {

    fun findOrder(orderId: OrderId): Mono<Order>
}

@Singleton
class OrderRepositoryImpl(
    private val dslContext: DSLContext
) : OrderRepository {

    override fun findOrder(orderId: OrderId): Mono<Order> {
        val items: Flux<OrderItem> = Flux.from(dslContext.select()
            .from(ORDERITEM)
            .where(ORDERITEM.ORDERID.eq(orderId.value)))
            .map { record -> OrderItem(record.get(ORDERITEM.PRODUCTID)!!, record.get(ORDERITEM.QUANTITY)!!) }

        val order = Mono.from(dslContext.select()
            .from(ORDER)
            .where(ORDER.ID.eq(orderId.value)))
            .map { record -> record.into(OrderRecord::class.java) }

        return order.toDomain(items)
    }

    companion object {
        private fun Mono<OrderRecord>.toDomain(itemsFlux: Flux<OrderItem>): Mono<Order> {
            return this.flatMap { record ->
                itemsFlux.collectList().map { items ->
                    with(record) {
                        Order(
                            orderId = OrderId(id!!),
                            customerId = CustomerId(customerid!!),
                            items = items,
                            shippingAddress = AddressId(shippingaddress!!),
                            status = OrderStatus.valueOf(status!!.name),
                            createdAt = createdat!!,
                            updatedAt = updatedat!!
                        )
                    }
                }
            }
        }
    }
}
