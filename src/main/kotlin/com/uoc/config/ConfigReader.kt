package com.uoc.config

import io.micronaut.context.annotation.Property
import jakarta.inject.Singleton

@Singleton
class ConfigReader {

    @Property(name = "database.url")
    lateinit var url: String

    @Property(name = "database.username")
    lateinit var username: String

    @Property(name = "database.password")
    lateinit var password: String

}