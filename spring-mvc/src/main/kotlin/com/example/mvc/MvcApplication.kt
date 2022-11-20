package com.example.mvc

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
class MvcApplication

fun main(args: Array<String>) {
    runApplication<MvcApplication>(*args)
}


@RestController
class Controller(
    private val personService: PersonService
) {

    @PostMapping("/person")
    fun createPerson(): ResponseEntity<Person> {
        val name = Random.nextBytes(50).toString()
        val person = personService.createPerson(name)

        return ResponseEntity.ok().body(person)
    }

    @GetMapping("/person")
    fun getPerson(): ResponseEntity<Any> {
        val id = Random.nextLong(1000000)
        val person = personService.getPerson(id)

        return if (person != null) {
            ResponseEntity.ok().body(person)
        } else {
            ResponseEntity.notFound().build()
        }
    }
}

@Service
class PersonService(
    private val personRepository: PersonRepository
) {
    fun createPerson(name: String): Person {
        val person = Person(name = name)
        return personRepository.save(person)
    }

    fun getPerson(id: Long): Person? {
        return personRepository.findByIdOrNull(id)
    }
}

@Repository
interface PersonRepository : JpaRepository<Person, Long> {

}

@Entity
@Table(name = "person")
class Person(
    @Column(nullable = false)
    val name: String,
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
)
