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
package com.example.events;

import lombok.Data;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.event.LoggingEventListener;
import org.springframework.data.util.CloseableIterator;

import com.example.SimpleConfiguration;

/**
 * @author Mark Paluch
 */
public class Events {

	private static void doWithMongo(MongoOperations operations) {

		operations.remove(Employee.class).all();

		Employee employee = getEmployee();

		operations.insertAll(Arrays.asList(employee, employee, employee, employee));

		CloseableIterator<Employee> iterator = operations.query(Employee.class).stream();
		iterator.forEachRemaining(System.out::println);

		iterator.close();
	}

	private static Employee getEmployee() {

		Employee employee = new Employee();

		employee.setName("Bender");
		employee.setFavoriteDrinks(Arrays.asList("Beer", "Booze"));
		employee.setMood("Bite my shiny metal ass!");
		employee.setBirthDate(LocalDate.parse("2996-01-01"));

		return employee;
	}

	public static void main(String[] args) {

		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SimpleConfiguration.class,
				EventConfig.class);

		doWithMongo(context.getBean(MongoOperations.class));

		context.stop();
	}

	@Document
	@Data
	static class Employee {

		String name;
		List<String> favoriteDrinks;
		String mood;
		@Field("date_of_birth") LocalDate birthDate;
	}

	@Configuration
	static class EventConfig {

		@Bean
		public LoggingEventListener listener() {
			return new LoggingEventListener();
		}
	}
}
