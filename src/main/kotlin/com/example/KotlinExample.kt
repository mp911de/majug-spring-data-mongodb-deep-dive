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
package com.example

import org.springframework.context.annotation.AnnotationConfigApplicationContext
import org.springframework.data.mongodb.core.MongoOperations
import org.springframework.data.mongodb.core.insert
import org.springframework.data.mongodb.core.mapping.Field
import org.springframework.data.mongodb.core.query
import java.time.LocalDate
import java.util.*

/**
 * @author Mark Paluch
 */
object KotlinExample {

	@JvmStatic
	fun main(args: Array<String>) {

		val context = AnnotationConfigApplicationContext(SimpleConfiguration::class.java)

		val operations = context.getBean(MongoOperations::class.java)

		val employee = Employee()

		employee.name = "Bender"
		employee.favoriteDrinks = Arrays.asList("Beer", "Booze")
		employee.mood = "Bite my shiny metal ass!"
		employee.birthDate = LocalDate.parse("2996-01-01")

		operations.insert<Employee>().one(employee)
		operations.query<Employee>().all().forEach(::println)
	}
}

data class Employee(var name: String?,
					var favoriteDrinks: List<String>,
					var mood: String?,
					@Field("date_of_birth") var birthDate: LocalDate?) {

	constructor() : this(null, Collections.emptyList(), null, null)
}


