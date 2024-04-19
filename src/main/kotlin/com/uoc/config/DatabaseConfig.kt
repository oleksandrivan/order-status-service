package com.uoc.config

import com.mysql.cj.jdbc.MysqlDataSource
import io.micronaut.context.annotation.Bean
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Property
import io.micronaut.context.annotation.Value
import org.slf4j.LoggerFactory
import javax.sql.DataSource

@Factory
class DatabaseConfig(
    private val configReader: ConfigReader
) {

    private val log = LoggerFactory.getLogger(DatabaseConfig::class.java)

    @Bean
    fun dataSource(): DataSource {
        val dataSource = MysqlDataSource()
        val url: String = if(System.getProperty("database.url") != null) System.getProperty("database.url") else configReader.url
        val username: String = if(System.getProperty("database.username") != null) System.getProperty("database.username") else configReader.username
        val password: String = if(System.getProperty("database.password") != null) System.getProperty("database.password") else configReader.password
        dataSource.setURL(url)
        dataSource.user = username
        dataSource.password = password
        log.info("Database configuration: $url, $username, $password")
        return dataSource
    }
}