package com.uoc.controller

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.uoc.client.DatabaseProxyClient
import com.uoc.controller.V1Controller.Companion.fold
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import reactor.core.publisher.Mono

@Controller("/v2")
class V2Controller(
    private val databaseProxyClient: DatabaseProxyClient
) {
    private val mapper = jacksonObjectMapper()

    @Get("/orders/{orderId}/status")
    fun getOrder(@PathVariable orderId: String): Mono<HttpResponse<String>> {
        val result = databaseProxyClient.getOrder(orderId.toDomain())
        return result.fold(
            onSuccess = { HttpResponse.ok(mapper.writeValueAsString(OrderStatusSuccessResponse(it.status))) },
            onError = { HttpResponse.serverError(mapper.writeValueAsString(OrderStatusFailureResponse(it.message!!))) }
        )
    }
}
