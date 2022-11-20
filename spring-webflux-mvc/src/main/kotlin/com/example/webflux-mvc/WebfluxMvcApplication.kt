package com.example.`webflux-mvc`

import io.r2dbc.h2.H2ConnectionConfiguration
import io.r2dbc.h2.H2ConnectionFactory
import io.r2dbc.h2.H2ConnectionOption
import io.r2dbc.spi.ConnectionFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import org.springframework.data.annotation.Id
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories
import org.springframework.data.relational.core.mapping.Table
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.http.ResponseEntity
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import kotlin.random.Random

@SpringBootApplication
@EnableR2dbcRepositories
class WebfluxMvcApplication

fun main(args: Array<String>) {
    runApplication<WebfluxMvcApplication>(*args)
}

//@Configuration
//@EnableR2dbcRepositories
//class R2bcConfiguration : AbstractR2dbcConfiguration() {
//
//    @Bean
//    override fun connectionFactory() = H2ConnectionFactory(
//        H2ConnectionConfiguration.builder()
//            .file("/Users/user/workspace/personal/tutorial-spring-performance-test/spring-webflux-mvc/data")
//            .property(H2ConnectionOption.DB_CLOSE_DELAY, "-1")
//            .property(H2ConnectionOption.DB_CLOSE_ON_EXIT, "FALSE")
//            .username("sa")
//            .password("sa")
//            .build()
//    )
//
//    @Bean
//    fun initializer(connectionFactory: ConnectionFactory): ConnectionFactoryInitializer {
//        val initializer = ConnectionFactoryInitializer()
//        initializer.setConnectionFactory(connectionFactory)
//
//        val resourceDatabasePopulator = ResourceDatabasePopulator()
//        resourceDatabasePopulator.addScript(ClassPathResource("schema.sql"))
//
//        initializer.setConnectionFactory(connectionFactory())
//        initializer.setDatabasePopulator(resourceDatabasePopulator)
//        return initializer
//    }
//}

@RestController
class Controller(
    private val personService: PersonService
) {

    @PostMapping("/person")
    fun createPerson(): Mono<ResponseEntity<Person>> {
        val name = Random.nextBytes(50).toString()

        return personService.createPerson(name)
            .map { ResponseEntity.ok().body(it) }
    }

    @GetMapping("/person")
    fun getPerson(): Mono<ResponseEntity<Any>> {
        val id = Random.nextLong(1000000)

        return personService.getPerson(id)
            .map {
                if (it != null) {
                    ResponseEntity.ok().body(it)
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
    fun createPerson(name: String): Mono<Person> {
        val person = Person(name = name)
        return personRepository.save(person)
    }

    fun getPerson(id: Long): Mono<Person?> {
        return personRepository.findById(id)
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
