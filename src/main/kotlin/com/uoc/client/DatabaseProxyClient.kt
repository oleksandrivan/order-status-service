package com.uoc.client

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.uoc.client.OrderProxyResponse.Companion.toDomain
import com.uoc.domain.*
import io.micronaut.http.HttpRequest
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.uri.UriBuilder
import jakarta.inject.Singleton
import reactor.core.publisher.Mono

interface DatabaseProxyClient {
    fun getOrder(orderId: OrderId): Mono<Order>
}

@Singleton
class DatabaseProxyClientImpl(
    @param:Client(id = "database-proxy") private val httpClient: HttpClient,
) : DatabaseProxyClient {

    private val mapper = jacksonObjectMapper()

    override fun getOrder(orderId: OrderId): Mono<Order> {
        val uri = UriBuilder.of("/v1/orders").path(orderId.value).build()
        val request: HttpRequest<*> = HttpRequest.GET<Any>(uri)
        return Mono.from(httpClient.retrieve(request, String::class.java))
            .map { mapper.readValue(it, OrderProxyResponse::class.java).toDomain() }
    }
}

data class OrderProxyResponse(
    val orderId: String,
    val customerId: Int,
    val addressId: Int,
    val status: String
){
    companion object{
        fun OrderProxyResponse.toDomain(): Order {
            return Order(
                orderId = OrderId(orderId),
                customerId = CustomerId(customerId),
                items = emptyList(),
                shippingAddress = AddressId(addressId),
                status = OrderStatus.valueOf(status)
            )
        }
    }
}
