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

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.data.mongodb.core.MongoOperations

/**
 * @author Mark Paluch
 */
@SpringBootApplication
class SpringDataMongodbDeepDiveApplication {
	@Bean
	fun clr(operations: MongoOperations): CommandLineRunner {

		return CommandLineRunner {
		}
	}
}

fun main(args: Array<String>) {
	SpringApplication.run(SpringDataMongodbDeepDiveApplication::class.java, *args)
}




