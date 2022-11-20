package com.example.`mvc-coroutine`

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import javax.persistence.*
import kotlin.random.Random

@SpringBootApplication
class MvcCoroutineApplication

fun main(args: Array<String>) {
    runApplication<MvcCoroutineApplication>(*args)
}


@RestController
class Controller(
    private val personService: PersonService
) {

    @PostMapping("/person")
    fun createPerson(): ResponseEntity<Person> {
        return runBlocking(Dispatchers.IO) {
            val person = withContext(Dispatchers.Default) {
                val name = Random.nextBytes(50).toString()
                personService.createPerson(name)
            }

            ResponseEntity.ok().body(person)
        }
    }

    @GetMapping("/person")
    fun getPerson(): ResponseEntity<Any> {
        return runBlocking(Dispatchers.IO) {
            val person = withContext(Dispatchers.Default) {
                val id = Random.nextLong(1000000)
                personService.getPerson(id)
            }

            if (person != null) {
                ResponseEntity.ok().body(person)
            } else {
                ResponseEntity.notFound().build()
            }
        }
    }
}

@Service
class PersonService(
    private val personRepository: PersonRepository
) {
    suspend fun createPerson(name: String): Person {
        return withContext(Dispatchers.IO) {
            val person = Person(name = name)
            personRepository.save(person)
        }
    }

    suspend fun getPerson(id: Long): Person? {
        return withContext(Dispatchers.IO) {
            personRepository.findByIdOrNull(id)
        }
    }
}

@Repository
interface PersonRepository : JpaRepository<Person, Long> {

}

@Entity
@Table(name = "person")
data class Person(
    @Column(nullable = false)
    val name: String,
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
)
