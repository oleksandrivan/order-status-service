package com.uoc.controller

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.uoc.domain.OrderId
import com.uoc.domain.OrderStatus
import com.uoc.service.OrderService
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import reactor.core.publisher.Mono

@Controller("/v1")
class V1Controller(
    private val orderService: OrderService
) {
    private val mapper = jacksonObjectMapper()

    @Get("/orders/{orderId}/status")
    fun getOrder(@PathVariable orderId: String): Mono<HttpResponse<String>> {
        val result = orderService.getOrderStatus(orderId.toDomain())
        return result.fold(
            onSuccess = { HttpResponse.ok(mapper.writeValueAsString(OrderStatusSuccessResponse(it))) },
            onError = { HttpResponse.serverError(mapper.writeValueAsString(OrderStatusFailureResponse(it.message!!))) }
        )
    }

    companion object {
        private fun <T, R> Mono<T>.fold(onSuccess: (T) -> R, onError: (Throwable) -> R): Mono<R> {
            return this
                .map(onSuccess)
                .onErrorResume { error -> Mono.just(onError(error)!!) }
        }
    }
}

private fun String.toDomain(): OrderId = OrderId(this)

sealed class OrderStatusResponse
data class OrderStatusSuccessResponse(val status: OrderStatus): OrderStatusResponse()
data class OrderStatusFailureResponse(val message: String): OrderStatusResponse()

