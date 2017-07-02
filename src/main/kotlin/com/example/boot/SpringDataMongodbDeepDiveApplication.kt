/*
 * Copyright 2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.boot

import org.bson.types.ObjectId
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.data.mongodb.core.CollectionOptions
import org.springframework.data.mongodb.core.MongoOperations
import org.springframework.data.mongodb.core.createCollection
import org.springframework.data.mongodb.core.dropCollection
import org.springframework.data.mongodb.repository.Tailable
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import java.time.Duration
import java.util.*
import java.util.function.Supplier
import java.util.stream.Stream

/**
 * @author Mark Paluch
 */
@SpringBootApplication
class SpringDataMongodbDeepDiveApplication {

	@Bean
	fun clr(repository: PersonRepository, operations: MongoOperations): CommandLineRunner {

		return CommandLineRunner {

			operations.dropCollection<Person>()
			operations.createCollection(Person::class, CollectionOptions.empty().capped().size(100000))

			val names = arrayOf("Bender", "Leela", "Fry", "Farnsworth", "Dr. Zoidberg")

			val stream = Stream.generate(Supplier { names[Random().nextInt(names.size)] }).map(::Person)

			Flux.interval(Duration.ofSeconds(2))
					.zipWith(Flux.fromStream(stream))
					.map { t -> t.t2 }
					.flatMap { person -> repository.save(person) }
					.subscribe()
		}
	}
}

fun main(args: Array<String>) {
	SpringApplication.run(SpringDataMongodbDeepDiveApplication::class.java, *args)
}

@RestController
class PersonController(val repository: PersonRepository) {

	@GetMapping("stream", produces = arrayOf(MediaType.APPLICATION_STREAM_JSON_VALUE))
	fun stream() = repository.findAllBy()
}


interface PersonRepository : ReactiveCrudRepository<Person, ObjectId> {

	@Tailable
	fun findAllBy(): Flux<Person>
}

data class Person(val name: String)
