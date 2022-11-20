import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.7.5" apply false
    id("io.spring.dependency-management") version "1.0.15.RELEASE" apply false
    id("org.jlleitschuh.gradle.ktlint") version "10.2.0" apply false
    kotlin("jvm") version "1.7.20" apply false
    kotlin("plugin.spring") version "1.7.20" apply false

    kotlin("plugin.jpa") version "1.7.20" // 1
    id ("org.jetbrains.kotlin.plugin.allopen") version "1.7.20" // 6
    id ("org.jetbrains.kotlin.plugin.noarg") version "1.7.20" // 7
}

noArg {
    annotation("javax.persistence.Entity") // 2
}

allOpen {
    annotation("javax.persistence.Entity") // 3
    annotation("javax.persistence.MappedSuperclass") // 4
    annotation("javax.persistence.Embeddable") // 5
}

allprojects {
    group = "com.example"

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = "17"
        }
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }
}

version = "0.0.1-SNAPSHOT"
//java.sourceCompatibility = JavaVersion.VERSION_17

buildscript {
    repositories {
        mavenCentral()
    }
}
