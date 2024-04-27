package com.uoc.service

import com.uoc.domain.OrderId
import com.uoc.domain.OrderStatus
import com.uoc.repository.OrderRepository
import jakarta.inject.Singleton
import reactor.core.publisher.Mono

interface OrderService {

    fun getOrderStatus(orderId: OrderId): Mono<OrderStatus>
}

@Singleton
class OrderServiceImpl(
    private val orderRepository: OrderRepository
) : OrderService {

    override fun getOrderStatus(orderId: OrderId): Mono<OrderStatus> =
        orderRepository.findOrder(orderId).map { it.status }
}