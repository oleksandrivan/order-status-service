package com.uoc.controller

import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig
import com.github.tomakehurst.wiremock.junit5.WireMockExtension
import io.micronaut.context.ApplicationContext
import io.micronaut.http.HttpRequest
import io.micronaut.http.client.HttpClient
import io.micronaut.runtime.server.EmbeddedServer
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import java.util.*


class V2ControllerTest {

    private fun getProperties(): Map<String, Any> {
        return Collections.singletonMap<String, Any>(
            "micronaut.http.services.database-proxy.url",
            wireMock.baseUrl()
        )
    }

    @Test
    fun testGetOrderStatus() {
        ApplicationContext.run(EmbeddedServer::class.java, getProperties()).use { server ->
            wireMock.stubFor(
                WireMock.get("/v1/orders/069738cb-1234-4d28-964d-5bcb41d48943")
                    .willReturn(
                        WireMock.aResponse()
                            .withStatus(200)
                            .withBody("""
                                {
                                  "orderId": "069738cb-1234-4d28-964d-5bcb41d48943",
                                  "status": "CANCELLED",
                                  "customerId": 1,
                                  "addressId": 1
                                }
                            """.trimIndent())
                    )
            )
            val client = HttpClient.create(server.url)
            val request = HttpRequest.GET<String>("/v2/orders/069738cb-1234-4d28-964d-5bcb41d48943/status")
            val response = client.toBlocking().exchange(request, String::class.java)
            assert(response.status.code == 200)
            assert(response.body().contains("status"))
            assert(response.body().contains("CANCELLED"))
        }
    }

    companion object {
        @JvmStatic
        @RegisterExtension
        var wireMock: WireMockExtension = WireMockExtension.newInstance()
            .options(wireMockConfig().dynamicPort())
            .build()
    }
}
