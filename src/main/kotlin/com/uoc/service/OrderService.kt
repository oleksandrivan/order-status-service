package com.uoc.service

import com.uoc.domain.OrderId
import com.uoc.domain.OrderStatus
import com.uoc.repository.OrderRepository
import jakarta.inject.Singleton

interface OrderService {

    fun getOrderStatus(orderId: OrderId): Result<OrderStatus>
}

@Singleton
class OrderServiceImpl(
    private val orderRepository: OrderRepository
) : OrderService {

    override fun getOrderStatus(orderId: OrderId): Result<OrderStatus> = runCatching {
        orderRepository.findOrder(orderId).getOrThrow().status
    }
}