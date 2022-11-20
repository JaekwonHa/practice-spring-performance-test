package com.example.`webflux-router`

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.annotation.Id
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories
import org.springframework.data.relational.core.mapping.Table
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Component
import org.springframework.stereotype.Repository
import org.springframework.web.reactive.function.server.*
import org.springframework.web.reactive.function.server.ServerResponse.notFound
import org.springframework.web.reactive.function.server.ServerResponse.ok
import kotlin.random.Random

@SpringBootApplication
@EnableR2dbcRepositories
class WebfluxRouterCoroutineApplication

fun main(args: Array<String>) {
    runApplication<WebfluxRouterCoroutineApplication>(*args)
}

@Configuration
class Router(
    private val personHandler: PersonHandler
) {
    @Bean
    fun route() = coRouter {
        listOf(
            POST("/person", personHandler::createPerson),
            GET("/person", personHandler::getPerson)
        )
    }
}

@Component
class PersonHandler {
    @Autowired
    lateinit var personRepository: PersonRepository

    suspend fun createPerson(req: ServerRequest): ServerResponse {
        val name = Random.nextBytes(50).toString()
        val person = Person(name = name)
        return ok().json().bodyValueAndAwait(personRepository.save(person))
    }

    suspend fun getPerson(req: ServerRequest): ServerResponse {
        val id = Random.nextLong(1000000)
        return personRepository.findById(id)?.let {
            ok().json().bodyValueAndAwait(it)
        } ?: notFound().buildAndAwait()
    }
}

@Repository
interface PersonRepository : CoroutineCrudRepository<Person, Long> {

}

@Table(name = "person")
data class Person(
    val name: String,
    @Id
    val id: Long? = null,
)
