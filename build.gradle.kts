import com.bmuschko.gradle.docker.tasks.image.DockerBuildImage
import java.io.ByteArrayOutputStream

plugins {
    id("org.jetbrains.kotlin.jvm") version "1.9.23"
    id("org.jetbrains.kotlin.plugin.allopen") version "1.9.23"
    id("com.google.devtools.ksp") version "1.9.23-1.0.19"
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("io.micronaut.application") version "4.3.5"
    id("io.micronaut.test-resources") version "4.3.5"
    id("io.micronaut.aot") version "4.3.5"
}

version = "0.1"
group = "com.uoc"

val kotlinVersion=project.properties.get("kotlinVersion")
val registry = project.properties.get("dockerRegistry") as String

repositories {
    mavenCentral()
}

dependencies {
    ksp("io.micronaut:micronaut-http-validation")
    ksp("io.micronaut.serde:micronaut-serde-processor")
    implementation("io.micronaut:micronaut-http-client")
    implementation("io.micronaut:micronaut-jackson-databind")
    implementation("io.micronaut:micronaut-management")
    implementation("io.micronaut.kotlin:micronaut-kotlin-runtime")
    implementation("io.micronaut.micrometer:micronaut-micrometer-core")
    implementation("io.micronaut.micrometer:micronaut-micrometer-registry-prometheus")
    implementation("io.micronaut.serde:micronaut-serde-jackson")
    implementation("org.jooq:jooq:3.19.7")
    implementation("mysql:mysql-connector-java:8.0.33")
    implementation("io.micronaut.sql:micronaut-jooq")
    implementation("org.jetbrains.kotlin:kotlin-reflect:${kotlinVersion}")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:${kotlinVersion}")
    implementation("io.asyncer:r2dbc-mysql:1.1.0")
    runtimeOnly("ch.qos.logback:logback-classic")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    testImplementation("org.testcontainers:junit-jupiter:1.19.7")
    testImplementation("org.testcontainers:mysql:1.19.7")
    testImplementation("org.testcontainers:testcontainers:1.19.7")
}


application {
    mainClass.set("com.uoc.ApplicationKt")
}
java {
    sourceCompatibility = JavaVersion.toVersion("21")
}


graalvmNative.toolchainDetection.set(false)
micronaut {
    runtime("netty")
    testRuntime("junit5")
    processing {
        incremental(true)
        annotations("com.uoc.*")
    }
    testResources {
        additionalModules.add("jdbc-mysql")
    }
    aot {
    // Please review carefully the optimizations enabled below
    // Check https://micronaut-projects.github.io/micronaut-aot/latest/guide/ for more details
        optimizeServiceLoading.set(false)
        convertYamlToJava.set(false)
        precomputeOperations.set(true)
        cacheEnvironment.set(true)
        optimizeClassLoading.set(true)
        deduceEnvironment.set(true)
        optimizeNetty.set(true)
    }
}

tasks.named<io.micronaut.gradle.docker.MicronautDockerfile>("dockerfile") {
    baseImage("eclipse-temurin:21-jre-jammy")
    exposedPorts.set(listOf(8080))
}


tasks.named<DockerBuildImage>("dockerBuild") {
    val commitId = execCmd("git rev-parse --short HEAD")
    images.set(listOf("$registry/order-status-service:latest", "$registry/order-status-service:$commitId"))
}

tasks.named<io.micronaut.gradle.docker.NativeImageDockerfile>("dockerfileNative") {
    jdkVersion.set("21")
    graalArch.set("amd64")
    exposedPorts.set(listOf(8080))
}

tasks.named<DockerBuildImage>("dockerBuildNative") {
    val commitId = execCmd("git rev-parse --short HEAD")
    images.set(listOf("$registry/order-status-service:latest", "$registry/order-status-service:$commitId"))
    platform.set("linux/amd64")
}

fun execCmd(command: String): String {
    val stdOut = ByteArrayOutputStream()
    project.exec {
        commandLine = command.split(" ")
        standardOutput = stdOut
    }
    return stdOut.toString().trim()
}


