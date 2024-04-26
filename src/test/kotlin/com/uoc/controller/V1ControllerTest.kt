package com.uoc.controller

import com.uoc.AbstractIntegrationTest
import com.uoc.repository.OrderRepository
import io.micronaut.http.HttpRequest
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import jakarta.inject.Inject
import org.junit.jupiter.api.Test

class V1ControllerTest: AbstractIntegrationTest() {

    @Inject
    @Client("/")
    lateinit var client: HttpClient

    @Test
    fun testGetOrderStatus() {

        val request = HttpRequest.GET<String>("/v1/orders/069738cb-adfe-4d28-964d-5bcb41d48943/status")
        val response = client.toBlocking().exchange(request, String::class.java)

        assert(response.status.code == 200)
        assert(response.body().contains("status"))
        assert(response.body().contains("CREATED"))
    }
}
