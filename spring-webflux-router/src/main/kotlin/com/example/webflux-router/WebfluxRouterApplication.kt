package com.example.`webflux-router`

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.annotation.Id
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories
import org.springframework.data.relational.core.mapping.Table
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.stereotype.Repository
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.notFound
import org.springframework.web.reactive.function.server.ServerResponse.ok
import org.springframework.web.reactive.function.server.body
import org.springframework.web.reactive.function.server.router
import reactor.core.publisher.Mono
import kotlin.random.Random

@SpringBootApplication
@EnableR2dbcRepositories
class WebfluxRouterApplication

fun main(args: Array<String>) {
    runApplication<WebfluxRouterApplication>(*args)
}

@Configuration
class Router(
    private val personHandler: PersonHandler
) {
    @Bean
    fun route() = router {
        listOf(
            POST("/person", personHandler::createPerson),
            GET("/person", personHandler::getPerson)
        )
    }
}

@Component
class PersonHandler(
    private val personRepository: PersonRepository
) {
    fun createPerson(req: ServerRequest): Mono<ServerResponse> {
        val name = Random.nextBytes(50).toString()
        val person = Person(name = name)
        return ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body<Person>(personRepository.save(person))
    }

    fun getPerson(req: ServerRequest): Mono<ServerResponse> {
        val id = Random.nextLong(1000000)
        return ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body<Person>(personRepository.findById(id))
            .switchIfEmpty(notFound().build())
    }
}

@Repository
interface PersonRepository : ReactiveCrudRepository<Person, Long> {

}

@Table(name = "person")
data class Person(
    val name: String,
    @Id
    val id: Long? = null,
)
