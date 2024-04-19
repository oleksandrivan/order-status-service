package com.uoc

import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.BeforeAll
import org.testcontainers.containers.MySQLContainer
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@Testcontainers
@MicronautTest
abstract class AbstractIntegrationTest {

    companion object {

        @Container
        val mySQLContainer: MySQLContainer<Nothing> = MySQLContainer<Nothing>("mysql:8.0.26")
            .apply {
                withDatabaseName("db")
                withExposedPorts(3306)
                withInitScript("init_mysql.sql")
                waitingFor(Wait.forHealthcheck())
            }
        @BeforeAll
        @JvmStatic
        fun setup() {
            mySQLContainer.start()
            System.setProperty("database.url", mySQLContainer.jdbcUrl)
            System.setProperty("database.username", mySQLContainer.username)
            System.setProperty("database.password", mySQLContainer.password)
        }
    }
}