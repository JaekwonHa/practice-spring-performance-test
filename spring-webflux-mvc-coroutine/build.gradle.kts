plugins {
    id("org.springframework.boot")
    id("io.spring.dependency-management")
    kotlin("jvm")
    kotlin("plugin.spring")
}

version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("io.r2dbc:r2dbc-h2")
    runtimeOnly("com.h2database:h2")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactive:1.6.4")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:1.6.4")
    runtimeOnly("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:1.6.4")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-slf4j:1.6.4")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
}
