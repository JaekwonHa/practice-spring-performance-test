package com.example.`webflux-mvc-coroutine`

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.annotation.Id
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories
import org.springframework.data.relational.core.mapping.Table
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import kotlin.random.Random

@SpringBootApplication
@EnableR2dbcRepositories
class WebfluxMvcCoroutineApplication

fun main(args: Array<String>) {
    runApplication<WebfluxMvcCoroutineApplication>(*args)
}


@RestController
class Controller(
    private val personService: PersonService
) {

    @PostMapping("/person")
    suspend fun createPerson(): ResponseEntity<Person> {
        val name = Random.nextBytes(50).toString()

        val person = personService.createPerson(name)
        return ResponseEntity.ok().body(person)
    }

    @GetMapping("/person")
    suspend fun getPerson(): ResponseEntity<Any> {
        val id = Random.nextLong(1000000)

        return personService.getPerson(id)?.let {
            ResponseEntity.ok().body(it)
        } ?: ResponseEntity.notFound().build()
    }
}

@Service
class PersonService(
    private val personRepository: PersonRepository
) {
    suspend fun createPerson(name: String): Person {
        val person = Person(name = name)
        return personRepository.save(person)
    }

    suspend fun getPerson(id: Long): Person? {
        return personRepository.findById(id)
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
