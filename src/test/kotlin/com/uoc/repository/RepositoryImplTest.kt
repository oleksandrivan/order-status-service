package com.uoc.repository

import com.uoc.AbstractIntegrationTest
import com.uoc.domain.*
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

@MicronautTest
class RepositoryImplTest : AbstractIntegrationTest() {

    @Inject
    lateinit var orderRepository: OrderRepository

    @Test
    fun when_getOrder_then_returnsOrderEntity() {

        val storedOrder = orderRepository.findOrder(OrderId("069738cb-adfe-4d28-964d-5bcb41d48943"))
        assert(storedOrder.isSuccess)
        storedOrder.onSuccess {
            assert(it.customerId == CustomerId(1))
            assert(it.shippingAddress == AddressId(1))
            assert(it.items.size == 2)
        }
    }
}
