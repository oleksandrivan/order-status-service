package com.uoc.controller

import com.uoc.AbstractIntegrationTest
import com.uoc.repository.OrderRepository
import com.uoc.repository.RepositoryImplTest.Companion.order
import io.micronaut.http.HttpRequest
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import jakarta.inject.Inject
import org.junit.jupiter.api.Test

class V1ControllerTest: AbstractIntegrationTest() {

    @Inject
    @Client("/")
    lateinit var client: HttpClient

    @Inject
    lateinit var orderRepository: OrderRepository

    @Test
    fun testGetOrderStatus() {
        val order = order()
        orderRepository.storeOrder(order)

        val request = HttpRequest.GET<String>("/v1/orders/${order.orderId.value}/status")
        val response = client.toBlocking().exchange(request, String::class.java)

        assert(response.status.code == 200)
        assert(response.body().contains("status"))
        assert(response.body().contains("CREATED"))
    }
}
